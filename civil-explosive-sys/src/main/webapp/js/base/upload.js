

define(function(require,exports,module){

	function addImg(uploadJson){
		var self = this;
		var imgUI = {
			eName:'div',
			cssClass:'upload_panel',
			addMode:'before',
			data:uploadJson,
			elements:[{
				eName:'img',
				cssClass:'browse_icon',
				src:'base/uploadresource/loadimg.do?id=' + uploadJson.id,
				alt:'双击预览',
				onDblclick:function(){
					var imgUrl = $(this).attr("src");
					$ui.createWin({
						title:'图片预览',
						width:1200,
						height:700,
						cssStyle:'background-color:#000;text-align:center;',
						maximizable:true,
						bodyUI:{eName:'img',src:imgUrl}
					});
				}
			},{
				eName:'a',
				cssClass:'remove_icon',
				onClick:function(){
					var jqPanel = $(this).parent('.upload_panel');
					var uploadJson = jqPanel.findOpts().data;
					if($ui.confirm("确定要删除此文件?",function(r){
						if(r){
							$ui.postEx("base/uploadresource/delete.do",{id:uploadJson.id},function(retJson){
								if(retJson.result) jqPanel.remove();
							});
						}
					}));
				}
			}]	
		};
		self.jqAdddiv.createUI(imgUI);
	}
	
	function Main(jqObj,loadParams){
		if(!loadParams.hostId || !loadParams.hostType){
			$ui.alert("参数不正确!");
			return;
		}
		var self = this;
		var mainUI = {
			eName:'div',
			cssClass:'upload_group',
			elements:[{
				eName:'div',
				id:'upload_panel_add',
				cssClass:'upload_panel',
				elements:{
					eName:"formEx",
					url:'base/uploadresource/upload.do',
					method:"post",//上传表单时，必须设置此值
					enctype:"multipart/form-data",//上传附件时，必须设置此值
					progressBar:'资源上传中...',
					onSave:function(retJson){
						$ui.print(retJson);
						if(retJson.result) addImg.call(self,retJson.data);
					},
					elements:[{
						eName:'input',
						type:'hidden',
						name:'name',
						value:'赞扬'
					},{
						eName:'label',
						'for':'upload'
					},{
						eName:'input',
						type:'file',
						name:'imgfile',
						cssStyle:'display:none',
						id:"upload",
						onChange:function(){
							newValue = $(this).val();
							if(!newValue) return;
				    		 var regx = /^(.+)\.(.+)$/.exec(newValue);
				    		 var appName = RegExp.$1;
				    		 var extName = RegExp.$2;
				    		 extName = extName.toLowerCase();
				    		 $ui.print(newValue + "," + extName);
				    		 if(extName != "jpg" 
				    			 && extName != "png"
				    				 && extName != "jpeg"){
				    			 $ui.alert("文件格式不正确！");
				    			 return;
				    		 }
				    		 //提交
				    		 self.jqAdddiv.findJq('formEx').formEx('submit',loadParams);
						}
					}]
				}
			}]
		};
		self.myWin = $ui.createWin({
			title:'附件管理',
			width:900,
			height:480,
			bodyUI:mainUI
		});
		self.jqAdddiv = self.myWin.find('#upload_panel_add');
		//加载图片
		$ui.postEx("base/uploadresource/jsons.do",loadParams,function(retJson){
			var uploadJsons = retJson.data;
			//遍历
			$.each(uploadJsons,function(i,uploadJson){
				addImg.call(self,uploadJson);
			});
		});
	}
	module.exports = Main;
});
