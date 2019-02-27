

define(function(require,exports,module){
	var queryUrl = "base/videorecord/jsons.do";
	var Playback = require("js/base/VideoPlay");
	//创建工具栏
	function createToolbar(gridOpts){
		var self = this;
		gridOpts.toolbarEx = [{
			eName:"label",
			text:"爆破单位",
			marginLeft:8,
			marginRight:8
		},{
			eName:"combobox",
			width:100,
			name:"unitId",
			valueField: "id",
			textField: "shortName",
			allFlag:true,
			url:"base/workingunit/boxJsonEx.do"
		},{
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
			{field:'cameraName',title:'设备名称'},
			//{field:'cameraSipId',title:'摄像头编码',width:150},
			//{field:'itemId',title:'项目id'},
			//{field:'videoPath',title:'videoPath',width:360},
			//{field:'videoPath2',title:'videoPath2',width:300},
			//{field:'recordStatus',title:'recordStatus'},
			{field:'startTime',title:'开始时间',width:150},
			{field:'endTime',title:'结束时间',width:150},
			{field:'videoSizeStr',title:'文件大小',width:120,align:'right'},
			//{field:'remark',title:'remark'},
			{field:"row_btns66",title:'操作',width:80}
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
				Playback.play("现场视频回放",row);
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
			rownumbers:true,
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
