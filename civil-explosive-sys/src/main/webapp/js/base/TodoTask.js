
/*
 * 待办任务和到期提醒模块
 * */
define(function(require,exports,module){
	
	//入口
	function Main(jqGroup,loadParams){
		var uiOpts = [{
			eName:'datagrid',fit:false,border:true,pagination:false,
			id:"task_datalist",
    		css:{height:"60%"},
    		title:"待办任务",
    		url:"sys/sysuser/todoTask.do",
    		fitColumns:true,scrollbarSize:0,
    		headerTools:[{
    			eName:"a",
    			href:"javascript:void(0)",
    			cls:"icon-refresh panel-tool-a",
    			onClick:function(){
    				jqTask.datalist("load");
    			}
    		}],
    		columns:[[
    			{field:'taskName',title:'任务名称',width1:120},
    			{field:'taskCount',title:'任务数量',width:60}
    		]]
		},{eName:"div",height:3},{
			eName:'datalist',
			id:"remind_datalist",
    		css:{height:"40%"},
    		title:"到期提醒",
    		url:"sys/sysuser/todoTask.do",
    		headerTools:[{
    			eName:"a",
    			href:"javascript:void(0)",
    			cls:"icon-refresh panel-tool-a",
    			onClick:function(){
    				jqRemind.datalist("load");
    			}
    		}]
		}];
		jqGroup.createUI(uiOpts);
		var jqTask = jqGroup.find("#task_datalist");
		var jqRemind = jqGroup.find("#remind_datalist");
	}
	
	return Main;
});
