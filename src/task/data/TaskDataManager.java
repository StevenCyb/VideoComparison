package task.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * This class contains all video informations needed in the other classes
 *
 * @author Steven Cybinski
 * @version 0.1
 */
public class TaskDataManager {
	/** Contains all video paths */
	private List<String> paths = new ArrayList<String>();
	/** Contains the corresponding hashes */
	private List<boolean[]> hashes = new ArrayList<boolean[]>();
	/** Mark hashes as loaded if the hash is not calculated on this run */
	private List<Boolean> isLoaded = new ArrayList<Boolean>();
	/** Contains all possible comparisons with the given videos */
	private List<String> comparisonList = new ArrayList<String>();

	/**
	 * Return the number of video paths
	 *
	 * @return 
	 */
	public int pathsSize() {
		return paths.size();
	}
	
	/**
	 * Return the number of possible comparisons 
	 * 
	 * @return
	 */
	public int comparisonListSize() {
		return comparisonList.size();
	}

	/**
	 * Get hash of the video on given index
	 * 
	 * @param index Index of the video
	 * @return Hash of this video
	 */
	public boolean[] getHashOn(int index) {
		return hashes.get(index);
	}

	/**
	 * Get the path of the video on given index
	 * 
	 * @param index Index of the video
	 * @return Path to this video
	 */
	public String getPathOn(int index) {
		return paths.get(index);
	}
	
	/**
	 * Get the comparison information on a given index
	 * 
	 * @param index Index of the comparison list
	 * @return Comparison details
	 */
	public String getItemIndexesOn(int index) {
		return comparisonList.get(index);
	}
	
	/**
	 * Set a hash for a video on the given index
	 * 
	 * @param index Index of the video
	 * @param hash Hash to be set
	 */
	public void setHashOn(int index, boolean[] hash) {
		hashes.set(index, hash);
	}
	
	/**
	 * Set the comparison result for given Index
	 * 
	 * @param index Index of the comparison list item
	 * @param path1 Path of the first video
	 * @param path2 Path of the second video
	 * @param result Result of the comparison (%-Match)
	 */
	public void setComparisonResultOn(int index, String path1, String path2, double result) {
		comparisonList.set(index, path1 + ";" + path2 + ";" + result);
	}

	/**
	 * Search recursively for videos on a given base path
	 * 
	 * @param path The base path
	 * @throws Exception
	 */
	public void searchVideos(String path) throws Exception {
		for(String fileExtension : new String[]{".mp4", ".avi", ".fvl"}) {
			Stream<Path> walk = Files.walk(Paths.get(path));
			for(String subPath : walk.map(x -> x.toString()).filter(f -> f.endsWith(fileExtension)).collect(Collectors.toList())) {
				paths.add(subPath);
				hashes.add(null);
				isLoaded.add(false);
			}
			walk.close();
		}
	}
	
	/**
	 * Generate a comparison list based on the existing video paths
	 */
	public void generateComparisonList() {
		comparisonList = new ArrayList<String>();
		for(int forward=0; forward<pathsSize(); forward++) {
			for(int backward=(pathsSize() - 1); backward>=0; backward--) {
				if(forward != backward && !comparisonList.contains(forward + ";" + backward) && !comparisonList.contains(backward + ";" + forward)) {
					comparisonList.add(forward + ";" + backward);
				}
			}
		}
	}

	/**
	 * Load hash collection from file
	 * 
	 * @param path Path to the collection file
	 * @throws Exception
	 */
	public void loadCollection(String path) throws Exception {
		FileReader fileReader = new FileReader(path);
		BufferedReader bufferReader = new BufferedReader(fileReader);
		String line = bufferReader.readLine();
		int index = 0;
		String []splitted;
		while (line != null) {
			splitted = line.split(";");
			index = paths.indexOf(splitted[0]);
			if(index != -1 && splitted[1].length() > 0) {
				isLoaded.set(index, true);
				boolean []hash = new boolean[splitted[1].length()];
				for(int i=0; i<splitted[1].length(); i++){
					hash[i] = splitted[1].charAt(i) == '1';
				}
				hashes.set(index, hash);
			}
			line = bufferReader.readLine();
		}
		bufferReader.close();
		fileReader.close();
	}

	/**
	 * Save hashes in a hash collection file
	 * 
	 * @param path Path of the collection
	 * @param append Append the new hashes or override the file
	 * @throws IOException
	 */
	public void saveCollection(String path, boolean append) throws IOException {
		File file = new File(path);
		FileWriter fileWriter = new FileWriter(file, append);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		for(int i=0; i<paths.size(); i++) {
			if(!(isLoaded.get(i) && append) && hashes.get(i) != null && hashes.get(i).length > 0) {
				isLoaded.set(i, true);
				String hashString = "";
				for(boolean b : hashes.get(i)) {
					hashString += b?'1':'0';
				}
				bufferedWriter.write(paths.get(i) + ";" + hashString + "\r\n");
			}
		}
		file.setLastModified(System.currentTimeMillis());
		bufferedWriter.close();
		fileWriter.close();
	}

	/**
	 * Write the results in a protocol file (CSV-Format)
	 * 
	 * @param path Path to the result file
	 * @throws IOException
	 */
	public void saveResult(String path) throws IOException {
		FileWriter fileWriter = new FileWriter(new File(path), false);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write("Video-A;Video-B;Consistency\r\n");
		for(int i=0; i<comparisonList.size(); i++) {
			bufferedWriter.write(comparisonList.get(i) + "\r\n");
		}
		bufferedWriter.close();
		fileWriter.close();
	}
}
