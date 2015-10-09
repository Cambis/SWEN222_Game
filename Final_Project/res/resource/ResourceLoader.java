package resource;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Loads files as input streams so we can load files when we export to a
 * runnable jar.
 *
 * @author Cameron Bryers 300326848 MMXV
 *
 */
public final class ResourceLoader {

	/**
	 * Create an input stream given a file path.
	 *
	 * @param path
	 *            - path to file to be loaded
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream load(String path) throws FileNotFoundException {
		// System.out.println("Path: " + path);
		InputStream input = new FileInputStream(path);
		return input;
	}
}
