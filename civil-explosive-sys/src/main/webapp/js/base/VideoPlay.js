
/*
 * 视频回放
 * */
define(function(require,exports,module){
	
	var winPaddingW = 15,winPaddingH = 40;
	
	function play(title,videoJson){
		var playId = "flv_" + new Date().getTime();
		var winWidth = 640,winHeight = 480;
		var jqWin = $ui.createWin({
			title:title || '视频回放',
			width:winWidth,
			height:winHeight,
			maximizable:true,
			modal:true,
			onResize:function(w,h){
				$ui.print(w + "xx" + h);
				if(!jqWin) return;
				var jqPlayer = jqWin.find("#" + playId);
				jqPlayer.width(w - winPaddingW).height(h - winPaddingH);
			},
			bodyUI:{
				eName:"div",
				id:playId
			}
		});
		$ui.print(videoJson.rtmpUrl + videoJson.videoPath1);
		$ui.print(videoJson.videoPath2);
		var flvOpts = {
			flashplayer: 'flv/player.swf',
			primary : 'flash',
		    file:videoJson.videoPath2,
		    streamer: videoJson.rtmpUrl + videoJson.videoPath1,
		    controlbar: 'bottom',
		    autostart:true,
		    width:winWidth - winPaddingW,
			height:winHeight - winPaddingH
		};
		jwplayer(playId).setup(flvOpts);
	}
	
	return {
		play:play
	}
});

