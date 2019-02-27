

define(function(require,exports,module){
	
	var carGpsUrl = "transport/carinfo/carGps.do";
	
	function onGps(){
		var self = this;
		var carMarkerMap = {};
		function onTimer(){
			$ui.postEx(carGpsUrl,function(retJson){
				var carsGps = retJson.data;
				$.each(carsGps,function(i,carGps){
					if(!carGps.gpsLng || !carGps.gpsLat) return;//空
					var point = new BMap.Point(carGps.gpsLng,carGps.gpsLat);
					var carMarker = carMarkerMap[carGps.carNum];
					if(carMarker){
						carMarker.setPosition(point);
					}else{
						var markWidth = 48,labelWidth = 200;
						var myIcon = new BMap.Icon("images/car.png", new BMap.Size(48, 48), {
								offset: new BMap.Size(markWidth, markWidth),
								imageOffset: new BMap.Size(0,0)
								});
						//计算偏移量，label居中
						var labelOffset = new BMap.Size(0 - (labelWidth - markWidth) / 2,-10);
						var label = new BMap.Label(carGps.carNum,{offset:labelOffset});//
						label.setStyle({"border":"none","backgroundColor":"none","text-align":"center"
								,"color":"#bb0000","width":labelWidth + "px","display":"inline-block"});
						var marker = new BMap.Marker(point,{icon: myIcon});
						marker.setLabel(label);
						self.baiduMap.addOverlay(marker);
						carMarkerMap[carGps.carNum] = marker;
					}
				});
			});
		}
		self.carTimer = window.setInterval(onTimer,1000 * 3);
		onTimer();
	}
	//入口
	function Main(jqGroup,loadParams){
		var self = this;
		var mapDomId = "map_" + new Date().getTime();
		jqGroup.attr("id",mapDomId);
		var map = new BMap.Map(mapDomId); // 创建地图实例
		var point = new BMap.Point(121.1524, 37.4828); // 创建点坐标
		map.centerAndZoom(point, 13); // 初始化地图,设置中心点坐标和地图级别
		map.addControl(new BMap.NavigationControl());
		map.addControl(new BMap.ScaleControl());
		self.baiduMap = map;
		onGps.call(self);
	}
	
	Main.prototype.onQuit = function(){
		var self = this;
		window.clearInterval(self.carTimer);
	}
	return Main;
});
