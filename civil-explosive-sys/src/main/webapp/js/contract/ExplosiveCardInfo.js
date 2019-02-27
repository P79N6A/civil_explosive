

define(function(require,exports,module){
	var mName = "炸药数量";
	var queryUrl = "contract/explosivecardinfo/jsons.do";
	var saveUrl = "contract/explosivecardinfo/save.do";
	var removeUrl = "contract/explosivecardinfo/delete.do";
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
				var row = {cardType:self.params.cardType,hostId:self.params.hostId};
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
			eName:"linkbutton",plain:true,
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
			{field:'cardType',title:'证件类型',hidden:true},
			{field:'hostId',title:'所属购买证',hidden:true},
			{field:'explosiveCategoryName',title:'炸药类别',editor:{
				required:true,
				eName:'combobox',
				name:'explosiveCategoryId',
				valueField: 'id',
				textField: 'name',
				url:'store/explosivecategory/boxJson.do',
				onSelect:function(record){
					var jqVarietBox = $(this).parents("form:eq(0)").findJq("explosiveVariet");
					jqVarietBox.combobox("reload",{categoryId:record.id});
				}
			}},
			{field:'explosiveVarietName',title:'炸药品种',width:150,editor:{
				required:true,
				eName:'combobox',
				name:'explosiveVariet',
				valueField: 'code',
				textField: 'name',
				url:'store/explosivevariety/boxJson.do',
				onBeforeLoad:function(params){
					return params.categoryId?true:false;
				}
			}},
			{field:'explosiveCount',title:'炸药数量',editor:{required:true,eName:"numberbox"}}
		]];
	}
	//入口
	function Main(jqGroup,loadParams){
		var self = this;
		self.jqGroup = jqGroup;
		self.params = loadParams;
		var gridOpts = {
			eName:"datagrid",
			fit:true,
			title:"炸药品种数量",
			pagination:false,
			border:true,
			autoload:false,
			idField:"id",
			url:queryUrl,
			queryParams:{cardType:loadParams.cardType,hostId:loadParams.hostId}
		}
		if(!loadParams.readonly) createToolbar.call(self,gridOpts);
		createColumns.call(self,gridOpts,loadParams);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	
	/*
	 * 对外方法
	 * 参数:
	 * 	IDS 获取资源IDs
	 * 	checkFlag 验证是否为空
	 * */
	function form2Ids(){
		var dataJsons = this.jqGrid.datagrid('getData').rows;
		if(dataJsons.length == 0) return null;
		var ids = [];
		$.each(dataJsons,function(i,dataJson){
			ids.push(dataJson.id);
		});
		return ids;
	}
	
	Main.prototype.form2Ids = form2Ids;
		
	
	return Main;
});
