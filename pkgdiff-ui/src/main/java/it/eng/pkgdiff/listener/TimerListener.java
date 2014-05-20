package it.eng.pkgdiff.listener;

import it.eng.pkgdiff.batch.TimerCleaner;

import java.util.Calendar;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TimerListener implements ServletContextListener {

	private final static long fONCE_PER_DAY = 1000 * 60 * 60 * 24;
	private Timer timer = new Timer();

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		timer.cancel();

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		Calendar date = Calendar.getInstance();

		date.set(Calendar.HOUR_OF_DAY, 23);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		timer.schedule(new TimerCleaner(arg0.getServletContext()),
				date.getTime(), fONCE_PER_DAY);

	}

}
