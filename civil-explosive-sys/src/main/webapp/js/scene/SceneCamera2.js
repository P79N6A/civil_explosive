	

//现场摄像头管理
define(function(require,exports,module){
	var mName = "现场摄像头";
	var queryUrl = "scene/scenecamera/jsons.do";
	var saveUrl = "scene/scenecamera/save.do";
	var removeUrl = "scene/scenecamera/delete.do";
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
				height:320
			}
		};
		jqWin = $ui.createFormDialog(dialogOpts,formUI);
	}
	
	function testModule(method){
		var self = this;
		var row = self.jqGrid.datagrid("getSelected");
		if (row == null) {
			$ui.alert("请选择一条要修改的记录 !");
			return;
		}
		$ui.postEx("camera/" + method + ".do",{deviceId:row.sipId,streamType:1,cameraId:1
			,platType:row.platType,autoSave:row.autoSave,appName:row.rtmpdCtx});
	}
	//创建工具栏
	function createToolbar(gridOpts){
		var self = this;
		gridOpts.toolbarEx = [{
			eName:"linkbutton",plain:true,
			text:"注册设备",iconCls:"icon-add",
			onClick:function(){
				testModule.call(self,"regDevice");
			}
		},{
			eName:"linkbutton",plain:true,
			text:"删除设备",iconCls:"icon-remove",
			onClick:function(){
				testModule.call(self,"removeDevice");
			}
		},{
			eName:"linkbutton",plain:true,
			text:"设备列表",iconCls:"icon-edit",
			onClick:function(){
				testModule.call(self,"deviceList");
			}
		},{
			eName:"linkbutton",plain:true,
			text:"同步状态",iconCls:"icon-refresh",
			onClick:function(){
				testModule.call(self,"syncDevice");
			}
		},{
			eName:"linkbutton",plain:true,
			text:"开始视频",iconCls:"icon-edit",
			onClick:function(){
				testModule.call(self,"startVideo");
			}
		},{
			eName:"linkbutton",plain:true,
			text:"结束视频",iconCls:"icon-edit",
			onClick:function(){
				testModule.call(self,"stopVideo");
			}
		},{
			eName:"linkbutton",plain:true,
			text:"开始保存",iconCls:"icon-save",
			onClick:function(){
				testModule.call(self,"startSave");
			}
		},{
			eName:"linkbutton",plain:true,
			text:"结束保存",iconCls:"icon-save",
			onClick:function(){
				testModule.call(self,"stopSave");
			}
		}];
	}
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{field:'id',title:'id',hidden:true},
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
			{field:'cameraName',title:'设备名称',editor:{required:true}},
			{field:'sipId',title:'设备id',width:200,editor:{required:true}},
			{field:'cameraCode',title:'摄像头编码',editor:{}},
			{field:'cameraMode',title:'设备型号',editor:{}},
			{field:'platTypeName',title:'平台类型',editor:{
				required:true,
				eName:'radiobox',
				name:'platType',
				data:[{text:'GB28181',value:'1'}
					,{text:'海康平台',value:'2'}]
			}},
			{field:'autoSaveName',title:'自动录制',editor:{
				eName:'radiobox',
				name:'autoSave',
				data:[{text:'是',value:'1'}
					,{text:'否',value:'0'}]
			}}
		]];
	}
	//入口
	function Main(jqGroup,loadParams){
		var self = this;
		var gridOpts = {
			eName:"datagrid",
			fit:true,
			border:false,
			autoload:false,
			idField:"id",
			url:queryUrl
		}
		createToolbar.call(self,gridOpts);
		createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
	