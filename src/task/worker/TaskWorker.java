package task.worker;

import task.data.TaskDataManager;
import task.manager.TaskManager;
/**
 * Super class for the worker.
 *
 * @author Steven Cybinski
 * @version 0.1
 */
public class TaskWorker extends Thread {
	/** Reference to the manager which is responsible for this worker */
	protected TaskManager manager;
	/** Reference to object that contains all necessary data */
	protected TaskDataManager taskDataManager;
	/** Flag if worker is ready */
	protected boolean finished = false;
	
	/**
	 * Constructor of the class
	 * 
	 * @param manager Manager that create this worker
	 * @param taskDataManager Object that contains all necessary data
	 */
	public TaskWorker(TaskManager manager, TaskDataManager taskDataManager) {
		this.manager = manager;
		this.taskDataManager = taskDataManager;
	}

	/**
	 * Return if worker is finished
	 * 
	 * @return
	 */
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * Placeholder for run function
	 */
	@Override
	public void run() {
	}
}
