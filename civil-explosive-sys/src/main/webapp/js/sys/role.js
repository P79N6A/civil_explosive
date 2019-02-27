
/*
 * xx模块
 * */
define(function(require,exports,module){
	//地址
	var queryUrl = "sys/sysrole/jsons.do";
	var saveUrl = "sys/sysrole/save.do";
	var removeUrl = "sys/sysrole/delete.do";
	
	//授权按钮事件
	function onAuth(){
		var self = this;
		var jqWin = null;
	   var row = self.jqGrid.datagrid('getSelected');
	   if(row == null){
			$ui.alert('请选择要授权的角色 !');
			return;
	   }
	   var treeForm = {
		   eName:"formEx",
		   url : 'sys/sysrole/saveRoleAuth.do',
		   onBeforeSave:function(data){
				var mytree = jqWin.findJq('tree');
				var nodes = mytree.tree('getChecked', 
						['checked','indeterminate']);
				var menuIds = [];
				$.each(nodes,function(i,node){
					menuIds.push(node.id);
				});
				data['menuIds'] = menuIds.join(',');
				data['roleId'] = row.roleId;
				return true;
			},
			onSave:function(retJson){
				if(retJson.result){
					self.jqGrid.datagrid('reload');
					jqWin.window('close');
				}
			},
			elements:{
				eName:'tree',
				cascadeCheck:true,
				checkbox:true,
				dataType:'list',
				nodeId:'menuId',
				nodeText:'menuName',
				nodePid:'parentId',
				values:row.menuIds,
				url:'sys/sysmenu/jsons.do'
			}
	   }
	   jqWin = $ui.createFormDialog({
		   debug:true,
		   formDialog:{
			   width:260,
			   height:410,
			   title:'角色授权'
		   },
		   onLayoutCreate:function(regions){
			   regions[0].cssStyle = "overflow:auto;";
			   regions.push(regions[1]);
			   regions[1] = {
				 region:'south',
				 height:8,
				 cssStyle:'border-bottom:1px solid #ccc;'
			   };
		   }
	   },treeForm);
	}
	
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
				title:title + "",
				height:160
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
		},{
			eName:'linkbutton',
			text:'授权',
			plain:true,
			iconCls:'icon-add',
			onClick:function(){
				onAuth.call(self);
			}
		}];
	}
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{field:'roleId',title:'roleId',hidden:true},
			{field:'roleName',title:'角色名称',editor:{required:true}},
			{field:'remark',title:'备注',editor:{}}
		]];
	}
	//入口
	function Main(jqGroup,loadParams){
		var self = this;
		var gridOpts = {
			eName:"datagrid",
			fit:true,
			pagination:false,
			border:false,
			autoload:false,
			idField:"roleId",
			url:queryUrl
		}
		createToolbar.call(self,gridOpts);
		createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
