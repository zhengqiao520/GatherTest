package com.splider;

import java.math.BigDecimal;
import java.util.Date;

public class BookInfo 
{
       public String isbn_no;
       public String book_name;
       public String author;
       public String press;
       public String  publication_date;
       public String category;
	   public  BigDecimal price;
       public String readable;
       public String imgurl;
       public  String describe;
       public  String create_time;
       public String  modify_time;
       public String imgSrc;
       public String getCreate_time() {
		return create_time;
	}
       public String getIsbn_no() {
		return isbn_no;
	}
   public void setImgSrc(String imgSrc) {
	   this.imgSrc=imgSrc;
   }
   public String getImgSrc() {
	   return imgSrc;
   }
	public void setIsbn_no(String isbn_no) {
		this.isbn_no = isbn_no;
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
	public String getPublication_date() {
		return publication_date;
	}
	public void setPublication_date(String publication_date) {
		this.publication_date = publication_date;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getReadable() {
		return readable;
	}
	public void setReadable(String readable) {
		this.readable = readable;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getStringe_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getModify_time() {
		return modify_time;
	}
	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}
       
   	
}
