package com.splider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ParserHtml {

    private static final String filterWords[]=new  String[] {"作者:","出版社:","出版时间:",":"};
    private static SaveBookInfo saveBookInfo=new  SaveBookInfo();
    private static String searchURL="http://search.dangdang.com/?key={0}&act=input&category_path=01.00.00.00.00.00&type=01.00.00.00.00.00";
    private static WebClient wc = new WebClient(BrowserVersion.getDefault());
    public static Logger logger =LogManager.getLogger(ParserHtml.class);
    
    
    private static final int meiZiPage= 67;
	public static void main(String[] args) throws Exception {
		start();
	}
	private static void start() throws Exception {
		try 
		{
			List<String> isbnList=readExcel("isbn.xls");
			if(isbnList.size()>0) {
				for(String item:isbnList) {
				    boolean syncBookResult=sysncBook(item);
				    if(syncBookResult) {
				    	System.out.println(item+"采集书本信息成功！");
				    	logger.info("采集成功：{}",item);
				    }
				    else {
				    	System.out.println(item+"采集书本信息失败！");
				    	logger.info("采集失败：{}",item);
				    }
				}
		      wc.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 wc.close();
		}
	}
	/**
	 * 读取excel信息
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private  static List<String> readExcel(String filepath) throws FileNotFoundException, IOException {
		List<String> listIsbns=new ArrayList<>();
		File file = new File(filepath);
	    String[][] result = ExcelHelper.getData(file, 1);
	    int rowLength = result.length;
	    for(int i=0;i<rowLength;i++) {
	        for(int j=0;j<result[i].length;j++) {
	        	listIsbns.add(result[i][0]);
	        }
	    }
	    return listIsbns;
	}
    
	
	private static Document convertHtmlUnitToJsoup(String requestUrl) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		  Document doc =null;
	      wc.getOptions().setJavaScriptEnabled(false); // 启用JS解释器，默认为true
	      wc.getOptions().setCssEnabled(false); // 禁用css支持
	      wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
	      wc.getOptions().setTimeout(10000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待
	      HtmlPage page = wc.getPage(requestUrl);
	      String pageXml = page.asXml(); // 以xml的形式获取响应文本
	      /** jsoup解析文档 */
	      // 把String转化成document格式
	      doc = Jsoup.parse(pageXml);
		  return doc;
	}
	/**
	 * 获取url信息
	 * @param isbn
	 * @return
	 * @throws Exception 
	 */
   public static boolean sysncBook(String isbn) throws Exception 
   {
 		Map<String,String> hsBookinfo=new HashMap<String,String>();
		String searchrUrl=MessageFormat.format(searchURL,isbn);
		Document doc = createDocument(searchrUrl);
		if(doc!=null) {
			Thread.sleep(1000);
			Elements selectElements = doc.select("#search_nature_rg>ul>li");
		     BookInfo book=new BookInfo();
			for(Element el:selectElements) {
			 Element  hrefElement=el.select("a").first();
			 if(hrefElement!=null) {
				  Element imageElement=el.select("li>a>img").first(); 
				  book.setImgSrc(imageElement.attr("src"));
				  book.setIsbn_no(isbn);
				  book.setBook_name(hrefElement.attr("title"));
				  hsBookinfo.put("link",hrefElement.attr("abs:href").toString());
			      Document targetDoc= createDocument(hsBookinfo.get("link"));
				 // Document targetDoc=convertHtmlUnitToJsoup(hsBookinfo.get("link"));
			      if (targetDoc!=null) {
			    	  Thread.sleep(1000);
					  Element priceElement=targetDoc.select("#dd-price").first();
					  String price=priceElement.text().replace("¥", "");
					  book.setPrice(new BigDecimal(price));
					 String selector="body > div.product_wrapper > div.product_main.clearfix > div.show_info > div > div.sale_box_left > div.messbox_info > span";
					 Elements extentElements=targetDoc.select(selector);
					 for(int i=0;i<extentElements.size();i++) {
				           Element element=extentElements.get(i);
				           String orginText=element.text();
				           String filterText=filterWords(orginText);
				           if (orginText.indexOf(filterWords[0])>-1) {
				        	   	book.setAuthor(filterText);
				           }
				           if(orginText.indexOf(filterWords[1])>-1) {
				        	 	book.setPress(filterText);
				           }
				           if (orginText.indexOf(filterWords[2])>-1) {
				        	   SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				        	   Date date=new Date();
				        	   String formatDate=format.format(date) ;
				        	   book.setPublication_date(formatDate);
				           }
					 }
					 //内容简介
					 Element descripElement=targetDoc.select("#detail_describe > ul > li:nth-child(4)").first();
					 if(null!=descripElement) {
						 String describe= descripElement.text();
						 book.setDescribe(describe);
					 }
					 else {
						 Element contentElement= targetDoc.select("#content > div.descrip > span:nth-child(2)").first();
						 if(null!=contentElement) {
							 String describe= contentElement.text();
							 book.setDescribe(describe);
						 }
					 }
					 book.setCategory("T");
					 book.setReadable("0");
					 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		             Date date=new Date();
					 book.setCreate_time((df.format(date)));
					 book.setModify_time(df.format(date));
					if(book.getImgSrc()!=""&&book.getImgSrc()!=null) 
					{
						 book.setImgurl(isbn+".jpg");
						 DownloadImage.downloadPicture(book.getImgSrc(),isbn+".jpg","d:\\picture" );
					}
				}
			 }
			}
			if(book.getBook_name()!=null&&book.getBook_name()!="") {
				return saveBook(book);
			}
		}
		return false;
   }


   private static Document createDocument(String  requestUrl) {
	   Map<String, String> cookies = new HashMap<>();
		//book.douban.com
		cookies.put("__utma", "81379588.1625906329.1478780180.1478780180.1478780180.1");
		cookies.put("__utmb", "81379588.1.10.1478780180");
		cookies.put("__utmc", "81379588");
		cookies.put("__utmz", "81379588.1478780180.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");
		cookies.put("_pk_id.100001.3ac3", "b8e7b1931da4acd1.1478780181.1.1478780181.1478780181.");
		cookies.put("_pk_ses.100001.3ac3", "*");
		//douban.com
		cookies.put("bid", "MvEsSVNL_Nc");
		//read.douban.com
		cookies.put("_ga", "GA1.3.117318709.1478747468");
		cookies.put("_pk_id.100001.a7dd", "ce6e6ea717cbd043.1478769904.1.1478769904.1478769904.");
		cookies.put("_pk_ref.100001.a7dd", "%5B%22%22%2C%22%22%2C1478769904%2C%22https%3A%2F%2Fbook.douban.com%2"
				+ "Fsubject_search%3Fsearch_text%3D%25E6%258E%25A8%25E8%258D%2590%25E7%25B3%25BB%25E7%25BB%259F%25"
				+ "E5%25AE%259E%25E8%25B7%25B5%26cat%3D1001%22%5D");
		//www.douban.com
		cookies.put("_pk_id.100001.8cb4", "237bb6b49215ebbc.1478749116.2.1478774039.1478749120.");
		cookies.put("_pk_ref.100001.8cb4", "%5B%22%22%2C%22%22%2C1478773525%2C%22https%3A%2F%2Fwww.baidu."
				+ "com%2Flink%3Furl%3DlQ4OMngm1b6fAWeomMO7xq6PNbBlxyhdnHqz9mIYN9-ycRbjZvFb1NQyQ7hqzvI46-WThP"
				+ "6A_Qo7oTQNP-98pa%26wd%3D%26eqid%3Da24e155f0000e9610000000258244a0c%22%5D");
		try 
		{
		     return Jsoup.connect(requestUrl).header("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)").cookies(cookies).timeout(3000).get();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  return null;
   }

   /**
    * 保存图书信息
    * @param book
    * @return
    */
   public static   boolean saveBook(BookInfo book) 
   {
	   String sql = "insert into book_info values ('" + book.getIsbn_no() + "', '" + book.getBook_name() + "', '"
				+ book.getAuthor() + "', '" + book.getPress() + "', "
				+ "'" + book.getPublication_date() + "', '" + book.getCategory() + "', '" + book.getPrice() + "', '" + book.getReadable()+ "', '" + book.getImgurl() + "','" + book.getDescribe() + "', '" + book.getCreate_time() + "', '" + book.getModify_time() + "')";
	   return  saveBookInfo.saveBookInfo(sql);
   }
   /**
    * 移除过滤字符串
    * @param source
    */
	private static String  filterWords(String source) {
			for(String str:filterWords) {
				if (source.indexOf(str)>-1) {
					source=source.replace(str, "");
				}
			}
			return source;
	}
}
