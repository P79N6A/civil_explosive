
/*
 * 系统发布
 * */
define(function(require,exports,module){
	
	
	function createWarForm(){
		var self = this;
		return {
			eName:"linkbutton",
			text:"war部署",
			onClick:function(){
				$ui.loadJs("libjs/boot/deploy.js");
			}
		};
	}
	
	function createVideoExeForm(){
		var self = this;
		return {
			eName:"formEx",
			url:'sys/videoexe/updateFile.do',
			method:"post",//上传表单时，必须设置此值
			enctype:"multipart/form-data",//上传附件时，必须设置此值
			progressBar:"发布进行中...",
			onSave:function(retJson){
				$ui.progress("close");
			},
			elements:{
				eName:'layoutTable',
				cssStyle:"width:98%;margin-top:8px;",
				cols:2,rows:1,
				trHeight:[30],
				tdWidth:[80,-1],
				cells:[['videoExe:',{
					eName:'filebox',
					accept:"app/*.exe",
					name:'exefile',
				    onChange:function(nv,ov){
			    		 if(nv == "") return;
			    		 var regx = /^(.+)\.(.+)$/.exec(nv);
			    		 var appName = RegExp.$1;
			    		 var extName = RegExp.$2;
			    		 if(extName != "exe"){
			    			 $ui.alert("文件格式不正确！");
			    			 $(this).textbox('clear');
			    			 return;
			    		 }
				    },
				    buttonText:'选择Exe',
				    width:"90%",
				    required:true
				   }
				]]
			}
		};
	}
	//入口
	function Main(jqGroup,loadParams){
		var self = this;
		var videoExeOpts = createVideoExeForm.call(self);
		var warOpts = createWarForm.call(self);
		var opts = [{
			eName:"fieldset",
			width:300,
			height:200,
			elements:[{
				eName:"legend",
				text:'Web更新部署'
			},{eName:"div",elements:warOpts}]
		},{
			eName:"fieldset",
			width:300,
			height:130,
			elements:[{
				eName:"legend",
				text:"视频流程序更新部署"
			},{eName:"formLayout",width:300,height:100,formUI:videoExeOpts}]
		}];
		var ui = {
			eName:"div",
			css:{margin:20},
			elements:opts
		}
		jqGroup.createUI(ui);
	}
	return Main;
});
