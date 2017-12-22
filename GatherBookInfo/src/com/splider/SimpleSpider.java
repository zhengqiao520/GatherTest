package com.splider;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.nodes.Document;

public class SimpleSpider {
	private static final int meiZiPage = 67;
	
	public static void main(String[] args) throws InterruptedException {
		SimpleSpider spider = new SimpleSpider();
		spider.getMeiZiImages();
		
	}
	private  void getMeiZiImages() throws InterruptedException {
/*	 RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).setConnectionRequestTimeout(6000).setConnectTimeout(6000).build();
	 CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();*/
	 for (int i = 1; i <= meiZiPage; i++) {
		 String url=MessageFormat.format(Utils.MMURL+"{0}",i);
	    Document document=Utils.createDocument(url);
	    Thread.sleep(5000);
	    new Thread(new MeiZiHtmlParser("",document,i)).start();
	 }
	}

}
