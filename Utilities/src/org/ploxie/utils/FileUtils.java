package org.ploxie.utils;

import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

	/**
	 * Gets a file from the classpath, if it doesn't exist it returns null
	 * 
	 * @param path
	 * @return
	 */
	public static InputStream getFile(String path) {
		return FileUtils.class.getClassLoader().getResourceAsStream(path);
	}

	/**
	 * Gets a file from the classpath, if it doesn't exist it returns null
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static byte[] getFileToBytes(String path) throws IOException {
		try (InputStream is = getFile(path)) {
			return ByteUtils.getBytes(is);
		}
	}
	
}
