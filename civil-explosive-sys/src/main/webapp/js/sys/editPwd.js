

define(function(require,exports,module){
	
	var txtWidth = "90%";
	//创建表单
	function createForm(){
		var self = this;
		return {
			eName:"formEx",
			url:'sys/savepassword.do',
			alertFlag:false,
			onBeforeSave:function(data){
				if(data.mm2	!= data.mm3){
					$ui.alert('两次输入新密码不同,请重新输入 !');
					return false;
				}
				return true;
			},
			onSave:function(retJson){
				if(retJson.result){//保存成功
					$ui.show('密码修改成功,请重新登录 !',function(){//
						self.myWin.window('close');
						 $ui.postEx("logout.do",null,function(){
							 self.jqDom.loadJsx("sys/login","js/sys/login");
	            		 });
					});
				}else $ui.alert(retJson.info);
			},
			elements:{
				eName:'layoutTable',
				cssStyle:"width:98%;margin-top:8px;",
				cols:2,rows:3,
				trHeight:[30],
				tdWidth:[80,-1],
				cells:[
				     ['输入旧密码',{eName:'textbox',type:'password',name:'mm1',width:txtWidth,required:true}],
				     ['输入新密码',{eName:'textbox',name:'mm2',type:'password',width:txtWidth,required:true}],
				     ['确认新密码',{eName:'textbox',name:'mm3',type:'password',width:txtWidth,required:true}],
				]
			}
		}
	}
	
	//入口
	function Main(jqObj,loadParams){
		var self = this;
		self.jqDom = jqObj;
		var formUI = createForm.call(self);
		self.myWin = $ui.createWin({
			title:'修改密码',
			width:300,
	    	height:200,
	    	bodyUI:{
	    		eName:"layoutExt",
	    		fit:true,
	    		elements:[{
	    			region:"center",
	    			elements:formUI
	    		},{
	    			region:"south",
	    			height:50,
	    			cssClass:"dialog_button_layout",
	    			elements:[{
	    				eName:'linkbutton',
	    				text:'保存',
	    				width:60,
	    	        	onClick:function(){
	    	        		myform.formEx("submit");//表单提交
	    	        	}
	    	        },{
	    	        	eName:'span',
	    	        	cssStyle:'margin:6px;'
	    	        },{
	    	        	eName:'linkbutton',
	    				text:'取消',
	    				width:60,
	    	        	onClick:function(){
	    	        		self.myWin.window("close");
	    	        	}
	    	        }]
	    		}]
	    	}
		});
	    var myform = self.myWin.findJq("formEx");
	}

	module.exports = Main;
    
});
