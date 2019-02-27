
//自定义组件
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],
	function(utils,garenUI,easyUI){
		/*
		 * combogrid组件默认值
		 */
		$.extend($.fn.combogrid.defaults, {
			editable:false,
			multiple:true,
			singleSelect: false,
			selectOnCheck:true,
			checkOnSelect:true,
			panelHeight:'auto',
			panelMaxHeight:200,
			allFlag:false,
			onChange:function(n,o){
				var boxObj = $(this);
				var opts = boxObj.combogrid('options');
				if(opts.allFlag){
					var text = $(this).textbox("getText");
					if("全部"==text) return;
					var mygrid = $(this).combogrid('grid');
					var rows = mygrid.datagrid("getRows");
					if(n.length == rows.length){
						setTimeout(function(){
							boxObj.combo("setText","全部");
						}, 0);
					}
				}
				if(opts.onChangeEx)
					opts.onChangeEx.call(this,n,o);
			}
		});
	}

);