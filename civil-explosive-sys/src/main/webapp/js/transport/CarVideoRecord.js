

define(function(require,exports,module){
	var queryUrl = "transport/carvideorecord/jsons.do";
	var Playback = require("js/base/VideoPlay");
	//创建工具栏
	function createToolbar(gridOpts){
		var self = this;
		gridOpts.toolbarEx = [{
			eName:"label",
			text:"开始时间",
			marginLeft:8,
			marginRight:8
		},{
			eName:"datebox",
			width:90,
			required:true,
			valType:"month-first",
			name:"kssj",
			value:"2018-07-06"
		},{
			eName:"label",
			text:"结束时间",
			marginLeft:8,
			marginRight:8
		},{
			eName:"datebox",
			width:90,
			required:true,
			name:"jssj",
			value:"2018-07-31"
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
			{field:'id',title:'id',hidden:true},
			{field:'unitName',title:'所属单位'},
			{field:'carName',title:'车辆名称',width:160},
			{field:'cameraName',title:'摄像头',width:60},
			//{field:'itemId',title:'项目id',editor:{}},
			//{field:'videoPath1',title:'videoPath1',editor:{}},
			//{field:'videoPath2',title:'videoPath2',editor:{}},
			{field:'startTime',title:'开始时间',width:130},
			{field:'endTime',title:'结束时间',width:130},
			{field:'videoSizeStr',title:'视频大小',align:"right"},
			{field:"row_btns66",title:'操作',width:80}
			//{field:'remark',title:'remark'}
		]];
	}
	function createRowBtns(gridOpts){
		var self = this;
		gridOpts.rowBtnsField = "row_btns66";
		gridOpts.rowBtns = [{
			name:"回放",
			onClick:function(row){
				if(!row.endTime){
					$ui.alert("视频正在录制中,不能回放!");
					return;
				}
				Playback.play("车辆视频回放",row);
			}
		}];
	}
	//入口
	function Main(jqGroup,loadParams){
		var self = this;
		var gridOpts = {
			eName:"datagrid",
			fit:true,
			border:false,
			autoload:false,
			idField:"id",
			url:queryUrl
		}
		createToolbar.call(self,gridOpts);
		createRowBtns.call(self,gridOpts);
		createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
