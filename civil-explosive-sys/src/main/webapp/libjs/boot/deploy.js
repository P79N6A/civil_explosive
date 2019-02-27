

define(function(require,exports,module){

	var txtWidth = "90%";
	
	//创建表格内容
	function createCells(){
		var self = this;
		return [['app:',{
			eName:'filebox',
			accept:"app/*.war",
			name:'warfile',
		    onChange:function(nv,ov){
	    		 if(nv == "") return;
	    		 var regx = /^(.+)\.(.+)$/.exec(nv);
	    		 var appName = RegExp.$1;
	    		 var extName = RegExp.$2;
	    		 if(extName != "war"){
	    			 $ui.alert("文件格式不正确！");
	    			 $(this).textbox('clear');
	    			 return;
	    		 }
	    		 $ui.print(RegExp.$1,self.myform.findJq("appName"));
	    		 //self.myform.findJq("appName").textbox('setValue',appName);
	    		 //self.myform.findJq("servName").textbox('setValue',"tomcat-" + appName);
		    },
		    buttonText:'选择war',
		    width:txtWidth,
		    required:true
		    }],['名称:',{eName:'textbox',name:'appName',value:"web",
		    	width:txtWidth,required:true}],
		    ['服务名称:',{
		    	eName:'textbox',
		    	name:'servName',
		    	value:"tomcat-explosive",
		    	//disabled:true,//($ui.config.os == "Linux")?true:false,
		    	width:txtWidth
		   }]];
	}
	//创建表单
	function createForm(){
		var self = this;
		var disabled = false;
		var cellsUI = createCells.call(self);
		return {
			eName:"formEx",
			url:'deploy.do',
			method:"post",//上传表单时，必须设置此值
			enctype:"multipart/form-data",//上传附件时，必须设置此值
			onSave:function(retJson){
				if(retJson.result){//保存成功
					window.setInterval(checkStatus,3000);
				}
			},
			elements:{
				eName:'layoutTable',
				cssStyle:"width:98%;margin-top:8px;",
				cols:2,rows:3,
				trHeight:[30],
				tdWidth:[80,-1],
				cells:cellsUI
			}
		}
	}
	
	//入口
	function main(){
		var self = this;
		var formUI = createForm.call(self);
		self.myWin = $ui.createWin({
			title:'发布新版本',
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
	    			height:60,
	    			cssClass:"dialog_button_layout",
	    			elements:[{
	    				eName:'linkbutton',
	    				text:'保存',
	    				width:60,
	    	        	onClick:function(){
	    	        		$ui.progress("发布进行中...");
	    	        		self.myform.formEx("submit");//表单提交
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
	    self.myform = self.myWin.findJq("formEx");
	}

	/*
	 * 检测状态
	 * */
	function checkStatus(){
		$ui.postEx("logout.do",function(retJson){
			if(retJson.result) {
				window.location = "./";
			}
			//else window.setTimeout(checkStatus,1000);
		});
	}
	
	module.exports = main;
});
