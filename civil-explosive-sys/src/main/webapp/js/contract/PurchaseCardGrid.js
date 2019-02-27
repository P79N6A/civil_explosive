
/*购买者列字段*/
define(function(require,exports,module){
	
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{field:'id',title:'id',hidden:true},
			{field:'code',title:'购买证编号',editor:{required:true}},
			{field:'statusName',title:'购买证状态'},
			{field:'unitId',title:'购买单位',hidden:true},
			{field:'itemName',title:'爆破作业合同',width:222,editor:{
				required:true,
				eName:'combobox',
				name:'itemId',
				pagination:true,
				valueField: 'id',
				textField: 'contractName',
				url:'contract/explosivecontractrecord/boxJsons.do'
			}},
			{field:'unitName',title:'购买单位',editor:{readonly:true},width:200},
			{field:'marketUnitName',title:'销售单位',width:222,editor:{
				required:true,
				eName:'combogrid',
				name:'marketingUnitId',
				valueField: 'id',
				textField: 'name',
				idField:'id',
				multiple:false,
				editable:true,
				mode:'remote',
				url:'base/marketunit/jsons.do',
				columns:[[{field:'id',title:'id',hidden:true}
				,{field:'name',title:'单位名称'}]]
			}},
			{field:'cardDate',title:'发证日期',editor:{required:true,eName:"datebox"}},
			{field:'cardEndDate',title:'有效期至',editor:{required:true,eName:"datebox"}},
			{field:'operatorName',title:'经办人',editor:{
				required:true,
				eName:'combobox',
				name:'operator',
				valueField: 'id',
				textField: 'nameAll',
				url:'base/workingperson/boxJsons.do'
			}},
			{field:'auditResultName',title:'审核进度',width:222,styler: function(value,row,index){
				var ret = null;
				if(row.auditStep != 1){
					ret = row.auditResult?"color:blue;":"color:red;";
				}
				return ret;
			}},
			{field:"row_btns66",title:'操作',width:80}
		]];
	}

	exports.createColumns = createColumns;
	
});
