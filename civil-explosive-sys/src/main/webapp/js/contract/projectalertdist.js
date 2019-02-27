

define(function(require,exports,module){

	function Main(jqObj,loadParams){

	var columns = [[
		{field:'id',title:'id',hidden:true},
		{field:'safe distance',title:'核定安全距离(m)',editor:{required:true}},
		{field:'orientation',title:'方位',editor:{}},
		{field:'byProtectionName',title:'被保护对象名称',editor:{}},
		{field:'projectId',title:'所属项目',editor:{required:true}}
	]];
	var formDialog = {
		title:'',
		height:250,
		formEx:{
			url:'contract/projectalertdist/save.do'
		}
	}

	var MainUI = {
		eName:'datagrid',
		idField:'id',
		url:'contract/projectalertdist/json.do',
		delUrl:'contract/projectalertdist/delete.do',
		columns:columns,
		formDialog:formDialog
	}

	jqObj.createUI(MainUI);

}
	module.exports = Main;

});
