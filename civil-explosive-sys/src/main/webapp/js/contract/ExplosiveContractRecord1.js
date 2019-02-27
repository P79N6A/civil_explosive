
/*
 * 运输证字段模块
 * */
define(function(require,exports,module){
	
	var ExplosiveItem = require("js/contract/ExplosiveItem");
	var mName = "爆破作业项目";
	var auditType = "备案";
	var queryUrl = "contract/explosivecontractrecord/jsons.do";
	var saveUrl = "contract/explosivecontractrecord/save1.do";
	var gridObj = require("js/contract/ExplosiveContractRecordGrid");
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null;
		//判断是否为浏览模式
		var btnDisabled = null;
		if(row.contractCheck){//项目审批
			btnDisabled = (row.auditResult && row.auditStep > 1
					&& row.auditStep <= 3)?true:false;
		}else{//合同备案
			btnDisabled = (row.auditResult && row.auditStep > 1
					&& row.auditStep <= 4)?true:false;
		}
		var formOpts = {
			formEx:{
				formData:row,url:saveUrl,
				onBeforeSave:function(params){
					//绑定上传资源ids
					var ids = [];
					jqUpload.formIDs(ids);
					params.uploadIds = ids.join(",");
					var itemParams = {};
					//是否审批
					if(1 == params.contractCheck){
						if(false == self.jqItem.formJson(itemParams)) return false;
						$.extend(params,itemParams);
					}
					return true;
				},
				onSave:function(retJson){
					if(!retJson.result) return;
					jqWin.window("close");
					self.jqGrid.datagrid("reload");
				}
			},
			onEditorCreate:function(column,editor){
				if(column.field == "delegateUnitName"){
					editor.readonly = false; 
					editor.editable = true;
					editor.eName = "combobox";
					editor.name = 'delegateUnitId';
					editor.valueField =  'id';
					editor.textField =  'name';
					editor.url = 'base/delegationunit/jsons.do';
				}else if(column.field == "contractCheckName"){
					editor.readonly = false;
				}
			},
			formTable:{
				cols:4
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		/*var dialogWidth = 720;
		var dialogHeight = 420;
		if(row.contractCheck){//审核
			dialogWidth = 920;
			dialogHeight = 870;
		}*/
		var dialogOpts = {
			formDialog:{
				title:auditType + mName,
				width:920,
				height:750
			},
			onLayoutCreate:function(regions){
				var panel = regions.pop();
				regions.push({//项目审批表单
					region:"south",
					height:380,
					cssStyle:"padding:6px;position: relative;overflow:auto;"
				});
				regions.push({//附件文档
					region:"south",
					height:80,
					cssStyle:"padding:3px;"
				});
				regions.push(panel);
			},
			formBtns:[{disabled:btnDisabled}]
		};
		jqWin = $ui.createFormDialog(dialogOpts,formUI);
		var jqLayout = jqWin.findJq('layoutExt');
		var jqRegion = jqLayout.layoutExt("getPanel",{region:'south',index:1});
		jqUpload = jqRegion.loadJs("js/base/upload2",{hostType:3,hostId:row.id,readonly:btnDisabled});
		//加载项目审批表单
		jqRegion = jqLayout.layoutExt("getPanel",{region:'south',index:0});
		self.jqItem = jqRegion.loadJs("js/contract/ExplosiveItem"
				,{data:row});
	}
	//创建工具栏
	function createToolbar(gridOpts){
		var self = this;
		gridOpts.toolbarEx = [{
			eName:"linkbutton",plain:true,
			text:"新增",iconCls:"icon-add",
			onClick:function(){
				var row = {contractCheck:0};
				var userJson = $ui.getGdata("user");
				row.applyUnitId = userJson.unitId;
				row.applyUnitName = userJson.unitName;
				row.auditStep = 1;//默认值
	        	gridEdit.call(self,"新增",row);
			}
		},{
			eName:"div",plain:true,
			elements:[{
				eName:"label",
				text:"是否审批",
				marginLeft:6,
				marginRight:6
			},{
				eName:'combobox',
				name:'contractCheck',
				width:60,
				data:[{value:"",text:"全部"},{value:"1",text:"是"},{value:"0",text:"否"}]
			},{
				eName:"label",
				text:"审核状态",
				marginLeft:6,
				marginRight:6
			},{
				eName:'combobox',
				name:'auditStatus',
				width:70,
				data:[{value:"",text:"全部"},{value:"1",text:"待审核"},{value:"2",text:"审核中"},{value:"3",text:"审核完"}]
			},{
				eName:"textbox",
				name:"q",
				prompt:"根据合同名称模糊查询",
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
		var gridOpts = {
			eName:"datagrid",
			fit:true,
			border:false,
			autoload:false,
			idField:"id",
			url:queryUrl,
			rownumbers:true
		}
		createToolbar.call(self,gridOpts);
		createRowBtns.call(self,gridOpts);
		gridObj.createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
