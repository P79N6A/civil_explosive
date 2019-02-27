
/*
 * */
define(function(require,exports,module){
	
	//创建列
	function createColumns(gridOpts){
		var self = this;
		gridOpts.columns = [[
			{field:'id',title:'id',hidden:true},
			{field:'contractNumber',title:'合同编号',editor:{required:true,validType:'length[0,50]'}},
			{field:'contractName',title:'合同名称',editor:{required:true,validType:'length[0,150]'},width:220},
			{field:'applyUnitId',title:'申请单位',hidden:true,width:80},
			{field:'applyUnitName',title:'申请单位',editor:{readonly:true,required:true},width:130},
			{field:'delegateUnitName',title:'委托单位',width:200,editor:{readonly:true}},
			{field:'contractSignDate',title:'合同签定日期',width:80,editor:{required:true,eName:"datebox"}},
			{field:'contractExpirationDate',title:'合同截止日期',width:90,editor:{required:true,eName:"datebox"}},
			{field:'contractCheckName',title:'是否审批',width:60,editor:{
				eName:'radiobox',
				name:'contractCheck',
				readonly:true,
				data:[{text:'是',value:'1'}
					,{text:'否',value:'0'}],
				onClick:function(val){
					self.jqItem.showMask(val);
				}
			}},{
				field:'policeName',
				title:'辖区派出所',
				width:80,
				editor:{
					required:true,
					eName:'combobox',
					name:'policeId',
					valueField: 'id',
					textField: 'shortName',
					url:'base/localpolicestation/boxJsons.do',
				}
			},{
				field:'taskAddr',
				title:'爆破作业地点',
				hidden:true,
				editor:{
					height:60,
					multiline:true,
					validType:'length[0,100]'
				}
			},
			{field:'contractExplain',title:'合同简要说明',hidden:true,width:200,editor:{
				height:60,multiline:true,validType:'length[0,980]'
			}},
			{field:'auditResultName',title:'审核进度',width:120,styler: function(value,row,index){
				var ret = null;
				if(row.auditStep != 1){
					ret = row.auditResult?"color:blue;":"color:red;";
				}
				return ret;
			}},
			{field:"row_btns66",title:'操作',width:60}
		]];
	}
	
	exports.createColumns = createColumns;
	
});

