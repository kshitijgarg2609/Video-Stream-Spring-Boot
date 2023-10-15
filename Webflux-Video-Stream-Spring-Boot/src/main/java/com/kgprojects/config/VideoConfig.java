package com.kgprojects.config;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VideoConfig
{
    @Bean(name = "main_video_dir")
    File videoDirectory()
	{
		File file = new File(System.getProperty("user.dir"),"Videos");
		if(!file.exists())
		{
			file.mkdir();
		}
		return file;
	}
    @Bean(name = "buffer_len")
    int buffer()
    {
    	return 10480;
    }
}
