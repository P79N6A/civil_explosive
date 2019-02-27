

define(function(require,exports,module){
	var mName = "爆破物品购买许可证";
	var auditType = "审批";
	var queryUrl = "contract/purchasecard/jsons.do";
	var saveUrl = "contract/purchasecard/save5.do";
	var gridObj = require("js/contract/PurchaseCardGrid");
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null;
		var btnDisabled = (row.auditStep == 4 && row.auditResult)?false:true;
		var formOpts = {
			formEx:{
				formData:row,url:saveUrl,
				onSave:function(retJson){
					if(!retJson.result) return;
					jqWin.window("close");
					self.jqGrid.datagrid("reload");
				}
			},
			onEditorCreate:function(column,editor){
				if(column.field == "operatorName"){
					editor.queryParams = {unitId:row.unitId};
				}else if("itemName"  == column.field){
					editor.queryParams = {applyUnitId:row.unitId};
				}else if("marketUnitName" == column.field){
					if(row.marketingUnitId){
						editor.queryParams = {id:row.marketingUnitId};
					}
				}
				editor.readonly = true;
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = {
			formDialog:{
				title:auditType + mName,
				height:620
			},
			onLayoutCreate:function(regions){
				var region = regions.pop();
				//炸药购买信息
				regions.push({
					region:"south",
					height:180,
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
		jqUpload = uploadDiv.loadJs("js/base/upload2",{hostType:5,hostId:row.id,readonly:true});
		//加载炸药购买datagrid
		regionDiv = jqLayout.layoutExt("getPanel",{region:'south',index:0});
		jqCard = regionDiv.loadJs("js/contract/ExplosiveCardInfo",{cardType:1,hostId:row.id,readonly:true});
	}
	//创建工具栏
	function createToolbar(gridOpts){
		var self = this;
		gridOpts.toolbarEx = [{
			eName:"label",
			text:"开始时间",
			marginLeft:8,
			marginRight:8
		},{
			eName:"datebox",
			width:90,
			required:true,
			name:"kssj",
			value:"2018-06-25"
		},{
			eName:"label",
			text:"结束时间",
			marginLeft:8,
			marginRight:8
		},{
			eName:"datebox",
			width:90,
			required:true,
			name:"jssj",
			value:"2018-11-28"
		},{
			eName:"linkbutton",plain:true,
			text:"查询",iconCls:"icon-add",
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
			url:queryUrl
		}
		createToolbar.call(self,gridOpts);
		createRowBtns.call(self,gridOpts);
		gridObj.createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
