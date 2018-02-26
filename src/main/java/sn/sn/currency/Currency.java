package sn.sn.currency;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sn.sn.constant.IConstant;
import sn.sn.db.DbHelper;

/**
 * 获得汇率数据
 * @author 王超
 */
public class Currency {

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
		return Math.pow(eurusd, IConstant.EURUSD) * 
				Math.pow(usdjpy, IConstant.USDJPY) * 
				Math.pow(gbpusd, IConstant.GBPUSD) * 
				Math.pow(usdcad, IConstant.USDCAD) * 
				Math.pow(usdsek, IConstant.USDSEK) * 
				Math.pow(usdchf, IConstant.USDCHF) * 
				IConstant.USDX;
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
		
		URL u = new URL(IConstant.CURRENCY_URL);
		URLConnection con = u.openConnection();
		try(InputStream inputStream = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));) {
			String temp;
			while ((temp = br.readLine()) != null) {
				if (eurusd == null && temp.indexOf(IConstant.EURUSD_TEXT) != -1) eurusd = 0d;
				if (usdjpy == null && temp.indexOf(IConstant.USDJPY_TEXT) != -1) usdjpy = 0d;
				if (gbpusd == null && temp.indexOf(IConstant.GBPUSD_TEXT) != -1) gbpusd = 0d;
				if (usdcad == null && temp.indexOf(IConstant.USDCAD_TEXT) != -1) usdcad = 0d;
				if (usdsek == null && temp.indexOf(IConstant.USDSEK_TEXT) != -1) usdsek = 0d;
				if (usdchf == null && temp.indexOf(IConstant.USDCHF_TEXT) != -1) usdchf = 0d;
				if (usdcny == null && temp.indexOf(IConstant.USDCNY_TEXT) != -1) usdcny = 0d;
				
				if (eurusd != null && eurusd == 0 && temp.trim().matches(IConstant.IS_DOUBLE)) eurusd = Double.parseDouble(temp);
				if (usdjpy != null && usdjpy == 0 && temp.trim().matches(IConstant.IS_DOUBLE)) usdjpy = Double.parseDouble(temp);
				if (gbpusd != null && gbpusd == 0 && temp.trim().matches(IConstant.IS_DOUBLE)) gbpusd = Double.parseDouble(temp);
				if (usdcad != null && usdcad == 0 && temp.trim().matches(IConstant.IS_DOUBLE)) usdcad = Double.parseDouble(temp);
				if (usdsek != null && usdsek == 0 && temp.trim().matches(IConstant.IS_DOUBLE)) usdsek = Double.parseDouble(temp);
				if (usdchf != null && usdchf == 0 && temp.trim().matches(IConstant.IS_DOUBLE)) usdchf = Double.parseDouble(temp);
				if (usdcny != null && usdcny == 0 && temp.trim().matches(IConstant.IS_DOUBLE)) usdcny = Double.parseDouble(temp);
				
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
		Map<String, Double> map = new Currency().getData();
		String sql = "insert into rate(eurusd, usdjpy, gbpusd, usdcad, usdsek, usdchf, usdcny, usdx) values("
					+ map.get("eurusd") + ", "
					+ map.get("usdjpy") + ", "
					+ map.get("gbpusd") + ", "
					+ map.get("usdcad") + ", "
					+ map.get("usdsek") + ", "
					+ map.get("usdchf") + ", "
					+ map.get("usdcny") + ", "
					+ map.get("usdx") + ")";
		int res = new DbHelper().insert(sql);
		System.out.println("rate：" + res + "；" + map.get("usdx") + "；" + new Date());
	}
}
