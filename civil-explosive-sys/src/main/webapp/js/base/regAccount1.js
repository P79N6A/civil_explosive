
/*
 * 治安帐号
 * */
define(function(require,exports,module){
	var mName = "账号";
	var auditType = "注册";
	var queryUrl = "sys/sysuser/jsons4Reg.do";
	var saveUrl = "sys/sysuser/regAccount.do";
	//编辑表单
	function gridEdit1(title,row){
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
				if(row.userId){
					if(column.field == "loginName"){
						editor.readonly = true;
					}
				}
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = {
			formDialog:{
				title:auditType + mName,
				height:222
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
				var row = {roleType:1,status:1};
	        	gridEdit1.call(self,"新增",row);
			}
		}];
	}
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{field:'userId',title:'userId',hidden:true},
			{field:'roleType',title:'roleType',hidden:true},
			{field:'roleNames',title:'角色',width:160,editor:{
				required:true,
				eName:'combobox',
				name:'roleIds',
				editable:false,
				valueField: 'roleId',
				textField: 'roleName',
				panelHeight:'auto',
				url:'sys/sysrole/jsons4Reg.do',
				queryParams:{roleType:1}
			}},
			{field:'loginName',title:'登录帐号',editor:{required:true}},
			{field:'realName',title:'用户姓名',width:80,editor:{required:true}},
			{field:'statusName',title:'用户状态',width:80,editor:{
				eName:'radiobox',
				name:'status',
				data:[{text:'正常',value:'1'}
					,{text:'停用',value:'0'}]
			}},
			{field:'regTime',title:'注册时间',width:130},
			{field:'lastLoginTime',title:'最后一次登录时间',width:130},
			{field:"row_btns66",title:'操作',width:150}
		]];
	}

	function createRowBtns(gridOpts){
		var self = this;
		gridOpts.rowBtnsField = "row_btns66";
		gridOpts.rowBtns = [{
			name:"编辑",
			onClick:function(row){
				row.rowType = row.roleType;
				if(row.roleType == 1) gridEdit1.call(self,"编辑",row);
				if(row.roleType == 2) gridEdit2.call(self,"编辑",row);
				if(row.roleType == 3) gridEdit3.call(self,"编辑",row);
			}
		},{
			name:"重置密码",
			cls:"row_btn2",
			onClick:function(row){
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
		},{
			name:"删除",
			cls:"row_btn2",
			onClick:function(row){
				$ui.confirm('确定要删除该帐号吗?',function(r){
					if(r){
						$ui.postEx('sys/sysuser/delete.do',{'userId':row.userId},
							function(retJson){
								if(retJson.result) self.jqGrid.datagrid("reload");
								$ui.checkret(retJson);
							}
						);
					}
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
			pagination:false,
			border:false,
			autoload:false,
			idField:"userId",
			url:queryUrl,
			queryParams:{roleType:1}
		}
		createRowBtns.call(self,gridOpts);
		createToolbar.call(self,gridOpts);
		createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
