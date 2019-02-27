
/*
 * 首页模块
 * */
define(function(require,exports,module){

	var TodoTask = require("js/base/TodoTask");
	
	var leftWidth = 220,foldWidth = 26;//折叠后宽度 
	
	//创建头文件
	function createHeaders(){
		var self = this;
		self.jqDom.find("#header_logout_btn").click(function(){
			$ui.postEx("logout.do",function(){
				self.jqDom.loadJsx("sys/login","js/sys/login");
			});
		});
		//编辑密码
		self.jqDom.find("#header_edit_pwd").click(function(){
			self.jqDom.loadJs("js/sys/editPwd",null,true);
		});
	}
	
	//创建左侧菜单
	function createMenuPanel(sysData){
		var self = this;
		var userJson = sysData.user;
		var menuList = sysData.menuList;
		var flag = false;
		var menuUI = $.map(menuList,function(menu1,i){
			if(menu1.parentId != 0) return null;//不是根目录
			var subMenus = $.map(menuList,//生成子菜单
					function(menu2,j){
						if(menu2.parentId != menu1.menuId) return null;
						return {
							eName:'a',
							cssClass:"menu_text",
							data:menu2,
							url:menu2.menuUrl,
			    			onClick:menuClick,
			    			text:menu2.menuName
				    	};
				    }
			);
			var menu = {
				eName:'div',
				cssClass:'menu_panel',
				elements:[{
					eName:'div',
					cssClass:'menu_header' + (flag?" menu_header_selected":""),
					onClick:headerClick,
					elements:[{
						eName:'span',
						cssClass:'menu_title',
						text:menu1.menuName				
					},{
						eName:'div',
						cssClass:'menu_icon_group',
						elements:{
							eName:'a',
							cssClass:"accordion-collapse " + (flag?"":"accordion-expand")
					    }
					}]
				},{
					eName:'div',
					cssClass:'menu_body',
					cssStyle:"display:" + (flag?"block;":"none;"),
					elements:subMenus
				}]
			};
			if(flag) flag = false;
			return menu;
		});
		var jqMenuPanel = self.jqDom.find('.div_layout_left_on');
		var jqMenuBody = jqMenuPanel.find(".panel_menu_body");
		jqMenuPanel.find("#app_user_name").text(userJson.realName);
		jqMenuBody.createUI(menuUI);
		
		/*
		 * 内部函数
		 * */
		//标题点击事件
		function headerClick(){
			var jqSelected = jqMenuBody.find('.menu_header_selected');
			//关闭上一个
			if(jqSelected[0] != $(this).get(0)){
				jqSelected.next(".menu_body").toggle();
				jqSelected.removeClass("menu_header_selected");
				jqSelected.find('.accordion-collapse').toggleClass("accordion-expand");
			}
			$(this).toggleClass('menu_header_selected');
			$(this).next(".menu_body").toggle();
			$(this).find('.accordion-collapse').toggleClass("accordion-expand");
		}
		
		//菜单点击事件
		function menuClick(){
			var jqSelected = jqMenuBody.find('.menu_text_selected');
			var jqObj = $(this);
			
			//关闭上一个
			if(jqSelected[0] != $(this).get(0)){
				jqSelected.removeClass("menu_text_selected");
			}
			jqObj.toggleClass('menu_text_selected');
			var menuData = jqObj.findOpts().data;
			var name = $.trim(jqObj.text());
			if(1 == menuData.menuType)
				createTab(self.jqTabs,{'title':menuData.menuName,'url':menuData.menuUrl});
			else if(3 == menuData.menuType){
				window.open("loadui.do?name=" + menuData.menuUrl);
				/*$ui.createWin({
					title:menuData.menuName,
					width:menuData.menuWidth || 800,
					height:menuData.menuHeight || 500,
					url:menuData.menuUrl,
					modal:false
				});*/
			}else if(4 == menuData.menuType){
				//var DeployObj = require("libjs/boot/deploy");
				//new DeployObj();
				$ui.loadJs("libjs/boot/deploy");
			}
		}
	}
	
	
	//创建tabs
	function createTab(tabObj,params){
		var url = params.url;
		if(url == false) return;
		if(!$ui.isJsUrl(url)){//
			params.href = url;
		}
		params = $.extend({
	   	    closable:true,
	   	    bodyCls:'dialog_panelbody',
	   	    method:"post",
	   	    onLoadError:function(){
	   	    	$ui.alert('404 !');
	   	    },tools:[{
	   	    	iconCls:'icon-mini-refresh',
	   	        handler:function(){
	   	        	var myTabs = tabObj;
	   	        	var pp = myTabs.tabs('getSelected');
	   	        	var ppopt = pp.panel('options');
	   	        	var litab = $(this).parent().parent();
	   	        	var title = litab.find('.tabs-title').text();
	   	        	//切换tab时，点击了刷新按钮，则不刷新tab
	   	        	if(title != ppopt.title) return;
	   	        	if($ui.isJsUrl(url)){
	   	        		pp.empty();
	   	        		pp.loadJs(url,params.queryParams);
		   			}else  pp.panel('refresh',ppopt.href);
	   	        }
	   	    }]
	   	},params);
		if (tabObj.tabs('exists', params.title)) {//存在选择
			tabObj.tabs('select', params.title);
		}else{//添加
			tabObj.tabs('add', params);
			if($ui.isJsUrl(url)){//加载js
				var pp = tabObj.tabs("getTab",params.title);
				pp.empty();
	        	var panelObj = pp.loadJs(url,params.queryParams);
	        	pp.data("uiObj",panelObj);
   			}
		}
	}
	
	//创建消息面板
	function createNoticePanel(title){
		var panelOpts = [{
			eName:"div",
			cls:"task_notice_panel",
			width:0.2,
			css:{background1:"blue"},
			elements:[{
				eName:'datalist',
	    		css:{height:"60%"},
	    		title:"待办任务",
	    		url:"base/",
	    		onBeforeLoad:function(){
	    			return false;
	    		}
			},{eName:"div",height:3},{
				eName:'datalist',
	    		css:{height:"40%"},
	    		title:"到期提醒",
	    		onBeforeLoad:function(){
	    			return false;
	    		}
			}]
		},{
			eName:"div",
			cls:"task_notice_panel",
			width:0.8,
			css:{background1:"red"},
			elements:[{
				eName:"div",
				elements:{
					eName:"img1",
					css:{width:"90%"},
					src:"images/echarts_pie.png"
				}
			},{
				eName1:"div",
				elements:[{
					eName:"img",
					css:{"margin-top":-100},
					width:500,height:300,
					src:"images/echarts_pie.png"
				},{
					eName:"img",
					width:100,height:300,
					src:"images/echarts_pie.png"
				}]
			}]
		}];
		return panelOpts;
	}
	
	//绑定事件
	function bindEvent(){
		var self = this;
		var tabsGroup = self.jqDom.find(".app_body > .panel_tabs_group");
		var openPanel = self.jqDom.find(".app_body > .div_layout_left_on");
		var closePanel = self.jqDom.find(".app_body > .div_layout_left_off");
		var leftArrow = self.jqDom.find(".panel_icon > .layout-button-left");
		var rightArrow = self.jqDom.find(".panel_banner_header > .layout-button-right");
		leftArrow.click(function(){
			openPanel.hide();
			closePanel.show();
			tabsGroup.css('margin-left',"30px");
			self.jqTabs.tabs("resize");
		});
		rightArrow.click(function(){
			closePanel.hide();
			openPanel.show();
			tabsGroup.css('margin-left',"263px");
			self.jqTabs.tabs("resize");
		});
		var noticeUIs = createNoticePanel.call(self);
		//创建tabs
		self.jqTabs = tabsGroup.createUI({
			eName:'tabs',
			fit:true,
			onBeforeClose:function(title,index){
				var jqTab = $(this).tabs("getTab",title);
				var panelObj = jqTab.data("uiObj");
				if(panelObj && panelObj.onQuit)
					panelObj.onQuit();
				return true;
			},
			elements:[{
    	    	title:'首页',
    	    	iconCls: 'icon_hamburg_home',
    	    	elements:[{
    	    		eName:"div",
    	    		id:"task_notice_panel",
    				cls:"notice_panel",
    				width:0.2
    	    	},{
    	    		eName:"div",
    				cls:"notice_panel",
    				width:0.8
    	    	}]
    	    }]
		});
		self.jqTabs.findJq("task_notice_panel").loadJs("js/base/TodoTask");
	}
	
	//入口
	function main(jqDom,loadParams) {
        var self = this;
        this.jqDom = jqDom;
        bindEvent.call(self);
        //header
        createHeaders.call(self);
        //获取数据
        $ui.postEx("sys/indexJson.do",function(dataJson){
        	if(dataJson.result){
        		//全局变量
        		$ui.setGdata("user",dataJson.data.user);
        		$ui.data("user",dataJson.data.user);
        		$ui.data("config",dataJson.data.config);
        		//创建菜单面板
        		createMenuPanel.call(self,dataJson.data);
        	}
        });
    }

    module.exports = main;
});
