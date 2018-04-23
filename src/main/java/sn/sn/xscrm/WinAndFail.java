package sn.sn.xscrm;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javafx.scene.control.TextArea;
import sn.sn.db.DbHelper;
import static sn.sn.constant.IConstant.*;

/** 
* @author 大超小然: 
* @datetime 2018年3月11日 下午9:26:57 
* @description 
*/
public class WinAndFail {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private SimpleDateFormat dateFormatTime = new SimpleDateFormat(TIME_FORMAT);
	
	private TextArea textArea;
	
	public WinAndFail(TextArea textArea) {
		this.textArea = textArea;
	}
	
	/**
	 * 显示当天buySell比例
	 * @throws Throwable
	 */
	public void buySell() throws Throwable {
		String date = dateFormat.format(System.currentTimeMillis());
		String buySql = "select sum(volume) volume from xscrmtrade where modifyTime >= '" + date + " 06:00:00' and openClose = 'open' and buySell = 'buy'";
		String sellSql = "select sum(volume) volume from xscrmtrade where modifyTime >= '" + date + " 06:00:00' and openClose = 'open' and buySell = 'sell'";
		
		String buySqlXS = buySql + " and source = 'XSJY'";
		String sellSqlXS = sellSql + " and source = 'XSJY'";
		String buySqlJD = buySql + " and source = 'JDGJS'";
		String sellSqlJD = sellSql + " and source = 'JDGJS'";
		
		List<Map<String, Object>> buyList = new DbHelper().select(buySql);
		List<Map<String, Object>> sellList = new DbHelper().select(sellSql);
		List<Map<String, Object>> buyListXS = new DbHelper().select(buySqlXS);
		List<Map<String, Object>> sellListXS = new DbHelper().select(sellSqlXS);
		List<Map<String, Object>> buyListJD = new DbHelper().select(buySqlJD);
		List<Map<String, Object>> sellListJD = new DbHelper().select(sellSqlJD);
		
		
		if (buyList.get(0).get("volume") != null && sellList.get(0).get("volume") != null) {
			Double buyCount = Double.parseDouble(buyList.get(0).get("volume").toString());
			Double sellCount = Double.parseDouble(sellList.get(0).get("volume").toString());
			Double res = new BigDecimal(buyCount - sellCount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			Double buyCountXS = 0d;
			Double sellCountXS = 0d;
			if (buyListXS.get(0).get("volume") != null) {
				buyCountXS = Double.parseDouble(buyListXS.get(0).get("volume").toString());
			}
			if (sellListXS.get(0).get("volume") != null) {
				sellCountXS = Double.parseDouble(sellListXS.get(0).get("volume").toString());
			}
			Double buyCountJD = 0d;
			Double sellCountJD = 0d;
			if (buyListJD.get(0).get("volume") != null) {
				buyCountJD = Double.parseDouble(buyListJD.get(0).get("volume").toString());
			}
			if (sellListJD.get(0).get("volume") != null) {
				sellCountJD = Double.parseDouble(sellListJD.get(0).get("volume").toString());
			}
			Double resXS = new BigDecimal(buyCountXS - sellCountXS).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			Double resJD = new BigDecimal(buyCountJD - sellCountJD).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			if (textArea.getLength() > TEXTAREA_LIMIT) textArea.clear();
			if (res >= 0) textArea.appendText("buy：" + res + "；");
			else textArea.appendText("sell：" + res + "；");
			textArea.appendText("XS：" + resXS + "；JD：" + resJD + "；" + dateFormatTime.format(System.currentTimeMillis()) + "\n");
		}
	}
}
