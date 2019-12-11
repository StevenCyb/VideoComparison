package task.manager;

import task.data.TaskDataManager;
import task.worker.TaskWorker;
/**
 * Super class for the task manager.
 *
 * @author Steven Cybinski
 * @version 0.1
 */
public class TaskManager {
	/** Reference to object that contains all necessary data */
	protected TaskDataManager taskDataManager;
	/** Array with all workers to manage */
	protected TaskWorker []workers;
	/** Maximal number of items to process */
	protected int maxIndex;
	/** Next or current index that can be reserved */
	protected int currentReservedIndex = -1;
	/** Number of items that was processed */
	protected int finishedIndexes = 0;

	/**
	 * Constructor of the class
	 * 
	 * @param taskDataManager Object that contains all necessary data
	 * @param threads Number of working threads 
	 * @param maxIndex Number of items to process
	 */
	public TaskManager(TaskDataManager taskDataManager, int threads, int maxIndex) {
		this.taskDataManager = taskDataManager;
		this.maxIndex = maxIndex;
		workers = new TaskWorker[threads];
	}

	/**
	 * Run all threads
	 */
	public void run() {
		for(int i=0; i<workers.length; i++) {
			workers[i].start();
		}
	}

	/**
	 * Return if all threads are finished
	 * 
	 * @return 
	 */
	public boolean isFinished() {
		for(int i=0; i<workers.length; i++) {
			if(!workers[i].isFinished()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Return the next index to process (is reserved for the corresponding task)
	 * 
	 * @return Reserved index
	 */
	public synchronized int getReservedIndex() {
		currentReservedIndex++;
		if(currentReservedIndex >= maxIndex) {
			return -1;
		}
		return currentReservedIndex;
	}
	
	/**
	 * Notify that a item was processed
	 */
	public synchronized void incrementFinishedIndexes() {
		finishedIndexes++;
	}

	/**
	 * Get the current progress
	 * 
	 * @return Array[% of finished items, numer of finished items, number of all items]
	 */
	public double[] getProgress() {
		return new double[] {(Double.valueOf(finishedIndexes) / Double.valueOf(maxIndex)) * 100.0, finishedIndexes, maxIndex};
	}
}
