package org.fl.noodle.common.distributedlock.db;

import static org.junit.Assert.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.fl.noodle.common.distributedlock.db.DbDistributedLock;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fl/noodle/common/distributedlock/db/noodlecommon-distributedlock-db-bean.xml"
})
public class DBDistributedLockTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	DbDistributedLock dbDistributedLock_master;
	
	@Autowired
	DbDistributedLock dbDistributedLock_slave;
	
	@Test
	public void testGetDiffTime() {
		dbDistributedLock_master.getDiffTime();
	}

	@Test
	public void testGetAlive() {
		assertTrue(dbDistributedLock_master.getAlive());
	}

	@Test
	public void testKeepAlive() {
		assertTrue(dbDistributedLock_master.keepAlive());
	}

	@Test
	public void testReleaseAlive() {
		assertTrue(dbDistributedLock_master.releaseAlive());
	}
	
	@Test
	public final void testGetStatus() throws Exception {
		
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				if (dbDistributedLock_master.getStatus() == true) {
					logger.info("Scheduler dbDistributedLock_master ReleaseLocker...");
					dbDistributedLock_master.releaseLocker();
				} else if (dbDistributedLock_slave.getStatus() == true) {
					logger.info("Scheduler dbDistributedLock_slave ReleaseLocker...");
					dbDistributedLock_slave.releaseLocker();
				}
			}
		}, 8, 8, TimeUnit.SECONDS);
		logger.info("Scheduler Start...");
		
		logger.info("dbDistributedLock_master WaitLocker...");
		assertTrue(dbDistributedLock_master.waitLocker());
		logger.info("dbDistributedLock_master Status: " + dbDistributedLock_master.getStatus());
		
		logger.info("dbDistributedLock_slave WaitLocker...");
		assertTrue(dbDistributedLock_slave.waitLocker());		
		logger.info("dbDistributedLock_slave Status: " + dbDistributedLock_slave.getStatus());
	}
}
