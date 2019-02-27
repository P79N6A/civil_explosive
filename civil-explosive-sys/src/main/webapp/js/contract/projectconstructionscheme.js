

define(function(require,exports,module){

	function Main(jqObj,loadParams){

	var columns = [[
		{field:'auditorId',title:'审核人',editor:{required:true}},
		{field:'gunMuzzleCount',title:'炮口个数',editor:{}},
		{field:'designerId',title:'设计人',editor:{required:true}},
		{field:'projectId',title:'所属项目',editor:{required:true}},
		{field:'approverId',title:'批准人',editor:{required:true}},
		{field:'schemeId',title:'schemeId',hidden:true},
		{field:'detonatorDosage',title:'雷管数量',editor:{}},
		{field:'maxExplosiveDosage',title:'一次性最大起爆药量',editor:{}},
		{field:'explosiveDosage',title:'炸药数量',editor:{}}
	]];
	var formDialog = {
		title:'',
		height:450,
		formEx:{
			url:'contract/projectconstructionscheme/save.do'
		}
	}

	var MainUI = {
		eName:'datagrid',
		idField:'schemeId',
		url:'contract/projectconstructionscheme/json.do',
		delUrl:'contract/projectconstructionscheme/delete.do',
		columns:columns,
		formDialog:formDialog
	}

	jqObj.createUI(MainUI);

}
	module.exports = Main;

});
