package sn.sn.xscrm;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import sn.sn.db.DbHelper;

/** 
* @author 大超小然: 
* @datetime 2018年3月11日 下午9:26:57 
* @description 
*/
public class WinAndFail {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 显示当天buySell比例
	 * @throws Throwable
	 */
	public void buySell() throws Throwable {
		String date = dateFormat.format(System.currentTimeMillis());
		String buySql = "select sum(volume) volume from xscrmtrade where modifyTime >= '" + date + " 06:00:00' and openClose = 'open' and buySell = 'buy';";
		String sellSql = "select sum(volume) volume from xscrmtrade where modifyTime >= '" + date + " 06:00:00' and openClose = 'open' and buySell = 'sell';";
		
		List<Map<String, Object>> buyList = new DbHelper().select(buySql);
		List<Map<String, Object>> sellList = new DbHelper().select(sellSql);
		
		if (buyList.get(0).get("volume") != null && sellList.get(0).get("volume") != null) {
			Double buyCount = new BigDecimal(buyList.get(0).get("volume").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			Double sellCount = new BigDecimal(sellList.get(0).get("volume").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			System.out.println("buySell：" + buyCount + " : " + sellCount);
		}
	}
}
