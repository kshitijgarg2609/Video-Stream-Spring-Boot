package com.kgprojects.handler;

import java.io.File;
import java.io.FilenameFilter;

public class Mp4Filter implements FilenameFilter
{
	@Override
	public boolean accept(File dir, String name)
	{
		File file = new File(dir,name);
		return file.isFile() && name.toLowerCase().endsWith(".mp4");
	}
}