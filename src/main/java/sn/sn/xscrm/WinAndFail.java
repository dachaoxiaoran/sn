package sn.sn.xscrm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sn.sn.db.DbHelper;

/** 
* @author 大超小然: 
* @datetime 2018年3月11日 下午9:26:57 
* @description 
*/
public class WinAndFail {

	private int volume = 1;
	
	private double point = 0.5;
	
	/**
	 * 获得用户数据，根据login分组
	 * @param buyOrSell
	 * @return
	 * @throws Throwable
	 */
	private Map<String, List<Map<String, String>>> getData(String buyOrSell) throws Throwable {
		String sql = "select * from sn.xscrmtrade where volume >= " + volume + " and buySell = '" + buyOrSell + "' order by login, id;";
		List<Map<String, String>> list = new DbHelper().select(sql);
		
		Map<String, List<Map<String, String>>> loginMap = new HashMap<>();
		String login = "";
		for (Map<String, String> map : list) {
			if (map.get("login").equals(login)) {
				loginMap.get(map.get("login")).add(map);
			} else {
				loginMap.put(map.get("login"), new ArrayList<Map<String, String>>());
				loginMap.get(map.get("login")).add(map);
				login = map.get("login");
			}
		}
		return loginMap;
	}
	
	
	private Map<String, Map<String, Integer>> getWinAndFail() throws Throwable {
		Map<String, Map<String,Integer>> resMap = new HashMap<>();
		
		Map<String, List<Map<String, String>>> buyMap = getData("buy");
		Map<String, List<Map<String, String>>> sellMap = getData("sell");
		
		Iterator<Entry<String, List<Map<String, String>>>> iterator = buyMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, List<Map<String, String>>> entry = iterator.next();
			String login = entry.getKey();
			List<Map<String, String>> list = entry.getValue();
			
			int win = 0;
			int fail = 0;
			double price = 0d;
			
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				if (price == 0 && map.get("openClose").equals("close")) {
					list.remove(i);
					i--;
					continue;
				}
				if (price == 0 && map.get("openClose").equals("open")) {
					price = Double.parseDouble(map.get("price"));
					list.remove(i);
					i--;
					continue;
				}
				if (price != 0 && map.get("openClose").equals("close")) {
					if (Double.parseDouble(map.get("price")) - price - point * Double.parseDouble(map.get("volume")) > 0) {
						win += 1;
					} else {
						fail += 1;
					}
					list.remove(i);
					i--;
					price = 0;
				}
			}
			Map<String, Integer> winFailMap = new HashMap<>();
			winFailMap.put("win", win);
			winFailMap.put("fail", fail);
			resMap.put(login, winFailMap);
		}
		
		iterator = sellMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, List<Map<String, String>>> entry = iterator.next();
			String login = entry.getKey();
			List<Map<String, String>> list = entry.getValue();
			
			int win = 0;
			int fail = 0;
			double price = 0d;
			
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				if (price == 0 && map.get("openClose").equals("close")) {
					list.remove(i);
					i--;
					continue;
				}
				if (price == 0 && map.get("openClose").equals("open")) {
					price = Double.parseDouble(map.get("price"));
					list.remove(i);
					i--;
					continue;
				}
				if (price != 0 && map.get("openClose").equals("close")) {
					if (price - point * Double.parseDouble(map.get("volume")) - Double.parseDouble(map.get("price")) > 0) {
						win += 1;
					} else {
						fail += 1;
					}
					list.remove(i);
					i--;
					price = 0;
				}
			}
			if (resMap.containsKey(login)) {
				Map<String, Integer> winFailMap = resMap.get(login);
				winFailMap.put("win", winFailMap.get("win") + win);
				winFailMap.put("fail", winFailMap.get("fail") + fail);
				resMap.put(login, winFailMap);
			} else {
				Map<String, Integer> winFailMap = new HashMap<>();
				winFailMap.put("win", win);
				winFailMap.put("fail", fail);
				resMap.put(login, winFailMap);
			}
			
		}
		
		return filterMap(resMap);
	}
	
	/**
	 * 将失败数大于成功数的记录过滤掉
	 * @param map
	 * @return
	 * @throws Throwable
	 */
	private Map<String, Map<String, Integer>> filterMap(Map<String, Map<String, Integer>> map) throws Throwable {
		Map<String, Map<String, Integer>> resMap = new HashMap<>();
		Iterator<Entry<String, Map<String, Integer>>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Map<String, Integer>> entry = iterator.next();
			Map<String, Integer> winFailMap = entry.getValue();
			if (winFailMap.get("win") > winFailMap.get("fail")) {
				resMap.put(entry.getKey(), winFailMap);
			}
		}
		return resMap;
	}
	
	public static void main(String[] args) {
		try {
			System.err.println(new WinAndFail().getWinAndFail());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
}
