
//自定义组件
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],function(utils,garenUI,easyUI){
	
	easyUI.regFn({//注册组件
	   name:'window',//组件名称
	   tag:'div',//html组件
	   onCreate:function(jqObj,uiOpts){//回调函数
		    var jqWin = jqObj.parent();
		    var bodyUI = bodyUI || uiOpts.bodyUI;
			jqWin.addClass(uiOpts.cls);//设置自定义样式
			//设置头样式
			jqWin.find("div.panel-header").addClass(uiOpts.headerCls);
			jqObj.addClass(uiOpts.bodyCls);
			jqObj.window("hcenter");//重新居中
			if($ui.isJsUrl(uiOpts.url)){
				jqObj.loadJs(uiOpts.url,uiOpts.queryParams);
			}else if(bodyUI){
				jqObj.createUI(bodyUI);
			}
			if(uiOpts.closeIcon){
				jqWin.find("div.panel-header").find("a").attr("class",uiOpts.closeIcon);
			}
			if(uiOpts.onLoad){
				uiOpts.onLoad.call(jqObj);
			}
			//定义title icon按钮
			if(uiOpts.headerTools){
				var jqPanelHeader = jqObj.panel("header");
				jqPanelHeader.find(".panel-tool").createUI(uiOpts.headerTools);
			}
	   }
	});

	$.extend($.fn.window.defaults,{
		collapsible:false,
		minimizable:false,
		resizable:false,
        maximizable:false,
        doSize:false,
        modal:true,
        width:500,
        height:200,
        onClose: function () {//关闭销毁窗口
            $(this).window('destroy');
        }
	});
	
});

