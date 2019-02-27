

/*
 * 组件工厂,即生成组件工厂
 * 对外提供统一接口
 * */
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],function(utils,garenUI,easyUI){
	//验证jquery对象是否为空
	function checkJqObj(jqObj){
		if(jqObj && jqObj.length > 0) return true;
		return false;
	}
	/*扩展jquery dom操作库*/
	$.fn.extend({
		/*
		 * 查询jeasyui options
		 * */
		findOpts:function(name){
			var obj = name?this.find(name):this;
			var tagName = obj.prop('nodeName') || '';
			tagName = tagName.toLowerCase();
			return obj.data(tagName + "_ui");
		},
		/*
		 * 模糊查询jquery对象
		 * */
		findJq:function(name){
			var jq = this;
			if(!name) return this;
			var rules = ['[uid="'+name+'"]',
			             '[uname="'+name+'"]',
			             '.' + name,
			             '#' + name,
			             '[nname="'+name+'"]',
			             '[name="'+name+'"]',
			             name];
			var jqObj = null;
			$.each(rules,function(i,rule){
				jqObj = jq.find(rule);
				if(checkJqObj(jqObj)) return false;
			});
			return jqObj;
		},
		//多个集合只获取第一个
		findJqOpts:function(name){
			var tagName = obj.prop('nodeName') || '';
			tagName = tagName.toLowerCase();
			return this.findJq(name).data(tagName + "_ui");
		},
		/*
		 * 渲染界面
		 * 参数为:
		 *  ui定义 json格式
		 *  返回jquery对象
		 * */
		createUI:function(uiOpts){
			var jqObj = $(this);
			var newui = null,uis = [];
			if($.isArray(uiOpts) == false) uiOpts = [uiOpts];//默认为数组
			//遍历所有组件，自动匹配组件构造器
			$.each(uiOpts,function(i,e){
				if(e == null) return;
				if("string" == typeof e  //文本直接添加
						|| 'number' == typeof e)  jqObj.append(e);
				else{
					var eName = e['eName'];
					if(typeof eName != "string") return true;
					//创建html元素
					newui = garenUI.createUI(jqObj,e);
					if(0 == newui){
						if(!e.uname) e.uname = e.eName;//复制ename
						if(!e.nname) e.nname = e.name;//复制name
						//创建jeasyui组件
						newui = easyUI.createUI(jqObj,e);
						if(!newui) $ui.print("组件不存在:" + eName);
					}else if(-1 == newui) $ui.print("组件验证没通过:" + eName);
					if('object' == typeof newui) uis.push(newui);
				}
			});
			if(uis.length == 1) return uis[0];
			else return uis;
		},
		/*
		 * 加载js模块
		 * 	url 模块地址
		 * 	params参数
		 *  clearFlag 是否清空，宿主对象
		 * */
		loadJs:function(url,params,clearFlag){
			if(typeof url != 'string') return;
			$ui.print(url);
			params = params || {};
			var ret = null;
			var jqObj = $(this);
			url = url.replace(/\.js$/i,'');
			if(!url) return null;
			if(!clearFlag) jqObj.empty();
			//异步调用，同步执行
			require([url],function(mm){
				if($.isFunction(mm))
					ret =  new mm(jqObj,params);
			});
			return ret;
		},
		/*
		 * 加载js模块
		 * 	url 模块地址
		 * 	params参数
		 *  clearFlag 是否清空，宿主对象
		 * */
		loadJsx:function(jspUrl,jsUrl,params,clearFlag){
			if(typeof jsUrl != 'string') return;
			var self = this;
			$.get("loadJsp.do?name=" + jspUrl,function(data){
				var jqDom = $(data);
				self.html(jqDom);
				jqDom.loadJs(jsUrl,params,true);
			});
		},
		loadJsp:function(url,fn){
			var self = this;
			$.get("loadJsp.do?name=" + url,function(data){
				self.html(data);
			});
		},
		/*
		 * 快速创建html
		 * 即：递归创建html字符串,并拼接之。
		 * */
		createHtml:function(uiOpts){
			$(this).append(garenUI.createHtmls(uiOpts));
		},
		/*
		 * 动态扩展组件方法属性
		 * 对已经实例的组件扩展其方法属性
		 * */
		updateOpt : function(uifun,newOpt){
		    try{
		    	if(this.length <= 0) return;
		        $.extend(this[uifun].call(this,'options'),newOpt);
		    }catch(err){
		        $ui.print(err);
		    }
		}
	});
});

