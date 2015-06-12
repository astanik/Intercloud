package de.tu_berlin.cit.intercloud.util.monitoring;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class PerformanceMeter {

	private final ConcurrentHashMap<Integer, Long> startTimes;

	private final ConcurrentHashMap<Integer, Long> stopTimes;

	private int startedTimers = 0;

	public PerformanceMeter() {
		this.startTimes = new ConcurrentHashMap<Integer, Long>();
		this.stopTimes = new ConcurrentHashMap<Integer, Long>();
	}

	public void startTimer(int id) {
		// increment timer count
		this.startedTimers++;
		// get current time in milliseconds
		long startTime = System.currentTimeMillis();
		// store the time
		this.startTimes.put(id, startTime);
	}

	public void stopTimer(int id) {
		// decrement timer count
		this.startedTimers--;
		// get current time in milliseconds
		long endTime = System.currentTimeMillis();
		// store the time
		this.stopTimes.put(id, endTime);
	}

	public void print() {
		if (this.startedTimers != 0)
			throw new RuntimeException(
					"Failed : Timer error : started timers = "
							+ this.startedTimers);

		System.out.println("Average execution time: " + this.getAverage()
				+ " ms");
		System.out.println("Minimum execution time: " + this.getMin() + " ms");
		System.out.println("Maximum execution time: " + this.getMax() + " ms");
	}

	public static String getHead() {
		return new String("Average Minimum Maximum Unit");
	}

	@Override
	public String toString() {
		return String.format("%s %s %s ms", this.getAverage(), this.getMin(),
				this.getMax());
	}

	private long getAverage() {
		long sum = 0;
		List<Long> times = this.getTotalTime();
		for (long time : times) {
			sum = sum + time;
		}
		return sum / times.size();
	}

	private long getMin() {
		long min = Long.MAX_VALUE;
		List<Long> times = this.getTotalTime();
		for (long time : times) {
			if (time < min)
				min = time;
		}
		return min;
	}

	private long getMax() {
		long max = Long.MIN_VALUE;
		List<Long> times = this.getTotalTime();
		for (long time : times) {
			if (time > max)
				max = time;
		}
		return max;
	}

	private List<Long> getTotalTime() {
		List<Long> timeList = new Vector<Long>();
		for (Enumeration<Integer> e = this.startTimes.keys(); e
				.hasMoreElements();) {
			int key = e.nextElement();
			Long start = this.startTimes.get(key);
			if (start == null)
				throw new RuntimeException(
						"Failed : Timer error : start time connot be found");
			Long stop = this.stopTimes.get(key);
			if (stop == null)
				throw new RuntimeException(
						"Failed : Timer error : stop time connot be found");
			timeList.add(stop - start);
		}
		return timeList;
	}

}
