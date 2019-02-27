

define(function(require,exports,module){
	var ExplosiveItem = require("js/contract/ExplosiveItem");
	
	var mName = "爆破作业项目";
	var auditType = "审核";
	var queryUrl = "contract/explosivecontractrecord/jsons.do";
	var saveUrl = "contract/explosivecontractrecord/save3.do";
	var gridObj = require("js/contract/ExplosiveContractRecordGrid");
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null;
		var btnDisabled = (row.auditStep == 2 && row.auditResult)?false:true;
		var formOpts = {
			formEx:{
				formData:row,url:saveUrl,
				onBeforeSave:function(params){
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
				editor.readonly = true;
			},
			formTable:{
				cols:4
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = {
			formDialog:{
				title:auditType + mName,
				width:920,
				height:800
			},
			onLayoutCreate:function(regions){
				var panel = regions.pop();
				regions.push({//项目审批表单
					region:"south",
					height:380,
					cssStyle:"padding:6px;position: relative;"
				});
				regions.push({//附件文档
					region:"south",
					height:80,
					cssStyle:"padding:3px;border:0px solid blue;"
				});
				regions.push({//备注
					region : "south",
					height : 50,
					cssStyle:"padding:6px 6px 0 6px;text-align:center;",
					elements:{
						eName:'textbox',
						multiline:true,
						readonly:btnDisabled,
						name:"remark",
						prompt:'输入审核备注',
						validType:'length[0,180]',
						cssStyle:'width:90%;height:90%'
					}
				});
				regions.push(panel);
			},
			formBtns:[{
				text:'同意',
				disabled:btnDisabled,
				onClick:function() {
					var jqForm = jqWin.findJq('formEx');
					var txt = jqWin.findJq("remark").val();
					jqForm.formEx("submit",{auditResult:1,remark:txt});
				}
			},{
				text:'拒绝',
				disabled:btnDisabled,
				onClick:function() {
					var jqForm = jqWin.findJq('formEx');
					var txt = jqWin.findJq("remark").val();
					jqForm.formEx("submit",{auditResult:0,remark:txt});
				}
			}]
		};
		jqWin = $ui.createFormDialog(dialogOpts,formUI);
		var jqLayout = jqWin.findJq('layoutExt');
		var jqRegion = jqLayout.layoutExt("getPanel",{region:'south',index:1});
		jqUpload = jqRegion.loadJs("js/base/upload2",{hostType:3,hostId:row.id,readonly:true});
		//加载项目审批表单
		jqRegion = jqLayout.layoutExt("getPanel",{region:'south',index:0});
		self.jqItem = jqRegion.loadJs("js/contract/ExplosiveItem"
				,{data:row,readonly:true});
	}
	//创建工具栏
	function createToolbar(gridOpts){
		var self = this;
		gridOpts.toolbarEx = [{
			eName:"linkbutton",plain:true,
			text:auditType,iconCls:"icon-edit",
			onClick:function(){
				var row = self.jqGrid.datagrid("getSelected");
				if (row == null) {
					$ui.alert("请选择一条要修改的记录 !");
					return;
				}
				gridEdit.call(self,"备案",row);
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
