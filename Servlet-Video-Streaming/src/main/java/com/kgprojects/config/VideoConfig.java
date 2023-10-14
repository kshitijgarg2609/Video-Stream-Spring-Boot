package com.kgprojects.config;

import java.io.File;

public class VideoConfig
{
	public static final String base_dir = "D:\\";
	public static File main_video_dir = new File(base_dir,"Videos");
	public static final int buffer_len = 10480;
	static
	{
		if(!main_video_dir.exists())
		{
			main_video_dir.mkdir();
		}
	}
}
