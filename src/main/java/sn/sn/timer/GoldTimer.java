package sn.sn.timer;

import java.util.Calendar;
import java.util.Timer;

import javafx.scene.control.TextArea;

import static sn.sn.constant.IConstant.*;

/**
 * 黄金定时
 * @author 王超
 */
public class GoldTimer {
	
	private TextArea textArea;
	
	public GoldTimer(TextArea textArea) {
		this.textArea = textArea;
	}

	/**
	 * 运行黄金定时任务，从周一早晨5点开始
	 */
	public void run() {
		Timer timer = new Timer();
		Calendar calendar = Calendar.getInstance();
		calendar.set(TIMER_YEAR, TIMER_MONTH, TIMER_DATE, TIMER_HOUR, TIMER_MINUTE, TIMER_SECOND);
		timer.schedule(new GoldTimerTask(textArea), calendar.getTime(), TIMER_WAIT);
	}
}
