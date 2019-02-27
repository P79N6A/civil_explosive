
/*
 * 微信小程序
 * */
define(function(require,exports,module){
	
	//加载flv player
	function loadPlayer(jqPanel,playId,videoJson,w,h,swfPath){
		var swfDom = jqPanel.find("#" + playId)[0];
		$ui.print(swfDom);
		//加载swf
		swfobject.embedSWF(swfPath, swfDom, w, h, 11);
		var playerCount = 0;
		var playerTimer = window.setInterval(function(){
			if(playerCount++ > 40) {
				$ui.print("死循环了");
				window.clearInterval(playerTimer);
			}
			try{
				var swfPlayer = jqPanel.find("#" + playId)[0];
				if(swfPlayer.play){
					swfPlayer.play(videoJson.rtmpUrl,videoJson.rtmpPath);
					window.clearInterval(playerTimer);
				}
			}catch(err){}
		},50);
	}
	
	//加载flv player
	function loadPusher(jqPanel,playId,videoJson,w,h,swfPath){
		var swfDom = jqPanel.find("#" + playId)[0];
		$ui.print(swfDom);
		//加载swf
		swfobject.embedSWF(swfPath, swfDom, w, h, 11);
		var playerCount = 0;
		var playerTimer = window.setInterval(function(){
			if(playerCount++ > 40) {
				$ui.print("死循环了");
				window.clearInterval(playerTimer);
			}
			try{
				var swfPlayer = jqPanel.find("#" + playId)[0];
				if(swfPlayer.startks){
					//swfPlayer.play(videoJson.rtmpUrl,videoJson.rtmpPath);
					window.clearInterval(playerTimer);
				}
			}catch(err){}
		},50);
	}
	
	//入口
	function main(jqDom,args){
		var self = this;
		var mainUI = [{
			eName:"div",
			css:{padding:20,width:'60%',margin:"auto"},
			height:600,
			elements:[{
				eName:"div",
				css:{float:"left",margin:26,"background-color":"black"},
				elements:{
					eName:"div",
					id:"video_player",
					width:640,height:480
				}
			},{
				eName:"div",
				css:{float:"left",margin:26,"background-color":"black","padding-top":20},
				elements:{
					eName:"div",
					id:"video_pusher",
					width:640,height:480
				}
			}]
		},{
			eName:"div",
			elements:[{
				eName:"input",type:"button",value:"拉流",css:{"margin-right":20},
				onClick:function(){
					var swfPlayer = jqDom.find("#video_player")[0];
					swfPlayer.play("rtmp://127.0.0.1/live","a1");
				}
			},{
				eName:"input",type:"button",value:"推流",
				onClick:function(){
					var swfPlayer = jqDom.find("#video_pusher")[0];
					swfPlayer.startks("rtmp://127.0.0.1/live","a2");
				}
			}]
		}];
		jqDom.createUI(mainUI);
		var swfDom = null;
		
		swfDom = jqDom.find("#video_player")[0];
		swfobject.embedSWF("flv/LivePlayer.swf?_1", swfDom, 368,640, 11);
		
		swfDom = jqDom.find("#video_pusher")[0];
		swfobject.embedSWF("flv/videoPusher.swf?_1", swfDom, 640,480, 11);
		
		window.flexMsg = function(msg){
			$ui.print(msg);
		};
		
	}
	return main;
});

