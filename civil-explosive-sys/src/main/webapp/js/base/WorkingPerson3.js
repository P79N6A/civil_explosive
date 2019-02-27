
/*
 * xx模块
 * */
define(function(require,exports,module){
	var mName = "从业人员";//模块名称
	var auditType = "审核";//审核类型
	var queryUrl = "base/workingperson/jsons.do";
	var saveUrl = "base/workingperson/save3.do";
	var gridObj = require("js/base/WorkingPersonGrid");
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null;
		var jqUpload = null;
		var btnDisabled = (row.auditStep == 2 && row.auditResult)?false:true;
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
			onEditorCreate:function(column,editor){
				editor.readonly = true;
			},formTable:{cols:4,trHeight:[32]}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = {
			formDialog:{
				title:auditType + mName,
				width:600,height:500
			},
			onLayoutCreate:function(regions){
				var region = regions.pop();
				regions.push({//附件
					region:"south",
					height:140,
					cssStyle:"padding:3px"
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
				regions.push(region);
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
		var uploadDiv = jqLayout.layoutExt("getPanel",{region:'south',index:0});
		jqUpload = uploadDiv.loadJs("js/base/upload2",{hostType:2,hostId:row.id,readonly:true});
	}
	//创建工具栏
	function createToolbar(gridOpts,unitId,licenceType){
		var self = this;
		gridOpts.toolbarEx = [{
			eName:"label",
			text:"所属单位",
			marginLeft:6,
			marginRight:6
		},{
			eName:'combobox',
			name:'unitId',
			editable:false,
			valueField: 'id',
			width:110,
			textField: 'shortName',
			panelHeight:'auto',
			url:'base/workingunit/boxJsonEx.do',
			allFlag:true
		},{
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
		}];
	}
	function createRowBtns(gridOpts){
		var self = this;
		gridOpts.rowBtnsField = "row_btns66";
		gridOpts.rowBtns = [{
			name:"审核",
			onClick:function(row){
				gridEdit.call(self,"审核",row);
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
			queryParams:{unitId:loadParams.unitId}
		}
		createRowBtns.call(self,gridOpts);
		createToolbar.call(self,gridOpts,loadParams.unitId,loadParams.licenceType);
		gridObj.createColumns.call(self,gridOpts,true);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
