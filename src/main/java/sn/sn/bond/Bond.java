package sn.sn.bond;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import static sn.sn.constant.IConstant.*;
import sn.sn.db.DbHelper;

/**
 * 
 * @author 王超
 */
public class Bond {
	
	private static Double globalPrice = null;
	
	private static Date globalDate = null;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);

	/**
	 * 获得美国10年期政府债券价格
	 * @return 美国10年期政府债券价格
	 * @throws Throwable
	 */
	private Double getData() throws Throwable {
		Double price = null;
		
		URL u = new URL(BONDS_URL);
		URLConnection con = u.openConnection();
		con.setConnectTimeout(READ_TIME_OUT);
		con.setReadTimeout(READ_TIME_OUT);
		try(InputStream inputStream = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));) {
			String temp;
			while ((temp = br.readLine()) != null) {
				if (price == null && temp.indexOf(BONDS_TEXT) != -1) price = 0d;
				if (price != null && price == 0 && temp.trim().matches(IS_DOUBLE)) price = Double.parseDouble(temp);
				if (price != null && price != 0) break;
			}
		}
		return price;
	}
	
	/**
	 * 将美国10年期政府债券价格写入数据库
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
				String sql = "insert into bond(price, modifyTime) values(" + globalPrice + ", '" + globalDateStr + "')";
				int res = new DbHelper().insert(sql);
				isHistory = false;
				System.out.println("bond：" + res + "；" + globalPrice + "；" + globalDateStr);
			}
		}
		
		globalPrice = price;
		globalDate = date;
		String dateStr = dateFormat.format(date);
		String sql = "insert into bond(price, modifyTime) values(" + price + ", '" + dateStr + "')";
		int res = new DbHelper().insert(sql);
		System.out.println("bond：" + res + "；" + price + "；" + dateStr);
	}
}
