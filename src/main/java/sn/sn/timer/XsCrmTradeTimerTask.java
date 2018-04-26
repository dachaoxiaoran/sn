package sn.sn.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

import static sn.sn.constant.IConstant.*;
import sn.sn.xscrm.XsCrmTradeData;

/**
 * 鑫圣客户交易数据获取任务
 * @author 王超
 */
public class XsCrmTradeTimerTask extends TimerTask {
	
	private static boolean isRunning = false;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);

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
				Thread.sleep(1000);		//鑫圣数据读取快
				if (System.currentTimeMillis() - currentMill <= 2000) continue;
				currentMill = System.currentTimeMillis();
				day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
				hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				if (day == TIMER_DAY_END && hour > TIMER_HOUR_END) {
					isRunning = false;
					break;
				}
				if ((day == 3 || day == 4 || day == 5 || day == 6) && hour == 5) {
					isRunning = false;
					break;
				}
				new XsCrmTradeData().insert();
			} catch(Throwable e) {
				System.out.println("xsCrmTrade_error：" + e.getMessage() + "；    " + dateFormat.format(System.currentTimeMillis()));
			}
		}
		
	}

}
