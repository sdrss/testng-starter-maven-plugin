package com.github.sdrss.testngstarter.mvnplugin.helper;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

public class FindSuites {
	
	public static File find(String fileName, String path) {
		File root = new File(System.getProperty("user.dir") + path);
		boolean recursive = true;
		Collection<?> files = FileUtils.listFiles(root, null, recursive);
		for (Iterator<?> iterator = files.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			if (file.getName().equals(fileName)) {
				return file;
			}
		}
		return null;
	}
}
