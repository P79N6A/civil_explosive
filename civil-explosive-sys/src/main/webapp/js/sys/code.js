
/*
 * 字典模块
 * */
define(function(require,exports,module){
	//地址
	var queryUrl = "sys/syscode/json.do";
	var saveUrl = "sys/syscode/save.do";
	var removeUrl = "sys/syscode/delete.do";
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null;
		var formOpts = {
			formEx:{
				formData:row,
				url:saveUrl,
				onSave:function(retJson){
					if(!retJson.result) return;
					//字典类型修改时，刷新
					if(1 == self.jqBox.combobox("getValue")){
						self.jqBox.combobox("reload");
					}
					self.jqGrid.datagrid("reload");
					jqWin.window("close");
				}
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = {
			formDialog:{
				title:title + "字典",
				height:346
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
				row.codeType = self.jqBox.combobox("getValue");
	        	row.codeTypeName = self.jqBox.combobox("getText");
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
					if (!r) return;// 取消
					self.jqGrid.datagrid("remove",[row,removeUrl]);
				});
			}
		},{
			eName:"combobox",
			width:100,
			panelWidth:130,
			panelHeight:200,
			name:"codeType",
			valueField: "codeValue",
			textField: "codeName",
			value:"1",//默认值
			url:"sys/syscode/jsons.do",
			queryParams:{codeType:1},
			onChange:function(){
				self.jqGrid.datagrid("load");
			}
		}];
	}
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{field:"codeId",title:"codeId",hidden:true},
			{field:"codeType",title:"codeType",hidden:true},
			{field:"codeTypeName",title:"字典类别",editor:{required:true,readonly:true}},
			{field:"codeName",title:"字典名称",editor:{required:true},width:130},
			{field:"codeValue",title:"字段值",editor:{required:true},width:60},
			{field:"codeDesc1",title:"字典描述一",editor:{}},
			{field:"codeDesc2",title:"字典描述二",editor:{}},
			{field:"codeDesc3",title:"字典描述三",editor:{}},
			{field:"codeDesc4",title:"字典描述四",editor:{}},
			{field:"remark",title:"备注",editor:{}}
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
			fitColumns:true,
			idField:"codeId",
			url:queryUrl,
			queryParams:{codeType:1}
		}
		createToolbar.call(self,gridOpts);
		createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqBox = jqGroup.findJq("combobox");
		self.jqGrid.datagrid("load");
	}
	return Main;
});
