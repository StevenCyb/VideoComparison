package task.manager;

import task.data.TaskDataManager;
import task.lcs.LCS;
import task.worker.ComparisonTaskWorker;
/**
 * Class that manage comparison worker
 *
 * @author Steven Cybinski
 * @version 0.1
 */
public class ComparisonTaskManager extends TaskManager {
	/**
	 * Constructor of the class
	 * 
	 * @param taskDataManager Object that contains all necessary data
	 * @param threads Number of working threads 
	 * @param lcs LCS algorithm to compare two items
	 */
	public ComparisonTaskManager(TaskDataManager taskDataManager, int threads, LCS lcs) {
		super(taskDataManager, threads, taskDataManager.comparisonListSize());
		for(int i=0; i<workers.length; i++) {
			workers[i] = new ComparisonTaskWorker(this, taskDataManager, lcs);
		}
	}
}
