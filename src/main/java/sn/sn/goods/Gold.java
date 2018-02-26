package sn.sn.goods;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import sn.sn.constant.IConstant;
import sn.sn.db.DbHelper;

/**
 * 黄金
 * @author 王超
 */
public class Gold {
	
	/**
	 * 获得黄金价格
	 * @return 黄金价格
	 * @throws Throwable
	 */
	private Double getData() throws Throwable {
		Double price = null;
		
		URL u = new URL(IConstant.GOODS_URL);
		URLConnection con = u.openConnection();
		try(InputStream inputStream = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));) {
			String temp;
			while ((temp = br.readLine()) != null) {
				if (price == null && temp.indexOf(IConstant.GOLD_TEXT) != -1) price = 0d;
				if (price != null && price == 0 && temp.trim().matches(IConstant.IS_DOUBLE)) price = Double.parseDouble(temp.replaceAll(",", ""));
				if (price != null && price != 0) break;
			}
		}
		return price;
	}
	
	/**
	 * 将黄金价格写入数据库
	 * @throws Throwable
	 */
	public void insertPrice() throws Throwable {
		Double price = getData();
		String sql = "insert into gold(price) values(" + price + ")";
		int res = new DbHelper().insert(sql);
		System.out.println("gold：" + res + "；" + price + "；" + new Date());
	}
}
