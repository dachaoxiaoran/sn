package sn.sn.xscrm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import sn.sn.constant.IConstant;
import sn.sn.db.DbHelper;

/**
 * 鑫圣客户交易数据
 * @author 王超
 */
public class XsCrmTradeData {

	private static List<String> globalList = new ArrayList<>();
	
	/**
	 * 获得鑫圣客户交易数据
	 * @return
	 * @throws Throwable
	 */
	private Map<String, Object> getData() throws Throwable {
		Map<String, Object> res = new HashMap<>();
		URL u = new URL(IConstant.XS_CRM_URL);
		URLConnection con = u.openConnection();
		con.setConnectTimeout(IConstant.READ_TIME_OUT);
		con.setReadTimeout(IConstant.READ_TIME_OUT);
		con.setDoOutput(true);
		OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
		osw.write("data: { size: 7, days: 7 }");
		osw.flush();
		osw.close();
		try(InputStream inputStream = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));) {
			String temp;
			while ((temp = br.readLine()) != null) {
				res = JSON.parseObject(temp, new TypeReference<Map<String, Object>>() {});
			}
		}
		return res;
	}
	
	/**
	 * 过滤鑫圣客户交易数据，只关心伦敦金
	 * @param originMap
	 * @return
	 * @throws Throwable
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map> filterData(Map<String, Object> originMap) throws Throwable {
		String message = ((JSONArray) originMap.get("Message")).toJSONString();
		List<Map> messageList = JSONObject.parseArray(message, Map.class);
		for (int i = 0; i < messageList.size(); i++) {
			Map<String, Object> map = messageList.get(i);
			if (!map.get("strSymbol").toString().equals(IConstant.LDJ_TEXT)) {
				messageList.remove(i);
				i--;
			}
		}
		return messageList;
	}
	
	/**
	 * 插入鑫圣客户交易数据
	 * @throws Throwable
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void insert() throws Throwable {
		List<String> tempList = new ArrayList<>();
		List<Map> list = filterData(getData());
		for (Map<String, Object> map : list) {
			map.remove("CreateTime");
			String mapStr = JSON.toJSONString(map);
			tempList.add(mapStr);
			boolean exist = false;
			for (String globalStr : globalList) {
				if (globalStr.equals(mapStr)) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				String sql = "insert into xscrmtrade(login, time, price, openClose, buySell, volume) values('" + map.get("strLogin") + "', '" + map.get("strTime") + "', " 
						+ map.get("dOpenPrice") + ", '" + map.get("strOpenClose") + "', '" + map.get("strBuySell") + "', " + map.get("dVolume") + ")";
				int res = new DbHelper().insert(sql);
				System.out.println("xscrmtrade：" + res + "；" + map.get("dOpenPrice") + "；" + map.get("strOpenClose") + "；" + map.get("strBuySell") + "；" + map.get("dVolume")
					+ "；" + map.get("strTime"));
				System.out.println();
			}
		}
		globalList.addAll(tempList);
		while (globalList.size() > 100) {
			globalList.remove(0);
		}
	}
}
