
/*
 * */
define(function(require,exports,module){
	//创建表格列
	function createColumns(){
		var self = this;
		return [[
			{field:'id',title:'id',hidden:true},
			{field:'contractNumber',title:'合同编号',editor:{required:true,validType:'length[0,50]'}},
			{field:'contractName',title:'合同名称',editor:{required:true,validType:'length[0,150]'},width:200},
			{field:'applyUnitId',title:'申请单位',hidden:true},
			{field:'applyUnitName',title:'申请单位',editor:{readonly:true,required:true},width:200},
			{field:'delegateUnitName',title:'委托单位',width:200,editor:{
				required:true,
				eName:'combobox',
				name:'delegateUnitId',
				valueField: 'id',
				textField: 'name',
				url:'base/delegationunit/json.do',
				icons:[{
					iconCls:'icon-add',
					handler: function(e){
						var self = $(e.data.target);
						$ui.createWin({
							title:'委托单位',
							width:450,
							height:320,
							url:'js/base/DelegationUnit.js',
							onClose:function(){
								self.combobox('reload');
							}
						});
					}
				}]
			}},
			{field:'contractSignDate',title:'合同签定日期',editor:{required:true,eName:"datebox"}},
			{field:'contractExpirationDate',title:'合同截止日期',editor:{required:true,eName:"datebox"}},
			{field:'contractCheckName',title:'是否审批',editor:{
				eName:'combobox',
				name:'contractCheck',
				data:[{text:'是',value:'1'},{text:'否',value:'0'}]
			}},
			{field:'taskAddr',title:'爆破作业地点',editor:{}},
			{field:'contractExplain',title:'合同简要说明',width:200,editor:{
				height:60,multiline:true,validType:'length[0,980]'
			}}
		]];
	}
	//入口
	function main(jqDom,args){
		var self = this;
		var columnsUI = createColumns.call(self);
		var mainUI = {
			eName:"datagrid",
			idField:"id",
			url:"contract/explosivecontractrecord/json.do",
			delUrl:"contract/explosivecontractrecord/delete.do",
			columns:columnsUI,
			formEdit:{
				formEx:{
					url:"contract/explosivecontractrecord/save.do",
				},
				formDialog:{
					title: "爆破作业合同备案",
					height:472
				}
			},
			onBeforeAdd:function(row){
				var userJson = $ui.getGdata("user");
				row.applyUnitId = userJson.unitId;
				row.applyUnitName = userJson.unitName;
				return true;
			}
		};
		jqDom.createUI(mainUI);
	}
	return main;
});

