
/*
 * 爆破作业合同项目审批模块
 * */
define(function(require,exports,module){
	
	//设计施工方案人员
	function createForm1(data){
		var self = this;
		var cells = [
			//第一行
			[{
				eName:"span",
				elements:"设计<br/>施工<br/>方案"
			},{},{
				eName:"span",
				text:"姓名"
			},{
				eName:"span",
				text:"许可证编号"
			},{
				eName:"span",
				text:"公民身份证号码"
			}],
			//第二行
			[{
				eName:"span",
				text:"设计人"
			},{
				eName:'combobox',
				required:true,
				name:'itemDesignerId',
				valueField: 'id',
				textField: 'nameAll',
				width:"96%",
				url:'base/workingperson/boxJsons.do',
				queryParams:{unitId:data.applyUnitId},
				onSelect:function(record){
					var jqTd = $(this).parent().next();
					jqTd.text(record.licenceCode);
					jqTd = jqTd.next();
					jqTd.text(record.idcard);
				}
			}],
			//第三行
			[{
				eName:"span",
				text:"审核人"
			},{
				eName:'combobox',
				required:true,
				name:'itemAuditId',
				valueField: 'id',
				textField: 'nameAll',
				width:"96%",
				url:'base/workingperson/boxJsons.do',
				queryParams:{unitId:data.applyUnitId},
				onSelect:function(record){
					var jqTd = $(this).parent().next();
					jqTd.text(record.licenceCode);
					jqTd = jqTd.next();
					jqTd.text(record.idcard);
				}
			}],
			//第三行
			[{
				eName:"span",
				text:"批准人"
			},{
				eName:'combobox',
				required:true,
				name:'itemApproveId',
				valueField: 'id',
				textField: 'nameAll',
				width:"96%",
				url:'base/workingperson/boxJsons.do',
				queryParams:{unitId:data.applyUnitId},
				onSelect:function(record){
					var jqTd = $(this).parent().next();
					jqTd.text(record.licenceCode);
					jqTd = jqTd.next();
					jqTd.text(record.idcard);
				}
			}]
		];
		return {
			eName:"layoutTable",
			cols:5,
			rows:4,
			trWidth:[50,-1],
			trHeight:[26],
			rowSpans:[[0,0,4]],
			cells:cells,
			onTdStyle:function(r,c){
				if(r == 0 && c == 0){
					this.width = 50;
				}
			}
		}
	}
	//box对应关系
	var boxMap = {
		"itemWorkId":"itemWorkStaffs",
		"itemAssessId":"itemAssessStaffs",
		"itemSupervisorId":"itemSupervisorStaffs"
	};
	//爆破作业单位
	function createForm2(data){
		var self = this;
		function onBoxBeforeLoad(params){
			return(params.unitId)?true:false;
		}
		function onBoxSelect(record){
			record = record || {};
			var jqTd = $(this).parents("td:eq(0)");
			var c = jqTd.attr("c");
			var jqTable = $(this).parents("table");
			var jqBox = jqTable.findJq(boxMap[$(this).attr("nname")]);
			var nodes = jqTable.find("td[c="+c+"]:gt(1):lt(9)");
			if(nodes.length == 9){
				//许可证
				$(nodes[0]).text(record.creditCode || "");
				//法人姓名
				$(nodes[1]).text(record.legalName || "");
				//法人电话
				$(nodes[2]).text(record.legalMobile || "");
				//技术负责人
				$(nodes[3]).text(record.techLeaderName || "");
				$(nodes[4]).text(record.techLeaderPhone || "");
				//项目技术负责人
				$(nodes[5]).text(record.techLeaderName || "");
				$(nodes[6]).text(record.techLeaderPhone || "");
				//保卫负责人
				$(nodes[7]).text(record.guardLeaderName || "");
				$(nodes[8]).text(record.guardLeaderPhone || "");
				
				//加载从业人员
				jqBox.combobox("reload",{unitId:record.id});
			}
		}
		var cells = [
			//第0行
			[{
				eName:"span",
				elements:"爆<br/>破<br/>作<br/>业<br/>单<br/>位"
			},{},{
				eName:"span",
				text:"爆破施工单位"
			},{
				eName:"span",
				text:"安全评估单位"
			},{
				eName:"span",
				text:"安全监理单位"
			}],
			//第一行
			[{},{
				eName:'combobox',
				required:true,
				name:'itemWorkId',
				prompt:"设计施工单位",
				width:"100%",
				editable:false,
				valueField: 'id',
				textField: 'name',
				panelHeight:'auto',
				clearFlag:true,
				url:'base/workingunit/boxJson.do',
				onSelect:onBoxSelect,
				onClear:onBoxSelect//清空
			},{
				eName:'combobox',
				required:true,
				name:'itemAssessId',
				width:"100%",
				prompt:"安全评估单位",
				editable:false,
				valueField: 'id',
				textField: 'name',
				panelHeight:'auto',
				clearFlag:true,
				url:'base/workingunit/boxJson.do',
				onSelect:onBoxSelect,
				onClear:onBoxSelect//清空
			},{
				eName:'combobox',
				required:true,
				name:'itemSupervisorId',
				width:"100%",
				prompt:"安全监理单位",
				editable:false,
				valueField: 'id',
				textField: 'name',
				clearFlag:true,
				url:'base/workingunit/boxJson.do',
				onSelect:onBoxSelect,
				onClear:onBoxSelect//清空
			}],
			//第二行
			[{
				eName:"span",
				text:"许可证编号"
			}],
			//第三行
			[{
				eName:"span",
				elements:"法定<br/>代表人"
			},{
				eName:"span",
				text:"姓名"
			}],
			//第四行
			[{
				eName:"span",
				text:"电话"
			}],
			//第五行
			[{
				eName:"span",
				elements:"技术<br/>负责人"
			},{
				eName:"span",
				text:"姓名"
			}],
			//第六行
			[{
				eName:"span",
				text:"电话"
			}],
			//第七行
			[{
				eName:"span",
				elements:"项目技术<br/>负责人"
			},{
				eName:"span",
				text:"姓名"
			}],
			//第八行
			[{
				eName:"span",
				text:"电话"
			}],
			//第九行
			[{
				eName:"span",
				elements:"治安保卫<br/>负责人"
			},{
				eName:"span",
				text:"姓名"
			}],
			//第十行
			[{
				eName:"span",
				text:"电话"
			}],
			//第十一行
			[{
				eName:"span",
				text:"项目参加人员"
			},{//爆破施工单位
				eName:'combobox',
				required:true,
				name:'itemWorkStaffs',
				width:"100%",
				prompt:"项目参加人员",
				multiple:true,
				editable:false,
				valueField: 'id',
				textField: 'nameAll',
				clearFlag:true,
				url:'base/workingperson/boxJsons.do',
				onBeforeLoad:onBoxBeforeLoad
			},{//安全评估单位
				eName:'combobox',
				required:true,
				name:'itemAssessStaffs',
				width:"100%",
				prompt:"项目参加人员",
				multiple:true,
				editable:false,
				valueField: 'id',
				textField: 'nameAll',
				clearFlag:true,
				url:'base/workingperson/boxJsons.do',
				onBeforeLoad:onBoxBeforeLoad
			},{//安全监理单位
				eName:'combobox',
				required:true,
				name:'itemSupervisorStaffs',
				width:"100%",
				prompt:"项目参加人员",
				multiple:true,
				editable:false,
				valueField: 'id',
				textField: 'nameAll',
				clearFlag:true,
				url:'base/workingperson/boxJsons.do',
				onBeforeLoad:onBoxBeforeLoad
			}]
		];
		return {
			eName:"layoutTable",
			css:{"font-size":13},
			marginTop:6,
			cols:6,
			rows:12,
			trWidth:[50,-1],
			trHeight:[26],
			colSpans:[[0,1,2],[1,1,2],[2,1,2],[11,1,2]],
			rowSpans:[[0,0,12],[3,1,2],[5,1,2],[7,1,2],[9,1,2]],
			cells:cells,
			onTdStyle:function(r,c){
				if(r == 0){
					if(c == 0) this.width = 30;
					else if(1 == c) this.width = 120;
				}
			}
		}
	}
	//创建表单内容
	function createForm(data){
		var self = this;
		var fromUI1 = createForm1.call(self,data);
		var fromUI2 = createForm2.call(self,data);
		return [fromUI1,fromUI2];
	}
	//入口
	function Main(jqDom,argv){
		var self = this;
		var formUIs = createForm.call(self,argv.data);
		var formOpts = [{
			eName:"formEx",
			formData:argv.data,
			elements:formUIs
		},{
			eName:"div",
			cls:"div_mask"
		}];
		formOpts.unshift({
			eName:"legend",
			text:"项目审批"
		});
		jqDom.createUI({
			eName:"fieldset",
			css:{padding:6},
			elements:formOpts
		});
		self.jqForm = jqDom.findJq("formEx");
		self.jqMask = jqDom.find(".div_mask");
		showMask.call(self,argv.data.contractCheck);
	}
	//表单获取
	function formJson(params){
		var self = this;
		return self.jqForm.form("form2Json",params);
	}
	//控制遮罩
	function showMask(val){
		if(1 == val) this.jqMask.hide();
		else this.jqMask.show();
	}
	//公共方法
	Main.prototype.formJson = formJson;
	Main.prototype.showMask = showMask;
	return Main;
});

