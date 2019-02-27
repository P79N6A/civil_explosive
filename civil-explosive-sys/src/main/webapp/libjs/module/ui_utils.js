

/*
 * 前端公共类
 * */
define("garenjs/utils",function(require,exports,module){

	window.$ui = {//顶级对象
		debug:function(){
			return 13 + 14;
		},
		garenModules:{'###':{}},
		//日志控制台输出,可多个参数
		print : function(){
	        window.console &&
        	$.each(arguments,function(i,v){
    			console.log(v);
    		});
	    },
	    alert:$.noop,show:$.noop,progress:$.noop,$GData:{},
	    data:function(key,params){
	    	if(params){
	    		$("body").data(key,params);
	    	}else{
	    		return $("body").data(key);
	    	}
	    },
	    setGdata:function(name,data){//设置全局变量
	    	if(typeof name != 'string') return;
	    	$ui.$GData[name] = data;
	    },
	    getGdata:function(name){//设置全局变量
	    	if(typeof name != 'string') return null;
	    	return $ui.$GData[name];
	    },
	    //验证结果，isNotPop : true不显示对话框,默认false
		checkret:function(retjson,fn,isNotPop){
			isNotPop = isNotPop || false;
			fn = fn || $.noop;
			var ret = null,alertFn = $.noop;
			try{
				if(retjson['id'] == 0){
					ret = true;
					alertFn = $ui.show;
				}else{
					ret = false;
					alertFn = $ui.alert;
				}
				if(isNotPop) fn(ret);//不弹窗
				else {
					alertFn && alertFn(retjson.info,function(){fn(ret);});
				}
			}catch(err){}
			return ret;
		},
		loadJs:function(jsUrl,params){
			$('<div></div>').loadJs(jsUrl,params);
		},
	    /*
		 * 同步加载数据
		 * 成功返回完整数据
		 * 错误返回null
		 */
		loadEx:function(url,params){
			return $ui.postEx(url,params,null,null,false);
			//return data.result?data:null;
		},
		postJson:function (url,params,fn){//表单提交json数据
			params = params || {};
			if(typeof params != "string") 
				params = $.toJSON(params);
			$ui.postEx({
				url:url,
				contentType:"application/json",
				params:params,
				onLoad:fn
			});
		},
		/*
		 * 多个参数:
		 * 参数:
		 * url请求地址,
		 * params 提交数据,
		 * onLoad 返回结果,函数
		 * 一个对象参数:
		 * {
		 *   url:请求地址,
		 *   params :提交数据,
		 *   onLoad :返回结果,函数
		 *   dataType:'json',文档类型,默认json
		 *   progressBar:'保存中...' //进度条显示的文本
		 *   async true 异步 false 同步
		 * }
		 */
		 postEx:function(url,params,result,timeout,async){
			if(url == null) return;
			var isJson = true;//是否返回结果为json
			var postParams = null;
			var dataResult = {};//最终返回结果
			timeout = timeout || 30 * 1000;//超时默认30秒
			async = async == undefined?true:async;//默认值为true
			//参数初始化
			if(typeof url == "string") {
				//params为函数,不是对象,则实为result
				if($.isFunction(params)){
					result = params;
					params = null;
				}
				//参数初始化
				postParams = {
					url:url,
					data:params
				};
			}else{
				postParams = url;
				postParams.data = postParams.params;
				result = postParams.onLoad;
			}
			//判断地址是否正确
			if(typeof postParams.url != "string") {
				$ui.alert("地址错误");
				return;
			}
			postParams = $.extend({
				dataType:"json",
				async:async,//默认异步
				method:'post',
				timeout:timeout,//默认请求时间
				success:function(data, textStatus){
					if(data == null) data = {id:-511,info:'null'}
					if(isJson){//若为json,加入验证结果
						if($.isArray(data)){//若为数组
							data = {id:0,obj:data,result:true}
						}else if($ui.checkret(data,null,true)){
							data.result = true;
						}else
							data.result = false;
					}else{
						var retData = data;
						data = {id:0,obj:retData,result:true}
					}
					//统一数据
					data.data = data.data || data.obj || data.rows || data.record;
					onLoad(data);
				},
				error:function(response, textStatus){//
					var statusText = response.statusText;
					var status = response.status;
					if(statusText == "timeout") status = 454;
					onLoad({
						id:status,//404,403,500
						info:statusText,
						result:false,
						obj:response.responseText
					});
				},
				beforeSend:function(){
					if(postParams.progressBar){
						$ui.progress({text:postParams.progressBar});
					}
				}
			},postParams);
			if(postParams.dataType.toLowerCase() != "json") isJson =false;
			//回复结果
			function onLoad(retJson){
				//session超时时，刷新页面
				if(-404 == retJson.id){
					//window.location.reload();
					//return;
				}
				if(postParams.progressBar){//关闭进度条
					$ui.messager.progress('close');
				}
				if(result){//回调函数
					retJson.obj = retJson.obj || retJson.rows;
					result(retJson);
				}
				if(postParams.async == false){//同步直接返回结果
					dataResult = retJson;
				}
			}
			$.ajax(postParams);//提交请求
			return dataResult;
		}
	};
	module.exports = window.$ui;
});
