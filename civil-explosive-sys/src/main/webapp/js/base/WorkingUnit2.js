
/*
 * xx模块
 * */
define(function(require,exports,module){
	var mName = "从业单位";//模块名称
	var auditType = "受理";//审核类型
	var queryUrl = "base/workingunit/json4check.do";
	var saveUrl = "base/workingunit/save2.do";
	var gridObj = require("js/base/WorkingUnitGrid");
	var personGridObj = require("js/base/WorkingPersonGrid");
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null;
		var jqUpload = null;
		var btnDisabled = (1 == row.auditStep)?false:true;
		var formOpts = {
			onEditorCreate:function(column,editor){
				if(column.field == "guardLeaderName" 
						|| column.field == "techLeaderName" ){
					editor.queryParams = {unitId:row.id};
				}
			},
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
				},
				onLoad:function(formData){//表单加载完毕
					if(row.endStartEx){
						$(this).findJq("endStart").datebox("disable");
					}
				}
			},
			onEditorCreate:function(column,editor){
				editor.readonly = true;
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
				width:800,height:720
			},
			onLayoutCreate:function(regions){
				var region = regions.pop();
				regions.push({
					region:"south",
					height:200,
					cssStyle:"padding:3px"
				});
				//附件
				regions.push({
					region:"south",
					height:80,
					cssStyle:"padding:3px"
				});
				//备注
				regions.push({
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
		var uploadDiv = jqLayout.layoutExt("getPanel",{region:'south',index:1});
		jqUpload = uploadDiv.loadJs("js/base/upload2",{hostType:1,hostId:row.id,readonly:btnDisabled});
		var jqDiv = jqLayout.layoutExt("getPanel",{region:'south',index:0});
		personGridObj.createSumGrid.call(self,jqDiv,row.id,1);
	}
	//创建工具栏
	function createToolbar(gridOpts){
		var self = this;
		gridOpts.toolbarEx = [{
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
			name:"受理",
			onClick:function(row){
				gridEdit.call(self,"受理",row);
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
			url:queryUrl
		}
		createRowBtns.call(self,gridOpts);
		createToolbar.call(self,gridOpts);
		gridObj.createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
