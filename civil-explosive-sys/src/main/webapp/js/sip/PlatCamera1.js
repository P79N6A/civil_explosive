
/*
 * 现场摄像头
 * */
define(function(require,exports,module){

	var deviceUrl = "sip/platformdevice/allCamera.do";
	var videoUrl = "sip/platformdevice/getVideoUrl.do";
	var closeVideoUrl = "sip/platformdevice/closeVideo.do";
	var ctrlCameraUrl = "sip/platformdevice/startCtrl.do";
	var stopCtrlCameraUrl = "sip/platformdevice/stopCtrl.do";
	
	//窗口大小调整
	function createCtrlBtns(videoJson){
		function ctrlDown(method){
			$ui.postEx(ctrlCameraUrl,{deviceId:videoJson.deviceId,cameraId:videoJson.cameraId,cmd:method});
		}
		function ctrlUp(method){
			$ui.postEx(stopCtrlCameraUrl,{deviceId:videoJson.deviceId,cameraId:videoJson.cameraId});
		}
		return [{
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"icon-add panel-tool-a",
			onMousedown:function(){
				ctrlDown(0x10);
			},
			onMouseup:ctrlUp
		},{
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"icon-remove panel-tool-a",
			onMousedown:function(){
				ctrlDown(0x20);
			},
			onMouseup:ctrlUp
		},{
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"icon_ctrl_right panel-tool-a",
			onMousedown:function(){
				ctrlDown(0x01);
			},
			onMouseup:ctrlUp
		},{
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"icon_ctrl_down panel-tool-a",
			onMousedown:function(){
				ctrlDown(0x04);
			},
			onMouseup:ctrlUp
		},{
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"icon_ctrl_up panel-tool-a",
			onMousedown:function(){
				ctrlDown(0x08);
			},
			onMouseup:ctrlUp
		},{
			addMode:'prepend',
			eName:"a",
			href:"javascript:void(0)",
			cls:"icon_ctrl_left panel-tool-a",
			onMousedown:function(){
				ctrlDown(0x02);
			},
			onMouseup:ctrlUp
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
			}
		});
		var panelOpts = {
			title:videoJson.name,
			closable:true,
			width:800 + winPaddingW,
			height:600 + winPaddingH,
			maximizable:true,
			onDestroy:function(){
				$ui.postEx(closeVideoUrl,{deviceId:videoJson.deviceId,cameraId:videoJson.cameraId});
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
			}
		});
		var panelOpts = {
			eName:"panel",
			title:videoJson.name,
			closable:true,
			style:{margin:6,float:'left'},
			width:400 + 10,
			height:300 + 33,
			onDestroy:function(){
				$ui.postEx(closeVideoUrl,{deviceId:videoJson.deviceId,cameraId:videoJson.cameraId});
			},
			onResize:function(w,h){
				$ui.print(w + "x" + h);
			},
			headerTools:toolIcons,
			elements:{eName:"div",id:playId}
		};
		var jqPanel = jqDom.createUI(panelOpts);
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
					swfPlayer.play(videoJson.rtmpUrl,videoJson.path);
					window.clearInterval(playerTimer);
				}
			}catch(err){}
		},50);
	}
	//创建datalist
	function createDeviceUI(){
		var self = this;
		return {
			eName:"datagrid",
			title:"摄像头列表",
			id:"cameraGrid",
			fit:true,
			pagination:false,
			border:true,
			idField:"cameraId",
			url:deviceUrl,
			queryParams:{deviceType:1},
			columns:[[
				{field:'cameraId',title:'cameraId',hidden:true},
				{field:'cameraDesc',title:'名称',width:160},
				{field:'status',title:'状态',width:50}
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
				$ui.postEx(videoUrl,{sipId:row.deviceId,cameraId:row.cameraId},function(retJson){
					if(retJson.result) {
						var videoJson = retJson.data;
						var videoPanels = self.jqRight.findJq("panel");
						var flag = false;
						videoPanels.each(function(i,videoPanel){
							var opts = $(this).findOpts();
							//$ui.print(opts);
							if(opts.title == row.cameraDesc){
								flag = true;
								return false;
							}
						});
						if(flag) return;
						videoJson.deviceId = row.deviceId;
						videoJson.cameraId = row.cameraId;
						videoJson.name = row.cameraDesc;
						createVideoPanel.call(self,self.jqRight,videoJson);
					}else $ui.alert(retJson.info);
				});
			}
		};
	}
	//入口
	function Main(jqGroup,loadParams){
		var self = this;
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
