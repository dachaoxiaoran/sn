package sn.sn.timer;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import sn.sn.currency.Currency;
import sn.sn.goods.Gold;

/**
 * 汇率任务
 * @author 王超
 */
public class RateTimerTask extends TimerTask {

	@Override
	public void run() {
		int day = 0;
		int hour = 0;
		while (true) {
			try {
				day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
				hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				if (day == 7 && hour > 5) break;	//周六早晨5点
				int rateResult = new Currency().insertRate();
				System.out.println("rate：" + rateResult + "；" + new Date());
				int goldResult = new Gold().insertPrice();
				System.out.println("gold：" + goldResult + "；" + new Date());
			} catch(Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
