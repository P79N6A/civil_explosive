
/*
 * xx模块
 * */
define(function(require,exports,module){
	var mName = "爆破作业运输许可证";
	var auditType = "备案";
	var queryUrl = "contract/outsidetransportcard/jsons.do";
	var saveUrl = "contract/outsidetransportcard/save.do";
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
				title:auditType + mName,
				height:370
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
			text:auditType,iconCls:"icon-edit",
			onClick:function(){
				var row = self.jqGrid.datagrid("getSelected");
				if (row == null) {
					$ui.alert("请选择一条要修改的记录 !");
					return;
				}
				gridEdit.call(self,"编辑",row);
			}
		}];
	}
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{field:'id',title:'id',hidden:true},
			{field:'code',title:'运输证编号',editor:{required:true}},
			{field:'status',title:'购买证状态',editor:{}},
			{field:'purchaseId',title:'购买单位',editor:{}},
			{field:'marketingId',title:'销售单位',editor:{}},
			{field:'carNum',title:'车牌号码',editor:{}},
			{field:'carrier',title:'承运人',editor:{}},
			{field:'beginDate',title:'发证日期',editor:{}},
			{field:'endDate',title:'有效期至',editor:{}},
			{field:'operator',title:'经办人',editor:{required:true}}
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
