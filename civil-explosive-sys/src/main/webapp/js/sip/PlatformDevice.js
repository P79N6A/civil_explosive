

define(function(require,exports,module){
	var mName = "平台设备";
	var queryUrl = "sip/platformdevice/jsons.do";
	var queryCameraUrl = "sip/platformdevice/cameraJsons.do";
	var saveUrl = "sip/platformdevice/save.do";
	var removeUrl = "sip/platformdevice/delete.do";
	var videoUrl = "sip/platformdevice/getVideoUrl.do";
	var closeVideoUrl = "sip/platformdevice/closeVideo.do";
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null;
		var formOpts = {
			formEx:{
				formData:row,url:saveUrl,
				onSave:function(retJson){
					if(!retJson.result) return;
					jqWin.window("close");
					self.jqGrid.datagrid("reload");
				}
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = {
			formDialog:{
				title:mName,
				height:280
			}
		};
		jqWin = $ui.createFormDialog(dialogOpts,formUI);
	}
	//创建工具栏
	function createToolbar(gridOpts){
		var self = this;
		gridOpts.toolbarEx = [{
			eName:"linkbutton",plain:true,
			text:"新增",iconCls:"icon-add",
			onClick:function(){
				var row = {};
	        	gridEdit.call(self,"新增",row);
			}
		},{
			eName:"linkbutton",plain:true,
			text:"编辑",iconCls:"icon-edit",
			onClick:function(){
				var row = self.jqGrid.datagrid("getSelected");
				if (row == null) {
					$ui.alert("请选择一条要修改的记录 !");
					return;
				}
				gridEdit.call(self,"编辑",row);
			}
		},{
			eName:"linkbutton",plain:true,
			text:"删除",iconCls:"icon-remove",
			onClick:function(){
				var row = self.jqGrid.datagrid("getSelected");
				if (row == null) {
					$ui.alert("请选择一条要删除的记录 !");
					return;
				}
				$ui.confirm("删除后不能撤销,你确定要删除此记录吗 ?", function(r) {
					if (!r) return;
					self.jqGrid.datagrid("remove",[row,removeUrl]);
				});
			}
		},{
			eName:"linkbutton",plain:true,hidden1:true,
			text:"停止拉流",iconCls:"icon-add",
			onClick:function(){
				var row1 = self.jqGrid.datagrid("getSelected");
				if (row1 == null) {
					$ui.alert("请选择一条记录 !");
					return;
				}
				var row2 = self.jqCameraGrid.datagrid("getSelected");
				if (row2 == null) {
					$ui.alert("请选择一条记录 !");
					return;
				}
				$ui.postEx("sip/platformdevice/stopVideo.do",{deviceId:row1.sipId,cameraId:row2.cameraId});
			}
		},{
			eName:"linkbutton",plain:true,hidden1:true,
			text:"云台控制开始",iconCls:"icon-add",
			onClick:function(){
				var row1 = self.jqGrid.datagrid("getSelected");
				if (row1 == null) {
					$ui.alert("请选择一条记录 !");
					return;
				}
				var row2 = self.jqCameraGrid.datagrid("getSelected");
				if (row2 == null) {
					$ui.alert("请选择一条记录 !");
					return;
				}
				$ui.postEx("sip/platformdevice/startCtrl.do",{deviceId:row1.sipId,cameraId:row2.cameraId,cmd:0x01,data:60});
			}
		},{
			eName:"linkbutton",plain:true,hidden1:true,
			text:"云台控制结束",iconCls:"icon-add",
			onClick:function(){
				var row1 = self.jqGrid.datagrid("getSelected");
				if (row1 == null) {
					$ui.alert("请选择一条记录 !");
					return;
				}
				var row2 = self.jqCameraGrid.datagrid("getSelected");
				if (row2 == null) {
					$ui.alert("请选择一条记录 !");
					return;
				}
				$ui.postEx("sip/platformdevice/stopCtrl.do",{deviceId:row1.sipId,cameraId:row2.cameraId});
			}
		}];
	}
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{field:'unitName',title:'所属从业单位',width:200,editor:{
				required:true,
				eName:'combobox',
				name:'unitId',
				editable:false,
				valueField: 'id',
				textField: 'name',
				panelHeight:'auto',
				url:'base/workingunit/boxJson.do'
			}},
			{field:'id',title:'id',hidden:true},
			{field:'deviceName',title:'设备名称',editor:{required:true}},
			//{field:'sipName',title:'设备sip用户名',editor:{required:true}},
			{field:'sipId',title:'设备sipId',width:150,editor:{required:true}},
			//{field:'sipPwd',title:'设备sip密码',editor:{required:true}},
			{field:'deviceMode',title:'设备型号',editor:{}},
			{field:'siteTypeName',title:'场所类型',editor:{
				required:true,
				eName:'combobox',
				name:'siteType',
				valueField: 'codeValue',
				textField: 'codeName',
				url:'sys/syscode/jsons.do',
				queryParams:{codeType:'h'}
			}},
			{field:'autoSaveName',title:'自动录制',editor:{
				eName:'radiobox',
				name:'autoSave',
				data:[{text:'是',value:'1'}
					,{text:'否',value:'0'}]
			}},
			{field:'onlineName',title:'是否在线'},
			{field:'timestamp',title:'心跳',width:150}
		]];
	}
	//
	function createCameraOpts(){
		var self = this;
		return {
			eName:"datagrid",
			title:"摄像头列表",
			id:"cameraGrid",
			fit:true,
			border:true,
			autoload:false,
			idField:"cameraId",
			url:queryCameraUrl,
			onDblClickRow:function(index,row){
				var deviceRow = self.jqGrid.datagrid("getSelected");
				$ui.postEx(videoUrl,{sipId:deviceRow.sipId,cameraId:row.cameraId},function(retJson){
					if(retJson.result) startVideo.call(self,retJson.data,deviceRow.sipId,row.cameraId);
					else $ui.alert(retJson.info);
				});
			},
			columns:[[
				{field:'cameraId',title:'cameraId',width:160},
				{field:'cameraName',title:'名称'},
				{field:'status',title:'状态'}
			]]
		}
	}
	//入口
	function Main(jqGroup,loadParams){
		var self = this;
		var gridOpts = {
			eName:"datagrid",
			id:"deviceGrid",
			fit:true,
			border:true,
			autoload:false,
			idField:"id",
			url:queryUrl,
			onClickRow:function(index,row){
				self.jqCameraGrid.datagrid("load",{sipId:row.sipId})
			}
		}
		var camearGridOpts = createCameraOpts.call(self);
		createToolbar.call(self,gridOpts);
		createColumns.call(self,gridOpts);
		var mainUI = {
			eName:"layoutExt",
			elements:[{
				region:"center",
				elements:gridOpts
			},{
				region:"east",
				width:500 ,
				elements:camearGridOpts
			}]
		}
		jqGroup.createUI(mainUI);
		self.jqGrid = jqGroup.find("#deviceGrid");
		self.jqCameraGrid = jqGroup.find("#cameraGrid");
		self.jqGrid.datagrid("load");
	}
	
	function startVideo(videoJson,deviceId,cameraId){
		return;
		var self = this;
		var playId = "flv_" + new Date().getTime();
		var winWidth = 640,winHeight = 480;
		$ui.createWin({
			title:'flv测试',
			width:winWidth,
			height:winHeight,
			id:playId,
			modal:false,
			onDestroy:function(){
				$ui.postEx(closeVideoUrl,{deviceId:deviceId,cameraId:cameraId});
			}
		});
		jwplayer(playId).setup({
			 flashplayer: 'flv/player.swf',
			 primary : 'flash',
		     file:videoJson.path,
		     streamer: videoJson.rtmpUrl,
		    controlbar: 'none',
		    autostart:true,
		    width:winWidth - 6,
			height:winHeight - 6
		});
	}
	return Main;
});
