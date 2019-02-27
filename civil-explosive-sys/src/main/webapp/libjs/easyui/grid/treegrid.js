
//自定义组件
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],function(utils,garenUI,easyUI){

	easyUI.regFn({
	   name:'treegrid',
	   tag:'table',
	   onBeforeCreate:function(opts){
			//设置字段默认值
			$.each(opts.columns,function(i,column){
				$.each(column,function(j,field){
					if(field.rowspan || field.colspan || field.hidden) return;
					if(field.width == null) field.width = 100;
					if(field.align == null) field.align = 'center';
				});
			});
		},
		onCreate : function(jqObj, uiOpts) {// 回调函数
			var mygrid = jqObj;
			if ($.isArray(uiOpts.toolbarEx)) {
				var toolbar = {
					eName : 'toolbar',
					addMode : "prepend",
					elements : uiOpts.toolbarEx
				};
				var panel = mygrid.treegrid("getPanel");
				panel.find('div.datagrid-toolbar').remove();// 删除旧的工具栏
				panel.createUI(toolbar);
				mygrid.datagrid('resize');// 重绘datagrid
			}
			return;// 退出
		}
	});
	/*
	 * Treegrid组件方法扩展
	 */
	$.extend($.fn.treegrid.methods,{
		getPrevNode:function(jq,id){//获取上一个兄弟节点，空返回Null
			var nodes = null;
			var node = jq.treegrid('getParent',id);
			//没有父节点
			if(node == null){
				 nodes = jq.treegrid('getRoots');
			}else{
				nodes = jq.treegrid('getChildren',node.id);
			}
			node = null;
			$.each(nodes,function(i,n){
				if(n.id == id){
					return false;
				}
				node = n;
			});
			return node;
		},
		getNextNode:function(jq,id){//获取下一个兄弟节点，空返回Null
			var nodes = null;
			var node = jq.treegrid('getParent',id);
			//没有父节点
			if(node == null){
				 nodes = jq.treegrid('getRoots');
			}else{
				nodes = jq.treegrid('getChildren',node.id);
			}
			node = null;
			//$ui.print(nodes);
			var flag = false;
			$.each(nodes,function(i,n){
				if(n.id == id){
					flag = true;//设置标志，获取下一个节点
				}else if(flag){
					node = n;
					return false;
				}
			});
			return node;
		},//远程删除行
		removeEx:function(jq,params){
			var row = params[0];
			var removeUrl = params[1];
			if (!removeUrl) return;
			var gridOpts = jq.datagrid("options");
			var rowKey = row[gridOpts.idField];
			if (!rowKey) {// 判断是否新增行
				var index = jq.datagrid('getRowIndex', row);
				jq.datagrid('deleteRow', index);
				return;
			}
			var idField = gridOpts.idField;// 主键字段
			var params = {};
			params[idField] = rowKey;// 主键参数
			$ui.postEx(removeUrl, params, function(retJson) {
				if (retJson.result) {
					jq.treegrid('remove',rowKey);
				} else {
					$ui.alert(retJson.info);
				}
			});
		}
	});

	/*
	 * Treegrid组件属性扩展
	 */
	$.extend($.fn.treegrid.defaults,{
		fit:true,
		autoload:true,
		border:false,
		pagination : false,
		dataType:'list',
		onDelete:function(retJson,id,row){
			if (retJson.result) {
				$(this).treegrid('remove',id);
			} else {
				$ui.alert(retJson.info);
			}
		},
		loader:function(param,success,error){
			var self = this;
			var opts = $(this).treegrid('options');
			//自动加载
			if(!opts.autoload){
				opts.autoload = true;
				return false;
			}
			//地址错误
			if(typeof opts.url != 'string'){
				error();
				return true;
			}
			var id = opts.idField;
			var pid = opts.nodePid || 'pid';
			var text = opts.treeField;
			$ui.postEx(opts.url, param, function(retJson) {
				var nodes = retJson.data;
				if (retJson.result) {
					if ('list' == opts.dataType) {
						nodes = $ui.list2Tree(retJson.data, id, pid, text);
					}
					success.call(self, nodes);
				} else error.call(self);
			});
			return true;
		}
	});
	
});

