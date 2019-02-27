

// 自定义组件
require([ 'garenjs/utils', 'garenjs/htmlui', 'garenjs/easyui'], 
		function(utils, garenUI,easyUI) {
	easyUI.regFn({// 注册组件
		name : 'datagrid',// 组件名称
		tag : 'table',// html组件
		onBeforeCreate:function(opts){
			//设置字段默认值
			$.each(opts.columns,function(i,column){
				$.each(column,function(j,field){
					if(field.rowspan || field.colspan || field.hidden) return;
					if(field.width == null) field.width = 100;
					if(field.align == null) field.align = 'center';
					//Yuanwen 2018-06-22 15:53 实现行内按钮
					if(opts.rowBtns && field.field == opts.rowBtnsField){
						createBtsUI(field,opts.rowBtns);
					}
				});
			});
		},
		onCreate : function(jqObj, uiOpts) {// 回调函数
			var mygrid = jqObj;
			//扩展工具栏
			if ($.isArray(uiOpts.toolbarEx)) { 
				var toolbar = {
					eName : 'toolbar',
					addMode : "prepend",
					elements : uiOpts.toolbarEx
				};
				var panel = mygrid.datagrid("getPanel");
				panel.find('div.datagrid-toolbar').remove();// 删除旧的工具栏
				panel.createUI(toolbar);
				mygrid.datagrid('resize');// 重绘datagrid
			}
			//定义title icon按钮
			if(uiOpts.headerTools){
				var jqPanelHeader = jqObj.datalist("getPanel").panel("header");
				jqPanelHeader.find(".panel-tool").createUI(uiOpts.headerTools);
			}
			return;// 退出
		}
	});
	
	/*
	 * 行内按钮相关函数
	 * */
	function createBtsUI(colOpts,btnsOpts){
		colOpts.formatter = function(value,row,index){
			var spanUI = "<span class='datagrid_row_btn' rowindex='" + index + "'>";
			$.each(btnsOpts,function(i,node){
				if(!node.name) return true;//continue;
				var cls = node.cls || "row_btn1";
				spanUI += "<a class='" + cls + "'>" + node.name + "</a>";
			});
			spanUI += "</span>";
			return spanUI;
		}
	}
	//行内按钮事件
	function onRowBtnsClick(opts){
		var self = $(this);
		var jqPanel = self.datagrid("getPanel");
		var jqBtns = jqPanel.find("span.datagrid_row_btn > a");
		var btnsOpts = opts.rowBtns;
		jqBtns.click(function(){
			var rowIndex = $(this).parent().attr('rowindex');
			var row = self.datagrid("getRowData",rowIndex);
			var name = $.trim($(this).text());
			$.each(btnsOpts,function(i,node){
				if(node.name == name){
					node.onClick && node.onClick.call(self,row,node.name);
				}
			});
		});
	}
	/*
	 * datagrid组件方法扩展
	 */
	$.extend($.fn.datagrid.methods, {
		getRowData:function(jq,index){//根据行索引获取行数据
			var datas = jq.datagrid("getRows");
			return datas[index];
		},
		removeRow:function(jq,id){//根据主键删除行
			var opts = jq.datagrid('options');
			var rows = jq.datagrid('getRows');
			$.each(rows,function(index,row){
				if(id == row[opts.idField]){
					jq.datagrid('deleteRow', index);
					return false;
				}
			});
		},
		//远程删除行
		remove:function(jq,params){
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
					jq.datagrid('removeRow',rowKey);
				} else {
					$ui.alert(retJson.info);
				}
			});
		},
		/*
		 * 参数格式:
		 * params:{
		 * 	 fomrEx:{},
		 *	 formTable:{},
		 *	 onEditorCreate:function(col,editor){}
		 * }
		 * */
		gridForm:function(jq,params){
			var opts = jq.datagrid('options');
			var tableUI = createFormTable(params,opts.columns);
			return $.extend({
				eName:"formEx",
				id:'datagrid_formedit',
				elements:tableUI
			},params.formEx);
		}
	});
	/*
	 * datagrid组件默认值
	 */
	$.extend($.fn.datagrid.defaults, {
		fit : true,
		autoload:true,
		pagination : true,
		singleSelect : true,
		pageList : [ 30, 60 ],
		pageSize : 30,
		border:false,
		onDelete:function(retJson,id,row){
			if (retJson.result) {
				$(this).datagrid('removeRow',id);
			} else {
				$ui.alert(retJson.info);
			}
		},
		onBeforeLoad : function(params) {
			var mygrid = $(this);
			var opts = $(this).datagrid('options');
			//自动加载
			if(!opts.autoload){
				opts.autoload = true;
				return false;
			}
			//封装表单
			var myform = null;
			var jqgridBody = mygrid.parents(".datagrid-wrap");
			if (opts.queryForm) {
				myform = jqgridBody.find(opts.queryForm);
			} else {
				myform = jqgridBody.find("form");
			}
			if (myform.length > 0) {
				if (myform.form('form2Json', params) == false)
					return false;
			}
			if (opts.onBeforeLoadEx) {
				if (false == opts.onBeforeLoadEx.call(this, params)) {
					$(this).datagrid('loadData', []);// 清空数据
					return false;
				}
			}
			return true;
		},
		loader:function(params,success,error){
			var self = this;
			var opts = $(this).datagrid('options');
			//地址错误
			if(typeof opts.url != 'string'){
				error();
				return;
			}
			// 分页参数重命名
			if (params['rows']) {
				params['pageSize'] = params['rows'];
				delete params['rows'];
			}
			if (params['page']) {
				params['pageNum'] = params['page'];
				delete params['page'];
			}
			$ui.postEx(opts.url, params, function(retJson) {
				if(retJson.result){
					if(retJson.rows)	success.call(self, retJson);
					else success.call(self, retJson.data);
					//行内按钮
					if(opts.rowBtns) onRowBtnsClick.call(self,opts);
				}else error.call(self,retJson);
			});
			return true;
		}
	});
	//创建表单表格
	function createFormTable(params,columns){
		var formTable = $.extend({
			eName : 'layoutTable',
			width : '100%',
			cols : 2,
			rows : 7,
			tdWidth : [ 80, -1,80,-1],
			trHeight:[30] //2017-05-31 10:00 Yuan 设置默认高度
		},params.formTable);
		var editFormUI = [],// 要编辑的元素
			hiddenForm = [], // 隐藏表单
			rowSize = 0;//行数
		// 遍历所有字段
		var tr = [];
		editFormUI.push(tr);
		$.each(columns, function(i, column) {
			$.each(column, function(j, col) {// 遍历列
				/*Yuanwen 2018-06-12 hidden加入回调*/
				if (!col.hidden && col.editor == null) return true;// 跳出，不隐藏,null为不可编辑行
				var editor = $.extend({
					eName : 'textbox',// 默认编辑类型
					name : col.field
				}, col.editor);
				editor.cssStyle = editor.cssStyle || "width:97%";
				if(params.onEditorCreate && false == params.onEditorCreate(col,editor)) return true;//continue;
				if(col.hidden && !col.editor){//真正的隐藏者
					hiddenForm.push({eName:"input",type:"hidden",name:col.field});
					return true;//仍为true则跳过
				}
				tr.push(col.title);//新增标题
				tr.push(editor);
				rowSize += 2;
				//确定行
				if(tr.length == formTable.cols){
					tr = [];//清空
					editFormUI.push(tr);
				}
			});
		});
		// 行数
		var len = parseInt(rowSize / formTable.cols);
		rowSize = (rowSize % formTable.cols == 0)?len:len + 1;
		formTable.rows = rowSize;
		formTable.cells = editFormUI;
		hiddenForm.unshift(formTable);
		return hiddenForm;
	}
});
