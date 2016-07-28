package capital.scalable.crawler.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class GoogleCrawlerService{

    @Autowired
    private JavascriptCrawlerService javascriptCrawlerService;

    @Autowired
    private SiteConnectorService siteConnectorService;

    @Value("${googleUrl}")
    private String googleUrl;


    @Value("${charset}")
    private String charset;


    public void run(String... args) throws Exception {
        Document doc = getGoogleResult(args);
        List<Future<List<String>>> javascriptNames = collectJavascriptLibNames(doc);
        System.out.println("Most commons Javascript libraries:");
        showTheMostCommonJavascriptLibraries(javascriptNames);
    }

    private Document getGoogleResult(String[] args) throws UnsupportedEncodingException {
        return siteConnectorService.connect(generateUrl(args));
    }

    private void showTheMostCommonJavascriptLibraries(List<Future<List<String>>> javascriptNames) {
        convertToMap(javascriptNames).entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(System.out::println);
    }

    private Map<String, Long> convertToMap(List<Future<List<String>>> javascriptNames) {
        return toMap(flattenList(javascriptNames));
    }

    private List<String> flattenList(List<Future<List<String>>> javascriptNames) {
        return javascriptNames.stream().flatMap(result -> getFutureResult(result).stream()).collect(Collectors.toList());
    }

    private List<Future<List<String>>> collectJavascriptLibNames(Document doc) {
        List<Future<List<String>>> javascriptNames = new ArrayList<>();

        Elements links = doc.select(".r a");
        links.stream().forEach( link -> {
            String url = link.attr("href");
            javascriptNames.add(javascriptCrawlerService.process(cleanLink(url)));
        });
        return javascriptNames;
    }

    private String generateUrl(String[] args) throws UnsupportedEncodingException {
        return googleUrl+ URLEncoder.encode(getArgumentAsQuery(args),charset) + "&num=150";
    }


    private static Map<String,Long> toMap(List<String> lst){
        return lst.stream().collect(Collectors.groupingBy(s -> s,
                Collectors.counting()));
    }

    private List<String> getFutureResult(Future<List<String>> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            return new ArrayList<>();
        }
    }

    private String getArgumentAsQuery( String[] args) {
        String result = "";
        for(String arg : args){
            result += arg + " ";
        }
        return result;
    }

    private String cleanLink(String url) {
        Pattern p = Pattern.compile("q=([^&]+)");
        Matcher m = p.matcher(url);
        return m.find() ? m.group(1) : "";
    }
}