package com.splider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Utils {
	public static final String MMURL="http://www.mmjpg.com/mm/";
	public static String convertStreamToString(InputStream in) throws UnsupportedEncodingException {  
        //BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder sb = new StringBuilder();  
        String line = null;  
        try {  
            while ((line = reader.readLine()) != null) {  
                sb.append(line + "/n");  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return sb.toString();  
    }  
	   public static Document createDocument(String  requestUrl) {
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
}