package org.fl.noodle.common.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fl.noodle.common.monitor.executer.AbstractExecuter;
import org.fl.noodle.common.monitor.executer.Executer;
import org.fl.noodle.common.monitor.schedule.ExecuterSchedule;

import org.junit.Test;

public class ExecuterScheduleTest {

	@Test
	public void test() {
		ExecuterTest executer = new ExecuterTest();
		executer.setDelay(1);
		executer.setInitialDelay(1);
		List<Executer> executerList = new ArrayList<Executer>();
		executerList.add(executer);
		ExecuterSchedule ExecuterSchedule = new ExecuterSchedule();
		ExecuterSchedule.setExecuterList(executerList);
		ExecuterSchedule.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		ExecuterSchedule.destroy();
	}
	
	private class ExecuterTest extends AbstractExecuter {

		@Override
		public void execute() throws Exception {
			System.out.println("ExecuterTest -> execute -> time: " + (new Date()).getTime());
		}
	}
}
