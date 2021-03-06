
/*
 * xx模块
 * */
define(function(require,exports,module){
	var mName = "账号";
	var auditType = "注册";
	var queryUrl = "sys/sysuser/jsons4Reg.do";
	var saveUrl = "sys/sysuser/regAccount.do";
	//从业单位
	function gridEdit3(title,row){
		var self = this;
		var jqWin = null;
		var jqSpan = null;
		var roleIds = row.roleIds;
		var formOpts = {
			formEx:{
				formData:row,url:saveUrl,
				onBeforeSave:function(params){
					if(params.roleIds == -1){
						delete params.roleId;
						var checkBox = jqSpan.find("input:checked");
						if(checkBox.length == 0){
							$ui.alert("从业单位不能为空！");
							return false;
						}
						var roleIds = [];
						checkBox.each(function(i,node){
							roleIds.push($(this).val());
						});
						params.roleIds = roleIds.join(",");
					}
					return true;
				},
				onSave:function(retJson){
					if(!retJson.result) return;
					jqWin.window("close");
					self.jqGrid.datagrid("reload");
				}
			},
			onEditorCreate:function(column,editor){
				if("roleNames" == column.field){
					editor.queryParams = {roleType:3};
					editor.width = 100;
					editor.onSelect = function(record){
						if(record.roleId == -1){
							jqSpan.show();
						}else jqSpan && jqSpan.hide();
					};
					editor.onLoadSuccess = function(datas){
						var selfBox = $(this);
						if(selfBox.data("isLoad")) return;
						selfBox.data("isLoad",true);
						var spanUI = [],data2 = [];
						var flag = false;
						$.each(datas,function(i,data){
							if(data.remark == 1) {
								var checked = cmpValues(roleIds,data.roleId);
								if(checked) flag = true;//属于从业单位
								spanUI.push({
									eName:"span",
									elements:[{
										eName:'input',
										type:'checkbox',
										checked:checked?true:null,
										value:data.roleId
									},{
										eName:'label',
										text:data.roleName
									}]
								});
							}else data2.push(data);
						});
						data2.unshift({
							roleId:-1,
							roleName:'从业单位'
						});
						window.setTimeout(function(){
							selfBox.combobox("setValue",flag?-1:roleIds);
							selfBox.combobox("loadData",data2);
						},100);
						var jqTd = selfBox.parents("td:eq(0)");
						jqSpan = jqTd.createUI({
							eName:"span",
							elements:spanUI
						});
					}
				}
				if(column.field == "policeName") return false;
				if(row.userId){
					if(column.field == "loginName"){
						editor.readonly = true;
					}
				}
			},
			formTable:{
				onTdStyle:function(r,c){
					if(r == 0 && c == 1) $(this).css("text-align","left");
				}
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = {
			formDialog:{
				title:auditType + mName,
				height:260,
				width:440
			}
		};
		jqWin = $ui.createFormDialog(dialogOpts,formUI);
	}
	/*
	 * 逗号分割字符串中，是否包含指定字符
	 * */
	function cmpValues(vals,val){
		var result = false;
		if(!vals) return false;
		var arrs = vals.split(",");
		$.each(arrs,function(i,v){
			if(v == val){
				result = true;
				return false;
			}
		});
		return result;
	}
	//创建工具栏
	function createToolbar(gridOpts){
		var self = this;
		gridOpts.toolbarEx = [{
			eName:"linkbutton",plain:true,
			text:"新增",iconCls:"icon-add",
			onClick:function(){
				var row = {roleType:3,status:1};
	        	gridEdit3.call(self,"新增",row);
			}
		}];
	}
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{field:'userId',title:'userId',hidden:true},
			{field:'unitId',title:'unitId',hidden:true},
			{field:'roleType',title:'roleType',hidden:true},
			{field:'roleNames',title:'角色',width:160,editor:{
				required:true,
				eName:'combobox',
				name:'roleIds',
				editable:false,
				valueField: 'roleId',
				textField: 'roleName',
				panelHeight:'auto',
				url:'sys/sysrole/jsons4Reg.do'
			}},
			{field:'loginName',title:'登录帐号',editor:{required:true}},
			{field:'realName2',title:'单位名称',hidden:true,editor:{required:true}},
			{field:'realName',title:'单位简称',width:180,editor:{required:true}},
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
			queryParams:{roleType:3}
		}
		createRowBtns.call(self,gridOpts);
		createToolbar.call(self,gridOpts);
		createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
