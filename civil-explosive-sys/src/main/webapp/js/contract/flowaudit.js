

define(function(require,exports,module){

	function Main(jqObj,loadParams){

	var columns = [[
		{field:'hostType',title:'审批类型',editor:{required:true}},
		{field:'remark',title:'remark',editor:{}},
		{field:'result',title:'审核结果0拒绝,1通过',editor:{required:true}},
		{field:'step',title:'步骤数',editor:{}},
		{field:'checkerId',title:'审批人',editor:{}},
		{field:'desc',title:'审核描述',editor:{required:true}},
		{field:'id',title:'id',hidden:true},
		{field:'flag',title:'新值',editor:{}},
		{field:'hostId',title:'宿主',editor:{}}
	]];
	var formDialog = {
		title:'',
		height:450,
		formEx:{
			url:'contract/flowaudit/save.do'
		}
	}

	var MainUI = {
		eName:'datagrid',
		idField:'id',
		url:'contract/flowaudit/json.do',
		delUrl:'contract/flowaudit/delete.do',
		columns:columns,
		formDialog:formDialog
	}

	jqObj.createUI(MainUI);

}
	module.exports = Main;

});
