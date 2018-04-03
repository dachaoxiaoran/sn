package sn.sn.timer;

import java.util.Calendar;
import java.util.Timer;

import javafx.scene.control.TextArea;

import static sn.sn.constant.IConstant.*;

/**
 * buySell比例定时
 * @author 王超
 */
public class BuySellTimer {
	
	private TextArea textArea;
	
	public BuySellTimer(TextArea textArea) {
		this.textArea = textArea;
	}

	/**
	 * 运行定时任务，从周一早晨6点开始
	 */
	public void run() {
		Timer timer = new Timer();
		Calendar calendar = Calendar.getInstance();
		calendar.set(TIMER_YEAR, TIMER_MONTH, TIMER_DATE, TIMER_HOUR, TIMER_MINUTE, TIMER_SECOND);
		timer.schedule(new BuySellTimerTask(textArea), calendar.getTime(), TIMER_WAIT);
	}
}
