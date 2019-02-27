
/*运输车辆管理*/
define(function(require,exports,module){
	var mName = "运输车辆";
	var queryUrl = "transport/carinfo/jsons.do";
	var saveUrl = "transport/carinfo/save.do";
	var removeUrl = "transport/carinfo/delete.do";
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
				height:350
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
			eName:"linkbutton",plain:true,
			text:"注册所有设备",iconCls:"icon-edit",
			onClick:function(){
				$ui.postEx("transport/carinfo/regAll.do");
			}
		},{
			eName:"linkbutton",plain:true,
			text:"注册设备",iconCls:"icon-add",
			onClick:function(){
				var row = self.jqGrid.datagrid("getSelected");
				if (row == null) {
					$ui.alert("请选择一条要修改的记录 !");
					return;
				}
				$ui.postEx("transport/carinfo/regCar.do",row);
			}
		},{
			eName:"linkbutton",plain:true,
			text:"开始视频",iconCls:"icon-add",
			onClick:function(){
				var row = self.jqGrid.datagrid("getSelected");
				if (row == null) {
					$ui.alert("请选择一条要修改的记录 !");
					return;
				}
				$ui.postEx("transport/carinfo/startVideo.do",row);
			}
		},{
			eName:"linkbutton",plain:true,
			text:"结束视频",iconCls:"icon-add",
			onClick:function(){
				var row = self.jqGrid.datagrid("getSelected");
				if (row == null) {
					$ui.alert("请选择一条要修改的记录 !");
					return;
				}
				$ui.postEx("transport/carinfo/stopVideo.do",row);
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
			{field:'carNum',title:'车辆号牌',editor:{required:true},width:160},
			{field:'deviceId',title:'设备编号',editor:{}},
			{field:'cameraCount',title:'设备头数量',editor:{eName:"numberbox",min:1,max:255}},
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
			}},
			{field:'rtmpdCtx',title:'rtmpd上下文',editor:{
				eName:'combobox',
				name:'rtmpdCtx',
				valueField: 'codeDesc1',
				textField: 'codeDesc1',
				url:'sys/syscode/jsons.do',
				queryParams:{'codeType':'k','codeDesc4':1}
			}},
			{field:'audioCode',title:'音频编码',editor:{
				eName:'combobox',
				name:'audioCode',
				valueField: 'codeValue',
				textField: 'codeName',
				url:'sys/syscode/jsons.do',
				queryParams:{'codeType':'l'}
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
