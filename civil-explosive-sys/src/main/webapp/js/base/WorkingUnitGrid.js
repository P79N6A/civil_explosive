
/*
 * 从业单位grid columns
 * */
define(function(require,exports,module){
	
	function createColumns(gridOpts,isSort){
		var self = this;
		gridOpts.columns = [[
			{field:'id',title:'id',hidden:true},
			{field:'typeNames',title:'单位类型',width:166,editor:{eName:'textbox',readonly:true}},
			{field:'name',title:'单位名称',editor:{required:true},width:180},
			{field:'address',title:'单位地址',editor:{required:true},width:260,hidden:true},
			{field:'creditCode',title:'企业信用代码',width:150,editor:{required:true}},
			{field:'aptitudeLevelName',title:'资质等级',width:60,editor:{
				required:true,
				eName:'combobox',
				name:'aptitudeLevel',
				valueField: 'codeValue',
				textField: 'codeName',
				url:'sys/syscode/jsons.do',
				queryParams:{codeType:4}
			}},
			{field:'endStartStr',title:'有效期至',width:70,editor:{
				eName:"div",
				elements:[{
					eName:'datebox',
					width:120,
					name:"endStart",
					required:true
				},{
					eName:"input",
					type:"checkbox",
					name:"endStartEx",
					//disabled:false,
					//checked:true,
					cssStyle:"vertical-align: middle;",
					value:"1",
					onClick:function(){
						$ui.print("aaaaaa:" + $(this).prop("checked"));
						var jqDatebox = $(this).parent().findJq("datebox");
						if($(this).prop("checked")) jqDatebox.datebox("disable");
						else jqDatebox.datebox("enable");
					}
				},{
					eName:"label",
					text:"长期"
				}]
			}},
			{field:'workingScope',title:'工作范围',width:160,editor:{required:true,eName:'textbox'}},
			{field:'licenceTypeName',title:'许可证类型',width:70,editor:{
				required:true,
				eName:'combobox',
				name:'licenceType',
				valueField: 'codeValue',
				textField: 'codeName',
				url:'sys/syscode/jsons.do',
				queryParams:{codeType:8}
			}},
			{field:'legalName',title:'法人名称',width:60,editor:{required:true}},
			{field:'legalIdcard',title:'法人身份证号码',editor:{},hidden:true},
			{field:'legalMobile',title:'法人手机号码',width:100,editor:{}},
			{field:'guardLeaderName',title:'保卫负责人',hidden:true,editor:{
				//required:true,
				eName:'combobox',
				name:'guardLeader',
				valueField: 'id',
				textField: 'name',
				url:'base/workingperson/boxJsons.do',
				queryParams:{licenceType:1}
			}},
			{field:'techLeaderName',title:'技术负责人',hidden:true,editor:{
				//required:true,
				eName:'combobox',
				name:'techLeader',
				valueField: 'id',
				textField: 'name',
				url:'base/workingperson/boxJsons.do',
				queryParams:{licenceType:2}
			}},
			{field:'netAsset',title:'净资产(万)',editor:{},hidden:true},
			{field:'dedicatedDeviceNetAsset',title:'设备净资产(万)',hidden:true,editor:{eName:'numberbox'}},
			{field:'registeredCapital',title:'注册资金(万)',hidden:true,editor:{eName:'numberbox'}},
			{field:'bankAccount',title:'银行帐号',hidden:true,editor:{}},
			{field:'auditResultName',title:'审核进度',width:80,sortable:isSort,styler: function(value,row,index){
				var ret = null;
				if(row.auditStep != 1){
					ret = row.auditResult?"color:blue;":"color:red;";
				}
				return ret;
			}},
			{field:"row_btns66",title:'操作',width:80}
		]];
	}
	//安全评价
	function createColumns1(gridOpts,isSort){
		var self = this;
		gridOpts.columns = [[
			{field:'id',title:'id',hidden:true},
			{field:'typeNames',title:'单位类型',width:120,editor:{eName:'textbox',readonly:true}},
			{field:'name',title:'单位名称',editor:{required:true},width:180},
			{field:'address',title:'单位地址',editor:{required:true},width:260,hidden:true},
			{field:'creditCode',title:'企业信用代码',width:150,editor:{required:true}},
			{field:'aptitudeLevelName',title:'资质等级',width:60,editor:{
				required:true,
				eName:'combobox',
				name:'aptitudeLevel',
				valueField: 'codeValue',
				textField: 'codeName',
				url:'sys/syscode/jsons.do',
				queryParams:{codeType:4}
			}},
			{field:'endStartStr',title:'有效期至',width:70,editor:{
				eName:"div",
				elements:[{
					eName:'datebox',
					width:120,
					name:"endStart",
					required:true
				},{
					eName:"input",
					type:"checkbox",
					name:"endStartEx",
					cssStyle:"vertical-align: middle;",
					value:"1",
					onClick:function(){
						$ui.print("aaaaaa:" + $(this).prop("checked"));
						var jqDatebox = $(this).parent().findJq("datebox");
						if($(this).prop("checked")) jqDatebox.datebox("disable");
						else jqDatebox.datebox("enable");
					}
				},{
					eName:"label",
					text:"长期"
				}]
			}},
			{field:'workingScope',title:'工作范围',editor:{required:true,eName:'textbox'}},
			{field:'licenceTypeName',title:'许可证类型',width:70,editor:{
				required:true,
				eName:'combobox',
				name:'licenceType',
				valueField: 'codeValue',
				textField: 'codeName',
				url:'sys/syscode/jsons.do',
				queryParams:{codeType:8}
			}},
			{field:'legalName',title:'法人名称',width:60,editor:{required:true}},
			{field:'legalIdcard',title:'法人身份证号码',editor:{},hidden:true},
			{field:'legalMobile',title:'法人手机号码',width:80,editor:{}},
			{field:'auditResultName',title:'审核进度',width:80,sortable:isSort,styler: function(value,row,index){
				var ret = null;
				if(row.auditStep != 1){
					ret = row.auditResult?"color:blue;":"color:red;";
				}
				return ret;
			}},
			{field:"row_btns66",title:'操作',width:80}
		]];
	}
	
	exports.createColumns = createColumns;
	exports.createColumns1 = createColumns1;
});
