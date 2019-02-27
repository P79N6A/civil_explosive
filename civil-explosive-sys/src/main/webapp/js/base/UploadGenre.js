
/*
 * xx模块
 * */
define(function(require,exports,module){
	//地址
	var queryUrl = "base/uploadgenre/jsons.do";
	var saveUrl = "base/uploadgenre/save.do";
	var removeUrl = "base/uploadgenre/delete.do";
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
				title:title + "上传文档类别",
				height:160
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
				row.typeCode = self.jqBox.combobox('getValue');
				row.typeCodeName = self.jqBox.combobox('getText');
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
			eName:'combobox',
			width:100,
			panelWidth:130,
			name:'typeCode',
			value:"1",
			valueField: 'codeValue',
			textField: 'codeName',
			url:'sys/syscode/jsons.do',
			queryParams:{codeType:9},
			onChange:function(newValue, oldValue){//combobox事件
				self.jqGrid.datagrid('load');
			}
		}];
	}
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{field:'id',title:'id',hidden:true},
			{field:'typeCodeName',title:'文档类型',editor:{required:true,readonly:true}},
			{field:'typeCode',title:'文档类型',hidden:true},
			{field:'name',title:'名称',editor:{required:true}}
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
		self.jqBox = jqGroup.findJq("combobox");
		self.jqGrid.datagrid("load");
	}
	return Main;
});
