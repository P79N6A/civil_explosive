

define(function(require,exports,module){
	var queryUrl = "logJsons.do";
	//创建工具栏
	function createToolbar(gridOpts){
		var self = this;
		gridOpts.toolbarEx = [{
			eName:"label",
			text:"开始时间",
			marginLeft:8,
			marginRight:8
		},{
			eName:"datetimebox",
			width:140,
			offsetMs:60 * 1000,
			required:true,
			valType:"month-first",
			name:"kssj",
			value:"2018-07-06"
		},{
			eName:"label",
			text:"间隔(1-5)",
			marginLeft:8,
			marginRight:8
		},{
			eName:"numberspinner",
			width:50,
			min: 1,
			max: 10,
			value:5,
			editable: false,
			name:"dateOffset"
		},{
			eName:"textbox",
			width:140,
			clearFlag:true,
			name:"thread"
		},{
			eName:"linkbutton",plain:true,
			text:"查询",iconCls:"icon-add",
			onClick:function(){
				self.jqGrid.datagrid("load");
			}
		}];
	}
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{field:'xh',title:'xh',hidden:true},
			{field:'dateStr',title:'时间戳',width:150},
			{field:'thread',title:'线程',width:140},
			{field:'cls_name',title:'类名称',width:280,align:"right"},
			{field:'log_level',title:'级别',width:60},
			{field:'msg',title:'内容',width:'auto',align:"left"}
		]];
	}
	//入口
	function Main(jqGroup,loadParams){
		var self = this;
		var gridOpts = {
			eName:"datagrid",
			fit:true,
			border:false,
			rownumbers:true,
			autoload:false,
			idField:"xh",
			stripe:true,
			pageSize:100,
			pageList:[100],
			url:queryUrl
		}
		createToolbar.call(self,gridOpts);
		createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
