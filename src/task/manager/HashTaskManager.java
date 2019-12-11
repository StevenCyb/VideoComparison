package task.manager;

import task.data.TaskDataManager;
import task.hash.Hash;
import task.worker.HashTaskWorker;

public class HashTaskManager extends TaskManager {
	/**
	 * Constructor of the class
	 * 
	 * @param taskDataManager Object that contains all necessary data
	 * @param threads Number of working threads 
	 * @param hash Algorithm to calculate the hash
	 */
	public HashTaskManager(TaskDataManager taskDataManager, int threads, Hash hash) {
		super(taskDataManager, threads, taskDataManager.pathsSize());
		for(int i=0; i<workers.length; i++) {
			workers[i] = new HashTaskWorker(this, taskDataManager, hash);
		}
	}

	/**
	 * Return the next index to process (is reserved for the corresponding task)
	 * 
	 * @return Reserved index
	 */
	@Override
	public synchronized int getReservedIndex() {
		currentReservedIndex++;
		while(currentReservedIndex < maxIndex && taskDataManager.getHashOn(currentReservedIndex) != null) {
			currentReservedIndex++;
			finishedIndexes++;
		}
		if(currentReservedIndex >= maxIndex) {
			return -1;
		}
		return currentReservedIndex;
	}
}
