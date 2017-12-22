package com.splider;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MeiZiHtmlParser implements Runnable {
	private String html;
	private int page;
	private Document document;
	public MeiZiHtmlParser(String html,Document document, int page) {
		// TODO Auto-generated constructor stub
		this.html = html;
		this.page = page;
		this.document=document;
	}
	
	@Override
	public void run() {
		
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<>();
		Elements parentElements=document.select("#content");
		if (parentElements!=null) {
		  Elements jpgElements=parentElements.select("#content > a > img");
		  if (jpgElements!=null) {
			String url=jpgElements.first().attr("src");
			int size=40;
			try {
				String total=document.select("#page > a:eq(-2)").first().text();
				size=Integer.parseInt(total);
			}
			catch (Exception e) {
				size=40;
			}
			if(url.endsWith(".jpg")) {
				int beginIndex=url.indexOf(".jpg");
				url=url.substring(0, beginIndex-1);
				for (int i = 1; i <= size; i++) {
					
					String new_url=MessageFormat.format(url+"{0}.jpg",i);
					list.add(new_url);
				}
			}
		}
		}
		for (String imageUrl : list) {
			if (imageUrl.indexOf("mmjpg") > 0) {
				new Thread(new MeiZiImageCreator(imageUrl, page)).start();
			}
		}
	}
}
