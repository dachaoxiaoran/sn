package sn.sn.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

import sn.sn.constant.IConstant;
import sn.sn.currency.Currency;

/**
 * 汇率任务
 * @author 王超
 */
public class RateTimerTask extends TimerTask {
	
	private static boolean isRunning = false;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void run() {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if (day == 1) return;				//周日
		if (day == 7 && hour > 6) return;	//周六早6点后
		if (day == 2 && hour < 6) return;	//周一早6点前
		if (isRunning) return;
		long currentMill = 0;
		while (true) {
			isRunning = true;
			try {
				if (System.currentTimeMillis() - currentMill <= 2000) continue;
				currentMill = System.currentTimeMillis();
				day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
				hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				if (day == IConstant.TIMER_DAY_END && hour > IConstant.TIMER_HOUR_END) {
					isRunning = false;
					break;
				}
				new Currency().insertRate();
			} catch(Throwable e) {
				System.out.println("rate_error:" + e.getMessage() + ";" + dateFormat.format(System.currentTimeMillis()));
			}
		}
	}
}
