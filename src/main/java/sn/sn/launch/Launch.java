package sn.sn.launch;

import sn.sn.currency.Currency;

/**
 * 
 * @author 王超
 */
public class Launch {

	public static void main(String[] args) {
		while (true) {
			try {
				new Currency().insertRate();
			} catch(Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
