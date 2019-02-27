
//自定义组件
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],function(utils,garenUI,easyUI){
	
	easyUI.regFn({//注册组件
		   name:'menubutton',//组件名称
		   tag:'a',//html组件
		   onCreate:function(jqObj,uiOpts){//回调函数
			   
		   },
		   onBeforeCreate:function(jqObj,uiOpts){
			   var menuId = "menu-" + new Date().getTime();
			   uiOpts.menu = '#' + menuId;
			   $.each(uiOpts.elements,function(i,m){
				   m.eName = "div";
			   });
			   var ui = {
				  eName:"div",
				  id:menuId,
				  elements:uiOpts.elements
			   }
			   $('body').createUI(ui);
		   }
	});
	
});

