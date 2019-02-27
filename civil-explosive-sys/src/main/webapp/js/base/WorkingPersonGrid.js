
/*
 * 从业人员
 * */
define(function(require,exports,module){
	
	var gridUrls = ["js/base/WorkingPerson1.js","js/base/WorkingPerson2.js","js/base/WorkingPerson3.js"];
	function createColumns(gridOpts,isRead){
		var self = this;
		gridOpts.columns = [[
			{field:'id',title:'id',hidden:true},
			{field:'flag',title:'flag',hidden:true},
			{field:'unitId',title:'所属单位1',hidden:true},
			{field:'unitName',title:'所属单位',width:80,hidden:!isRead},//制度是显示
			{field:'name',title:'姓名',width:66,editor:{required:true}},
			{field:'idcard',title:'身份证号码',width:140,editor:{}},
			{field:'phone',title:'联系电话',editor:{required:true}},
			{field:'techLevelName',title:'技术等级',width:80,editor:{
				required:true,
				eName:'combobox',
				name:'techLevel',
				valueField: 'codeValue',
				textField: 'codeName',
				url:'sys/syscode/jsons.do',
				queryParams:{codeType:6}
			}},
			{field:'licenceTypeName',title:'许可证类型',editor:{
				required:true,
				eName:'combobox',
				name:'licenceType',
				valueField: 'codeValue',
				textField: 'codeName',
				url:'sys/syscode/jsons.do',
				queryParams:{codeType:5}
			}},
			{field:'licenceCode',title:'许可证编号',editor:{}},
			//{field:'licenceName',title:'许可证名称',width:180,editor:{}},
			{field:'cardNum',title:'人员卡编号',width:120,editor:{}},
			{field:'licenceEndDate',title:'许可证<br/>有效期至',editor:{required:true,eName:'datebox'}},
			{field:'endowmentEndDate',title:'养老保险<br/>有效期至',editor:{eName:'datebox'}},
			{field:'labourEndDate',title:'劳动合同<br/>有效期至',editor:{eName:'datebox'}},
			{field:'statusName',title:'状态',width:50,editor:{
				required:true,
				eName:'combobox',
				name:'status',
				valueField: 'codeValue',
				textField: 'codeName',
				url:'sys/syscode/jsons.do',
				queryParams:{codeType:7}
			}},
			{field:'auditResultName',title:'审核进度',width:70,styler : function(value,row,index){
				var ret = null;
				if(row.auditStep != 1){
					ret = row.auditResult?"color:blue;":"color:red;";
				}
				return ret;
			}},
			{field:"row_btns66",title:'操作',width:80}
		]];
	}
	//人员统计datagrid
	//gridType权限类别:0 备案 1 受理 2 审核
	function createSumGrid(jqGroup,unitId,gridType){
		var self = this;
		var jqGrid = null;
		var gridOpts = {
			eName:"datagrid",
			title:"从业人员统计",
			border:true,
			pagination:false,
			url:"base/workingperson/typeCount.do",
			queryParams:{unitId:unitId},
			rowBtnsField:"row_btns66",
			rowBtns : [{
				name:"详情",
				onClick:function(row){
					$ui.createWin({
						title:"从业人员明细",
						width:1360,
						height:500,
						url:gridUrls[gridType],
						queryParams:{licenceType:row.licence_type_value,unitId:unitId}
					});
				}
			}],
			headerTools:[{
    			eName:"a",
    			href:"javascript:void(0)",
    			cls:"icon-refresh panel-tool-a",
    			onClick:function(){
    				jqGrid.datagrid("load");
    			}
    		}],
			columns:[[{field:"licence_type_name",title:'人员类型',width:180},
				{field:"licence_type_count",title:'数量',width:80}
				,{field:"row_btns66",title:'操作',width:80}]]
		};
		jqGrid = jqGroup.createUI(gridOpts);
	}
	
	exports.createColumns = createColumns;
	exports.createSumGrid = createSumGrid;
});

