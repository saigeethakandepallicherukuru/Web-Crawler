import java.io.IOException;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public class AllUrls {
	public void visitAllUrls(Page page, WebURL URL) {
		String indicator=new String();
		String url=URL.getURL();
		//nytimes.com
		//bbc.com/news
		if(url.contains("nytimes.com")) {
			indicator="OK";
			SaiCrawler.uniqueUrlsWithinNews.add(url);
		} else {
			indicator="N_OK";
			SaiCrawler.uniqueUrlsOutsideNews.add(url);	
		}
		
		String[] outputValues={url,indicator};
		Controller.allUrlsCSV.writeNext(outputValues); 
	}
	
	// close the allUrlsCSV writer
		public void closeCSVFile() {
			try {
				Controller.allUrlsCSV.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
}
