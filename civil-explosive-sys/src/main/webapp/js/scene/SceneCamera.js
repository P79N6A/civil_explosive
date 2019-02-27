
/*
 * 现场摄像头,实时播放
 * */
define(function(require,exports,module){
	var deviceUrl = "scene/scenecamera/online.do";
	var videoUrl = "scene/scenecamera/playVideo.do";
	var ctrlCameraUrl = "scene/scenecamera/ctrlCamera.do";
	//窗口大小调整
	function createCtrlBtns(videoJson){ 
		function ctrlCamera(method){
			$ui.postEx(ctrlCameraUrl,{id:videoJson.id,cmd:method});
		}
		return [{
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"icon-add panel-tool-a",
			onClick:function(){
				ctrlCamera(4);
			}
		},{
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"icon-remove panel-tool-a",
			onClick:function(){
				ctrlCamera(5);
			}
		},{
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"icon_ctrl_right panel-tool-a",
			onClick:function(){
				ctrlCamera(1);
			}
		},{
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"icon_ctrl_down panel-tool-a",
			onClick:function(){
				ctrlCamera(2);
			}
		},{
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"icon_ctrl_up panel-tool-a",
			onClick:function(){
				ctrlCamera(0);
			}
		},{
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"icon_ctrl_left panel-tool-a",
			onClick:function(){
				ctrlCamera(3);
			}
		}];
	}
	//创建视频播放窗口
	function createVideoWin(videoJson){
		var self = this;
		var playId = "flv_" + new Date().getTime();
		var jqWin = null;
		var winPaddingW = 15,winPaddingH = 40;
		var toolIcons = createCtrlBtns(videoJson);
		toolIcons.unshift({
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"panel-tool-min panel-tool-a",
			onClick:function(){
				createVideoPanel.call(self,self.jqRight,videoJson);
				jqWin.window("close");
				self.onlineVideos[videoJson.name] = {
						deviceId:videoJson.deviceId,
						cameraId:videoJson.cameraId
				};
			}
		});
		var panelOpts = {
			title:videoJson.name,
			closable:true,
			width:800 + winPaddingW,
			height:600 + winPaddingH,
			maximizable:true,
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
			headerTools:toolIcons,
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
		var toolIcons = createCtrlBtns(videoJson);
		toolIcons.unshift({
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"panel-tool-max panel-tool-a",
			onClick:function(){
				createVideoWin.call(self,videoJson);
				jqPanel.panel("destroy");//销毁
				self.onlineVideos[videoJson.name] = {
					deviceId:videoJson.deviceId,
					cameraId:videoJson.cameraId
				};
			}
		});
		var panelOpts = {
			eName:"panel",
			title:videoJson.name,
			closable:true,
			style:{margin:6,float:'left'},
			width:400 + 10,
			height:300 + 33,
			onClose:function(){
				self.onlineVideos[videoJson.name] = null;
				$(this).window('destroy');
			},
			onResize:function(w,h){
				$ui.print(w + "x" + h);
			},
			headerTools:toolIcons,
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
		window.onFlexInit = function(){
			$ui.print(videoJson.rtmpUrl + "," + videoJson.rtmpPath);
			window.setTimeout(function(){
				var swfPlayer = jqPanel.find("#" + playId)[0];
				swfPlayer.play(videoJson.rtmpUrl, videoJson.rtmpPath);
			},100);
		};
		//加载swf
		swfobject.embedSWF("flv/LivePlayer.swf?_3", swfDom, w, h, 11);
	}
	//创建datalist
	function createDeviceUI(){
		var self = this;
		return {
			eName:"datagrid",
			title:"现场摄像头列表",
			id:"cameraGrid",
			fit:true,
			pagination:false,
			border:true,
			idField:"cameraId",
			url:deviceUrl,
			fitColumns:true,
			loadMsg:"",
			scrollbarSize:0,
			queryParams:{deviceType:1},
			rowStyler:function(index,row){
				return (row.online)?"color:black":"color:red";
			},
			columns:[[
				{field:'cameraId',title:'cameraId',hidden:true},
				{field:'cameraName',title:'名称',width:160}
			]],
			headerTools:[{
    			eName:"a",
    			href:"javascript:void(0)",
    			cls:"icon-refresh panel-tool-a",
    			onClick:function(){
    				self.jqGrid.datagrid("load");
    			}
    		}],
			onClickRow:function(index,row){
				if(!row.online) return;
				$ui.progress("视频开启中...");
				$ui.postEx(videoUrl,{id:row.id,cameraId:1},function(retJson){
					$ui.progress("close");
					if(retJson.result) {
						var videoJson = retJson.data;
						var videoPanels = self.jqRight.findJq("panel");
						var flag = false;
						videoPanels.each(function(i,videoPanel){
							var opts = $(this).findOpts();
							//$ui.print(opts);
							if(opts.title == row.cameraName){
								flag = true;
								return false;
							}
						});
						if(flag) return;
						var videoJson = {
							rtmpUrl:retJson.data.rtmpUrl,
							rtmpPath:retJson.data.playPath,
							name:retJson.data.cameraName,
							deviceId:row.sipId,
							cameraId:row.cameraCode,
							id:retJson.data.id
						};
						createVideoPanel.call(self,self.jqRight,videoJson);
					}else $ui.alert(retJson.info);
				});
			}
		};
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
