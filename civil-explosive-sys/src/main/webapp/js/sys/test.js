

define(function(require,exports,module){

	//测试flv player
	function test_jqplayer(jqDom){
		var ui = {
			eName:"div",
			cssStyle:"border:1px solid red;margin:10px;padding:6px;",
			elements:[{
				eName:"div",
				elements:{
					eName:"formEx",
					cssStyle:"margin:12px;",
					elements:[{
						eName:"textbox",
						name:"rtmpUrl",
						value:"rtmp://192.168.44.3/video1/",
						width:262
					},{
						eName:"textbox",
						name:"rtmpPath",
						width:380
					},{
						eName:"linkbutton",
						text:"开始",
						width:60,
						onClick:function(){
							$ui.postEx("sys/test01.do");
							return;
							$ui.print("hello world");
							var jqForm = jqFlv.findJq("formEx");
							var params = {};
							jqForm.formEx("form2Json",params);
							$ui.print(params);
							
							jwplayer("flv_player111").setup({
								flashplayer: 'flv/player.swf',
								primary : 'flash',
							    file:params.rtmpPath,
							    streamer: params.rtmpUrl,
							    controlbar: 'bottom',
							    autostart:false,
							    width:640,
								height:480
							});
						}
					}]
				}
			},{
				eName:"div",
				id:"flv_player111",
				height:480,
				width:640,
			}]
		}
		var jqFlv = jqDom.createUI(ui);
	}
	
	function main(jqObj,loadParams){
		test_jqplayer(jqObj);
	}
	
	module.exports = main;

});
