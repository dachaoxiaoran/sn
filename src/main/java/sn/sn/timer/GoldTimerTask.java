package sn.sn.timer;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import sn.sn.constant.IConstant;
import sn.sn.goods.Gold;

/**
 * 黄金任务
 * @author 王超
 */
public class GoldTimerTask extends TimerTask {

	@Override
	public void run() {
		int day = 0;
		int hour = 0;
		long currentMill = 0;
		while (true) {
			try {
				if (System.currentTimeMillis() - currentMill <= 1000) continue;
				currentMill = System.currentTimeMillis();
				day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
				hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				if (day == IConstant.TIMER_DAY_END && hour > IConstant.TIMER_HOUR_END) break;
				int goldResult = new Gold().insertPrice();
				System.out.println("gold：" + goldResult + "；" + new Date());
			} catch(Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
