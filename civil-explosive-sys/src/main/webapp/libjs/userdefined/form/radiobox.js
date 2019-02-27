
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],function(utils,garenUI,easyUI){
	
	var uname = "radiobox";
	easyUI.regFn({//注册组件
	   name:uname,//组件名称
	   tag:'div'//html组件
	});
	//生成组件
	function init(target){
		var opts = $.data(target, uname).options;
		$(target).addClass('datagrid-radiobox');
		var uis = [];
		$.each(opts.data,function(i,data){
			var ui = {
				eName:"span",
				elements:[{
					eName:"input",
					type:"radio",
					disabled:opts.disabled,
					readonly:opts.readonly,
					name:opts.name,
					value:data.value,
					onClick:function(){
						if(opts.readonly) return false;
						opts.onClick && opts.onClick.call(self,$(this).val());
					}
				},{
					eName:"label",
					text:data.text
				}]
			};
			uis.push(ui);
		});
		//$ui.print(uis);
		$(target).createUI(uis);
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
		},
		test:function(jq,a,b){
			$ui.print('abcd',a,b);
			return "eee";
		}
	};
		
	$.fn[uname].parseOptions = function(target){
		return $.extend({}, $.parser.parseOptions(target, []));
	};
	
	$.fn[uname].defaults = {
		data:[]
	};
		
});

