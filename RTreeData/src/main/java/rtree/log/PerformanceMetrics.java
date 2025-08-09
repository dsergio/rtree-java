package rtree.log;

/**
 * Class to track and report performance metrics for R-Tree operations.
 * It records the number of reads, adds, updates, and the time taken for each operation.
 * 
 */
public class PerformanceMetrics {
	
	private int numReads;
	private int numAdds;
	private int numUpdates;
	private long readTime;
	private long addTime;
	private long updateTime;
	
	/**
	 * Default constructor initializes all counters to zero.
	 */
	public PerformanceMetrics() {
		numReads = 0;
		numAdds = 0;
		numUpdates = 0;
		readTime = 0;
		addTime = 0;
		updateTime = 0;
	}
	
	/**
	 * Adds a read operation and the time taken for it.
	 * @param time the time taken for the read operation in milliseconds
	 */
	public void addRead(long time) {
		numReads++;
		readTime += time;
	}
	
	/**
	 * Adds an add operation and the time taken for it.
	 * @param time the time taken for the add operation in milliseconds
	 */
	public void addAdd(long time) {
		numAdds++;
		addTime += time;
	}
	
	/**
	 * Adds an update operation and the time taken for it.
	 * @param time the time taken for the update operation in milliseconds
	 */
	public void addUpdate(long time) {
		numUpdates++;
		updateTime += time;
	}
	
	/**
	 * Get the number of read operations performed.
	 * @return the number of read operations
	 */
	public int getNumReads() {
		return numReads;
	}
	
	/**
	 * Get the number of add operations performed.
	 * @return the number of add operations
	 */
	public int getNumAdds() {
		return numAdds;
	}
	
	/**
	 * Get the number of update operations performed.
	 * @return the number of update operations
	 */
	public int getNumUpdates() {
		return numUpdates;
	}
	
	/**
	 * Get the total time taken for read operations.
	 * @return the total read time in milliseconds
	 */
	public long getReadTime() {
		return readTime;
	}
	
	/**
	 * Get the total time taken for add operations.
	 * @return the total add time in milliseconds
	 */
	public long getAddTime() {
		return addTime;
	}

	/**
	 * Get the total time taken for update operations.
	 * @return the total update time in milliseconds
	 */
	public long getUpdateTime() {
		return updateTime;
	}
	
	/**
	 * Get a summary of the performance metrics.
	 * @return a formatted string summarizing the performance metrics
	 */
	public String getPerformanceSummary() {
		return String.format("Reads: %d, Adds: %d, Updates: %d, Read Time: %d ms, Add Time: %d ms, Update Time: %d ms",
				numReads, numAdds, numUpdates, readTime, addTime, updateTime);
	}

}
