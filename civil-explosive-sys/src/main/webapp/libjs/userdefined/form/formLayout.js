
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],function(utils,garenUI,easyUI){
	
	var uname = "formLayout";
	easyUI.regFn({//注册组件
	   name:uname,//组件名称
	   tag:'div'//html组件
	});
	//生成组件
	function init(target){
		var opts = $.data(target, uname).options;
		var jqForm = null;
		var ui = {
			eName:"layoutExt",
    		fit:true,
    		elements:[{
    			region:"center",
    			elements:opts.formUI
    		},{
    			region:"south",
    			height:60,
    			cssClass:"dialog_button_layout",
    			elements:[{
    				eName:'linkbutton',
    				text:'保存',
    				width:60,
    	        	onClick:function(){
    	        		jqForm.formEx("submit");//表单提交
    	        	}
    	        },{
    	        	eName:'span',
    	        	cssStyle:'margin:6px;'
    	        }]
    		}]
		}
		var jqLayout = $(target).createUI(ui);
		jqForm = jqLayout.findJq("formEx");
	}
	
	//构造函数
	$.fn[uname] = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn[uname].methods[options];
			if (method){
				return method(this, param);
			}
		}
		options = options || {};
		return this.each(function(){
			var state = $.data(this, uname);
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, uname, {
					options: $.extend({}, $.fn[uname].defaults,
							$.fn[uname].parseOptions(this), options)
				});
			}
			init(this);
		});
	};
	
	$.fn[uname].methods = {
		options: function(jq){
			return $.data(jq[0], uname).options;
		}
	};
		
	$.fn[uname].parseOptions = function(target){
		return $.extend({}, $.parser.parseOptions(target, []));
	};
	
	$.fn[uname].defaults = {
		data:[]
	};
		
});

