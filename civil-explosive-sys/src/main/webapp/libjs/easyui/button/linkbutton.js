
//自定义组件
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],function(utils,garenUI,easyUI){
	
	easyUI.regFn({//注册组件
	   name:'linkbutton',//组件名称
	   tag:'a',//html组件
	   onCreate:function(jqObj,uiOpts){//回调函数
		   var margin = parseInt(uiOpts.mLeft);
		   margin && jqObj.css("margin-left",margin + "px");
		   margin = parseInt(uiOpts.mRight);
		   margin && jqObj.css("margin-right",margin + "px");
	   }
	});
	
	$.extend($.fn.linkbutton.methods,{
		setText : function(jqDom,text){//
			jqDom.linkbutton('options').text = text;
			var jqText = jqDom.findJq('l-btn-text');
			jqText.text(text);
		}
	});
	
	$.extend($.fn.linkbutton.defaults, {
		plain : false
    });
	
});

