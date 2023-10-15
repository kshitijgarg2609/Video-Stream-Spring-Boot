package com.kgprojects.handler;

import java.io.File;
import java.util.function.Supplier;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.kgprojects.util.VideoStreamUtils;

public class MonoSupplierByteArray implements Supplier<ResponseEntity<byte[]>>
{
	private File video_file;
	private String range;
	private int buffer_len;
	public MonoSupplierByteArray()
	{
		super();
	}
	
	public MonoSupplierByteArray(File video_file, String range, int buffer_len)
	{
		super();
		this.video_file = video_file;
		this.range = range;
		this.buffer_len = buffer_len;
	}

	@Override
	public ResponseEntity<byte[]> get()
	{
		try
		{
			long file_len=video_file.length();
			long rng[]=VideoStreamUtils.processRangeHeaderValue(range, buffer_len, file_len);
			long file_range=rng[1]-rng[0]+1;
			HttpHeaders head = new HttpHeaders();
			head.setContentLength(file_range);
			head.add("Content-Range",String.format("bytes %d-%d/%d",rng[0],rng[1],file_len));
			head.add("Accept-Ranges", "bytes");
			head.setContentType(MediaType.parseMediaType("video/mp4"));
			byte dta[]=VideoStreamUtils.byteExtract(video_file,buffer_len,rng);
			return new ResponseEntity<byte[]>(dta, head, HttpStatus.PARTIAL_CONTENT);
		}
		catch(Exception e)
		{
			return ResponseEntity.notFound().build();
		}
	}
}