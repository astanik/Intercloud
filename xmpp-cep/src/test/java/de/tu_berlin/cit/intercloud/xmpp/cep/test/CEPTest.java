package de.tu_berlin.cit.intercloud.xmpp.cep.test;

import java.lang.management.ManagementFactory;
import java.util.Timer;
import java.util.TimerTask;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.UpdateListener;
import com.sun.management.OperatingSystemMXBean;

import de.tu_berlin.cit.intercloud.xmpp.cep.ComplexEventProcessor;
import de.tu_berlin.cit.intercloud.xmpp.cep.StatementBuilder;
import de.tu_berlin.cit.intercloud.xmpp.cep.eventlog.LogDocument;
import de.tu_berlin.cit.intercloud.xmpp.cep.events.CpuUtilizationEvent;

public class CEPTest {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String sensorURI = "xmpp://gateway.cit.tu-berlin.de#/sensor/sen2";

	private static final String vmURI = "xmpp://gateway.cit.tu-berlin.de#/compute/vm1";

	private final OperatingSystemMXBean osManager;
	
	private Timer timer = null;

	public CEPTest() {
		java.lang.management.OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
		if(bean instanceof OperatingSystemMXBean)
			this.osManager = (OperatingSystemMXBean) bean;
		else
			throw new RuntimeException("com.sun.management.OperatingSystemMXBean is not supported.");
	}

	private class CpuTimerTask extends TimerTask {
				
	    @Override
	    public void run() {
	    	int sysCpu = (int) osManager.getSystemCpuLoad() * 100;
	    	int jvmCpu = (int) osManager.getProcessCpuLoad() * 100;
	    	LogDocument event = CpuUtilizationEvent.build(sensorURI, vmURI, sysCpu);
	    	// process event
	    	ComplexEventProcessor.getInstance().processEvent(event);
	    	logger.info("Event had been processed");
	    }
	}

	public void start() {
        TimerTask timerTask = new CpuTimerTask();
        //running timer task as daemon thread
        this.timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
        logger.info("TimerTask started");
	}

	public void stop() {
        this.timer.cancel();
        logger.info("TimerTask cancelled");
	}

	public class MyListener implements UpdateListener {
	    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
	    	logger.info("Event occured: " + newEvents.length);
	    	for(int i = 0; i < newEvents.length; i++) {
	    		EventBean event = newEvents[i];
	    		EventType type = event.getEventType();
	    		String[] pro = type.getPropertyNames();
	    		for(int t = 0; t<pro.length; t++) {
	    			logger.info("." + pro[t] + ".");
	    			Object o = event.get(pro[t]);
	    			if(o == null)
	    				logger.info("is null");
	    			else
	    				logger.info(o.toString());
	    		}
//		        logger.info("object=" + event.get("object"));
//		        logger.info("subject=" + event.get("subject"));
//		        logger.info("timestamp=" + event.get("timestamp"));
	    	}
	        
	    }
	}
	
	@Test
	public void cepTest() {
		EPStatement st = StatementBuilder.buildCpuUtilization(sensorURI, vmURI, 5);
		MyListener listener = new MyListener();
		st.addListener(listener);
		// start measuring 
		this.start();
		logger.info("CPU meter has been started.");
		try {
			// wait 10 seconds
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.stop();
		logger.info("CPU meter has been stopped.");

//		EPStatement st = StatementBuilder.build(sensorURI, vmURI);
	}
	
}
