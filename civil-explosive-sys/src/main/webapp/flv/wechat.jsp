<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE;IE=10;IE=9;IE=8"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>微信小程序视频直播测试</title>
</head> 
<body>
	<OBJECT classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
		codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,40,0"
		WIDTH="550" HEIGHT="400" id="myMovieName">
		<PARAM NAME=movie VALUE="../flv/LivePlayer.swf">
		<PARAM NAME=quality VALUE=high>
		<PARAM NAME=bgcolor VALUE=#FFFFFF>
		<EMBED src="../flv/LivePlayer.swf" quality=high
			bgcolor=#FFFFFF WIDTH="550" HEIGHT="400" NAME="myMovieName" ALIGN=""
			TYPE="application/x-shockwave-flash"
			PLUGINSPAGE="http://www.macromedia.com/go/getflashplayer"></EMBED>
	</OBJECT>
</body>
</html>
