/*
 * 注册默认组件
 * 扩展工具函数
 * */
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],function(utils,garenUI,easyUI){
	easyUI.regFns([
	       {name:'filebox',tag:'input'},{name:'tree',tag:'ul'},
	       {name:'numberbox',tag:'input'},{name:'combo',tag:'input'},
	       {name:'passwordbox',tag:'input'},{name:'timespinner',tag:'input'},
	       {name:'switchbutton',tag:'input'},{name:'combotree',tag:'input'},
	       {name:'combogrid',tag:'input'},
	       {name:'linkbutton',tag:'a'},{name:'datetimespinner',tag:'input'},
	       {name:'numberspinner',tag:'input'},
	      {name:'searchbox',tag:'input'}]);
	var utils = {
		parseNum:function (num){//格式化数字
			num = parseInt(num,10);
			return num < 10?"0"+num:num;
		},
		/*
		 * 参数：
		 *  dateNum 时间毫秒数
		 * 返回值:yyyy-MM-dd格式
		 * */
		date2Str:function(dateNum){
			var date = new Date(dateNum);//日期
			var fn2Num = $ui.parseNum;
			return isNaN(date) ? "" : date.getFullYear() 
					+ '-' + fn2Num(date.getMonth() + 1) 
					+ '-' + fn2Num(date.getDate());
		},
		/*
		 * 参数：
		 *  dateNum 时间毫秒数
		 * 返回值:yyyy-MM-dd HH:mm格式
		 * */
		time2Str:function (dateNum){//解析时间
			var date = new Date(dateNum);//时间
			var fn2Num = $ui.parseNum;
			return isNaN(date) ? "" : date.getFullYear() 
				  + '-' + fn2Num(date.getMonth() + 1)
				  + '-' + fn2Num(date.getDate())  
				  + " " + fn2Num(date.getHours()) 
				  + ":" + fn2Num(date.getMinutes());
		},
		/*计时函数:
		 *  $ui.timeStart("key"); //开始
		 *  $ui.time("key","xxx");
		 * */
		timer:{},
		timeStart:function(key){
			$ui.timer[key] =  new Date().getTime();
		},
		time:function(key,pre){
			var start = $ui.timer[key];
			if(start){
				pre = pre ? pre + " : " : "";
				$ui.print(pre + (new Date().getTime() 
						- start) + "ms");
			}
		},
		timeEnd:function(key,pre){
			var end = new Date().getTime();
			var start = $ui.timer[key];
			if(start == undefined) return;
			if(pre == undefined) pre = "";
			else pre += ' : ';
			$ui.print(pre + (end - start) + "ms");
			delete $ui.timer[key];
		}
	}
	var uiUtils = {
		createWin:function(param,bodyUI){//创建对话框
	    	param.bodyUI = param.bodyUI || bodyUI;
	    	param.eName = "window";
	    	return easyUI.createUI($('body'),param);
	    },
	    /*
	     * 进度条
	     * */
		progress:function(txt){
			if('close' == txt) $.messager.progress("close");
			else $.messager.progress({text:txt});
		},
	    /*
		 * 弹出确认窗口
		 * */
	    confirm:function(msg,backFun){//弹出信息
	    	$.messager.confirm("系统提示信息",msg,backFun);
	    },
	    /*
	     * 弹出提示对话框
	     */
	    alert:function(msg,fn,width){//弹出信息
	    	$.messager.alert({
	    		title:"系统提示信息",
	    		width:width || 380,
	    		msg:msg,
	    		icon:"warning",
	    		fn:fn
	    	});
	    },
	    /*
	     * 提示信息弹窗显示
	     * **/
	    show:function(msg,fn,width){
	    	$.messager.alert({title:"系统提示信息",width:width 
	    		|| 380,msg:msg,icon:"info",fn:fn});
	    },
	    /*
	     * 提示信息弹窗显示
	     * **/
	    error:function(msg,fn,width){
	    	$.messager.alert({title:"系统提示信息",width:width 
	    		|| 380,msg:msg,icon:"error",fn:fn});
	    },
	    showMask:function(flag){
			var mask = null;
			var docobj = $(document);
			if(flag){
				mask = $('<div class="window-mask"></div>');
				$('body').append(mask);
				mask.width(docobj.width());
				mask.height(docobj.height());
			}else{
				$('.window-mask').remove();
			}
		},
		/*
		 * 列表转为树结构
		 * nodes数组
		 * id主键
		 * pid父节点
		 * text文本
		 * */
		list2Tree : function (nodes,id,pid,text){
			var rootNodes = [];
			function findNode(node){//查找子节点
				$.each(nodes,function(j,node2){
					if(node2 == node) return;
					if(node2[id] == node[pid]){
						node2.children = node2.children || [];
						node2.children.push(node);
						node.noRoot = true;
					}
				});
			}
			$.each(nodes,function(i,node){
				node.id = node[id];
				node.text = node[text];
				findNode(node);
			});
			$.each(nodes,function(i,node){
				if(!node.noRoot) {
					node.checked = false;
					rootNodes.push(node);
				}
			});
			return rootNodes;
		},
		/*
		 * 设置叶子节点状态
		 * */
		treeChecked:function(nodes,orgIds){
			$.each(orgIds,function(i,orgId){
				$.each(nodes,function(j,node){
					if(node.children && node.children.length > 0) 
						$ui.treeChecked(node.children,orgIds);
					else if(node.id == orgId)
						node.checked = true;
				});	
			});
		},
		/*
		 * 元素扩展
		 * UIs为原数组
		 * exUIs 要扩展的
		 */
		extUIs:function (UIs,extUIs){
			//扩展为undefined或uis不为数组,则使用默认
			if($.isArray(UIs) == false 
					|| extUIs == undefined)
				return UIs;
			if($.isArray(extUIs) == false){//若不为数组
				UIs.push(extUIs);
				return UIs;
			}
			if(extUIs.length == 0) return UIs;
			//若扩展第一个元素为Null,则返回扩展uis,替换原uis
			if(extUIs[0] == null){
				return extUIs.slice(1);
			}
			return UIs.concat(extUIs);//追加
		},
		 //判断请求地址是否为js
	    isJsUrl : function (url){
	    	return (/\.js$/g.test(url)) ? true : false;
		},
		/*
		 * 参数：
		 *  params:{
		 *  	formDialog:{},//对话框参数
		 *  	onLayoutCreate:function(regionUIs,layoutUI,dialogUI){},//布局定义回调
		 *  	formBtns:[]//默认按钮
		 *  }
		 * */
		createFormDialog:function(params,formUI){
			var jqWin = null;
			var formBtns = createFormBtns();
			var regions = [{
				region:"center",
				cssStyle:"padding:6px;",
				elements:formUI
			},{
				region:"south",
				height:50,
				cssClass:"dialog_button_layout",
				elements:formBtns
			}];
			var layoutUI = {
				eName:'layoutExt',
				elements:regions
			};
			var dialogUI = $.extend({
				title:'对话框',
				width:400,
				height:300,
				bodyUI:layoutUI
			},params.formDialog || {});
			//回调函数
			params.onLayoutCreate && params.onLayoutCreate.call(self,regions,layoutUI,dialogUI);
			jqWin = $ui.createWin(dialogUI);
			return jqWin;
			//创建默认工具栏
			function createFormBtns(){
				//默认按钮
				var formBtns = [{
					eName:'linkbutton',
					text:'保存',
					plain:false,
					width:60,
					mRight:8,
					onClick:function() {
						var jqForm = null;
						if(formUI.id){
							jqForm = jqWin.find("#" + formUI.id);
						}else{
							jqForm = jqWin.findJq("formEx");
						}
						if(jqForm.length < 1){
							$ui.print("表单获取错误!");
						}else if(jqForm.length == 1){
							jqForm.formEx("submit");
						}else{
							$ui.print("多个表单被发现! ");
						}
					}
				},{
					eName:'linkbutton',
					text:'取消',
					plain:false,
					width:60,
					mLeft:8,
					onClick:function() {
						jqWin.window("close");
					}
				}];
				return $.extend(true,[],formBtns,params.formBtns||[]);
			}
		}
	}
	//工具函数扩展到$上
	$.extend(window.$ui,utils,uiUtils);
	//检测浏览器版本
	var browser = {msie:false,msie7:false,msie8:false,msie9:false,
			msie10:false,msie11:false,chrome:false,firefox:false}
	var ver = null,u = window.navigator.userAgent.toLocaleLowerCase();
	if(/(msie) ([\d.]+)/.test(u)) {
		ver = parseInt(/(msie) ([\d.]+)/.exec(u)[2]);
		browser.msie = true;
		if(ver < 8) browser.msie7 = true;
		else if(ver == 8) browser.msie8 = true;
		else if(ver == 9) browser.msie9 = true;
		else if(ver == 10) browser.msie10 = true;
	}else if(/(chrome)\/([\d.]+)/.test(u)) browser.chrome = true;
	else if(/(trident)\/([\d.]+)/.test(u)) {
		browser.msie11 = true;
		browser.msie = true;
	}else if(/(firefox)\/([\d.]+)/) browser.firefox = true;
	else if(/(safari)\/([\d.]+)/.test(u)) browser.safari = true;
	else if(/(opera)\/([\d.]+)/) browser.opera = true;
	$ui.browser = browser;
});

