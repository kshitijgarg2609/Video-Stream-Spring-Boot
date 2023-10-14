package com.kgprojects.util;

import java.io.File;
import java.io.FileInputStream;

public class VideoStreamUtils
{
	public static byte[] byteExtract(File file_name,int buffer_len,long range[])
	{
		int file_range=(int)(range[1]-range[0]+1);
		byte buffer[] = null;
		FileInputStream fis=null;
		try
		{
			buffer = new byte[file_range];
			fis = new FileInputStream(file_name);
			fis.skip(range[0]);
			fis.read(buffer,0,file_range);
			fis.close();
		}
		catch (Exception e)
		{
		}
		finally
		{
			try
			{
				fis.close();
			}
			catch(Exception ee)
			{
				
			}
		}
		return buffer;
	}
	public static long[] processRangeHeaderValue(String a,long buffer_len,long file_len)
	{
		long range[] = new long[2];
		int i=0;
		for(char c : a.toCharArray())
		{
			if(Character.isDigit(c))
			{
				range[i]=(range[i]*10)+(c-'0');
			}
			else if(c=='-')
			{
				i++;
			}
		}
		if(range[0]>=range[1])
		{
			range[1]=range[0]+buffer_len-1;
		}
		if(range[1]>=file_len)
		{
			range[1]=file_len-1;
		}
		return range;
	}
}