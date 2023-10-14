<%@ page import="com.kgprojects.config.VideoConfig" %>
<%@ page import="com.kgprojects.handler.Mp4Filter" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>List of Videos</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            background: #fff;
        }
    </style>
</head>
<body>
    <header>
        <h1>VIDEOS</h1>
    </header>
    <main>
        <%
        for(String fileName : VideoConfig.main_video_dir.list(new Mp4Filter()))
        {
        	%>
        	<div>
        	<a href="video/<%= fileName %>" title="<%= fileName %>"><%= fileName %></a>
        	</div>
        	<%
        }
        %>
    </main>
</body>
</html>