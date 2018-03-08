package sn.sn.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import sn.sn.constant.IConstant;

/**
 * 数据库操作
 * @author 王超
 */
public class DbHelper {
	
	/**
	 * insert sql
	 * @param sql
	 * @return
	 * @throws Throwable
	 */
	public int insert(String sql) throws Throwable {
		int res = 0;
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			Class.forName(IConstant.MYSQL_NAME);
			conn = (Connection) DriverManager.getConnection(IConstant.MYSQL_URL, IConstant.MYSQL_USER, IConstant.MYSQL_PASSWORD);
			pst = (PreparedStatement) conn.prepareStatement(sql);
			res = pst.executeUpdate();
		} catch(Exception e) {
			throw e;
		} finally {
			try {
				if (pst != null) pst.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		return res;
	}
	
	/**
	 * select sql
	 * @param sql
	 * @return List<Map<String, String>>
	 * @throws Throwable
	 */
	public List<Map<String, String>> select(String sql)throws Throwable {
		List<Map<String, String>> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			Class.forName(IConstant.MYSQL_NAME);
			conn = (Connection) DriverManager.getConnection(IConstant.MYSQL_URL, IConstant.MYSQL_USER, IConstant.MYSQL_PASSWORD);
			pst = (PreparedStatement) conn.prepareStatement(sql);
			ResultSet resultSet = pst.executeQuery();
			
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			String[] names = new String[resultSetMetaData.getColumnCount()];
			for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) names[i - 1] = resultSetMetaData.getColumnName(i);
			
			while (resultSet.next()) {
				Map<String, String> map = new HashMap<>();
				for (int i = 0; i < names.length; i++) map.put(names[i], resultSet.getObject(i + 1).toString());
				list.add(map);
			}
		} catch(Exception e) {
			throw e;
		} finally {
			try {
				if (pst != null) pst.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		try {
			List<Map<String, String>> list = new DbHelper().select("select * from gold order by id");
			for (int i = 1; i < list.size(); i++) {
				Double price1 = Double.parseDouble(list.get(i).get("price"));
				Double price2 = Double.parseDouble(list.get(i - 1).get("price"));
				if (Math.abs(price1 - price2) > 4) {
					System.err.println(list.get(i - 1));
					System.err.println(list.get(i));
					System.err.println();
				}
			}
			
//			List<Map<String, String>> list = new DbHelper().select("select * from gold where modifyTime >= '2018-03-06 10:44:56' and modifyTime <= '2018-03-06 11:44:56' order by id");
//			for (Map<String, String> map : list) {
//				System.err.println(map);
//			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
