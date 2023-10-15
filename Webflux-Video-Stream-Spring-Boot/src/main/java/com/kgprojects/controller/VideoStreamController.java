package com.kgprojects.controller;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kgprojects.handler.MonoSupplierByteArray;
import com.kgprojects.handler.Mp4Filter;

import reactor.core.publisher.Mono;

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
	public String listVideos(Model mv)
	{
		List<String> list = new LinkedList<>();
		for(String fileName : main_video_dir.list(new Mp4Filter()))
		{
			list.add(fileName);
		}
		mv.addAttribute("videofiles", list);
		return "listvideos";
	}
	
	@GetMapping("/video/{fileName}")
	public Mono<ResponseEntity<byte[]>> streamVideo(@PathVariable String fileName, RequestEntity<byte[]> req)
	{
		String range=req.getHeaders().getOrDefault("range", Arrays.asList("0-")).get(0);
		File video_file = new File(main_video_dir,fileName);
		return Mono.fromSupplier(new MonoSupplierByteArray(video_file, range, buffer_len));
	}
}