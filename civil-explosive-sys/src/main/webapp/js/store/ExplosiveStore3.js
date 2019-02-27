
/*
 * xx模块
 * */
define(function(require,exports,module){
	var mName = "炸药仓库";//模块名称
	var auditType = "审核";//审核类型
	var queryUrl = "store/explosivestore/jsons.do";
	var saveUrl = "store/explosivestore/save3.do";
	var gridObj = require("js/store/ExplosiveStoreGrid");
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null;
		var jqUpload = null;
		var btnDisabled = (row.auditStep == 2 && row.auditResult)?false:true;
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
				height:320
			},
			onLayoutCreate:function(regions){
				var region = regions.pop();
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
			name:'unitId',
			editable:false,
			valueField: 'id',
			width:110,
			textField: 'shortName',
			panelHeight:'auto',
			url:'base/workingunit/boxJsonEx.do',
			allFlag:true
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
	function Main(jqGroup,args){
		var self = this;
		var gridOpts = {
			eName:"datagrid",
			fit:true,
			border:false,
			autoload:false,
			idField:"id",
			url:queryUrl,
			queryParams:{unitId:args.unitId}
		}
		createRowBtns.call(self,gridOpts);
		createToolbar.call(self,gridOpts);
		gridObj.createColumns.call(self,gridOpts,true);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
