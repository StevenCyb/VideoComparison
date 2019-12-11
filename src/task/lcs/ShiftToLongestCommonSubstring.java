package task.lcs;
/**
 * Class to find longest common subhash by shifting
 *
 * @author Steven Cybinski
 * @version 0.1
 */
public class ShiftToLongestCommonSubstring extends LCS {
	/** Store the hamming distance threashold (when is an image "equal" (<=10% error)) */
	private int hammingDistanceThreshold; 
	
	/**
	 * Constructor of the class
	 * 
	 * @param hashSize Size of the hash
	 */
	public ShiftToLongestCommonSubstring(int hashSize) {
		super(hashSize);
		hammingDistanceThreshold = (int) Math.ceil(hashSize * 0.1);
	}

	/**
	 * Routine to calculate the LCS by shifting.
	 * Runtime O(n log(m))
	 * Memory O(n)
	 * Since the algorithm is difficult to follow due to the hashes, the string version should be understood firs:
	 * 
	 * private static int search(String a, String b) {
	 * 	int globalMax = 0, localMax = 0;
	 * 	if (a.length() < b.length()) {
	 * 		String tmp = a;
	 * 		a = b;
	 * 		b = tmp;
	 * 	}
	 * 	for (int iA = 0; iA < (a.length()); iA++) {
	 * 		localMax = 0;
	 * 		for (int iB = 0; iB < Math.min(a.length(), b.length()); iB++) {
	 * 			if (a.charAt(iB) == b.charAt(iB)) {
	 * 				localMax++;
	 * 				if (localMax > globalMax) {
	 * 					globalMax = localMax;
	 * 				}
	 * 			} else {
	 * 				localMax = 0;
	 * 			}
	 * 		}
	 * 		a = a.substring(1);
	 * 	}
	 * 	return globalMax;
	 * }
	 */
	@Override
	public double longestCommonSubhash(boolean[] hashA, boolean[] hashB) {
		double hammingDistance = 0;
		int globalMax = 0, localMax = 0, shift = 0;
		if (hashA.length < hashB.length) {
			boolean[] tmp = hashA;
			hashA = hashB;
			hashB = tmp;
		}
		for (int iA = 0; iA < (hashA.length - shift); iA += hashSize) {
			localMax = 0;
			for (int iB = 0; iB < Math.min(hashA.length - shift, hashB.length); iB += hashSize) {
				hammingDistance = 0;
				for (int i = 0; i < hashSize; i++) {
					if (hashA[iB + i + shift] != hashB[iB + i]) {
						hammingDistance++;
					}
				}
				if (hammingDistance <= hammingDistanceThreshold) {
					localMax++;
					if (localMax > globalMax) {
						globalMax = localMax;
					}
				} else {
					localMax = 0;
				}
			}
			shift += hashSize;
		}
		return ((double) globalMax / ((double) Math.min(hashA.length, hashB.length) / (double) hashSize)) * 100;
	}
}
