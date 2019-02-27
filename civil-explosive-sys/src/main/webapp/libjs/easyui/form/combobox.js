
//textbox组件
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],
		function(utils,garenUI,easyUI){
	easyUI.regFn({
	   name:'combobox',
	   tag:'input',
	   onBeforeCreate:function(uiOpts){
		   //设置清空标志
		   var clearFlag = uiOpts.clearFlag || $.fn.combobox.defaults.clearFlag;
		   if(clearFlag){
			   uiOpts.icons = uiOpts.icons || [];
			   uiOpts.icons.push({
				   iconCls:'icon-clear',
				   handler: function(e){
						$(e.data.target).combobox('clear');
						uiOpts.onClear.call(e.data.target);
				   }
			   });
		   }
	   }
	});
	//扩展方法
	//重新加载数据
	$.extend($.fn.combobox.methods,{
	});
	
	//自定义加载数据
	function loader(params,success,error){
		var jq = $(this);
		var uiOpts = jq.combobox('options');
		if(uiOpts.url == null) return;
		if(!uiOpts.autoLoad){
			uiOpts.autoLoad = true;
			return;
		}
	    $ui.postEx(uiOpts.url, params, function(retJson) {
			var data = retJson.data || [];
			if(retJson.result == false) {
				error(retJson.info);
				return;
			}
			//添加全部
			if (uiOpts.allFlag && data.length > 0) {
				var o = {};
				o[uiOpts.valueField] = '';
				o[uiOpts.textField] = "全部";
				data.unshift(o);
			}
			// 遍历设置当前值
			var flag = true;
			if(uiOpts.value == "") uiOpts.value = null;
			$.each(data, function(i, item) {
				if(uiOpts.value != null){
					if (item[uiOpts.valueField] == uiOpts.value) {
						item.selected = true;
						flag = false;
						return false;//结束
					}
				}else{
					if (i == uiOpts.selectIndex) {
						item.selected = true;
						flag = false;
						return false;//结束
					}
				}
			});
			success(data);
		});
	}
	// 扩展属性
	$.extend($.fn.combobox.defaults, {
		clearFlag:false,
		selectIndex:-1,
		editable:false,
		panelHeight:'auto',
		autoLoad:true,
		loader:function(param,success,error){
			loader.call(this,param,success,error);
		},
		allFlag:false,//是否加入全部
		onClear:$.noop
	});
});


