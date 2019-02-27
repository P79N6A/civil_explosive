
/*
 * xx模块
 * */
define(function(require,exports,module){
	var mName = "从业单位";//模块名称
	var auditType = "备案";//审核类型
	var queryUrl = "base/workingunit/jsons.do";
	var saveUrl = "base/workingunit/save1.do";
	var gridObj = require("js/base/WorkingUnitGrid");
	var personGridObj = require("js/base/WorkingPersonGrid");
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null;
		var jqUpload = null;
		var btnDisabled = (row.auditResult && row.auditStep > 1
				&& row.auditStep < 3)?true:false;
		var formOpts = {
			onEditorCreate:function(column,editor){
				if(column.field == "guardLeaderName" 
						|| column.field == "techLeaderName" ){
					editor.queryParams = $.extend({},editor.queryParams,{unitId:row.id});
				}
			},
			formEx:{
				formData:row,url:saveUrl,
				onBeforeSave:function(formParams){
					//设置无效
					if(formParams.endStart) formParams.endStartEx = 0; 
					//绑定上传资源ids
					var ids = [];
					jqUpload.formIDs(ids);
					formParams.uploadIds = ids.join(",");
					return true;
				},
				onSave:function(retJson){
					if(!retJson.result) return;
					jqWin.window("close");
					self.jqGrid.datagrid("reload");
				},
				onLoad:function(formData){//表单加载完毕
					if(row.endStartEx){
						$(this).findJq("endStart").datebox("disable");
					}
				}
			},
			formTable:{
				cols:4,
				tdWidth:[110,-1,110,-1],
				trHeight:[32]
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = {
			formDialog:{
				title:auditType + mName,
				width:800,height:670
			},
			onLayoutCreate:function(regions){
				var region = regions.pop();
				regions.push({
					region:"south",
					height:200,
					cssStyle:"padding:3px"
				});
				regions.push({
					region:"south",
					height:80,
					cssStyle:"padding:3px"
				});
				regions.push(region);
			},
			formBtns:[{disabled:btnDisabled}]
		};
		jqWin = $ui.createFormDialog(dialogOpts,formUI);
		var jqLayout = jqWin.findJq('layoutExt');
		var uploadDiv = jqLayout.layoutExt("getPanel",{region:'south',index:1});
		jqUpload = uploadDiv.loadJs("js/base/upload2",{hostType:1,hostId:row.id});
		var jqDiv = jqLayout.layoutExt("getPanel",{region:'south',index:0});
		personGridObj.createSumGrid.call(self,jqDiv,row.id,0);
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
	function Main(jqGroup,loadParams){
		var self = this;
		var gridOpts = {
			eName:"datagrid",
			fit:true,
			border:false,
			autoload:false,
			pagination:false,
			idField:"id",
			url:queryUrl,
			remoteSort:false
		}
		createRowBtns.call(self,gridOpts);
		var userJson = $ui.data("user");
		if(userJson.roleIds==11) gridObj.createColumns1.call(self,gridOpts);
		else gridObj.createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
