package com.splider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveBookInfo {
	public static final String url = "jdbc:mysql://localhost:3306/world";
	public static final String name = "com.mysql.jdbc.Driver";
	public static final String user = "root";
	public static final String password = "123456";

	public Connection conn = null;
	public PreparedStatement pst = null;

	public boolean saveBookInfo(String sql) {
		boolean result=false;
		try {
			Class.forName(name);// 指定连接类型
			conn = DriverManager.getConnection(url, user, password);// 获取连接
			pst = conn.prepareStatement(sql);// 准备执行语句
		    result=pst.execute();
		} catch (Exception e) {
			e.printStackTrace();
			result=false;
		} finally {
			try {
				this.conn.close();
				this.pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
