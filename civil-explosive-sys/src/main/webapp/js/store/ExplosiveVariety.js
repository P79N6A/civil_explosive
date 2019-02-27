

define(function(require,exports,module){
	var mName = "炸药类别";
	var queryUrl = "store/explosivevariety/jsons.do";
	var saveUrl = "store/explosivevariety/save.do";
	var removeUrl = "store/explosivevariety/delete.do";
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null;
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
				if(column.field == "categoryName"){
					editor.data = self.jqDatalist.datalist("getRows");
					var row = self.jqDatalist.datalist("getSelected");
					if(row) editor.value = row.id;
				}
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = {
			formDialog:{
				title:title + mName,
				height:220
			}
		};
		jqWin = $ui.createFormDialog(dialogOpts,formUI);
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
			eName:"linkbutton",plain:true,
			text:"编辑",iconCls:"icon-edit",
			onClick:function(){
				var row = self.jqGrid.datagrid("getSelected");
				if (row == null) {
					$ui.alert("请选择一条要修改的记录 !");
					return;
				}
				gridEdit.call(self,"编辑",row);
			}
		},{
			eName:"linkbutton",plain:true,hidden:true,
			text:"删除",iconCls:"icon-remove",
			onClick:function(){
				var row = self.jqGrid.datagrid("getSelected");
				if (row == null) {
					$ui.alert("请选择一条要删除的记录 !");
					return;
				}
				$ui.confirm("删除后不能撤销,你确定要删除此记录吗 ?", function(r) {
					if (!r) return;
					self.jqGrid.datagrid("remove",[row,removeUrl]);
				});
			}
		}];
	}
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{field:'id',title:'id',hidden:true},
			{field:'categoryName',title:'所属类别',editor:{
				required:true,
				eName:'combobox',
				name:'categoryId',
				valueField: 'id',
				textField: 'name'
			}},
			{field:'name',title:'品种名称',editor:{required:true,validType:'length[2,50]'},width:160},
			{field:'code',title:'品种代码',editor:{required:true,validType:'length[1,11]'}},
			{field:'specification',title:'品种规格',editor:{validType:'length[1,10]'}}
		]];
	}
	//入口
	function Main(jqGroup,loadParams){
		var self = this;
		var gridOpts = {
			eName:"datagrid",
			fit:true,
			border:true,
			autoload:false,
			idField:"id",
			url:queryUrl,
			onBeforeLoadEx:function(params){
				$ui.print(1);
				var row = self.jqDatalist.datalist("getSelected");
				if(row){
					params.categoryId = row.id;
				}
			}
		}
		createToolbar.call(self,gridOpts);
		createColumns.call(self,gridOpts);
		var layoutOpts = {
			eName:"layoutExt",
			elements:[{
				region:"west",
				width:120,
				css:{padding:3},
				elements:{
					eName:"datalist",
					border:true,
					title:"炸药类别",
					fit:true,
					valueField: 'id',
					textField: 'name',
					url:"store/explosivecategory/boxJson.do",
					onSelect:function(index,row){
						self.jqGrid.datagrid('load');
					},
					onBeforeSelect:function(index,row){
						var r = $(this).datalist("getSelected");
						if(r == row){
							$(this).datalist("clearSelections");
							self.jqGrid.datagrid('load');
							return false;
						}
						return true;
					}
				}
			},{
				region:"center",
				elements:gridOpts
			}]
		}
		jqGroup.createUI(layoutOpts);
		self.jqGrid = jqGroup.findJq("datagrid");
		self.jqDatalist = jqGroup.findJq("datalist");
		$ui.print(2);
		self.jqGrid.datagrid("load");
		$ui.print(3);
	}
	return Main;
});
