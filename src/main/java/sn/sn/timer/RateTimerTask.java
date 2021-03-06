package sn.sn.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

import javafx.scene.control.TextArea;

import static sn.sn.constant.IConstant.*;
import sn.sn.currency.Currency;

/**
 * 汇率任务
 * @author 王超
 */
public class RateTimerTask extends TimerTask {
	
	private static boolean isRunning = false;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);

	private TextArea textArea;
	
	public RateTimerTask(TextArea textArea) {
		this.textArea = textArea;
	}
	@Override
	public void run() {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if (day == 1) return;				//周日
		if (day == 7 && hour > 6) return;	//周六早6点后
		if (day == 2 && hour < 6) return;	//周一早6点前
		if (isRunning) return;
		while (true) {
			isRunning = true;
			try {
				day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
				hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				if (day == TIMER_DAY_END && hour > TIMER_HOUR_END) {
					isRunning = false;
					break;
				}
				new Currency(textArea).insertRate();
				Thread.sleep(30000);
			} catch(Throwable e) {
				System.out.println("rate_error：" + e.getMessage() + "；    " + dateFormat.format(System.currentTimeMillis()));
			}
		}
	}
}
