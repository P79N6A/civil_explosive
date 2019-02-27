
//自定义组件
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],function(utils,garenUI,easyUI){
	
	easyUI.regFn({//注册组件
	   name:'panel',//组件名称
	   tag:'div',//html组件
	   onCreate:function(jqObj,uiOpts){//回调函数
		    var jqBody = jqObj.panel("body");
		    jqBody.createUI(uiOpts.elements);
		    //定义title icon按钮
			if(uiOpts.headerTools){
				var jqPanelHeader = jqObj.panel("header");
				jqPanelHeader.find(".panel-tool").createUI(uiOpts.headerTools);
			}
	   }
	});

	$.extend($.fn.panel.defaults,{
		onClose:function(){
			$(this).panel("destroy");
		}
	});
	
});

