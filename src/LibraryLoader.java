import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import exceptions.NotSupportedException;
/**
 * Class to load the native libraries
 * 
 * @author Steven
 * @version 0.1
 */
public class LibraryLoader {
	
	/**
	 * Load native libraries from given path
	 * 
	 * @param path Path to the library
	 * @throws Exception
	 */
	public static void loadLibrary(String path) throws IOException {
		InputStream inputStream = VideoComparison.class.getResourceAsStream(path);
		File fileOut = File.createTempFile("lib", ".dll");
		if (fileOut != null) {
			OutputStream outputStream = new FileOutputStream(fileOut);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
            System.load(fileOut.toString());
		}
	}
	
	/**
	 * Load all needed native libraries
	 * 
	 * @throws Exception
	 */
	public static void loadLibrary() throws Exception {
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Windows")) {
			int bitness = Integer.parseInt(System.getProperty("sun.arch.data.model"));
	        if (bitness == 64) {
	        	loadLibrary("/lib/x64/opencv_java411.dll");
	        	loadLibrary("/lib/x64/opencv_videoio_ffmpeg411_64.dll");
            } else {
            	loadLibrary("/lib/x86/opencv_java411.dll");
            	loadLibrary("/lib/x86/opencv_videoio_ffmpeg411.dll");
            }
		} else {
			throw new NotSupportedException("OS \"" + osName + "\" not supported at this version.");
		}
	}
}
