/*
 * 运输证字段模块
 * */
define(function(require,exports,module){
	
	//创建列
	function createColumns(gridOpts){
		var self = this;
		var userJson = $ui.getGdata("user");
		gridOpts.columns = [[
			{field:'id',title:'id',hidden:true},
			{field:'code',title:'运输证编号',editor:{required:true}},
			{field:'statusName',title:'购买证状态'},
			{field:'policeName',title:'辖区派出所',editor:{
				required:true,
				eName:'combobox',
				name:'policeId',
				valueField: 'id',
				textField: 'shortName',
				url:'base//localpolicestation/boxJsons.do'
			}},
			{field:'itemName',title:'爆破作业合同',width:280,editor:{
				required:true,
				eName:'combobox',
				name:'itemId',
				valueField: 'id',
				textField: 'contractName',
				url:'contract/explosivecontractrecord/boxJsons.do'
			}},
			{field:'carrierUnitName',title:'承运单位',width:200,editor:{
				required:true,
				eName:'combobox',
				name:'carrierUnitId',
				editable:false,
				valueField: 'id',
				textField: 'name',
				panelHeight:'auto',
				value:userJson.unitId,
				url:'base/workingunit/boxJson.do',
				onSelect:function(record){
					var jqTable = $(this).parents("table:eq(0)");
					var jqCarrierBox = jqTable.findJq("carrierId");
					if(jqCarrierBox.data("isFirst")){
						jqCarrierBox.combobox("clear");
					}else{
						jqCarrierBox.data("isFirst",true);
					}
					jqCarrierBox.combobox("reload",{unitId:record.id});
				}
			}},
			{field:'carrierName',title:'承运人',editor:{
				eName:'combobox',
				name:'carrierId',
				valueField: 'id',
				textField: 'nameAll',
				url:'base/workingperson/boxJsons.do',
				queryParams:{unitId:userJson.unitId}
			}},
			{field:'carNum',title:'车牌号码',editor:{}},
			{field:'beginDate',title:'发证日期',editor:{required:true,eName:"datebox"}},
			{field:'endDate',title:'有效期至',editor:{required:true,eName:"datebox"}},
			{field:'operatorName',title:'经办人',editor:{
				required:true,
				eName:'combobox',
				name:'operator',
				valueField: 'id',
				textField: 'nameAll',
				url:'base/workingperson/boxJsons.do'
			}},
			{field:'auditResultName',title:'审核进度',width:222,styler : function(value,row,index){
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
