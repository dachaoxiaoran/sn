package sn.sn.timer;

import java.util.Calendar;
import java.util.Timer;

import static sn.sn.constant.IConstant.*;

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
		calendar.set(TIMER_YEAR, TIMER_MONTH, TIMER_DATE, TIMER_HOUR, TIMER_MINUTE, TIMER_SECOND);
		timer.schedule(new RateTimerTask(), calendar.getTime(), TIMER_WAIT);
	}
}
