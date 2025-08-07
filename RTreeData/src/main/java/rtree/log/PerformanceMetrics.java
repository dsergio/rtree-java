package rtree.log;

public class PerformanceMetrics {
	
	private int numReads;
	private int numAdds;
	private int numUpdates;
	private long readTime;
	private long addTime;
	private long updateTime;
	
	public PerformanceMetrics() {
		numReads = 0;
		numAdds = 0;
		numUpdates = 0;
		readTime = 0;
		addTime = 0;
		updateTime = 0;
	}
	
	public void addRead(long time) {
		numReads++;
		readTime += time;
	}

	public void addAdd(long time) {
		numAdds++;
		addTime += time;
	}

	public void addUpdate(long time) {
		numUpdates++;
		updateTime += time;
	}

	public int getNumReads() {
		return numReads;
	}

	public int getNumAdds() {
		return numAdds;
	}

	public int getNumUpdates() {
		return numUpdates;
	}

	public long getReadTime() {
		return readTime;
	}

	public long getAddTime() {
		return addTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public String getPerformanceSummary() {
		return String.format("Reads: %d, Adds: %d, Updates: %d, Read Time: %d ms, Add Time: %d ms, Update Time: %d ms",
				numReads, numAdds, numUpdates, readTime, addTime, updateTime);
	}

}
