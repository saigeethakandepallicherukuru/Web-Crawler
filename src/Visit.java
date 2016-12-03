import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Visit {
	private final static String PATTERN=".*(\\.(html|htm|doc|pdf|docx|png|jpeg|gif))$";
	private final static Pattern FILTERS=Pattern.compile(PATTERN);
	
	public void visitUrls(Page page) {
		String url=page.getWebURL().getURL();
		String contentType=page.getContentType();
		if(contentType.contains("text/html")||contentType.contains("application/msword")||
				contentType.contains("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
				contentType.contains("application/pdf")) {
			processPage(page,contentType,url);
		} else if(FILTERS.matcher(url).matches()) {
			processPage(page,contentType,url);
		} else {
		/*	if(!SaiCrawler.contentTypes.containsKey(contentType)) {
				SaiCrawler.contentTypes.put(contentType,1);
			} else {
				SaiCrawler.contentTypes.put(contentType,SaiCrawler.contentTypes.get(contentType)+1);
			} */
			
		/*	String []outputValues={url,"","",contentType};
			Controller.visitCSV.writeNext(outputValues); */
		}
		
	}
	
	public static void processPage(Page page, String contentType, String url) {
		if(page.getParseData() instanceof BinaryParseData || page.getParseData() instanceof HtmlParseData) {
			BinaryParseData binaryParseData;
			HtmlParseData htmlParseData;
			byte[] b=page.getContentData();
			String []str=page.getContentType().split(";");
			Set<WebURL> links=null;
			
			if(page.getParseData() instanceof BinaryParseData) {
				binaryParseData=(BinaryParseData)page.getParseData();
				links=binaryParseData.getOutgoingUrls();
			}
			if(page.getParseData() instanceof HtmlParseData) {
				htmlParseData=(HtmlParseData)page.getParseData();
				links=htmlParseData.getOutgoingUrls();
			}
			
			if(!SaiCrawler.contentTypes.containsKey(str[0])) {
				SaiCrawler.contentTypes.put(str[0],1);
			} else {
				SaiCrawler.contentTypes.put(str[0],SaiCrawler.contentTypes.get(str[0])+1);
			}
			
			String[] outputValues={url,Integer.toString(b.length),Integer.toString(links.size()),contentType};
			Controller.visitCSV.writeNext(outputValues);
			
			saveFileSize(b);
			
		}
	}
	
	public static void saveFileSize(byte[] b) {
		if(b.length<1024) {
			if(!SaiCrawler.fileSizes.containsKey("<1KB")) {
				SaiCrawler.fileSizes.put("<1KB",1);
			} else {
				SaiCrawler.fileSizes.put("<1KB",SaiCrawler.fileSizes.get("<1KB")+1);
			}
		} else if(b.length>=1024 && b.length<10240) {
			if(!SaiCrawler.fileSizes.containsKey("1KB ~ <10KB")) {
				SaiCrawler.fileSizes.put("1KB ~ <10KB",1);
			} else {
				SaiCrawler.fileSizes.put("1KB ~ <10KB",SaiCrawler.fileSizes.get("1KB ~ <10KB")+1);
			}
		} else if(b.length>=10240 && b.length<102400) {
			if(!SaiCrawler.fileSizes.containsKey("10KB ~ <100KB")) {
				SaiCrawler.fileSizes.put("10KB ~ <100KB",1);
			} else {
				SaiCrawler.fileSizes.put("10KB ~ <100KB",SaiCrawler.fileSizes.get("10KB ~ <100KB")+1);
			}
		} else if(b.length>=102400 && b.length<1048576) {
			if(!SaiCrawler.fileSizes.containsKey("100KB ~ <1MB")) {
				SaiCrawler.fileSizes.put("100KB ~ <1MB",1);
			} else {
				SaiCrawler.fileSizes.put("100KB ~ <1MB",SaiCrawler.fileSizes.get("100KB ~ <1MB")+1);
			}
		} else {
			if(!SaiCrawler.fileSizes.containsKey(">1MB")) {
				SaiCrawler.fileSizes.put(">1MB",1);
			} else {
				SaiCrawler.fileSizes.put(">1MB",SaiCrawler.fileSizes.get(">1MB")+1);
			}
		}
	}
	
	// close the visitCSV writer
	public void closeCSVFile() {
		try {
			Controller.visitCSV.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
