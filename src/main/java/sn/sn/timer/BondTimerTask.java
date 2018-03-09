package sn.sn.timer;

import java.util.Calendar;
import java.util.TimerTask;

import sn.sn.bond.Bond;
import sn.sn.constant.IConstant;

/**
 * 债权任务
 * @author 王超
 */
public class BondTimerTask extends TimerTask {

	private static boolean isRunning = false;
	
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
			try {
				if (System.currentTimeMillis() - currentMill <= 2000) continue;
				currentMill = System.currentTimeMillis();
				day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
				hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				if (day == IConstant.TIMER_DAY_END && hour > IConstant.TIMER_HOUR_END) {
					isRunning = false;
					break;
				}
				new Bond().insertPrice();
			} catch(Throwable e) {
				System.out.println("bond_error:" + e.getMessage());
			}
		}
	}
}
