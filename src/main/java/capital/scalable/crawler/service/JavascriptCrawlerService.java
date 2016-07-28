package capital.scalable.crawler.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JavascriptCrawlerService {


    @Autowired
    private SiteConnectorService siteConnectorService;


    @Async
    public Future<List<String>> process(String url){
        List<String> results = new ArrayList<>();

            Document document = siteConnectorService.connect(url);
            document.getElementsByTag("script").stream().forEach(element -> {
                String javascriptPath = element.attr("src");
                if (!StringUtils.isEmpty(javascriptPath)){
                    String baseName = getBaseName(javascriptPath);
                    if (!StringUtils.isEmpty(baseName))
                        results.add(baseName);
                }

            });

        return new AsyncResult<>(results);
    }

    private String getBaseName(String javascriptPath) {
        Pattern p = Pattern.compile("[^/]+\\.js");
        Matcher m = p.matcher(javascriptPath);
        return m.find() ? m.group(0).replaceAll("\\.js","") : "";
    }


}
