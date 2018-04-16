package sn.sn.xscrm;

import static sn.sn.constant.IConstant.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import sn.sn.db.DbHelper;

/**
 * 金道客户交易数据
 * @author 王超
 */
public class JdCrmTradeData {
	
	private static List<String> globalList = new ArrayList<>();

	/**
	 * 获得金道客户交易数据
	 * @return
	 * @throws Throwable
	 */
	private Map<String, Object> getData() throws Throwable {
		Map<String, Object> res = new HashMap<>();
		URL u = new URL(JD_CRM_URL);
		URLConnection con = u.openConnection();
		con.setConnectTimeout(READ_TIME_OUT);
		con.setReadTimeout(READ_TIME_OUT);
		con.setDoOutput(true);
		try(InputStream inputStream = con.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, ENCODE));) {
				String temp;
				while ((temp = br.readLine()) != null) {
					res = JSON.parseObject(temp, new TypeReference<Map<String, Object>>() {});
				}
			}
			return res;
	}
	
	/**
	 * 插入金道客户交易数据
	 * @throws Throwable
	 */
	public void insert() throws Throwable {
		Map<String, Object> dataMap = getData();
		int i = 1;
		while (dataMap.containsKey("loginname" + i)) {
			if (dataMap.get("productCode" + i).toString().equals(LDJ_TEXT)) {
				Map<String, Object> insertMap = new HashMap<>();
				insertMap.put("login", dataMap.get("loginname" + i));
				insertMap.put("time", dataMap.get("tradeTime" + i));
				insertMap.put("price", dataMap.get("price" + i));
				if (dataMap.get("operateType" + i).toString().equals(CREATE)) insertMap.put("openClose", "open");
				else if (dataMap.get("operateType" + i).toString().equals(CLEAR)) insertMap.put("openClose", "close");
				if (dataMap.get("orderType" + i).toString().equals(SELL)) insertMap.put("buySell", "sell");
				else if (dataMap.get("orderType" + i).toString().equals(BUY)) insertMap.put("buySell", "buy");
				insertMap.put("volume", dataMap.get("lot" + i));
				
				String mapStr = JSON.toJSONString(insertMap);
				boolean exist = false;
				for (String globalStr : globalList) {
					if (globalStr.equals(mapStr)) {
						exist = true;
						break;
					}
				}
				if (!exist) {
					globalList.add(mapStr);
					while (globalList.size() > 100) {
						globalList.remove(0);
					}
					String sql = "insert into xscrmtrade(login, time, price, openClose, buySell, volume, source) values('" + insertMap.get("login") + "', '" + insertMap.get("time")
						+ "', " + insertMap.get("price") + ", '" + insertMap.get("openClose") + "', '" + insertMap.get("buySell") + "', " + insertMap.get("volume") + ", 'JDGJS')";
					new DbHelper().insert(sql);
				}
			}
			
			i++;
		}
		
	}
}
