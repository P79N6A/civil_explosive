

define(function(require,exports,module){

	function isImage(fileType){
		return (fileType=="png" 
			|| fileType == "jpg"
				|| fileType == "jpeg")?true:false;
	}
	/*
	 * 添加面板
	 * */
	function addPanel(data){
		var self = this;
		var jqForm = null;
		var forId = "upload_" + new Date().getTime();
		var uis = [{
			eName:'a',
			text:data.name,
			onClick:function(){
				var dataJson = $(this).parents('.upload_div_panel').findOpts().data;
				$ui.print(dataJson);
				var imgId = dataJson.id || 0;
				var imgUrl = 'base/uploadresource/loadimg.do?id=' + imgId;
				if(isImage(dataJson.fileType)){
					$ui.createWin({
						title:'图片预览',
						width:1200,
						height:700,
						bodyCls:'upload_preview',
						maximizable:true,
						bodyUI:{eName:'img',src:imgUrl}
					});
				}else{
					if(dataJson.fileType){
						window.open("base/uploadresource/down.do?id=" 
								+ imgId + "&fileType="+dataJson.fileType);
					}
				}
			}	
		},{eName:'label',text:'添加',cssClass:'add','for':forId},{
			eName:'label',
			cssClass:'remove',
			text:'删除',
			onClick:function(){
				var selfDom = $(this);
				$ui.confirm("确定要删除吗?",function(r){
					if(r){
						$ui.postEx("base/uploadresource/delete.do",{id:data.id},function(retJson){
							if(retJson.result) {
								selfDom.parents('.upload_div_panel').findOpts().data.id = null;
								selfDom.hide();
								jqForm.find('label.add').show();
								jqForm.find('a').removeClass("upload_ok");
								delete data.id;//清空
							}
						});
					};
				});
			}
		},{
			eName:'input',
			type:'file',
			name:'imgfile',
			cssStyle:'display:none',
			id:forId,
			onChange:function(){
				newValue = $(this).val();
				//支持的格式
				var extNames = {"jpg":1,"png":1,"jpeg":1
						,"doc":1,"xls":1,"docx":1,"xlsx":1,"pdf":1};
				if(!newValue) return;
	    		 var regx = /^(.+)\.(.+)$/.exec(newValue);
	    		 var appName = RegExp.$1;
	    		 var extName = RegExp.$2;
	    		 extName = extName.toLowerCase();
	    		 if(!extNames[extName]){
	    			 $ui.alert("文件格式不正确！");
	    			 return;
	    		 }
	    		 //提交
	    		 jqForm.formEx('submit',{hostGenre:data.hostGenre
	    			 		,hostId:self.params.hostId,extName:extName});
			}
		}];
		if(data.id){//浏览
			uis[0].cssClass = 'upload_ok';
			uis[1].cssStyle = "display:none";
		}else{//新增
			uis[2].cssStyle = "display:none";
		}
		//只读模式
		if(self.params.readonly){
			uis[1].cssStyle = "display:none";
			uis[2].cssStyle = "display:none";
		}
		var jqPanel = self.jqFieldset.createUI({
			eName:'div',
			cssClass:'upload_div_panel',
			data:data,
			elements:{
				eName:'formEx',
				url:'base/uploadresource/upload.do',
				method:"post",//上传表单时，必须设置此值
				enctype:"multipart/form-data",//上传附件时，必须设置此值
				progressBar:'资源上传中...',
				onSave:function(retJson){
					if(retJson.result){
						var dataJson = $(this).parents('.upload_div_panel').findOpts().data;
						dataJson.id = retJson.data.id;
						dataJson.fileType = retJson.data.fileType;
						jqForm.find('a').addClass("upload_ok");
						jqForm.find('label.add').hide();
						jqForm.find('label.remove').show();
					}
				},
				elements:uis
			}
		});
		jqForm = jqPanel.findJq('formEx');
	}
	//加载数据
	function loadData(){
		var self = this;
		var params = self.params;
		var hostId = params.hostId || -1;
		var postParams = {hostId:hostId,hostGenre:params.hostType};
		$ui.postEx("base/uploadresource/jsons.do",postParams,function(retJson){
			var uploadJsons = retJson.data;
			//遍历
			$.each(uploadJsons,function(i,uploadJson){
				addPanel.call(self,uploadJson);
			});
		});
	}
	/*
	 * 参数:
	 * 	params:{
	 * 	 hostId:12,
	 * 	 hostType:2,
	 * 	 readonly:true
	 * }
	 * */
	function Main(jqDom,params){
		var self = this;
		self.jqDom = jqDom;
		self.params = params;
		var ui = {
			eName:'fieldset',
			height:'92%',
			elements:[{
				eName:'legend',
				text:'相关文档'
			}]
		}
		self.jqFieldset = jqDom.createUI(ui);
		loadData.call(self);//加载数据
	}
	
	/*
	 * 对外方法
	 * 参数:
	 * 	IDS 获取资源IDs
	 * 	checkFlag 验证是否为空
	 * */
	Main.prototype.formIDs = function(ids,checkFlag){
		var ret = true;
		this.jqFieldset.find('.upload_div_panel').each(function(){
			var data = $(this).findOpts().data;
			if(data.id){
				ids.push(data.id);
			}else if(checkFlag){
				$ui.alert("[" + data.name + "]不能为空! ");
				ret = false;
				return false;
			}
		});
		return ret;
	}
	return Main;
});
