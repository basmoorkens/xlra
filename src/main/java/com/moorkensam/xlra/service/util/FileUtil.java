package com.moorkensam.xlra.service.util;

public class FileUtil {

	/**
	 * This parses a path+ filename ie ../folder/subfolder/file.txt and returns
	 * the last part of it.
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileNameFromPath(String path) {
		if (path == null) {
			return "";
		}
		if (!path.contains("/")) {
			return path;
		}
		String[] parts = path.split("/");
		return parts[parts.length - 1];
	}

}
