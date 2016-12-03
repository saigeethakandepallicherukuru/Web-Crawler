import java.io.IOException;

import edu.uci.ics.crawler4j.url.WebURL;

public class Fetch {
	
	// Write the fetched URLs to fetchCSV
	public void writeFetchURLs(WebURL webUrl, int statusCode) {
		String url=webUrl.getURL();
		String[] resValues={url,Integer.toString(statusCode)};
		Controller.fetchCSV.writeNext(resValues);
	}
	
	// close the fetchCSV writer
	public void closeCSVFile() {
		try {
			Controller.fetchCSV.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
