package sn.sn.goods;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.scene.control.TextArea;

import static sn.sn.constant.IConstant.*;
import sn.sn.db.DbHelper;

/**
 * 黄金
 * @author 王超
 */
public class Gold {
	
	private static Double globalPrice = null;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
	
	private TextArea textArea;
	
	public Gold(TextArea textArea) {
		this.textArea = textArea;
	}
	
	/**
	 * 获得黄金价格
	 * @return 黄金价格
	 * @throws Throwable
	 */
	private Double getData() throws Throwable {
		Double price = null;
		
		URL u = new URL(XAUUSD_URL + System.currentTimeMillis());
		URLConnection con = u.openConnection();
		con.setConnectTimeout(READ_TIME_OUT);
		con.setReadTimeout(READ_TIME_OUT);
		try(InputStream inputStream = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));) {
			String temp;
			while ((temp = br.readLine()) != null) {
				if (!temp.trim().equals("")) {
					temp = temp.substring(temp.indexOf(XAUUSD_TEXT) + XAUUSD_TEXT.length());
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
		Date date = new Date();
		Double price = getData();
		if (globalPrice != null) {
			if (globalPrice.doubleValue() == price.doubleValue()) {	//中间的重复数据不入库
				return;
			}
		}
		
		String change = "";
		if (globalPrice != null) {
			double changeRes = new BigDecimal(price - globalPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (changeRes >= 0) {
				change += "\t+" + changeRes;
			} else {
				change = "\t" + changeRes;
			}
		}
		globalPrice = price;
		String dateStr = dateFormat.format(date);
		String sql = "insert into gold(price, modifyTime) values(" + price + ", '" + dateStr + "')";
		new DbHelper().insert(sql);
		if (textArea.getLength() > 10000) textArea.clear();
		textArea.appendText("gold：" + price + change + "；\t" + dateStr + "\n");
	}	
}
