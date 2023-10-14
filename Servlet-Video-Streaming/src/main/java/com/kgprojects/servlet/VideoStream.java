package com.kgprojects.servlet;

import java.io.File;
import java.io.IOException;

import com.kgprojects.config.VideoConfig;
import com.kgprojects.util.VideoStreamUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "VideoStream",urlPatterns = "/video/*",loadOnStartup = 1)
public class VideoStream extends HttpServlet
{
	private static final long serialVersionUID = -8484006043613735237L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		File video_file = new File(VideoConfig.main_video_dir,request.getPathInfo());
		if(!video_file.exists())
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String range = request.getHeader("Range");
		if(range==null)
		{
			range="0-";
		}
		long file_len=video_file.length();
		long rng[]=VideoStreamUtils.processRangeHeaderValue(range, VideoConfig.buffer_len, file_len);
		long file_range=rng[1]-rng[0]+1;
		ServletOutputStream out = response.getOutputStream();
		response.setContentLength((int)file_range);
		response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
		response.addHeader("Content-Range",String.format("bytes %d-%d/%d",rng[0],rng[1],file_len));
		response.addHeader("Accept-Ranges", "bytes");
		response.setContentType("video/mp4");
		out.write(VideoStreamUtils.byteExtract(video_file,VideoConfig.buffer_len,rng));
		out.close();
	}
}