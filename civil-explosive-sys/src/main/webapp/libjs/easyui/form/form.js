
//自定义组件
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],function(utils,garenUI,easyUI){
	$.extend($.fn.form.methods,{
		form2Json : function(sform,formjson){//form表单，解析为form对象
			if(!$(sform).form('validate')) return false;
			formjson = formjson || {};
			var opt = $(sform).form('options');
			var forms = $(sform).serializeArray();
			var newforms = {};
			$.each(forms,function(i,item){
				var key = item['name'];
				var val = $.trim(item['value']);
				if('$&' == val) return;//全部标志
				if(opt.clearEmpty && val == '') return;//默认空字符串过滤掉
				if(newforms[key])  newforms[key] += ',' + val;
				else newforms[key] = val;
			});
			$.extend(formjson,newforms);
			return true;
		}
	});
	
	$.extend($.fn.form.defaults,{
		clearEmpty:true//空字符串是否上传
	});
	
});

