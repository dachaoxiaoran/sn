package sn.sn.launch;

import sn.sn.timer.BondTimer;
import sn.sn.timer.GoldTimer;
import sn.sn.timer.RateTimer;

/**
 * 
 * @author 王超
 */
public class Launch {

	public static void main(String[] args) {
		new RateTimer().run();
		new GoldTimer().run();
		new BondTimer().run();
	}
}
