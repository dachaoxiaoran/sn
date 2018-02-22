package sn.sn.db;

import java.sql.DriverManager;
import java.sql.SQLException;

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
	
//	public static void main(String[] args) {
//		while (true) {
//			Map<String, Double> map = new Currency().getData();
//			String sql = "insert into rate(eurusd, usdjpy, gbpusd, usdcad, usdsek, usdchf, usdcny, usdx) values("
//					+ map.get("eurusd") + ", "
//					+ map.get("usdjpy") + ", "
//					+ map.get("gbpusd") + ", "
//					+ map.get("usdcad") + ", "
//					+ map.get("usdsek") + ", "
//					+ map.get("usdchf") + ", "
//					+ map.get("usdcny") + ", "
//					+ map.get("usdx") + ")";
//			System.err.println(new DbHelper().insert(sql));
//		}
//	}
}
