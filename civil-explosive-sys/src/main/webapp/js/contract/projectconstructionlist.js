

define(function(require,exports,module){

	function Main(jqObj,loadParams){

	var columns = [[
		{field:'id',title:'id',hidden:true},
		{field:'listType',title:'名单类型(1设计，2评估，3监理)',editor:{required:true}},
		{field:'personId',title:'人员外键',editor:{required:true}},
		{field:'projectId',title:'所属项目',editor:{required:true}}
	]];
	var formDialog = {
		title:'',
		height:200,
		formEx:{
			url:'contract/projectconstructionlist/save.do'
		}
	}

	var MainUI = {
		eName:'datagrid',
		idField:'id',
		url:'contract/projectconstructionlist/json.do',
		delUrl:'contract/projectconstructionlist/delete.do',
		columns:columns,
		formDialog:formDialog
	}

	jqObj.createUI(MainUI);

}
	module.exports = Main;

});
