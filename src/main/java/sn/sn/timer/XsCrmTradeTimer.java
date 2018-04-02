package sn.sn.timer;

import java.util.Calendar;
import java.util.Timer;

import static sn.sn.constant.IConstant.*;

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
		calendar.set(TIMER_YEAR, TIMER_MONTH, TIMER_DATE, TIMER_HOUR, TIMER_MINUTE, TIMER_SECOND);
		timer.schedule(new XsCrmTradeTimerTask(), calendar.getTime(), TIMER_WAIT);
	}
}
