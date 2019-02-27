<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width" />
<title>Jwplayer Test</title>
</head>
<body>
<script type='text/javascript' src='jwplayer6.js?_1'></script>
<div class="spiffyfg">
<center>
<b>RTMP</b>
<div id='mediaspace'>This text will be replaced</div>
<script type='text/javascript'>
  jwplayer('mediaspace').setup({
		flashplayer : 'jwplayer6.swf',
		file : 'rtmp://127.0.0.1:1935/video1/2018/bb',
		autostart:true,
		//bufferlength:1,
		width : 640,
		height : 480,
		"controls": false
  });
</script>
</center>
</div>
</body>
</html>
