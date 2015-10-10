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
	 * Create an input stream given a file path. Note: you should not include
	 * "res" in the file path, even though there is an error checker for this
	 * just do not do it.
	 *
	 * @param path
	 *            - path to file to be loaded
	 * @return InputStream to pass to a Scanner
	 * @throws FileNotFoundException
	 */
	public static InputStream load(String path) throws FileNotFoundException {
		// System.out.println("Path: " + path);
		// InputStream input = new FileInputStream(path);
		InputStream input = ResourceLoader.class.getResourceAsStream(path);
		//

		// "res" error
		if (input == null && path.contains("res")) {
			input = ResourceLoader.class.getResourceAsStream(path.substring(3));
			// System.out.println("No, Null here");
		}

		// Maybe the filepath needs an extra "/"
		if (input == null && path.contains("res")) {
			// System.out.println("Null here");
			input = ResourceLoader.class.getResourceAsStream("/" + path.substring(3));
		}

		// File not found, fatal error
		if (input == null) {
			throw new FileNotFoundException("Invalid filepath: " + path);
		}

		return input;
	}
}
