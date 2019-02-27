

define(function(require,exports,module){
	var queryUrl = "transport/carlinegps/jsons.do";
	var locusUrl = "transport/carlinegps/gpsJsons.do";
	//地图中车辆轨迹回放
	function carLocusBack(carJson){
		var self = this;
		var mapDomId = "map_" + new Date().getTime();
		var jqWin = $ui.createWin({
			title:carJson.carName +"-轨迹回放",
			width:1200,
			height:800,
			maximizable:true,
			id:mapDomId
		});
		var map = new BMap.Map(mapDomId); // 创建地图实例
		var point = new BMap.Point(121.1524, 37.4828); // 创建点坐标
		map.centerAndZoom(point, 13); // 初始化地图,设置中心点坐标和地图级别
		map.addControl(new BMap.NavigationControl());
		map.addControl(new BMap.ScaleControl());
		map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
		self.baiduMap = map;
		$ui.postEx(locusUrl,{carCode:carJson.carCode,lineStart:carJson.lineStart,lineEnd:carJson.lineEnd},function(retResult){
			$ui.print(retResult);
			var sy = new BMap.Symbol(BMap_Symbol_SHAPE_BACKWARD_OPEN_ARROW, {
			    scale: 0.6,//图标缩放大小
			    strokeColor:'#fff',//设置矢量图标的线填充颜色
			    strokeWeight: '2',//设置线宽
			});
			var icons = new BMap.IconSequence(sy, '10', '30');
			var points = [];
			$.each(retResult.data,function(i,gpsJson){
				points.push(new BMap.Point(gpsJson.gpsLng,gpsJson.gpsLat));
			});
			$ui.print(points);
			var polyline =new BMap.Polyline(points, {
			    enableEditing: false,//是否启用线编辑，默认为false
			    enableClicking: true,//是否响应点击事件，默认为true
			    icons:[icons],
			    strokeWeight:'8',//折线的宽度，以像素为单位
			    strokeOpacity: 0.8,//折线的透明度，取值范围0 - 1
			    strokeColor:"#18a45b" //折线颜色
			});
			map.addOverlay(polyline);          //增加折线
		});
	}
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
			{field:'unitName',title:'所属单位',width1:200},
			{field:'carName',title:'车辆号牌'},
			//{field:'lineStart',title:'起点',editor:{required:true}},
			//{field:'lineEnd',title:'终点',editor:{}},
			{field:'startTime',title:'开始时间',width:130},
			{field:'endTime',title:'结束时间',width:130},
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
				carLocusBack.call(self,row);
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
		createRowBtns.call(self,gridOpts);
		createToolbar.call(self,gridOpts);
		createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
