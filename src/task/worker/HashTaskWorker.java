package task.worker;

import exceptions.NotExtendsException;
import task.data.TaskDataManager;
import task.hash.Hash;
import task.manager.HashTaskManager;
/**
 * Super class for the worker.
 *
 * @author Steven Cybinski
 * @version 0.1
 */
public class HashTaskWorker extends TaskWorker {
	/** Reference to hash algorithm to use */
	private Hash hash;

	/**
	 * Constructor of the class
	 * 
	 * @param manager Manager that create this worker
	 * @param taskDataManager Object that contains all necessary data
	 * @param hash Hash algorithm to use
	 */
	public HashTaskWorker(HashTaskManager manager, TaskDataManager taskDataManager, Hash hash) {
		super(manager, taskDataManager);
		this.hash = hash;
	}

	/**
	 * Run routine of the worker
	 */
	@Override
	public void run() {
		int currentIndex = manager.getReservedIndex();
		while (currentIndex != -1) {
			try {
				taskDataManager.setHashOn(currentIndex, hash.pHashVideos(taskDataManager.getPathOn(currentIndex)));
			} catch (NotExtendsException e) {
				e.printStackTrace();
			}
			manager.incrementFinishedIndexes();
			currentIndex = manager.getReservedIndex();
		}
		finished = true;
	}
}
