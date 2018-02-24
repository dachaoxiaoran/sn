package sn.sn.timer;

import java.util.Calendar;
import java.util.Timer;

import sn.sn.constant.IConstant;

/**
 * 黄金定时
 * @author 王超
 */
public class GoldTimer {

	/**
	 * 运行黄金定时任务，从周一早晨5点开始
	 */
	public void run() {
		Timer timer = new Timer();
		Calendar calendar = Calendar.getInstance();
		calendar.set(IConstant.TIMER_YEAR, IConstant.TIMER_MONTH, IConstant.TIMER_DATE, IConstant.TIMER_HOUR, IConstant.TIMER_MINUTE, IConstant.TIMER_SECOND);
		timer.schedule(new GoldTimerTask(), calendar.getTime(), IConstant.TIMER_WAIT);
	}
}
