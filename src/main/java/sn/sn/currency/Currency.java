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
	 * 美指计算
	 * @param eurusd	欧元->美元汇率
	 * @param usdjpy	美元->日元汇率
	 * @param gbpusd	英镑->美元汇率
	 * @param usdcad	美元->加元汇率
	 * @param usdsek	美元->瑞典克朗汇率
	 * @param usdchf	美元->瑞士法郎汇率
	 * @return	美指
	 */
	private double calculateUsdx(double eurusd, double usdjpy, double gbpusd, double usdcad, double usdsek, double usdchf) {
		return Math.pow(eurusd, EURUSD) * Math.pow(usdjpy, USDJPY) * Math.pow(gbpusd, GBPUSD) * Math.pow(usdcad, USDCAD) * Math.pow(usdsek, USDSEK) * Math.pow(usdchf, USDCHF) * USDX;
	}
	
	/**
	 * 获得欧元->美元汇率，美元->日元汇率，英镑->美元汇率，美元->加元汇率，美元->瑞典克朗汇率，美元->瑞士法郎汇率，美元->人民币汇率，美指
	 * @return 欧元->美元汇率，美元->日元汇率，英镑->美元汇率，美元->加元汇率，美元->瑞典克朗汇率，美元->瑞士法郎汇率，美元->人民币汇率，美指
	 * @throws Throwable
	 */
	private Map<String, Double> getData() throws Throwable {
		Map<String, Double> resMap = null;
		Double eurusd = null;
		Double usdjpy = null;
		Double gbpusd = null;
		Double usdcad = null;
		Double usdsek = null;
		Double usdchf = null;
		Double usdcny = null;
		
		URL u = new URL(CURRENCY_URL);
		URLConnection con = u.openConnection();
		con.setConnectTimeout(READ_TIME_OUT);
		con.setReadTimeout(READ_TIME_OUT);
		try(InputStream inputStream = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));) {
			String temp;
			while ((temp = br.readLine()) != null) {
				if (eurusd == null && temp.indexOf(EURUSD_TEXT) != -1) eurusd = 0d;
				if (usdjpy == null && temp.indexOf(USDJPY_TEXT) != -1) usdjpy = 0d;
				if (gbpusd == null && temp.indexOf(GBPUSD_TEXT) != -1) gbpusd = 0d;
				if (usdcad == null && temp.indexOf(USDCAD_TEXT) != -1) usdcad = 0d;
				if (usdsek == null && temp.indexOf(USDSEK_TEXT) != -1) usdsek = 0d;
				if (usdchf == null && temp.indexOf(USDCHF_TEXT) != -1) usdchf = 0d;
				if (usdcny == null && temp.indexOf(USDCNY_TEXT) != -1) usdcny = 0d;
				
				if (eurusd != null && eurusd == 0 && temp.trim().matches(IS_DOUBLE)) eurusd = Double.parseDouble(temp);
				if (usdjpy != null && usdjpy == 0 && temp.trim().matches(IS_DOUBLE)) usdjpy = Double.parseDouble(temp);
				if (gbpusd != null && gbpusd == 0 && temp.trim().matches(IS_DOUBLE)) gbpusd = Double.parseDouble(temp);
				if (usdcad != null && usdcad == 0 && temp.trim().matches(IS_DOUBLE)) usdcad = Double.parseDouble(temp);
				if (usdsek != null && usdsek == 0 && temp.trim().matches(IS_DOUBLE)) usdsek = Double.parseDouble(temp);
				if (usdchf != null && usdchf == 0 && temp.trim().matches(IS_DOUBLE)) usdchf = Double.parseDouble(temp);
				if (usdcny != null && usdcny == 0 && temp.trim().matches(IS_DOUBLE)) usdcny = Double.parseDouble(temp);
				
				if (eurusd != null && eurusd != 0 && usdjpy != null && usdjpy != 0 && gbpusd != null && gbpusd != 0 && usdcad != null && usdcad != 0 && usdsek != null 
						&& usdsek != 0 && usdchf != null && usdchf != 0 && usdcny != null) break;
			}
		}
		resMap = new HashMap<>();
		resMap.put("eurusd", eurusd);
		resMap.put("usdjpy", usdjpy);
		resMap.put("gbpusd", gbpusd);
		resMap.put("usdcad", usdcad);
		resMap.put("usdsek", usdsek);
		resMap.put("usdchf", usdchf);
		resMap.put("usdcny", usdcny);
		resMap.put("usdx", calculateUsdx(eurusd, usdjpy, gbpusd, usdcad, usdsek, usdchf));
		
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
		if (textArea.getLength() > 10000) textArea.clear();
		textArea.appendText("rate：" + map.get("usdx").toString().substring(0, 7) + change + "；\t"
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
