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
	public List<Map<String, Object>> select(String sql)throws Throwable {
		List<Map<String, Object>> list = new ArrayList<>();
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
				Map<String, Object> map = new HashMap<>();
				for (int i = 0; i < names.length; i++) map.put(names[i], resultSet.getObject(i + 1));
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
}
