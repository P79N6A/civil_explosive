
/*
 * xx模块
 * */
define(function(require,exports,module){
	//地址
	var queryUrl = "base/marketunit/jsons.do";
	var saveUrl = "base/marketunit/save.do";
	var removeUrl = "base/marketunit/delete.do";
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null;
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
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = {
			formDialog:{
				title:title + "销售单位",
				height:420
			},
			onLayoutCreate:function(regions){
				var region = regions.pop();
				regions.push({
					region:"south",
					height:80,
					cssStyle:"padding:3px"
				});
				regions.push(region);
			}
		};
		jqWin = $ui.createFormDialog(dialogOpts,formUI);
		var jqLayout = jqWin.findJq('layoutExt');
		var uploadDiv = jqLayout.layoutExt("getPanel",{region:'south',index:0});
		jqUpload = uploadDiv.loadJs("js/base/upload2",{hostType:7,hostId:row.id,readonly:false});
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
			eName:"searchbox",
			name:"q",
			prompt:"根据名称模糊查询",
			searcher:function(){
				self.jqGrid.datagrid("load");
			}
		}];
	}
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{field:'id',title:'id',hidden:true},
			{field:'province',title:'所在地',editor:{required:true}},
			{field:'name',title:'单位名称',align:'left',width:260,editor:{required:true}},
			{field:'address',title:'单位地址',width:260,editor:{}},
			{field:'licenseNumber',title:'许可证编号',align:'left',width:180,editor:{}},
			{field:'phone',title:'固定电话',editor:{validType:'length[1,15]'}},
			{field:'mobile',title:'移动电话',editor:{validType:'length[1,15]'}},
			{field:'contacts',title:'联系人',editor:{}},
			{field:'remark',title:'备注',editor:{}},
			{field:"row_btns66",title:'操作',width:120}
		]];
	}
	function createRowBtns(gridOpts){
		var self = this;
		gridOpts.rowBtnsField = "row_btns66";
		gridOpts.rowBtns = [{
			name:"编辑",
			onClick:function(row,btnName){
				gridEdit.call(self,btnName,row);
			}
		},{
			name:"删除",
			onClick:function(row,btnName){
				$ui.confirm("删除后不能撤销,你确定要删除此记录吗 ?", function(r) {
					if (!r) return;
					self.jqGrid.datagrid("remove",[row,removeUrl]);
				});
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
		createRowBtns.call(self,gridOpts);
		createToolbar.call(self,gridOpts);
		createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
