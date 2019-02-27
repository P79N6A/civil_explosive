
//自定义组件
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],function(utils,garenUI,easyUI){
	
	easyUI.regFn({// 注册组件
		name : 'datalist',// 组件名称
		tag : 'ul',// html组件
		onBeforeCreate:function(opts){
		},
		onCreate : function(jqObj, uiOpts) {
			if(uiOpts.headerTools){//定义工具栏数据
				var jqPanelHeader = jqObj.datalist("getPanel").panel("header");
				jqPanelHeader.find(".panel-tool").createUI(uiOpts.headerTools);
			}
		}
	});
	
	/*
	 * 方法扩展
	 */
	$.extend($.fn.datalist.methods,{
		
	});
	
	/*
	 * 组件扩展属性
	 */
	$.extend($.fn.datalist.defaults, {
		loader:function(params,success,error){
			var self = this;
			var opts = $(this).datalist('options');
			$ui.postEx(opts.url, params, function(retJson) {
				if(retJson.result) success.call(self, retJson.data);
				else error.call(self,retJson);
			});
			return true;
	   }
	});
	
});

