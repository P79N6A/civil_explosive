

define(function(require,exports,module)'{'
	var mName = "";
	var queryUrl = "{2}/jsons.do";
	var saveUrl = "{2}/save.do";
	var removeUrl = "{2}/delete.do";
	//编辑表单
	function gridEdit(title,row)'{'
		var self = this;
		var jqWin = null;
		var formOpts = '{'
			formEx:'{'
				formData:row,url:saveUrl,
				onSave:function(retJson)'{'
					if(!retJson.result) return;
					jqWin.window("close");
					self.jqGrid.datagrid("reload");
				}
			}
		};
		var formUI = self.jqGrid.datagrid("gridForm",formOpts);
		var dialogOpts = '{'
			formDialog:'{'
				title:mName,
				height:{3}
			}
		};
		jqWin = $ui.createFormDialog(dialogOpts,formUI);
	}
	//创建工具栏
	function createToolbar(gridOpts)'{'
		var self = this;
		gridOpts.toolbarEx = ['{'
			eName:"linkbutton",plain:true,
			text:"新增",iconCls:"icon-add",
			onClick:function()'{'
				var row = '{'};
	        	gridEdit.call(self,"新增",row);
			}
		},'{'
			eName:"linkbutton",plain:true,
			text:"编辑",iconCls:"icon-edit",
			onClick:function()'{'
				var row = self.jqGrid.datagrid("getSelected");
				if (row == null) '{'
					$ui.alert("请选择一条要修改的记录 !");
					return;
				}
				gridEdit.call(self,"编辑",row);
			}
		},'{'
			eName:"linkbutton",plain:true,
			text:"删除",iconCls:"icon-remove",
			onClick:function()'{'
				var row = self.jqGrid.datagrid("getSelected");
				if (row == null) '{'
					$ui.alert("请选择一条要删除的记录 !");
					return;
				}
				$ui.confirm("删除后不能撤销,你确定要删除此记录吗 ?", function(r) '{'
					if (!r) return;
					self.jqGrid.datagrid("remove",[row,removeUrl]);
				});
			}
		}];
	}
	//创建列
	function createColumns(gridOpts)'{'
		var self = this;
		gridOpts.columns = [[
			{0}
		]];
	}
	//入口
	function Main(jqGroup,loadParams)'{'
		var self = this;
		var gridOpts = '{'
			eName:"datagrid",
			fit:true,
			border:false,
			autoload:false,
			idField:"{1}",
			url:queryUrl
		}
		createToolbar.call(self,gridOpts);
		createColumns.call(self,gridOpts);
		self.jqGrid = jqGroup.createUI(gridOpts);
		self.jqGrid.datagrid("load");
	}
	return Main;
});
