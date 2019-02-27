

define(function(require,exports,module){

	function Main(jqObj,loadParams){

	var columns = [[
		{field:'id',title:'id',hidden:true},
		{field:'startGpsLat',title:'开始纬度坐标',editor:{}},
		{field:'name',title:'路段名称',editor:{}},
		{field:'endGpsLng',title:'结束经度坐标',editor:{}},
		{field:'endGpsLat',title:'结束纬度坐标',editor:{}},
		{field:'lineId',title:'所属路线',editor:{required:true}},
		{field:'startGpsLng',title:'开始经度坐标',editor:{}},
		{field:'orderNum',title:'顺序号',editor:{}}
	]];
	var formDialog = {
		title:'',
		height:400,
		formEx:{
			url:'contract/transportlinelist/save.do'
		}
	}

	var MainUI = {
		eName:'datagrid',
		idField:'id',
		url:'contract/transportlinelist/json.do',
		delUrl:'contract/transportlinelist/delete.do',
		columns:columns,
		formDialog:formDialog
	}

	jqObj.createUI(MainUI);

}
	module.exports = Main;

});
