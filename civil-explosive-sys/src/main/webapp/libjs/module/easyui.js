

/*
 * easyUI组件工厂
 * */
define("garenjs/easyui",function(require,exports,module){
	var garenUI = require("garenjs/htmlui");
	var uiNames = [];
	//对外接口
	return {
		createUI:createUI,
		regFn:function(fn){
			if(fn) uiNames.push(fn);
		},
		regFns:function(fns){
			uiNames = uiNames.concat(fns);
		}
	};
	/*
	 * 创建EasyUI
	 * UIData:组件定义数据
	 * jqObj UI组件容器
	 * 步骤：
	 * 1查询定义语法，null 退出
	 * 2 创建html元素
	 * 3 创建easyui 组件
	 * 4 调用回调函数
	 * 5 完毕
	 * 返回值:
	 * 	null 创建失败
	 *  obj 返回定义options
	 */
	function createUI(jqObj,UIData){
		//查找UI定义
		var uiObj = findEasyUI(uiNames,UIData.eName);
		if(uiObj == null) return 0;//未找到
		var eName = UIData.eName;
		UIData.eName = uiObj.tag;
		var childNodes = UIData.elements;
		UIData.elements = null;//不包含子子节点
		var myobj = garenUI.createUI(jqObj,UIData);
		if(myobj <= 0) return -1;//创建错误
		var htmlUI = myobj.findOpts();
		htmlUI.elements = childNodes;
		htmlUI.eName = eName;
		myobj.unbind();//取消所有事件绑定
		if(uiObj.onBeforeCreate){//创建时回调函数
			uiObj.onBeforeCreate.call(myobj,htmlUI);
		}
		myobj[htmlUI.eName](htmlUI);//创建组件
		htmlUI = myobj[htmlUI.eName]("options");//更新配置信息
		if(uiObj.onCreate){//创建时回调函数
			uiObj.onCreate(myobj,htmlUI);
		}
		return myobj;
	}
	//查询EasyUI组件定义
	function findEasyUI(uiNames,name){
		var obj = null;
		$.each(uiNames,function(i,ui){
			if(name == ui.name){
				obj = ui;
				return false;
			}
		});
		return obj;
	}
});
