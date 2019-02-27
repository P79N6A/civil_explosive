
/*
 * 菜单模块
 * */
define(function(require,exports,module){
	//地址
	var queryUrl = "sys/sysmenu/jsons.do";
	var saveUrl = "sys/sysmenu/save.do";
	var removeUrl = "sys/sysmenu/delete.do";
	
	//调整菜单顺序
	function saveMenuOrder(node1,node2){
		var self = this;
		var params = {
			'id1':node1.id,
			'order1':node1.orderNum,
			'id2':node2.id,
			'order2':node2.orderNum
		};
		$ui.postEx('sys/sysmenu/order.do',params,
			function(retJson){
				if(retJson.result){
					self.jqGrid.treegrid('reload');
				}
			}
		);
	}
	//迁移菜单
	function migrationMenus(srcNode){
		var self = this;
		var myWin = null;
		var treeForm = {
			eName:"div",
			elements:{
				eName:'tree',
				checkbox:false,
				dataType:'list',
				nodeId:'menuId',
				nodeText:'menuName',
				nodePid:'parentId',
				url:'sys/sysmenu/jsons.do'
				}
		}
		var dialogOpts = {
			formDialog:{
				width:260,
				height:410,
				title:'菜单迁移'
			},
			formBtns:[{
				onClick:function(){//保存
					var jqTree = myWin.findJq('tree');
	        		var destNode = jqTree.tree('getSelected');
	        		if(!destNode){
	        			$ui.alert("请选择目标节点 !");
	        			return;
	        		}
	        		var node = destNode;
	        		do{
	        			if(node.id == srcNode.id) break;
	        			node = jqTree.tree("getParent",node.target);
	        			if(!node) break;
	        		}while(true);
	        		if(node){
	        			$ui.alert("目标节点不合适 !");
	        		}
	        		var params = {menuId:srcNode.id,parentId:destNode.id};
	        		$ui.postEx("sys/sysmenu/save.do",params,function(retJson){
	        			if(retJson.result){
	    					self.jqGrid.treegrid('reload');
	    					myWin.window('close');
	    				}
	        		});
				}
			}]
		};
		myWin = $ui.createFormDialog(dialogOpts,treeForm);
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
					self.jqGrid.treegrid("reload");
				}
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = {
			formDialog:{
				title:title + "菜单",
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
				var newRow = {};
				var row = self.jqGrid.treegrid('getSelected');
				newRow.parentId = row?row.menuId:0;
	        	gridEdit.call(self,"新增",newRow);
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
				var rows = self.jqGrid.treegrid('getChildren',row.menuId);
				if(rows.length > 0){
					$ui.alert("包含子节点，不能删除 !");
					return false;
				}
				$ui.confirm("删除后不能撤销,你确定要删除此记录吗 ?", function(r) {
					if (!r) return;
					self.jqGrid.treegrid("removeEx",[row,removeUrl]);
				});
			}
		},{
			eName:'linkbutton',
			text:'上移',
			plain:true,
			iconCls:'icon_arrow_up',
			cssStyle:"vertical-align: middle;",
			onClick:function(){
				var node = self.jqGrid.treegrid('getSelected');
				if(null == node){
					$ui.alert('请选择要上移的菜单 !');
					return;
				}
				var prevNode = self.jqGrid.treegrid('getPrevNode',node.id);
				if(prevNode == null) {
					$ui.alert("到顶了 !");
					return null;
				}
				saveMenuOrder.call(self,node,prevNode);
		    }
	  },{
		  	eName:'linkbutton',
			text:'下移',
			plain:true,
			iconCls:'icon_arrow_down',
		    onClick:function(){
			var node = self.jqGrid.treegrid('getSelected');
			if(null == node){
				 $ui.alert('请选择要上移的菜单 !');
				 return;
			}
			var nextNode = self.jqGrid.treegrid('getNextNode',node.id);
			if(nextNode == null) {
				$ui.alert("到底了 !");
				return null;
			}
			saveMenuOrder.call(self,node,nextNode);
		 }
	 },{
		  	eName:'linkbutton',
			text:'迁移',
			plain:true,
			iconCls:'icon-cut',
		    onClick:function(){
				var node = self.jqGrid.treegrid('getSelected');
				if(null == node){
					 $ui.alert('请选择要迁移的菜单 !');
					 return;
				}
				migrationMenus.call(self,node);
		    }
	 }];
	}
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{title:'主键',field:'menuId',hidden:true},
			{title:'父节点',field:'parentId',hidden:true},
	        {title:'菜单名称',field:'menuName',width:222,align:'left',editor:{required:true}},
	        {title:'菜单地址',field:'menuUrl',width:320,align:'left',editor:{}},
	        {title:'菜单类型',field:'menuType',width:60,align:'center',editor:{}},
	        {title:'备注',field:'remark',width:160,align:'center',editor:{}}
		]];
	}
	//入口
	function Main(jqGroup,loadParams){
		var self = this;
		var gridOpts = {
			eName:"treegrid",
			idField:"menuId",
			treeField:'menuName',
			nodePid:'parentId',
			lines:true,
			url:queryUrl
		}
		createToolbar.call(self,gridOpts);
		createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
