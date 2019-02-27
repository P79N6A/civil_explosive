
/*
 * 炸药仓库
 * */
define(function(require,exports,module){
	
	function createColumns(gridOpts,isRead){
		var self = this;
		gridOpts.columns = [[
			{field:'id',title:'id',hidden:true},
			{field:'unitId',title:'所属单位',hidden:true},
			{field:'unitName',title:'所属单位',width:80,hidden:!isRead},//制度是显示
			{field:'name',title:'仓库名称',editor:{required:true}},
			{field:'standardCapacity',title:'核定容量',editor:{required:true,eName:'numberbox'}},
			/*{field:'monitorCameraCount',title:'监控探头数量',editor:{eName:'numberbox'}},
			{field:'alarmInbreakName',title:'入侵报警',editor:{
				eName:'combobox',
				name:'alarmInbreak',
				data:[{text:'是',value:1},{text:'否',value:0}]
			}},*/
			{field:'safeGradeName',title:'安全评级单位',width:180,editor:{
				eName:'combobox',
				name:'safeGradeId',
				editable:false,
				valueField: 'id',
				textField: 'name',
				panelHeight:'auto',
				url:'base/workingunit/boxJson.do',
				queryParams:{}
			}},
			{field:'safeGradeEndDate',title:'安全评价有效期',editor:{required:true,eName:'datebox'}},
			{field:'storeTypeName',title:'仓库类型',editor:{
				eName:'combobox',
				name:'storeType',
				valueField: 'codeValue',
				textField: 'codeName',
				url:'sys/syscode/jsons.do',
				queryParams:{codeType:'f'}
			}},
			{field:'auditResultName',title:'审核进度',width:70,styler: function(value,row,index){
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
