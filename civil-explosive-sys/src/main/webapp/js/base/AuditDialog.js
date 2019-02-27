
/*
 * 审核封装
 * */
define(function(require,exports,module){

	//构造函数
	function AuditDialog(){}
	
	/*
	 * 备案
	 * 参数格式:
	 * params:{
	 * 	jqGrid:xx,
	 *  auditUrl:xxx,
	 *  cols:2,
	 *  title:'xxx',
	 *  width:111,
	 *  height:222,
	 *  upload:{},
	 *  2018-05-05 10:17扩展参数
	 *  btnDisabled:true/false
	 *  
	 *  tdWidth:[110,-1,110,-1],
		trHeight:[35],
	 * }
	 * 2018-05-05 10:14
	 * 	扩展表单状态验证，不符合条件只能浏览
	 *  1 按钮的状态btnDisabled:true无效/false有效
	 *  2 备注的有无 markerFalg
	 *  3 窗口的大小条件
	 * */
	function saveForm(params,row){
		var jqGrid = params.jqGrid;
		var jqWin = null;
		var jqUpload = null;
		var formOpts = {
			formEx:{
				url:params.auditUrl,
				formData:row,
				onBeforeSave:function(formParams){
					//绑定上传资源ids
					if(params.upload){
						var ids = [];
						jqUpload.formIDs(ids);
						formParams.uploadIds = ids.join(",");
					}
					return true;
				},
				onSave:function(retJson){
					if(retJson.result){
						jqGrid.datagrid("reload");
						jqWin.window('close');
					}
				}
			},
			formTable:{
				cols:params.cols || 2,
				tdWidth:params.tdWidth,
				trHeight:[32]
			}
		};
		var dialogOpts = {
			formDialog:{
				title:params.title,
				width:params.width,
				height:params.height
			},
			onLayoutCreate:function(regions){
				var panel = regions.pop();
				if(params.upload){//文档附件
					regions.push({
						region:"south",
						height:80,
						cssStyle:"padding:3px"
					});
				}
				regions.push(panel);//备份
		   },
		   formBtns:[{
			   disabled:params.btnDisabled
		   }]
		};
		var formUI = jqGrid.datagrid("gridForm",formOpts);
		jqWin = $ui.createFormDialog(dialogOpts,formUI); 
		if(params.upload){
			//渲染附件上传模块
			var jqLayout = jqWin.findJq('layoutExt');
			var uploadDiv = jqLayout.layoutExt("getPanel",{region:'south',index:0});
			params.upload.hostId = row.id;//设置主键
			jqUpload = uploadDiv.loadJs("js/base/upload2",params.upload);
		}
	}
	/*
	 * 审核
	 * 参数格式:
	 * params:{
	 * 	jqGrid:xx,
	 *  auditUrl:xxx,
	 *  cols:2,
	 *  title:'xxx',
	 *  width:111,
	 *  height:222,
	 *  upload:{},
	 *  2018-05-05 10:17扩展参数
	 *  btnDisabled:true/false
	 *  markerFlag:true/false
	 * }
	 * 2018-05-05 10:14
	 * 	扩展表单状态验证，不符合条件只能浏览
	 *  1 按钮的状态btnDisabled:true无效/false有效
	 *  2 备注的有无 markerFalg
	 *  3 窗口的大小条件
	 * */
	function audit(params,row){
		var jqGrid = params.jqGrid;
		var jqWin = null;
		var height = params.height;
		var formOpts = {
			formEx:{
				url:params.auditUrl,
				formData:row,
				onSave:function(retJson){
					if(retJson.result){
						jqGrid.datagrid("reload");
						jqWin.window('close');
					}
				}
			},
			formTable:{
				cols:params.cols || 2,
				tdWidth:params.tdWidth,
				trHeight:[32]
			}
		};
		var dialogOpts = {
			formDialog:{
				title:params.title,
				width:params.width,
				height:params.height
			},
			onLayoutCreate:function(regions){
				var panel = regions.pop();
				if(params.upload){//文档附件
					regions.push({
						region:"south",
						height:80,
						cssStyle:"padding:3px"
					});
				}
				regions.push({//备注
					region : "south",
					height : 50,
					cssStyle:"padding:6px 6px 0 6px;text-align:center;",
					elements:{
						eName:'textbox',
						multiline:true,
						name:"remark",
						prompt:'输入审核备注',
						validType:'length[0,180]',
						cssStyle:'width:90%;height:90%'
					}
				});
				regions.push(panel);//备份
		   },
		   formBtns:[{
				text:'同意',
				disabled:params.btnDisabled,
				onClick:function() {
					var jqForm = jqWin.findJq('formEx');
					var txt = jqWin.findJq("remark").val();
					jqForm.formEx("submit",{auditResult:true,remark:txt});
				}
			},{
				text:'拒绝',
				disabled:params.btnDisabled,
				onClick:function() {
					var jqForm = jqWin.findJq('formEx');
					var txt = jqWin.findJq("remark").val();
					jqForm.formEx("submit",{auditResult:false,remark:txt});
				}
			}]
		};
		var formUI = jqGrid.datagrid("gridForm",formOpts);
		jqWin = $ui.createFormDialog(dialogOpts,formUI); 
		if(params.upload){
			//渲染附件上传模块
			var jqLayout = jqWin.findJq('layoutExt');
			var uploadDiv = jqLayout.layoutExt("getPanel",{region:'south',index:0});
			params.upload.hostId = row.id;//设置主键
			uploadDiv.loadJs("js/base/upload2",params.upload);
		}
	}
	/*
	 * 备案
	 * */
	function audit1(params,row){
		if(!row){//编辑
			row = params.jqGrid.datagrid('getSelected');
			if(!row){
				$ui.alert("请选择一条记录 !");
				return;
			}
			var stepCount = params.stepCount || 3;
			//中间步骤且审核通过，则浏览模式
			if(row.auditResult && row.auditStep > 1
					&& row.auditStep < stepCount){
				params.btnDisabled = true;
				params.upload.readonly = true;
			}else{//浏览模式
				params.btnDisabled = false;
			}
		}
		saveForm(params,row);
	}
	//受理
	function audit2(params){
		var row = params.jqGrid.datagrid('getSelected');
		if(!row){
			$ui.alert("请选择一条记录 !");
			return;
		}
		params.btnDisabled = (1 == row.auditStep)?false:true;
		audit(params,row);
	}
	//审核
	function audit3(params){
		var row = params.jqGrid.datagrid('getSelected');
		if(!row){
			$ui.alert("请选择一条记录 !");
			return;
		}
		params.btnDisabled = (row.auditStep == 2 && row.auditResult)?false:true;
		audit(params,row);
	}
	//公共方法
	AuditDialog.audit1 = audit1;
	AuditDialog.audit2 = audit2;
	AuditDialog.audit3 = audit3;
	return AuditDialog;
});

