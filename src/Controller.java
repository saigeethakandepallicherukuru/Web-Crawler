import java.io.FileWriter;

import au.com.bytecode.opencsv.CSVWriter;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

	 // CSV writer
	 public static CSVWriter fetchCSV;
	 public static CSVWriter visitCSV;
	 public static CSVWriter allUrlsCSV;
	 
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String crawlStorageFolder = "data/crawl";
		 int numberOfCrawlers = 7;
		 CrawlConfig config = new CrawlConfig();
		 config.setCrawlStorageFolder(crawlStorageFolder);
		 config.setMaxPagesToFetch(10000);
		 config.setPolitenessDelay(100);
		 config.setMaxDepthOfCrawling(16);
		 config.setUserAgentString("crawler4j (SaiGeetha)"); 
		 config.setIncludeBinaryContentInCrawling(true);
		 
		 String directory="/Users/saigeetha/Documents/School Documents/Fall 2016/Assignments/CSCI_572_HW2";
		 fetchCSV =new CSVWriter(new FileWriter(directory+"/fetch.csv", true),',');
		 visitCSV = new CSVWriter(new FileWriter(directory+"/visit.csv", true),',');
		 allUrlsCSV = new CSVWriter(new FileWriter(directory+"/urls.csv", true),',');
		 
		 String[] fetchValues={"URLs","HTTP Status Codes"};
		 fetchCSV.writeNext(fetchValues);
		 
		 String[] visitValues={"URLs", "Size", "# of Outlinks", "Content-Type"};
		 visitCSV.writeNext(visitValues);
		 
		 String[] allUrlsValues={"URLs", "Indicator"};
		 allUrlsCSV.writeNext(allUrlsValues);
		 
		 /*
		 * Instantiate the controller for this crawl.
		 */
		 PageFetcher pageFetcher = new PageFetcher(config);
		 RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		 RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		 CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		 /*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		 controller.addSeed("http://www.nytimes.com/");
		 /*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * 
		 * will reach the line after this only when crawling is finished.
		 */
		 controller.start(SaiCrawler.class, numberOfCrawlers);
		 
		 //Close all CSV writer pointers
		 fetchCSV.close();
		 visitCSV.close();
		 allUrlsCSV.close();
	}

}
