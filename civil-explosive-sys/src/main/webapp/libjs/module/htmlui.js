
/*
 * html元素实现工厂
 * */
define("garenjs/htmlui",function(require,exports,module){
	
	//支持的html元素名称
	var htmlNames = ['div','a','span','form','img','p','fieldset', 'legend',
          'label','select','option','textarea','optgroup','button','video',
          'map','area','table','thead','tbody','th','tr','object',
         {name:'input',closeFlag:true},{name:'br',closeFlag:true},
         {name:'td',check:function(tdData){return tdData.status;}},'tfoot',
         'ul','ol','li','dl','dt','dd'
	];
	//属性集合,check回调转换值
	var htmlAttrs = ['name','src','href','alt','uId','checked',
		'muted','autoplay','controls',//html5相关属性
		'datafield','type','tabindex','dir','onselectstart',//禁止选中文本IE8-IE9
		'value',"method","enctype",'for','codebase','classid','uname','nname',
	    'disabled','id','title',{name:'class',alias:'cssClass'},{name:'class',alias:'cls'},
	    "r","c",
        {name:'colspan',alias:'colSpan',check:function(v){return v > 1;}},
        {name:'rowspan',alias:'rowSpan',check:function(v){return v > 1;}}
	];
	var eventObj = [
        /*****键盘事件****/
        {key:'onKeydown',val:'keydown'}, {key:'onKeyup',val:'keyup'},{key:'onKeypress',val:'keypress'},
        /*******焦点相关事件********/
        {key:'onFocus',val:'focus'},{key:'onBlur',val:'blur'},{key:'onChange',val:'change'},
        {key:'onFocusin',val:'focusin'},{key:'onFocusout',val:'focusout'},
        /*******鼠标相关事件********/
        {key:'onClick',val:'click'},{key:'onDblclick',val:'dblclick'},
        {key:'onMousedown',val:'mousedown'},{key:'onMouseup',val:'mouseup'},
        {key:'onMouseenter',val:'mouseenter'},{key:'onMouseleave',val:'mouseleave'},
        {key:'onMousemove',val:'mousemove'},{key:'onMouseout',val:'mouseout'},
        {key:'onMouseover',val:'mouseover'},{key:'onScroll',val:'scroll'},
        /*其他事件*/
        {key:'onSelect',val:'select'},{key:'onSubmit',val:'submit'},{key:'onUnload',val:'unload'}
	];
	/*
	 * 对外接口
	 * */
	var uiObj = {
		createUI:createUI,
		createHtmls:createHtmls
	}
	return uiObj;
	/*
	 * 渲染DOM
	 * */
	function renderDom(jqObj,myobj,UIData){
		var addMode = UIData.addMode || 'append';//默认追加
		switch(addMode){
		case 'prepend'://前置
			jqObj.prepend(myobj); 
			break;
		case 'append'://追加
			jqObj.append(myobj);
			break;
		case 'html'://覆盖
			jqObj.html(myobj);
			break;
		case 'before'://同级前面
			jqObj.before(myobj);
			break;
		case 'after'://同级后面
			jqObj.after(myobj);
			break;
		default://追加
			jqObj.append(myobj);
			break;
		}
	}
	/*
	 * 创建html元素
	 * UIData:元素定义数据
	 * addMode元素添加DOM的方式:
	 * 		prepend顶部插入,
	 *  	append尾部添加,
	 *  	html覆盖,
	 *  	before同级前面,
	 *  	after同级后面
	 * 返回值：正常object组件对象
	 * 0元素未定义 -1 创建失败 
	 */
	function createUI(jqObj,UIData){
		var tagName = UIData.eName;
		var tagObj = checkHtmlTag(tagName);
		if(tagObj == null) return 0;//tag不存在
		else if(tagObj.check //验证合法性
				&& tagObj.check(UIData) == false) return -2;
		//创建前调用
		UIData.onCreate && UIData.onCreate.call(UIData);
		var html = "<" + tagName + parseAtrr(UIData);
		html += (tagObj.closeFlag)?"/>":"></"+tagName+">";
		if(UIData.debug) $ui.print(html);
		var myobj =  $(html);//创建Dom对象
		//添加css
		UIData.css && myobj.css(UIData.css);
		renderDom(jqObj,myobj,UIData);//渲染对象
		var jqOpts = $.extend(true,{},UIData);//复制组件定义
		myobj.data(tagName + "_ui",jqOpts);//绑定DOM中
		bindEvent(jqOpts,myobj);//绑定事件
		initSize(jqObj,myobj,jqOpts);
		//no子元素
		if(tagObj.closeFlag) return myobj;
		/*
		 * 递归处理子元素
		 * */
		var se = jqOpts.elements;
		var st = jqOpts.text; 
		//无须创建子元素
		if(jqOpts.noChild || (!se && !st)) return myobj;
		if(chnum(se)) myobj.html(se);
		else if(chnum(st))	myobj.text(st);
		else myobj.createUI(se);//创建子元素，调用工厂方法，有可能包含子组件
		return myobj;
	}
	//创建元素,仅生成网页，不绑定事件
	function createHtml(UIData){
		var tagName = UIData.eName;
		var tagObj = checkHtmlTag(tagName);
		if(tagObj == null ||
			(tagObj.check 
					&& tagObj.check(UIData) == false)) 
			return "";
		var html = null;
		//没有子元素，直接返回
		if(tagObj.closeFlag) return "<" + tagName + parseAtrr(UIData) + "/>";
		else html = "<" + tagName + parseAtrr(UIData) + ">";
		if(UIData.elements || UIData.text){
			if(chnum(UIData.elements)){
				html += UIData.elements;
			}else if(chnum(UIData.text)){
				html += UIData.text;
			}else html += createHtmls(UIData.elements);//创建子元素
		}
		html += "</"+tagName+">";
		return html;//返回组件
	}
	//递归创建元素集合
	function createHtmls(uiOpts){
		var html = "";
		if($.isArray(uiOpts) == false) uiOpts = [uiOpts];//默认为数组
		$.each(uiOpts,function(i,e){//遍历所有组件，自动匹配组件构造器
			if(e == null) return;
			html += chnum(e)?e:createHtml(e);
		});
		return html;
	}
	//判断为字符或数字对象
	function chnum(obj){
		return ("string" == typeof obj 
		|| "number" == typeof obj)?true:false;
	}
	//验证原生html元素
	function checkHtmlTag(tagName){
		var flag = false;
		var tagObj = null;
		$.each(htmlNames,function(i,obj){
			var name = null;
			if(typeof obj =="string"){
				name = obj;
				if(name == tagName){
					flag = true;
					tagObj = {name:name};
					return false;
				}
			}else{
				name = obj.name;
				if(name == tagName){
					flag = true;
					tagObj=obj;
					return false;
				}
			}
		});
		return tagObj;
	}
	/*
	 * 计算元素大小
	 * */
	function initSize(jqObj,myobj,opts){
		var width = jqObj.width();
		var height = jqObj.height();
		autoSet('outerWidth',width);
		autoSet('outerHeight',height);
		function autoSet(key,size){
			try{
				var num = parseSize(size,opts[key]);
				if(num) myobj[key](num);
			}catch(e){$ui.print(e)}
		}
	}
	
	/*
     * 解析不同大小格式
     * 	参数:
     *  parentSize 数字：如100，280
     *  size 格式:0.125,45%
     *  返回值：整形或null
     * */
    function parseSize(parentSize,size){
		if(false == $.isNumeric(parentSize) ||
			size == undefined) return null;
		var num = parentSize;
		if($.isNumeric(size)){
			num = parseFloat(size);
			if(num > 0 && num < 1)
				num = parseInt(num * parentSize,10);
		}else if(typeof size == 'string' 
				&& size.indexOf('%') != -1){
			num = parseInt(size,10) * parentSize;
			num = parseInt(num / 100,10);
		}
		return num;
	}
	//绑定元素事件
	function bindEvent(jqOpts,jqObj){
		if(!jqObj) return;
		$.each(eventObj,function(i,e){
			if(jqOpts[e.key]){
				jqObj[e.val](jqOpts[e.key]);
			}
		});
	}
	//解析元素属性
	function parseAtrr(UIData,cls){
		var result = " ";
		//遍历所有属性
		$.each(htmlAttrs,function(i,attr){
			var obj = attr;
			if(typeof attr == 'string'){
				obj = {name : attr,alias : attr}
			}
			//判断是否定义
			var val = UIData[obj.alias];
			if(val!=undefined){
				if(obj.check){
					if(obj.check(val)){
						result += ' ' + obj.name + '="' + val + '" ';
					}
				}else result += ' ' + obj.name + '="' + val + '" ';
			}
		});
		result += parseStyle(UIData);
		return result;
	}
	/*解析大小
	 * 参数:size 可能取值:
	 * '18%'  => width:18%
	 * 25     => width:25px;
	 * 26.3   => width:26px;
	 * 0.123  => width:12.3%
	 * */
	function parseStyle(UIData){
		function styleAttr(name,tagName){//解析样式中属性
			var str = "",num = null;
			var value = UIData[name];
			tagName = tagName || name;
			if(value == undefined) return str;
			if(typeof value == 'string' && value.indexOf('%') != -1){//百分比
				str += tagName + ":"+value+";";
			}else if($.isNumeric(value)){
				num = parseFloat(value);
				if(num == 0 || num >= 1) str = tagName + ":" + value+"px;";
				else str = tagName + ":" +(num * 100) + '%;';
			}
			return str;
		}
		var style = "";//解析样式
		var width = UIData.width,height = UIData.height;
		style += styleAttr("width");
		style += styleAttr("height");
		style += styleAttr("top");
		style += styleAttr("right");
		style += styleAttr("bottom");
		style += styleAttr("left");
		style += styleAttr("minHeight","min-height");
		style += styleAttr("minWidth","min-width");

		style += styleAttr("marginTop","margin-top");
		style += styleAttr("marginLeft","margin-left");
		style += styleAttr("marginRight","margin-right");
		style += styleAttr("marginBottom","margin-bottom");
		
		style += styleAttr("paddingTop","padding-top");
		style += styleAttr("paddingLeft","padding-left");
		style += styleAttr("paddingRight","padding-right");
		style += styleAttr("paddingBottom","padding-bottom");
		
		//加入隐藏功能
		if(UIData.hidden) style += "display:none;";
		if(typeof UIData.cssStyle == 'string'){
			style += UIData.cssStyle;
		}
		return style==""?style:(' style="' + style + '" ');
	}
});
