
/*
 * 自定义组件picscroll
 * 图片滚动动画
 * */
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],function(utils,garenUI,easyUI){
	
	easyUI.regFn({//注册组件
	   name:"picscroll",//组件名称
	   tag:'div'//html组件
	});
	
	//开启动画 返回true正常，false错误
	function startAnimation(target,num){
		var jqDom = $(target);
		var jqData = $.data(target, "picscroll");
		//若dom已经删除，则清空定时器
		if(undefined == jqData)	 return false;
		var opts = jqData.options;
		var jqBody = jqDom.find('.scroll_body');
		var jqNav = jqDom.find('.nav_dot');
		var width = 0 - jqBody.width();
		var loginBkImg = jqBody.find('img:first');
		var lastImg = jqBody.find('img:last');
		loginBkImg.animate({
			'margin-left': width + 'px'
		},'slow',function(){
			loginBkImg.css('margin-left', '0');
			lastImg.after(loginBkImg);//插入最后
			//切换焦点
			jqNav.find('div.dot_enter').removeClass('dot_enter');
			jqNav.find('div:eq('+num+')').addClass('dot_enter');
		});
		return true;
	}
	//创建导航标志
	function createDot(imgCount) {
		var dotUI = {
			eName : 'div',
			cssClass : "nav_dot",
			elements:[]
		}
		for(var i = 0;i < imgCount;i++){
			var dot = {
				eName : 'div',
				width : 15,
				height : 15
			}
			if(i == 0) dot.cssClass = "dot_enter";
			dotUI.elements.push(dot);
		}
		return dotUI;
	}
	//生成组件
	function init(target){
		var jqDivimg = $(target);
		var opts = $.data(target, "picscroll").options;
		jqDivimg.addClass("animation_pic_scroll");
		//创建图片
		var imgUIs = $.map(opts.picUrls,function(url,i){
			return {eName:'img',src:url}
		});
		var picCount = imgUIs.length;
		if(picCount == 0) return;
		var navDotUIs = createDot(imgUIs.length);
		jqDivimg.createUI([{
			eName:'div',
			cssClass:'scroll_body',
			height:opts.bodyHeight,
			elements:imgUIs
		},navDotUIs]);
		if(picCount < 1) return;//一张图片不需要启动滚动动画
		var num = 0;
		var timeId = window.setInterval(function(){
			if(++num == picCount) num = 0;
			if(false == startAnimation(target,num)){
				clearInterval(timeId);
			}
		}, opts.switchTime);
		return;
	}
	//构造函数
	$.fn.picscroll = function(options, param){
		if (typeof options == 'string'){//调用方法
			var method = $.fn.picscroll.methods[options];
			if (method){
				return method(this, param);
			}else{
				return this.form(options, param);
			}
		}
		options = options || {};
		return this.each(function(){
			var state = $.data(this, "picscroll");
			if (state){
				$.extend(state.options, options);
				$(this).empty();//清空
			} else {
				state = $.data(this, "picscroll", {
					options: $.extend({}, $.fn.picscroll.defaults, 
							$.fn.picscroll.parseOptions(this), options)
				});
			}
			//绘制子元素
			init(this);
		});
	};
	//方法区域
	$.fn.picscroll.methods = {
		options: function(jq){
			return $.data(jq[0], "picscroll").options;
		},
		submit:function(jq,params){
			return jq.each(function(){
				submit(this,params);
			});
		}
	};
	$.fn.picscroll.parseOptions = function(target){
		return $.extend({}, $.parser.parseOptions(target, []));
	};
	//属性
	$.fn.picscroll.defaults = {
		switchTime:5000,//图片切换时间
		bodyHeight:0.9,
		picUrls:[]//图片地址
	};
});

