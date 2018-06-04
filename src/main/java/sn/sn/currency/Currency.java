package sn.sn.currency;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.TextArea;

import static sn.sn.constant.IConstant.*;
import sn.sn.db.DbHelper;

/**
 * 获得汇率数据
 * @author 王超
 */
public class Currency {
	
	private static Double globalPrice = null;
	
	private static Double eurusd = null;
	private static Double usdjpy = null;
	private static Double gbpusd = null;
	private static Double usdcad = null;
	private static Double usdsek = null;
	private static Double usdchf = null;
	private static Double usdcny = null;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
	
	private DecimalFormat decimalFormat = new DecimalFormat("#,##0.0000");//格式化设置  
	private DecimalFormat decimalFormatJP = new DecimalFormat("#,##0.00");//格式化设置  
	
	private TextArea textArea;
	
	public Currency(TextArea textArea) {
		this.textArea = textArea;
	}
	
	/**
	 * 获得欧元->美元汇率，美元->日元汇率，英镑->美元汇率，美元->加元汇率，美元->瑞典克朗汇率，美元->瑞士法郎汇率，美元->人民币汇率，美指
	 * @return 欧元->美元汇率，美元->日元汇率，英镑->美元汇率，美元->加元汇率，美元->瑞典克朗汇率，美元->瑞士法郎汇率，美元->人民币汇率，美指
	 * @throws Throwable
	 */
	private Map<String, Double> getData() throws Throwable {
Map<String, Double> resMap = new HashMap<>();
		
		URL u = new URL(MONEY_URL);
		URLConnection con = u.openConnection();
		con.setConnectTimeout(READ_TIME_OUT);
		con.setReadTimeout(READ_TIME_OUT);
		try(InputStream inputStream = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, ENCODE));) {
			String temp;
			while ((temp = br.readLine()) != null) {
				String[] aTemp = temp.split(",");
				if (!resMap.containsKey("eurusd")) {
					resMap.put("eurusd", Double.parseDouble(aTemp[1]));
				} else if (!resMap.containsKey("usdjpy")) {
					resMap.put("usdjpy", Double.parseDouble(aTemp[1]));
				} else if (!resMap.containsKey("gbpusd")) {
					resMap.put("gbpusd", Double.parseDouble(aTemp[1]));
				} else if (!resMap.containsKey("usdcad")) {
					resMap.put("usdcad", Double.parseDouble(aTemp[1]));
				} else if (!resMap.containsKey("usdsek")) {
					resMap.put("usdsek", Double.parseDouble(aTemp[1]));
				} else if (!resMap.containsKey("usdchf")) {
					resMap.put("usdchf", Double.parseDouble(aTemp[1]));
				} else if (!resMap.containsKey("usdcny")) {
					resMap.put("usdcny", Double.parseDouble(aTemp[1]));
				} else if (!resMap.containsKey("usdx")) {
					resMap.put("usdx", Double.parseDouble(aTemp[1]));
				}
			}
		}
		return resMap;
	}
	
	/**
	 * 将汇率数据写入数据库
	 * @throws Throwable
	 */
	public void insertRate() throws Throwable {
		Date date = new Date();
		Map<String, Double> map = getData();
		if (globalPrice != null) {
			if (globalPrice.doubleValue() == map.get("usdx").doubleValue()) {	//中间的重复数据不入库
				return;
			}
		}
		
		String change = "";
		String changeUsdeur = "";
		String changeUsdjpy = "";
		String changeUsdgbp = "";
		String changeUsdcad = "";
		String changeUsdsek = "";
		String changeUsdchf = "";
		String changeUsdcny = "";
		
		if (globalPrice != null) {
			double changeRes = new BigDecimal(map.get("usdx") - globalPrice).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (changeRes >= 0) {
				change += "+" + decimalFormat.format(changeRes);
			} else {
				change = "" + decimalFormat.format(changeRes);
			}
			
			double changeUsdeurRes = new BigDecimal(1 / map.get("eurusd") - 1 / eurusd).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (changeUsdeurRes >= 0) {
				changeUsdeur += "+" + decimalFormat.format(changeUsdeurRes);
			} else {
				changeUsdeur = "" + decimalFormat.format(changeUsdeurRes);
			}
			
			double changeUsdjpyRes = new BigDecimal(map.get("usdjpy") - usdjpy).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (changeUsdjpyRes >= 0) {
				changeUsdjpy += "+" + decimalFormatJP.format(changeUsdjpyRes);
			} else {
				changeUsdjpy = "" + decimalFormatJP.format(changeUsdjpyRes);
			}
			
			double changeUsdgbpRes = new BigDecimal(1 / map.get("gbpusd") - 1 / gbpusd).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (changeUsdgbpRes >= 0) {
				changeUsdgbp += "+" + decimalFormat.format(changeUsdgbpRes);
			} else {
				changeUsdgbp = "" + decimalFormat.format(changeUsdgbpRes);
			}
			
			double changeUsdcadRes = new BigDecimal(map.get("usdcad") - usdcad).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (changeUsdcadRes >= 0) {
				changeUsdcad += "+" + decimalFormat.format(changeUsdcadRes);
			} else {
				changeUsdcad = "" + decimalFormat.format(changeUsdcadRes);
			}
			
			double changeUsdsekRes = new BigDecimal(map.get("usdsek") - usdsek).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (changeUsdsekRes >= 0) {
				changeUsdsek += "+" + decimalFormat.format(changeUsdsekRes);
			} else {
				changeUsdsek = "" + decimalFormat.format(changeUsdsekRes);
			}
			
			double changeUsdchfRes = new BigDecimal(map.get("usdchf") - usdchf).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (changeUsdchfRes >= 0) {
				changeUsdchf += "+" + decimalFormat.format(changeUsdchfRes);
			} else {
				changeUsdchf = "" + decimalFormat.format(changeUsdchfRes);
			}
			
			double changeUsdcnyRes = new BigDecimal(map.get("usdcny") - usdcny).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (changeUsdcnyRes >= 0) {
				changeUsdcny += "+" + decimalFormat.format(changeUsdcnyRes);
			} else {
				changeUsdcny = "" + decimalFormat.format(changeUsdcnyRes);
			}
		}
		globalPrice = map.get("usdx");
		eurusd = map.get("eurusd");
		usdjpy = map.get("usdjpy");
		gbpusd = map.get("gbpusd");
		usdcad = map.get("usdcad");
		usdsek = map.get("usdsek");
		usdchf = map.get("usdchf");
		usdcny = map.get("usdcny");
		
		String dateStr = dateFormat.format(date);
		String sql = "insert into rate(eurusd, usdjpy, gbpusd, usdcad, usdsek, usdchf, usdcny, usdx, modifyTime) values("
					+ map.get("eurusd") + ", "
					+ map.get("usdjpy") + ", "
					+ map.get("gbpusd") + ", "
					+ map.get("usdcad") + ", "
					+ map.get("usdsek") + ", "
					+ map.get("usdchf") + ", "
					+ map.get("usdcny") + ", "
					+ map.get("usdx") + ", '" + dateStr + "')";
		new DbHelper().insert(sql);
		if (textArea.getLength() > TEXTAREA_LIMIT) textArea.clear();
		textArea.appendText(map.get("usdx").toString() + change + "；\t"
							+ "usdeur：" + new BigDecimal(1 / map.get("eurusd")).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue() + changeUsdeur + "；\t"
							+ "usdjpy：" + new BigDecimal(map.get("usdjpy")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + changeUsdjpy + "；\t"
							+ "usdgbp：" + new BigDecimal(1 / map.get("gbpusd")).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue() + changeUsdgbp + "；\t"
							+ "usdcad：" + new BigDecimal(map.get("usdcad")).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue() + changeUsdcad + "；\t"
							+ "usdsek：" + new BigDecimal(map.get("usdsek")).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue() + changeUsdsek + "；\t"
							+ "usdchf：" + new BigDecimal(map.get("usdchf")).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue() + changeUsdchf + "；\t"
							+ "usdcny：" + new BigDecimal(map.get("usdcny")).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue() + changeUsdcny + "；\t"
							+ dateStr + "\n");
	}
}
