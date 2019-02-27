
/*
 * xx模块
 * */
define(function(require,exports,module){
	var mName = "从业人员";//模块名称
	var auditType = "备案";//审核类型
	var queryUrl = "base/workingperson/jsons.do";
	var saveUrl = "base/workingperson/save1.do";
	var gridObj = require("js/base/WorkingPersonGrid");
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
				onBeforeSave:function(formParams){
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
				}
			},
			formTable:{
				cols:4,
				trHeight:[32]
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = {
			formDialog:{
				title:auditType + mName,
				width:600,height:460
			},
			onLayoutCreate:function(regions){
				var region = regions.pop();
				regions.push({
					region:"south",
					height:150,
					cssStyle:"padding:3px"
				});
				regions.push(region);
			},
			formBtns:[{disabled:btnDisabled,text:"保存并提交审核",width:100}]
		};
		jqWin = $ui.createFormDialog(dialogOpts,formUI);
		var jqLayout = jqWin.findJq('layoutExt');
		var uploadDiv = jqLayout.layoutExt("getPanel",{region:'south',index:0});
		jqUpload = uploadDiv.loadJs("js/base/upload2"
				,{hostType:2,hostId:row.id,readonly:btnDisabled});
	}
	//创建工具栏
	function createToolbar(gridOpts,unitId,licenceType){
		var self = this;
		gridOpts.toolbarEx = [{
			eName:"linkbutton",plain:true,
			text:"新增",iconCls:"icon-add",
			onClick:function(){
				var row = {unitId:unitId};
	        	gridEdit.call(self,"新增",row);
			}
		},{
			eName:"linkbutton1",plain:true,hidden:true,
			text:auditType,iconCls:"icon-edit",
			onClick:function(){
				var row = self.jqGrid.datagrid("getSelected");
				if (row == null) {
					$ui.alert("请选择一条要修改的记录 !");
					return;
				}
				gridEdit.call(self,"编辑",row);
			}
		},{
			eName:"div",
			elements:[{
				eName:"label",
				text:"许可证类型",
				marginLeft:6,
				marginRight:6
			},{
				eName:'combobox',
				name:'licenceType',
				valueField: 'codeValue',
				textField: 'codeName',
				url:'sys/syscode/jsons.do',
				queryParams:{codeType:5},
				value:licenceType,
				width:80,
				allFlag:true
			},{
				eName:"label",
				text:"审核状态",
				marginLeft:6,
				marginRight:6
			},{
				eName:'combobox',
				name:'auditStatus',
				width:80,
				data:[{value:"",text:"全部"},{value:"1",text:"待审核"},{value:"2",text:"审核中"},{value:"3",text:"审核完"}]
			},{
				eName:"textbox",
				name:"q",
				prompt:"根据姓名模糊查询",
				clearFlag:true
			},{
				eName:"linkbutton",plain:true,
				text:"查询",iconCls:"icon-search",
				onClick:function(){
					self.jqGrid.datagrid("load");
				}
			}]
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
	function Main(jqGroup,loadParams){
		var self = this;
		var userJson = $ui.data("user");
		var gridOpts = {
			eName:"datagrid",
			fit:true,
			border:false,
			autoload:false,
			rownumbers:true,
			idField:"id",
			url:queryUrl,
			queryParams:{unitId:userJson.unitId}
		}
		createRowBtns.call(self,gridOpts);
		createToolbar.call(self,gridOpts,userJson.unitId,loadParams.licenceType);
		gridObj.createColumns.call(self,gridOpts,false);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
