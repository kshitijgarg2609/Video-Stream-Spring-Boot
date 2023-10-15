package com.kgprojects.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kgprojects.handler.Mp4Filter;
import com.kgprojects.util.VideoStreamUtils;

@Controller
public class VideoStreamController
{
	@Autowired
	@Qualifier("main_video_dir")
	private File main_video_dir;
	
	@Autowired
	@Qualifier("buffer_len")
	private int buffer_len;
	
	@GetMapping("/")
	public ModelAndView listVideos(ModelAndView mv)
	{
		List<String> list = new LinkedList<>();
		for(String fileName : main_video_dir.list(new Mp4Filter()))
		{
			list.add(fileName);
		}
		mv.addObject("videofiles", list);
		mv.setViewName("listvideos");
		return mv;
	}
	
	@GetMapping("/video/{fileName}")
	public ResponseEntity<byte[]> streamVideo(@PathVariable String fileName, RequestEntity<byte[]> req)
	{
		String range=req.getHeaders().getOrDefault("range", Arrays.asList("0-")).get(0);
		try
		{
			File video_file = new File(main_video_dir,fileName);
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