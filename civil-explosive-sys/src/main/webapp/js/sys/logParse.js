
/*
 * 日志在线分析
 * */
define(function(require,exports,module){

	function main(jqGroup,loadParams){
		var ui = {
			eName:"div",
			css:{"border":"0px solid red","position":"absolute",top:0,bottom:0,left:0,right:0},
			elements:[{
				eName:"div",
				css:{padding:6,border:"1px solid #aaa"},
				height:30,
				elements:[{
					eName:"combobox",
					name:"logPath",
					width:120,
					valueField: "codeDesc1",
					textField: "codeName",
					selectIndex:0,//默认值
					url:"sys/syscode/jsons.do",
					queryParams:{codeType:'j'},
					onChange:function(logPath){
						$ui.print(logPath);
						var jqlogNameBox = jqForm.findJq("logName");
						jqlogNameBox.combobox("clear");
						jqlogNameBox.combobox("reload",{logPath:logPath});
					}
				},{
					eName:"combobox",
					name:"logName",
					autoLoad:false,
					valueField: "logName",
					textField: "logName",
					url:"sys/logparse/logFiles.do",
					selectIndex:0
				},{
					eName:"numberbox",
					name:"logTime",
					precision:0,
					min:0,
					max:1000,
					width:50
				}/*{
					eName:"timespinner",
					width:90,
					name:"logTime",
					showSeconds:true
				}*/,{
					eName:"linkbutton",
					width:60,
					text:"查询",
					onClick:function(){
						jqForm.formEx("submit");
					}
				},{
					eName:"linkbutton",
					width:60,
					text:"日志下载",
					onClick:function(){
						var params = {};
						jqForm.formEx("form2Json",params);
						$ui.progress("日志压缩中...");
						$ui.postEx("sys/logparse/logGZip.do",params,function(retResult){
							$ui.progress("close");
							if(retResult.result){
								window.open("sys/logparse/downLog.do?logName=" 
										+ params.logName);
							}
						});
					}
				}]
			},{
				eName:"div",
				css:{"position":"absolute",padding:6,top:50,bottom:0,left:0,right:0,"overflow":"auto"},
				elements:{
					eName:"table",
					cls:"log_parse",
					id:"logBody",
				}
			}]
		};
		var formUI = {
			eName:"formEx",
			url:"sys/logparse/logRead.do",
			elements:ui,
			alertFlag:false,
			onSave:function(result){
				var jsons = result.data;
				var html = "";
				$.each(jsons,function(i,json){
					var cls = i % 2 == 0?"even":"odd";
					html += '<tr class="'+cls+'">';
					html += "<td class=\"dateprefix\">" + json.datePrefix + "</td>";
					html += "<td>" + json.body + "</td>";
					html += "</tr>";
				});
				jqlogBody.html(html);
			}
		}
		var jqForm = jqGroup.createUI(formUI);
		var jqlogBody = jqGroup.find("#logBody");
	}
	
	module.exports = main;

});
