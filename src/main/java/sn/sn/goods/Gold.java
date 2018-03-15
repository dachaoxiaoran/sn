package sn.sn.goods;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import sn.sn.constant.IConstant;
import sn.sn.db.DbHelper;

/**
 * 黄金
 * @author 王超
 */
public class Gold {
	
	private static Double globalPrice = null;
	
	private static Date globalDate = null;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 获得黄金价格
	 * @return 黄金价格
	 * @throws Throwable
	 */
	private Double getData() throws Throwable {
		Double price = null;
		
		URL u = new URL(IConstant.XAUUSD_URL + System.currentTimeMillis());
		URLConnection con = u.openConnection();
		con.setConnectTimeout(IConstant.READ_TIME_OUT);
		con.setReadTimeout(IConstant.READ_TIME_OUT);
		try(InputStream inputStream = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));) {
			String temp;
			while ((temp = br.readLine()) != null) {
				if (!temp.trim().equals("")) {
					temp = temp.substring(temp.indexOf(IConstant.XAUUSD_TEXT) + IConstant.XAUUSD_TEXT.length());
					temp = temp.substring(0, temp.indexOf("\""));
					price = Double.parseDouble(temp);
				}
			}
		}
		return price;
	}
	
	/**
	 * 将黄金价格写入数据库
	 * @throws Throwable
	 */
	public void insertPrice() throws Throwable {
		boolean isHistory = false;
		Date date = new Date();
		Double price = getData();
		if (globalPrice != null) {
			if (globalPrice.doubleValue() == price.doubleValue()) {	//中间的重复数据不入库
				globalDate = date;
				isHistory = true;
				return;
			} else if (isHistory) {
				String globalDateStr = dateFormat.format(globalDate);
				String sql = "insert into gold(price, modifyTime) values(" + globalPrice + ", '" + globalDateStr + "')";
				int res = new DbHelper().insert(sql);
				isHistory = false;
				System.out.println("gold：" + res + "；" + globalPrice + "；" + globalDateStr);
				System.out.println();
			}
		}
		
		globalPrice = price;
		globalDate = date;
		String dateStr = dateFormat.format(date);
		String sql = "insert into gold(price, modifyTime) values(" + price + ", '" + dateStr + "')";
		int res = new DbHelper().insert(sql);
		System.out.println("gold：" + res + "；" + price + "；" + dateStr);
		System.out.println();
	}	
}
