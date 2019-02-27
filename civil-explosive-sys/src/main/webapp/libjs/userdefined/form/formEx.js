
/*
 * 自定义组件formEx
 * 扩展form组件功能
 * 实现文件上传
 * */
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],function(utils,garenUI,easyUI){
	
	easyUI.regFn({//注册组件
	   name:"formEx",//组件名称
	   tag:'form'//html组件
	});
	//生成组件
	function init(target){
		var myform = $(target);
		var opts = $.data(target, "formEx").options;
		$(target).createUI(opts.elements);
		//初始化表单数据数据
		setTimeout(function(){//异步执行
			/*
			 * 加载表单数据
			 * 表单数据来源
			 * formData
			 * formUrl 数据查询地址
			 * formParams 数据查询参数
			 */
			if(opts.formData){
				myform.form('load',opts.formData);
				if(opts.onLoad){
					opts.onLoad.call(myform,opts.formData);
				}
			}else{
				if(opts.formUrl){
					opts.loadMsg && $ui.progress(opts.loadMsg);
					$ui.postEx(opts.formUrl,opts.formParams,function(retJson){
						$ui.progress("close");
						if(retJson.result){
							opts.formData = retJson.obj;
							myform.form('load',retJson.obj);
						}else{
							$ui.print('查询数据错误:',retJson);
						}
						if(opts.onLoad){
							opts.onLoad.call(myform,opts.formData);
						}
					});
				}else{
					if(opts.onLoad){
						opts.onLoad.call(myform);
					}
				}
			}
	    }, 0);
	}
	//创建方法
	function submit(target,params){//获取表格数据
		var myform = $(target);
		var opts = $.data(target, "formEx").options;
		params = params || {};
		var progressBar = opts.progressBar;
		var alertFlag = opts.alertFlag;
		if(alertFlag == undefined) alertFlag = true;//默认值
		if(opts.url == undefined) {
			$ui.print("地址为空");
			return false;
		}
		var enctype  = opts.enctype ;
		if(enctype == "multipart/form-data"){//上传附件
			myform.form('submit',{
    			url:opts.url,
    			onSubmit: function(formParams){
    				//表单验证
    				if(!$(this).form('validate')) return false;
    				//复制参数
    				$.extend(formParams,params);
    				if(false == opts.onBeforeSave.call(target,params)) return false;
    				//开启进度条
    				if(progressBar) $ui.progress(progressBar);
    				return true;
    			},
    			success: function(data){
    				if(progressBar) $ui.progress('close');//关闭进度条
    				var retJson = null;
    				try{
    					retJson = $.parseJSON(data);
    				}catch(e){
    					retJson = {id:-1,info:"发生异常",result:false};
    				}
    				retJson.result = $ui.checkret(retJson,null,alertFlag);
    				retJson.data = retJson.obj || retJson.rows || [];
    				if(opts.onSave) opts.onSave.call(target,retJson);
    			}
			});
		}else{//正常表单
			if(!myform.form("form2Json",params)) return false;
			if(false == opts.onBeforeSave.call(target,params)) return false;
			if(progressBar) $ui.progress(progressBar);
			$ui.postEx(opts.url,params,function(retJson){
				if(progressBar) $ui.progress('close');//关闭进度条
				if(opts.onSave) opts.onSave.call(target,retJson);
				if(alertFlag){
					if(retJson.result) $ui.show(retJson.info);
					else $ui.alert(retJson.info);
				}
			});
		}
		return true;
	}
	//构造函数
	$.fn.formEx = function(options, param){
		if (typeof options == 'string'){//调用方法
			var method = $.fn.formEx.methods[options];
			if (method){
				return method(this, param);
			}else{
				return this.form(options, param);
			}
		}
		options = options || {};
		return this.each(function(){
			var state = $.data(this, "formEx");
			if (state){
				$.extend(state.options, options);
				$(this).empty();//清空
			} else {
				state = $.data(this, "formEx", {
					options: $.extend({}, $.fn.formEx.defaults, 
							$.fn.formEx.parseOptions(this), options)
				});
			}
			//绘制子元素
			init(this);
		});
	};
	//方法区域
	$.fn.formEx.methods = {
		options: function(jq){
			return $.data(jq[0], "formEx").options;
		},
		submit:function(jq,params){
			return submit(jq[0],params);
		},
		load:function(jq,params){
			return jq.each(function(){
				var jqform = $(this);
				jqform.form('load',params);
				var opts = jqform.formEx('options');
				$.each(opts.fields,function(i,f){
					var vals = params[f] || '';
					vals = vals.split(',');
					var boxes = jqform.find("input[name="+f+"]");
					$.each(vals,function(i,v){
						boxes.each(function(j,box){
							if(this.value == v){
								this.checked = true;
								return false;
							}
						});
					});
				});
			});
		},
	};
	$.fn.formEx.parseOptions = function(target){
		return $.extend({}, $.parser.parseOptions(target, []));
	};
	$.fn.formEx.defaults = {
		url:null,
		formUrl:null,
		formData:null,
		enctype:"",
		alertFlag:true,
		progressBar:null,
		onSave:function(){},
		onLoad:function(){},
		onBeforeSave:function(){return true}
	};
});

