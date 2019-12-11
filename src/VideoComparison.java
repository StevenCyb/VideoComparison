import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import argument.definition.flag.FlagArgumentDefinition;
import argument.definition.integer.IntegerArgumentDefinition;
import argument.definition.integer.rules.IntegerArgumentProportionRule;
import argument.definition.integer.rules.IntegerArgumentRule;
import argument.definition.rules.NumberProportionRuleType;
import argument.definition.string.StringArgumentDefinition;
import argument.definition.string.rules.StringArgumentRule;
import argument.parser.ArgumentParser;
import task.data.TaskDataManager;
import task.hash.PHash;
import task.lcs.ShiftToLongestCommonSubstring;
import task.manager.ComparisonTaskManager;
import task.manager.HashTaskManager;
import task.manager.TaskManager;
/**
 * This is the main class of this application.
 *
 * @author Steven Cybinski
 * @version 0.1
 */
public class VideoComparison {
	/**
	 * Main function.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.print(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + ": Parse-Arguments -> ");
			ArgumentParser argumentParser = new ArgumentParser(true, true, true);
			argumentParser.addHelpArgument(new String[] {"-h", "-help"}, "This arguments are available:");
			StringArgumentDefinition videoDirPath = new StringArgumentDefinition(new String[]{"-vd", "-video_directory"}, "Video directory", true, "", new StringArgumentRule[] {});
			StringArgumentDefinition resultPath = new StringArgumentDefinition(new String[]{"-rp", "-result_path"}, "Result path", false, "./result.csv", new StringArgumentRule[] {});
			StringArgumentDefinition pHashCollectionPath = new StringArgumentDefinition(new String[]{"-pc", "-phash_collection"}, "Select a pHash collection source and/or saving destination", false, "", new StringArgumentRule[] {});
			IntegerArgumentDefinition threads = new IntegerArgumentDefinition(new String[] {"-t", "-threads"}, "Number of threads", false, 1, new IntegerArgumentRule[] {new IntegerArgumentProportionRule(NumberProportionRuleType.GREATER_EQUAL, 1)});
			IntegerArgumentDefinition hashSize = new IntegerArgumentDefinition(new String[] {"-hs", "-hash_size"}, "Size of the hash", false, 256, new IntegerArgumentRule[] {new IntegerArgumentProportionRule(NumberProportionRuleType.GREATER_EQUAL, 4)});
			IntegerArgumentDefinition saveEachNPercentage = new IntegerArgumentDefinition(new String[] {"-senp", "-save_each_n_percentage"}, "How often (percentage steps) should the hashs be saved (0=only at end)", false, 10, new IntegerArgumentRule[] {new IntegerArgumentProportionRule(NumberProportionRuleType.GREATER_EQUAL, 0)});
			FlagArgumentDefinition dropUnneededHashs = new FlagArgumentDefinition(new String[] {"-duh", "-drop_unneeded_hashs"}, "Drop unneeded hashs in given collection", false, false);
			argumentParser.add(videoDirPath);
			argumentParser.add(resultPath);
			argumentParser.add(pHashCollectionPath);
			argumentParser.add(threads);
			argumentParser.add(hashSize);
			argumentParser.add(saveEachNPercentage);
			argumentParser.add(dropUnneededHashs);
			argumentParser.parse(args);
			System.out.println("OK");
			
			if(!isValidSequenceMember(4, hashSize.getValue())) {
				throw new Exception("The size must be in the sequence of four (e.g. 4, 16, 64, ...)");
			}
			
			System.out.print(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + ": Load library ->  ");
			LibraryLoader.loadLibrary();
			System.out.println("OK");
			
			System.out.print(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + ": Search for available videos ->  ");
			TaskDataManager taskDataManager = new TaskDataManager();
			taskDataManager.searchVideos(videoDirPath.getValue());
			if(taskDataManager.pathsSize() <= 1) {
				throw new Exception("Path must contain at least two video files");
			}
			System.out.println("Found " + taskDataManager.pathsSize());
			
			if(pHashCollectionPath.getValue().length() > 0) {
				System.out.print(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + ": Check for available hash collection -> ");
				File pHashCollectionFile = new File(pHashCollectionPath.getValue());
				if(pHashCollectionFile.exists() && pHashCollectionFile.isFile()) {
					System.out.print("[Load] ");
					taskDataManager.loadCollection(pHashCollectionPath.getValue());
				} else {
					System.out.print("[Create new] ");
					pHashCollectionFile.createNewFile();
				}
				System.out.println("OK");
			}

			boolean finished = false;
			double[] progress = new double[] {-1, -1, -1};
			double previousProgress = -1;
			int lastSave = 0;
			System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + ": Creat " + (threads.getValue()>1?threads.getValue() + " worker-threads":" worker-thread") + " and run hash calculation:");
			TaskManager taskManager = new HashTaskManager(taskDataManager, threads.getValue(), new PHash(hashSize.getValue()));
			taskManager.run();
			do {
				finished = taskManager.isFinished();
				progress = taskManager.getProgress();
				if(progress[1] != previousProgress) {
					previousProgress = progress[1];
					String progressbar = "";
					for(int i=10; i<=100; i+=10) {
						progressbar += (i <= progress[0])?"#":" ";
					}
					System.out.print("|" + progressbar + "| " + new DecimalFormat("000.00").format(progress[0]) + "% [" + ((int) progress[1]) + "/" + ((int) progress[2]) + "]\r");
				}
				if(pHashCollectionPath.getValue().length() > 0 && saveEachNPercentage.getValue() != 0 && lastSave + saveEachNPercentage.getValue() <= progress[0]) {
					while(lastSave + saveEachNPercentage.getValue() <= progress[0]) {
						lastSave += saveEachNPercentage.getValue();
					}
					taskDataManager.saveCollection(pHashCollectionPath.getValue(), true);
				}
				Thread.sleep(10);
			} while(!finished);

			if(pHashCollectionPath.getValue().length() > 0) {
				taskDataManager.saveCollection(pHashCollectionPath.getValue(), !dropUnneededHashs.getValue());
			}

			System.out.println("");
			System.out.print(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + ": Create compare list -> ");
			taskDataManager.generateComparisonList();
			System.out.println("OK");

			finished = false;
			previousProgress = -1;
			System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + ": Creat " + (threads.getValue()>1?threads.getValue() + " worker-threads":" worker-thread") + " and run comparison:");
			taskManager = new ComparisonTaskManager(taskDataManager, threads.getValue(), new ShiftToLongestCommonSubstring(hashSize.getValue()));
			taskManager.run();
			do {
				finished = taskManager.isFinished();
				progress = taskManager.getProgress();
				if(progress[1] != previousProgress) {
					previousProgress = progress[1];
					String progressbar = "";
					for(int i=10; i<=100; i+=10) {
						progressbar += (i <= progress[0])?"#":" ";
					}
					System.out.print("|" + progressbar + "| " + new DecimalFormat("000.00").format(progress[0]) + "% [" + ((int) progress[1]) + "/" + ((int) progress[2]) + "]\r");
				}
				Thread.sleep(10);
			} while(!finished);
			
			System.out.println("\r\n" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + ": ");
			taskDataManager.saveResult(resultPath.getValue());
			System.out.println("OK");
			
			System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + ": Finished.");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Check if a given number is in a given sequence.
	 * 
	 * @param seq Sequence where n must be included
	 * @param n Number to check
	 * @return Is n a member of the given sequence
	 */
	static boolean isValidSequenceMember(int seq, int n) {
		int nBinary = seq;
		do {
			if (n == nBinary) {
				return true;
			}
			nBinary *= seq;
		} while (nBinary <= n);
		return false;
	}
}
