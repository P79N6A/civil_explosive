
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],
		function(utils,garenUI,easyUI){
	
	//timestamp = new Date(2016,11,4).getTime();
	//$ui.print(new Date(2016,11,4));
	easyUI.regFn({//注册组件
		   name:'datetimebox',//组件名称
		   tag:'input',//html组件
		   onCreate:function(jqObj,uiOpts){
			   var offsetMs = uiOpts.offsetMs || 0;
			   datestr = formatDate(createDate() - offsetMs);
			   jqObj.datetimebox('setValue',datestr);
		   }
	});
	var perDay = 1000 * 60 * 60 * 24;
	//日期格式化,格式：yyyy-MM-dd
	function formatDate(dateNum){
		var date = new Date(dateNum);
		return date.getFullYear() + "-" 
				+ (date.getMonth() + 1) + "-" + date.getDate()
				+ " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
	}
	//月初
	function getMonthFirst(){
		var date = createDate();
		date.setDate(1);
		return formatDate(date.getTime());
	}
	//月末
	function getMonthLast(dateNum){
		var date = createDate();
		var month = date.getMonth();
		date.setMonth(month + 1,0);
		return formatDate(date.getTime());
	}
	//周一
	function getWeekFirst(dateNum){
		var date = createDate();
		var week = date.getDay();
		if(week == 0) week = 7;
		date = new Date(date.getTime() - (week - 1) * perDay);
		return formatDate(date.getTime());
	}
	//周日
	function getWeekLast(dateNum){
		var date = createDate();
		var week = date.getDay();
		if(week == 0) week = 7;
		date = new Date(date.getTime() + (7 - week) * perDay);
		return formatDate(date.getTime());
	}
	//创建日期
	function createDate(){
		//获取系统时间戳
		var timestr = $('html meta#server_info').attr('data-timestr');
		var date = new Date();
		if(timestr){
			var strs = timestr.split(",");
			date = new Date(strs[0],parseInt(strs[1])-1,strs[2]
				,strs[3],strs[4],strs[5],strs[6]);
		}
		return date;
	}
});

