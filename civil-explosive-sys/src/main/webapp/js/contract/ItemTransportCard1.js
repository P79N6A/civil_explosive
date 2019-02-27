
define(function(require,exports,module){
	var mName = "运输证";
	var auditType = "备案";
	var queryUrl = "contract/itemtransportcard/jsons.do";
	var saveUrl = "contract/itemtransportcard/save1.do";
	var gridObj = require("js/contract/ItemTransportCardGrid");
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null,jqUpload = null,jqCard = null;
		var btnDisabled = (row.auditResult && row.auditStep > 1
				&& row.auditStep <= 4)?true:false;
		var formOpts = {
			formEx:{
				formData:row,url:saveUrl,
				onBeforeSave:function(formParams){
					//绑定上传资源ids
					var ids = [];
					jqUpload.formIDs(ids);
					formParams.uploadIds = ids.join(",");
					ids = jqCard.form2Ids();
					if(ids == null) {
						$ui.alert("炸药品种数量信息不能为空!");
						return false;
					}
					formParams.cardIds = ids.join(",");
					return true;
				},
				onSave:function(retJson){
					if(!retJson.result) return;
					jqWin.window("close");
					self.jqGrid.datagrid("reload");
				}
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = {
			formDialog:{
				title:auditType + mName,
				height:650
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
				regions.push(region);
			},
			formBtns:[{disabled:btnDisabled}]
		};
		jqWin = $ui.createFormDialog(dialogOpts,formUI);
		var jqLayout = jqWin.findJq('layoutExt');
		//加载附件
		var regionDiv = jqLayout.layoutExt("getPanel",{region:'south',index:1});
		jqUpload = regionDiv.loadJs("js/base/upload2",{hostType:6,hostId:row.id,readonly:btnDisabled});
		//加载炸药购买datagrid
		regionDiv = jqLayout.layoutExt("getPanel",{region:'south',index:0});
		var hostId = row.id || (0 - parseInt(Math.random() * 1000));
		jqCard = regionDiv.loadJs("js/contract/ExplosiveCardInfo",{cardType:2,hostId:hostId,readonly:btnDisabled});
	}
	//创建工具栏
	function createToolbar(gridOpts){
		var self = this;
		gridOpts.toolbarEx = [{
			eName:"linkbutton",plain:true,
			text:"新增",iconCls:"icon-add",
			onClick:function(){
				var row = {};
	        	gridEdit.call(self,"新增",row);
			}
		},{
			eName:"div",
			elements:[]
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
		self.jqGroup = jqGroup;
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
