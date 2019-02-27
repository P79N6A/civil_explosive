

define(function(require,exports,module){
	var ExplosiveItem = require("js/contract/ExplosiveItem");
	
	var mName = "爆破作业项目";
	var auditType = "受理";
	var queryUrl = "contract/explosivecontractrecord/jsons.do";
	var saveUrl = "contract/explosivecontractrecord/save2.do";
	var gridObj = require("js/contract/ExplosiveContractRecordGrid");
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null;
		var btnDisabled = (1 == row.auditStep)?false:true;
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
					cssStyle:"padding:6px;position: relative;overflow:auto;"
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
			eName:"label",
			text:"所属单位",
			marginLeft:6,
			marginRight:6
		},{
			eName:'combobox',
			name:'applyUnitId',
			editable:false,
			valueField: 'id',
			width:110,
			textField: 'shortName',
			panelHeight:'auto',
			url:'base/workingunit/boxJsonEx.do',
			allFlag:true
		},{
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
