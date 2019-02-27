
/*
 * xx模块
 * */
define(function(require,exports,module){
	var mName = "炸药仓库";//模块名称
	var auditType = "备案";//审核类型
	var queryUrl = "store/explosivestore/jsons.do";
	var saveUrl = "store/explosivestore/save1.do";
	var gridObj = require("js/store/ExplosiveStoreGrid");
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null;
		var jqUpload = null;
		var btnDisabled = (row.auditResult && row.auditStep > 1
					&& row.auditStep < 3)?true:false;
		var formOpts = {
			formEx:{
				formData:row,url:saveUrl,
				onSave:function(retJson){
					if(!retJson.result) return;
					jqWin.window("close");
					self.jqGrid.datagrid("reload");
				}
			},
			formTable:{
				cols:2,trHeight:[32]
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = {
			formDialog:{
				title:auditType + mName,
				height:270
			},
			formBtns:[{disabled:btnDisabled}]
		};
		jqWin = $ui.createFormDialog(dialogOpts,formUI);
	}
	//创建工具栏
	function createToolbar(gridOpts,unitId){
		var self = this;
		gridOpts.toolbarEx = [{
			eName:"linkbutton",plain:true,
			text:"新增",iconCls:"icon-add",
			onClick:function(){
				var row = {unitId:unitId};
	        	gridEdit.call(self,"新增",row);
			}
		}];
	}
	function createRowBtns(gridOpts){
		var self = this;
		gridOpts.rowBtnsField = "row_btns66";
		gridOpts.rowBtns = [{
			name:"编辑",
			onClick:function(row){
				gridEdit.call(self,"编辑",row);
			}
		}];
	}
	//入口
	function Main(jqGroup,args){
		var self = this;
		var userJson = $ui.data("user");
		var gridOpts = {
			eName:"datagrid",
			fit:true,
			border:false,
			autoload:false,
			idField:"id",
			url:queryUrl,
			queryParams:{unitId:userJson.unitId}
		}
		createRowBtns.call(self,gridOpts);
		createToolbar.call(self,gridOpts,userJson.unitId);
		gridObj.createColumns.call(self,gridOpts,false);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
