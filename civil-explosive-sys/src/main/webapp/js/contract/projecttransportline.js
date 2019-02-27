

define(function(require,exports,module){

	function Main(jqObj,loadParams){

	var columns = [[
		{field:'gpsType',title:'GPS类型(1国标 2百度 3高德)',editor:{}},
		{field:'lineCount',title:'路段数',editor:{}},
		{field:'id',title:'id',hidden:true},
		{field:'lineStart',title:'路线起点',editor:{}},
		{field:'lineEnd',title:'路线终点',editor:{}},
		{field:'projectId',title:'所属项目',editor:{required:true}}
	]];
	var formDialog = {
		title:'',
		height:300,
		formEx:{
			url:'contract/projecttransportline/save.do'
		}
	}

	var MainUI = {
		eName:'datagrid',
		idField:'id',
		url:'contract/projecttransportline/json.do',
		delUrl:'contract/projecttransportline/delete.do',
		columns:columns,
		formDialog:formDialog
	}

	jqObj.createUI(MainUI);

}
	module.exports = Main;

});
