
/*
 * */
define(function(require,exports,module){
	//创建表格列
	function createColumns(){
		var self = this;
		return [[
			{field:'hostId',title:'宿主',editor:{required:true}},
			{field:'hostType',title:'审批类型',editor:{required:true}},
			{field:'step',title:'步骤数',editor:{required:true}},
			{field:'result',title:'审核结果0拒绝,1通过',editor:{required:true}},
			{field:'id',title:'id',hidden:true},
			{field:'remark',title:'remark',editor:{}},
			{field:'desc',title:'审核描述',editor:{}},
			{field:'auditId',title:'审批人',editor:{required:true}}
		]];
	}
	//入口
	function main(jqDom,args){
		var self = this;
		var columnsUI = createColumns.call(self);
		var mainUI = {
			eName:"datagrid",
			idField:"id",
			url:"base/auditflow/json.do",
			delUrl:"base/auditflow/delete.do",
			columns:columnsUI,
			formEdit:{
				formEx:{
					url:"base/auditflow/save.do",
				},
				formDialog:{
					title: "",
					height:310
				}
			}
		};
		jqDom.createUI(mainUI);
	}
	return main;
});

