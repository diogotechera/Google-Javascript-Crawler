package capital.scalable.crawler.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SiteConnectorService {

    @Value("${userAgent}")
    private String userAgent;

    private static final Logger log = LoggerFactory.getLogger(SiteConnectorService.class);

    public Document connect(String url){
        try {

            return Jsoup.connect(url).timeout(0).userAgent(userAgent).get();

        } catch (Exception e) {
            log.error("Error getting url="+url);
        }
        return new Document("/error");
    }
}
