package com.splider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MeiZiImageCreator implements Runnable {

	private static int count = 1;
	private String imageUrl;
	private int page;
	private StringBuffer basePath;
	
	public MeiZiImageCreator(String imageUrl, int page) {
		// TODO Auto-generated constructor stub
		this.imageUrl = imageUrl;
		this.page = page;
		basePath = new StringBuffer("D:/meizitu/page_"+page);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		File dir = new File(basePath.toString());
		if (!dir.exists()) {
			dir.mkdirs();
			System.out.println("妹子图片存放于"+basePath+"目录下");
		}
		
		String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		File file = new File(basePath + "/" + page + "--" + imageName);
		try {
			OutputStream os = new FileOutputStream(file);
			URL url = new URL(imageUrl);
			InputStream in = url.openStream();
			byte[] buff = new byte[1024];
			while(true){
				int readed = in.read(buff);//读取内容长度
				if(readed == -1){
					break;
				}
				byte[] temp = new byte[readed];
				System.arraycopy(buff, 0, temp, 0, readed);//内容复制
				//写入到文件中
				os.write(temp);
			}
			System.out.println("第" + (count++) + "张妹子：" + file.getAbsolutePath());
			in.close();
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}