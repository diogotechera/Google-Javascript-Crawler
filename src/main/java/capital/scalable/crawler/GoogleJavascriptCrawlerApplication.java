package capital.scalable.crawler;

import capital.scalable.crawler.service.GoogleCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
@EnableAutoConfiguration
public class GoogleJavascriptCrawlerApplication implements CommandLineRunner {

	@Autowired
	private GoogleCrawlerService googleCrawlerService;

	public static void main(String[] args) {
		SpringApplication.run(GoogleJavascriptCrawlerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		googleCrawlerService.run(args);
	}
}
