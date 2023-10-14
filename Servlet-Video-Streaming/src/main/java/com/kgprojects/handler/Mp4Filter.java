package com.kgprojects.handler;

import java.io.File;
import java.io.FilenameFilter;

public class Mp4Filter implements FilenameFilter
{
	@Override
	public boolean accept(File dir, String name)
	{
		return name.toLowerCase().endsWith(".mp4");
	}
}
