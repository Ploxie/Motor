package org.ploxie.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

	/**
	 * Gets a file from the classpath, if it doesn't exist it returns null
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static InputStream getFileAsInputStream(String path) throws FileNotFoundException {
		return new FileInputStream(getFile(path));
	}
	
	public static File getFile(String path) {
		return new File(System.getProperty("user.dir")+"\\../"+path);
	}

	/**
	 * Gets a file from the classpath, if it doesn't exist it returns null
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static byte[] getFileToBytes(String path) throws IOException {
		try (InputStream is = getFileAsInputStream(path)) {
			return ByteUtils.getBytes(is);
		}
	}
	
}
