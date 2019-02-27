
/*
 * xx模块
 * */
define(function(require,exports,module){
	//地址
	var queryUrl = "sys/sysuser/jsons.do";
	var saveUrl = "sys/sysuser/save.do";
	var removeUrl = "sys/sysuser/delete.do";
	//编辑表单
	function gridEdit(title,row){
		var self = this;
		var jqWin = null;
		$ui.print(row);
		var formOpts = {
			formEx:{
				formData:row,url:saveUrl,
				onLoad:function(){
	        		var obj = this.findJq("loginName");
	        		var loginName = obj.textbox('getValue');
	        		if(loginName){
	        			obj.textbox('readonly',true);
	        		}
	        		if('admin' == loginName)
	        			this.findJq("roleId").combobox('readonly',true);
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
				title:title + "管理员",
				height:380
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
				if(row.loginName == 'admin'){
	        		$ui.alert('默认管理员不能删除 !');
	        		return false;
	        	}
				$ui.confirm("删除后不能撤销,你确定要删除此记录吗 ?", function(r) {
					if (!r) return;
					self.jqGrid.datagrid("remove",[row,removeUrl]);
				});
			}
		},{
			eName:'linkbutton',
			text:'重置密码',
			plain:true,
			iconCls:'icon_Modify_Pwd',
			onClick:function(){
			   var row = self.jqGrid.datagrid('getSelected');
			   if(row == null){
					$ui.alert('请选择要重置密码的用户 !');
					return;
			   }
			   $ui.confirm('确定要重置该用户的密码吗?',function(r){
					if(r){
						$ui.postEx('sys/sysuser/pwdReset.do',{'userId':row.userId},
							function(retJson){
								$ui.checkret(retJson);
							}
						);
					}
			   });
			}
		}];
	}
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{field:'userId',title:'userId',hidden:true},
			{field:'roleNames',title:'所属角色',editor:{
				required:true,
				eName:'combobox',
				name:'roleIds',
				editable:false,
				multiple:true,
				valueField: 'roleId',
				textField: 'roleName',
				panelHeight:'auto',
				url:'sys/sysrole/jsons.do'
			}},
			{field:'loginName',title:'登录帐号',editor:{required:true,validType:'length[4,18]'}},
			{field:'realName',title:'真实姓名',editor:{}},
			{field:'idCard',title:'身份证号码',editor:{validType:'length[0,18]'}},
			{field:'phone',title:'固定电话',editor:{}},
			{field:'mobile',title:'移动电话',editor:{}},
			{field:'unitName',title:'所属单位',width:220,editor:{
				eName:'combobox',
				name:'unitId',
				editable:false,
				valueField: 'id',
				textField: 'name',
				panelHeight:'auto',
				url:'base/workingunit/boxJson.do'
			}},
			//{field:'validDate',title:'有效日期',width:90,editor:{required1:true,eName:'datebox'}},
			{field:'regTime',title:'注册时间',width:130},
			{field:'lastLoginTime',title:'最后一次登录时间',width:130},
			{field:'remark',title:'备注',editor:{}}
		]];
	}
	//入口
	function Main(jqGroup,loadParams){
		var self = this;
		var gridOpts = {
			eName:"datagrid",
			fit:true,
			border:false,
			autoload:false,
			idField:"userId",
			url:queryUrl
		}
		createToolbar.call(self,gridOpts);
		createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
