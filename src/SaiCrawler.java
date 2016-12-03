import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class SaiCrawler extends WebCrawler {
	
	Fetch fetchObject;
	Visit visitObject;
	AllUrls allUrlsObject;
	
	//Statistic variables
	public static long fetchesAttempted=0;
	public static long fetchesSucceeded=0;
	public static long fetchesAborted=0;
	public static long fetchesFailed=0;
	public static long urlsExtracted=0;
	
	//Set to keep track of unique URLs
	public static Set<String> uniqueUrlsExtracted = new HashSet<String>();
	public static Set<String> uniqueUrlsWithinNews = new HashSet<String>();
	public static Set<String> uniqueUrlsOutsideNews = new HashSet<String>();
	
	//to keep track of status codes, file sizes and content types
	public static HashMap<Integer,Integer> statusCodes=new HashMap<Integer,Integer>();
	public static HashMap<String,Integer> fileSizes=new HashMap<String,Integer>();
	public static HashMap<String,Integer> contentTypes=new HashMap<String,Integer>();
	
	/* SaiCrawler Constructor */
	public SaiCrawler() {
		fetchObject=new Fetch();
		visitObject=new Visit();
		allUrlsObject=new AllUrls();
	}
	
 /**
 * This method receives two parameters. The first parameter is the page
 * in which we have discovered this new url and the second parameter is
 * the new url. You should implement this function to specify whether
 * the given url should be crawled or not (based on your crawling logic).
 * In this example, we are instructing the crawler to ignore urls that
 * have css, js, git, ... extensions and to only accept urls that start
 * with "http://www.viterbi.usc.edu/". In this case, we didn't need the
 * referringPage parameter to make the decision.
 */
 @Override
 public boolean shouldVisit(Page referringPage, WebURL url) {
	 String href = url.getURL();
	 allUrlsObject.visitAllUrls(referringPage,url);
	 // to keep track of all the urls extracted
	 urlsExtracted++;
	 uniqueUrlsExtracted.add(href);
	 return href.startsWith("http://www.nytimes.com/");
}
	 
 /**
  * This function is called when a page is fetched and ready
  * to be processed by your program.
  */
  @Override
  public void visit(Page page) {
	  visitObject.visitUrls(page);
	  String url = page.getWebURL().getURL();
	  System.out.println("URL: " + url);
	  if (page.getParseData() instanceof HtmlParseData) {
		  HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
		  String text = htmlParseData.getText();
		  String html = htmlParseData.getHtml();
		  Set<WebURL> links = htmlParseData.getOutgoingUrls();
		  System.out.println("Text length: " + text.length());
		  System.out.println("Html length: " + html.length());
		  System.out.println("Number of outgoing links: " + links.size());
		  }
  }
  
  @Override
  protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
	 fetchObject.writeFetchURLs(webUrl, statusCode);
	 fetchesAttempted++;
    if (statusCode != HttpStatus.SC_OK) {
    	if(statusCode>=300 && statusCode<400) {
    		fetchesAborted++;
    	} else {
    		fetchesFailed++;
    	}
    }
    
    if(statusCode==200) {
    	fetchesSucceeded++;
    	if(!statusCodes.containsKey(statusCode)) {
    		statusCodes.put(statusCode,1);
    	} else {
    		statusCodes.put(statusCode,statusCodes.get(statusCode)+1);
    	}
    } else {
    	if(!statusCodes.containsKey(statusCode)) {
    		statusCodes.put(statusCode,1);
    	} else {
    		statusCodes.put(statusCode,statusCodes.get(statusCode)+1);
    	}
    }
  }
  
  /**
   * This function is called by controller before finishing the job.
   * You can put whatever stuff you need here.
   */
  @Override
  public void onBeforeExit() {
    dumpMyData();
  }
  
  public void dumpMyData() {
	  // To write the report contents to a output file
	  PrintStream output=null;
	  try{
		  output=new PrintStream(new FileOutputStream("/Users/saigeetha/Documents/School Documents/Fall 2016/Assignments/CSCI_572_HW2/CrawlReport.txt"));
		  System.setOut(output);
		  System.out.println("Name: Sai Geetha Kandepalli Cherukuru");
		  System.out.println("USC ID : 7283210853");
		  System.out.println("News site crawled: nytimes.com");
		  System.out.println();
		  System.out.println("Fetch Statistics");
		  System.out.println("================");
		  System.out.println("# fetches attempted: "+fetchesAttempted);
		  System.out.println("# fetches succeeded: "+fetchesSucceeded);
		  System.out.println("# fetches aborted: "+fetchesAborted);
		  System.out.println("# fetches failed: "+fetchesFailed);
		  System.out.println();
		  System.out.println("Outgoing URLs:");
		  System.out.println("================");
		  System.out.println("Total URLs extracted:"+urlsExtracted);
		  System.out.println("# unique URLs extracted: "+uniqueUrlsExtracted.size());
		  System.out.println("# unique URLs within News Site: "+uniqueUrlsWithinNews.size());
		  System.out.println("# unique URLs outside News Site: "+uniqueUrlsOutsideNews.size());
		  System.out.println();
		  System.out.println("Status Codes:");
		  System.out.println("================");
		  
		  for(Integer k: statusCodes.keySet()) {
			  Integer value=statusCodes.get(k);
			  System.out.println(k+": "+value);
		  }
		  
		  System.out.println();
		  System.out.println("File Sizes:");
		  System.out.println("================");
		  
		  for(String k: fileSizes.keySet()) {
			  Integer value=fileSizes.get(k);
			  System.out.println(k+": "+value);
		  }
		  
		  System.out.println();
		  System.out.println("Content Types:");
		  System.out.println("================");
		  
		  for(String k: contentTypes.keySet()) {
			  Integer value=contentTypes.get(k);
			  System.out.println(k+": "+value);
		  }
		  
	  } catch(FileNotFoundException e) {
		  e.printStackTrace();
	  }
  }
}
