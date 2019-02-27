
/*
 * 现场摄像头
 * */
define(function(require,exports,module){
	var mName = "摄像头";
	var queryUrl = "sip/platformdevice/allCamera.do";
	var videoUrl = "sip/platformdevice/getVideoUrl.do";
	
	//云台控制面板
	function createCtrlPanel(jqDom,deviceId,cameraId){
		var self = this;
		
	}
	//入口
	function Main(jqGroup,loadParams){
		var self = this;
		var playId = "flv_" + new Date().getTime();
		var postParams = null;
		var gridOpts = {
			eName:"datagrid",
			title:"摄像头列表",
			id:"cameraGrid",
			fit:true,
			pagination:false,
			border:true,
			idField:"cameraId",
			url:queryUrl,
			queryParams:{deviceType:1},
			onDblClickRow:function(index,row){
				postParams = {deviceId:row.deviceId,cameraId:row.cameraId};
				$ui.postEx(videoUrl,{sipId:row.deviceId,cameraId:row.cameraId},function(retJson){
					if(retJson.result) {
						var videoJson = retJson.data;
						var swfDom = jqGroup.find("#" + playId).get(0);
						swfDom.play(videoJson.rtmpUrl,videoJson.path);
					}else $ui.alert(retJson.info);
				});
			},
			columns:[[
				{field:'cameraId',title:'cameraId',hidden:true},
				{field:'cameraDesc',title:'名称',width:160},
				{field:'status',title:'状态',width:50}
			]]
		}
		//停止控制云台
		function startCtrl(method){
			if(postParams){
				$ui.print("开始");
				$ui.postEx("sip/platformdevice/startCtrl.do",$.extend(postParams,{cmd:method}));
			}
		}
		//停止控制云台
		function stopCtrl(){
			if(postParams){
				$ui.print("结束");
				$ui.postEx("sip/platformdevice/stopCtrl.do",postParams);
			}
		}
		var mainOpts = {
			eName:"layoutExt",
			fit:false,
			width:1200,
			height:600,
			elements:[{
				region:"west",
				width:240 ,
				elements:gridOpts
			},{
				region:"center",
				elements:{
					eName:"div",
					cls:"div_flv_group",
					elements:[{
						eName:"div",
						cssStyle:"background-color:#000;",
						elements:{eName:"div",id:playId}
					},{
						eName:"div",
						width:240,
						cssStyle:"height:auto;",
						marginTop:100,
						elements:[{
							eName:"div",
							cls:"ptz_div",
							elements:{
								eName:"a",
								cls:"ptz_icon  ptz_icon_up",
								onMousedown:function(){
									startCtrl(0x08);
								},
								onMouseup:stopCtrl
							}
						},{
							eName:"div",
							cls:"ptz_div",
							elements:[{
								eName:"a",
								cls:"ptz_icon  ptz_icon_left",
								onMousedown:function(){
									startCtrl(0x02);
								},
								onMouseup:stopCtrl
							},{
								eName:"a",
								cls:"ptz_icon  ptz_icon_zoomout",
								onMousedown:function(){
									startCtrl(0x20);
								},
								onMouseup:stopCtrl
							},{
								eName:"a",
								cls:"ptz_icon  ptz_icon_zoomin",
								onMousedown:function(){
									startCtrl(0x10);
								},
								onMouseup:stopCtrl
							},{
								eName:"a",
								cls:"ptz_icon  ptz_icon_right",
								onMousedown:function(){
									startCtrl(0x01);
								},
								onMouseup:stopCtrl
							}]
						},{
							eName:"div",
							cls:"ptz_div",
							elements:{
								eName:"a",
								cls:"ptz_icon ptz_icon_down",
								onMousedown:function(){
									startCtrl(0x04);
								},
								onMouseup:stopCtrl
							}
						}]
					}]
				}
			}]
		}
		jqGroup.createUI(mainOpts);
		self.jqGrid = jqGroup.find("#cameraGrid");
		var swfDom = jqGroup.find("#" + playId).get(0);
		//加载swf
		swfobject.embedSWF("flv/LivePlayer.swf?_" 
			+ new Date().getTime(), swfDom, 640, 480, 10);	
	}
	return Main;
});
