
/*
 * */
define(function(require,exports,module){
	//入口
	function main(jqDom,args){
		var self = this;
		var mainUI = {
			eName:"div",
			cssStyle:"margin:20px;",
			elements:[{
				eName:'combobox',
				width:100,
				panelWidth:130,
				name:'typeCode',
				value:"1",
				data:[{text:'开始',value:1},{text:'过程',value:2},{text:'结束',value:3}]
			},{
				eName:'combobox',
				name:'storeType',
				valueField: 'codeValue',
				textField: 'codeName',
				url:'sys/syscode/jsons.do',
				queryParams:{codeType:'f'}
			}]
		};
		jqDom.createUI(mainUI);
	}
	return main;
});

