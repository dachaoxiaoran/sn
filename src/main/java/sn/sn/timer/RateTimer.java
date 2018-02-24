package sn.sn.timer;

import java.util.Calendar;
import java.util.Timer;

import sn.sn.constant.IConstant;

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
		calendar.set(IConstant.TIMER_YEAR, IConstant.TIMER_MONTH, IConstant.TIMER_DATE, IConstant.TIMER_HOUR, IConstant.TIMER_MINUTE, IConstant.TIMER_SECOND);
		timer.schedule(new RateTimerTask(), calendar.getTime(), IConstant.TIMER_WAIT);
	}
}
