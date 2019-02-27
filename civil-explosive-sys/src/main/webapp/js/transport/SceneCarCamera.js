
/*
 * 车辆摄像头实时播放
 * */
define(function(require,exports,module){

	var deviceUrl = "transport/carinfo/treeJson.do";
	var videoUrl = "transport/carinfo/liveVideo.do";
	var carStatusUrl = "transport/carinfo/carStatus.do";
	
	//创建视频播放窗口
	function createVideoWin(videoJson){
		var self = this;
		var playId = "flv_" + new Date().getTime();
		var jqWin = null;
		var winPaddingW = 15,winPaddingH = 40;
		var toolIcons = [{
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"panel-tool-min panel-tool-a",
			onClick:function(){
				createVideoPanel.call(self,self.jqRight,videoJson);
				jqWin.window("destroy");
				self.onlineVideos[videoJson.name] = {
						deviceId:videoJson.deviceId,
						cameraId:videoJson.cameraId
				};
			}
		}];
		var panelOpts = {
			title:videoJson.name,
			closable:true,
			width:800 + winPaddingW,
			height:600 + winPaddingH,
			maximizable:true,
			headerTools:toolIcons,
			onClose:function(){
				self.onlineVideos[videoJson.name] = null;
			},
			onResize:function(w,h){
				$ui.print(w + "x" + h);
				if(!jqWin) return;
				var jqPlayer = jqWin.find("#" + playId);
				if(jqPlayer[0].play)
					jqPlayer.width(w - winPaddingW).height(h - winPaddingH);
			},
			bodyUI:{
				eName:"div",
				id:playId
			}
		};
		jqWin = $ui.createWin(panelOpts);
		self.onlineVideos[videoJson.name] = {
				deviceId:videoJson.deviceId,
				cameraId:videoJson.cameraId
		};
		loadPlayer(jqWin,playId,videoJson,800,600);
	}
	//创建video player panel
	function createVideoPanel(jqDom,videoJson){
		var self = this;
		var playId = "flv_" + new Date().getTime();
		var widthPadding = 10,heightPadding = 33;
		var toolIcons = [{
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"panel-tool-max panel-tool-a",
			onClick:function(){
				jqPanel.panel("destroy");//销毁
				createVideoWin.call(self,videoJson);
				self.onlineVideos[videoJson.name] = {
					deviceId:videoJson.deviceId,
					cameraId:videoJson.cameraId
				};
			}
		}];
		var panelOpts = {
			eName:"panel",
			title:videoJson.name,
			closable:true,
			style:{margin:6,float:'left'},
			width:400 + 10,
			height:300 + 33,
			headerTools:toolIcons,
			onClose:function(){
				self.onlineVideos[videoJson.name] = null;
				$(this).window('destroy');
			},
			onResize:function(w,h){
				$ui.print(w + "x" + h);
			},
			elements:{eName:"div",id:playId}
		};
		var jqPanel = jqDom.createUI(panelOpts);
		self.onlineVideos[videoJson.name] = {
				deviceId:videoJson.deviceId,
				cameraId:videoJson.cameraId
		};
		loadPlayer(jqPanel,playId,videoJson,400,300);
		//设置面板大小
		function setPanelSize(w,h){
			jqPanel.panel("resize",{width:w + widthPadding,height:h + heightPadding});
			var jqPlayer = jqPanel.find("#" + playId);
			jqPlayer.width(w).height(h);
		}
	}
	//加载flv player
	function loadPlayer(jqPanel,playId,videoJson,w,h){
		var swfDom = jqPanel.find("#" + playId)[0];
		//加载swf
		swfobject.embedSWF("flv/LivePlayer.swf?_1", swfDom, w, h, 11);
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
	//创建datalist
	function createDeviceUI(){
		var self = this;
		return {
			eName:"panel",
			title:"运输车辆实时视频",
			fit:true,
			elements:{
				eName:'tree',
				id:"car_tree",
				cascadeCheck:true,
				checkbox:false,
				lines:true,
				dataType:'list',
				nodeId:'nodeId',
				nodeText:'nodeText',
				nodePid:'parentId',
				url:deviceUrl,
				onLoadSuccess:function(){
					online($(this));
				},
				onSelect:function(node){
					if(node.nodeType == 3){
						var parentNode = $(this).tree("getParent",node.target);
						if(!parentNode.online){
							$ui.alert("设备不在线!");
							return;
						}
						var title = node.carNum + "-" + node.nodeText;
						$ui.progress("视频开启中...");
						$ui.postEx(videoUrl,{deviceId:node.deviceId,cameraId:node.cameraId},function(retJson){
							$ui.progress("close");
							if(retJson.result) {
								var dataJson = retJson.data;
								var videoPanels = self.jqRight.findJq("panel");
								var flag = false;
								videoPanels.each(function(i,videoPanel){
									var opts = $(this).findOpts();
									//$ui.print(opts);
									if(opts.title == title){
										flag = true;
										return false;
									}
								});
								if(flag) return;
								var videoJson = {
									rtmpUrl:dataJson.rtmpUrl,
									rtmpPath:dataJson.playPath,
									deviceId:node.deviceId,
									cameraId:node.cameraId,
									name:title
								};
								createVideoPanel.call(self,self.jqRight,videoJson);
							}else $ui.alert(retJson.info);
						});
					}
				}
			}
		};
	}
	//节点状态
	function online(jqTree){
		function carTimer(){
			$ui.postEx(carStatusUrl,function(dataJson){
				var carJsons = dataJson.data;
				$.each(carJsons,function(i,carJson){
					var node = jqTree.tree("find",carJson.deviceId);
					if(carJson.online){
						$(node.target).css("color","black");
					}else
						$(node.target).css("color","red");
					node.online = carJson.online;
				});
			});
		}
		window.setInterval(carTimer,20 * 1000);
		carTimer();
	}
	//入口
	function Main(jqGroup,loadParams){
		var self = this;
		self.onlineVideos = {};
		var deviceOpts = createDeviceUI.call(self);
		var mainOpts = {
			eName:"div",
			cls:"abs_layout",
			elements:[{
				eName:"div",
				cls:"abs_panel",
				width:0.15,
				elements:deviceOpts
			},{
				eName:"div",
				cls:"abs_panel",
				id:"abs_panel_right",
				width:0.85
			}]
		}
		jqGroup.createUI(mainOpts);
		self.jqGrid = jqGroup.findJq("datagrid");
		self.jqRight = jqGroup.find("#abs_panel_right");
	}
	return Main;
});
