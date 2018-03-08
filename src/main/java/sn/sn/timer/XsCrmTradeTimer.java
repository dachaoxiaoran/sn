package sn.sn.timer;

import java.util.Calendar;
import java.util.Timer;

import sn.sn.constant.IConstant;

/**
 * 鑫圣客户交易数据获取定时
 * @author 王超
 */
public class XsCrmTradeTimer {

	/**
	 * 运行鑫圣客户交易数据获取任务，从周一早晨5点开始
	 */
	public void run() {
		Timer timer = new Timer();
		Calendar calendar = Calendar.getInstance();
		calendar.set(IConstant.TIMER_YEAR, IConstant.TIMER_MONTH, IConstant.TIMER_DATE, IConstant.TIMER_HOUR, IConstant.TIMER_MINUTE, IConstant.TIMER_SECOND);
		timer.schedule(new XsCrmTradeTimerTask(), calendar.getTime(), IConstant.TIMER_WAIT);
	}
}
