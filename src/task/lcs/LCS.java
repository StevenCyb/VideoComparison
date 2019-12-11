package task.lcs;
/**
 * Super class for longest common subhash algorithms
 *
 * @author Steven Cybinski
 * @version 0.1
 */
public class LCS {
	/** Size of the hash for a single frame stack */
	protected int hashSize;
	
	/**
	 * Constructor of the class
	 * 
	 * @param hashSize
	 */
	public LCS(int hashSize) {
		this.hashSize = hashSize;
	}
	
	/**
	 * Function to determine longest common subhash
	 * 
	 * @param hashA First hash
	 * @param hashB Second hash
	 * @return Match in %
	 */
	public double longestCommonSubhash(boolean[] hashA, boolean[] hashB) {
		return -1;
	}
}
