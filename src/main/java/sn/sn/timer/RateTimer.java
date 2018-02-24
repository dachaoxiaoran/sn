package sn.sn.timer;

import java.util.Calendar;
import java.util.Timer;

/**
 * 汇率定时
 * @author 王超
 */
public class RateTimer {

	/**
	 * 运行汇率定时任务，从周一早晨5点开始
	 */
	public void run() {
		Timer timer = new Timer();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2018, 1, 26, 5, 0, 0);
		//calendar.set(2018, 1, 23, 15, 49, 0);
		timer.schedule(new RateTimerTask(), calendar.getTime(), 604800000);
		//timer.schedule(new RateTimerTask(), calendar.getTime(), 1000);
	}
}
