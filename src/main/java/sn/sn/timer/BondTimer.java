package sn.sn.timer;

import java.util.Calendar;
import java.util.Timer;

import javafx.scene.control.TextArea;

import static sn.sn.constant.IConstant.*;

/**
 * 债券定时
 * @author 王超
 */
public class BondTimer {
	
	private TextArea textArea;
	
	public BondTimer(TextArea textArea) {
		this.textArea = textArea;
	}

	/**
	 * 运行债券定时任务，从周一早晨6点开始
	 */
	public void run() {
		Timer timer = new Timer();
		Calendar calendar = Calendar.getInstance();
		calendar.set(TIMER_YEAR, TIMER_MONTH, TIMER_DATE, TIMER_HOUR, TIMER_MINUTE, TIMER_SECOND);
		timer.schedule(new BondTimerTask(textArea), calendar.getTime(), TIMER_WAIT);
	}
}
