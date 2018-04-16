package sn.sn.bond;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.scene.control.TextArea;

import static sn.sn.constant.IConstant.*;
import sn.sn.db.DbHelper;

/**
 * 
 * @author 王超
 */
public class Bond {
	
	private static Double globalPrice = null;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
	
	private TextArea textArea;
	
	public Bond(TextArea textArea) {
		this.textArea = textArea;
	}

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
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, ENCODE));) {
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
		Date date = new Date();
		Double price = getData();
		if (globalPrice != null) {
			if (globalPrice.doubleValue() == price.doubleValue()) {	//中间的重复数据不入库
				return;
			}
		}
		
		globalPrice = price;
		String dateStr = dateFormat.format(date);
		new DbHelper().insert(String.format(INSERT_BOND, price, dateStr));
		if (textArea.getLength() > TEXTAREA_LIMIT) textArea.clear();
		textArea.appendText("bond：" + price + "；    " + dateStr + "\n");
	}
}
