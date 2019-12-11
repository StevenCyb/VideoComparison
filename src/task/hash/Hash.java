package task.hash;

import org.opencv.core.Mat;

import exceptions.NotExtendsException;
/**
 * Super class for hash algorithms
 *
 * @author Steven Cybinski
 * @version 0.1
 */
public class Hash {
	/** Size of the hash for a single frame stack */
	protected int hashSize;
	
	/**
	 * Constructor of the class
	 * 
	 * @param hashSize Size of the hash for a single frame stack 
	 */
	public Hash(int hashSize) {
		this.hashSize = hashSize;
	}
	
	/**
	 * Merge two boolean arrays
	 * 
	 * @param a The first array
	 * @param b The second array
	 * @return The merged array
	 */
	public boolean[] mergeArrays(boolean[] a, boolean[] b) {
		boolean[] merged = new boolean[a.length + b.length];
		int globalIndex = 0;
		for (int i = 0; i < a.length; i++) {
			merged[globalIndex] = a[i];
			globalIndex++;
		}
		for (int i = 0; i < b.length; i++) {
			merged[globalIndex] = b[i];
			globalIndex++;
		}
		return merged;
	}

	/**
	 * Placeholder for function which hash a frame
	 * 
	 * @param frame Frame mat
	 * @return Hash
	 * @throws NotExtendsException
	 */
	public boolean[] hashFrame(Mat frame) throws NotExtendsException {
		throw new NotExtendsException("Hash is an superclass without functionality");
	}

	/**
	 * Placeholder for function which hash a video
	 * 
	 * @param path Path to the video
	 * @return Hash
	 * @throws NotExtendsException
	 */
	public boolean[] pHashVideos(String path) throws NotExtendsException {
		throw new NotExtendsException("Hash is an superclass without functionality");
	}
}
