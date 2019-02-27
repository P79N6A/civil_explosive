

define(function(require,exports,module){

	function Main(jqObj,loadParams){

	var columns = [[
		{field:'remark',title:'备注',editor:{required:true}},
		{field:'explosiveVariety',title:'所属品种',editor:{required:true}},
		{field:'id',title:'id',hidden:true},
		{field:'storeId',title:'storeId',editor:{}},
		{field:'explosiveCount',title:'炸药数量',editor:{required:true}}
	]];
	var formDialog = {
		title:'',
		height:250,
		formEx:{
			url:'store/explosivestoreinventory/save.do'
		}
	}

	var MainUI = {
		eName:'datagrid',
		idField:'id',
		url:'store/explosivestoreinventory/json.do',
		delUrl:'store/explosivestoreinventory/delete.do',
		columns:columns,
		formDialog:formDialog
	}

	jqObj.createUI(MainUI);

}
	module.exports = Main;

});
