package task.hash;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import exceptions.NotExtendsException;
/**
 * Class with a pHash algorithm
 *
 * @author Steven Cybinski
 * @version 0.1
 */
public class PHash extends Hash {
	/** The image dimension to match the hash size */
	private int dim;

	/**
	 * Constructor of the class
	 * 
	 * @param hashSize Size of the hash for a single frame stack 
	 */
	public PHash(int hashSize) {
		super(hashSize);
		dim = (int) Math.sqrt(hashSize);
	}

	/**
	 * 
	 * 
	 * @param Frame mat which will be hashed
	 * @return Hash of frame
	 * @throws NotExtendsException
	 */
	@Override
	public boolean[] hashFrame(Mat frame) {
		Imgproc.resize(frame, frame, new Size(dim, dim), 0, 0, Imgproc.INTER_AREA);
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
		double mean = Core.mean(frame).val[0];
		Imgproc.threshold(frame, frame, mean, 255, Imgproc.THRESH_BINARY);
		boolean[] hash = new boolean[hashSize];
		for (int c = 0; c < frame.cols(); c++) {
			for (int r = 0; r < frame.rows(); r++) {
				hash[r + c * dim] = frame.get(new int[] { r, c })[0] != 0;
			}
		}
		return hash;
	}

	/**
	 * Create a hash of a video defined by his path
	 * 
	 * @param path Path to the video
	 * @return Hash of the video
	 */
	@Override
	public boolean[] pHashVideos(String path) {
		VideoCapture video = new VideoCapture(path);
		if (video.isOpened()) {
			int fps = (int) Math.round(video.get(Videoio.CAP_PROP_FPS));
			int frameStackCounter = 0;
			boolean[] videoHash = new boolean[0];
			Mat frame = new Mat();
			Mat frameStack = null;
			boolean doBreak = false;
			while (true) {
				if (video.read(frame)) {
					if(frameStack == null) {
						frameStack = frame.clone();
					} else {
						Core.add(frame, frameStack, frameStack);
					}
					frameStackCounter++;
				} else {
					doBreak = true;
				}
				if((frameStackCounter >= fps || doBreak) && frameStack != null) {
					Core.divide(frameStack, new Scalar(frameStackCounter), frameStack);
					videoHash = super.mergeArrays(videoHash, hashFrame(frameStack));
					frameStackCounter = 0;
					frameStack = null;
				}
				if(doBreak) {
					break;
				}
			}
			video.release();
			return videoHash;
		} else {
			return null;
		}
	}
}
