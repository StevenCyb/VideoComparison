package task.worker;

import task.data.TaskDataManager;
import task.lcs.LCS;
import task.manager.ComparisonTaskManager;
/**
 * Super class for the worker.
 *
 * @author Steven Cybinski
 * @version 0.1
 */
public class ComparisonTaskWorker extends TaskWorker {
	/** Reference to LCM algorithm to use */
	private LCS lcs;
	
	/**
	 * Constructor of the class
	 * 
	 * @param manager Manager that create this worker
	 * @param taskDataManager Object that contains all necessary data
	 * @param lcs LCM algorithm to use
	 */
	public ComparisonTaskWorker(ComparisonTaskManager manager, TaskDataManager taskDataManager, LCS lcs) {
		super(manager, taskDataManager);
		this.lcs = lcs;
	}

	/**
	 * Run routine of the worker
	 */
	@Override
	public void run() {
		int currentIndex = manager.getReservedIndex();
		boolean[] hashA, hashB;
		while (currentIndex != -1) {
			String[] parts = taskDataManager.getItemIndexesOn(currentIndex).split(";");
			int []indexes = new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
			hashA = taskDataManager.getHashOn(indexes[0]);
			hashB = taskDataManager.getHashOn(indexes[1]);
			taskDataManager.setComparisonResultOn(currentIndex, taskDataManager.getPathOn(indexes[0]), taskDataManager.getPathOn(indexes[1]), lcs.longestCommonSubhash(hashA, hashB));
			manager.incrementFinishedIndexes();
			currentIndex = manager.getReservedIndex();
		}
		finished = true;
	}
}
