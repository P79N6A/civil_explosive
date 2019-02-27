
//自定义组件
require(['garenjs/utils','garenjs/htmlui','garenjs/easyui'],function(utils,garenUI,easyUI){
	
	/*
	 * Tree组件方法扩展
	 */
	$.extend($.fn.tree.methods,{
		getPrevNode:function(jq,target){//获取上一个兄弟节点，空返回Null
			var nodes = null;
			var node = jq.tree('getParent',target);
			//没有父节点
			if(node == null){
				 nodes = jq.tree('getRoots');
			}else{
				nodes = jq.tree('getChildren',node.target);
			}
			node = null;
			$.each(nodes,function(i,n){
				if(n.target == target){
					return false;
				}
				node = n;
			});
			return node;
		},
		getNextNode:function(jq,target){//获取下一个兄弟节点，空返回Null
			var nodes = null;
			var node = jq.tree('getParent',target);
			//没有父节点
			if(node == null){
				 nodes = jq.tree('getRoots');
			}else{
				nodes = jq.tree('getChildren',node.target);
			}
			node = null;
			//$ui.print(nodes);
			var flag = false;
			$.each(nodes,function(i,n){
				if(n.target == target){
					flag = true;//设置标志，获取下一个节点
				}else if(flag){
					node = n;
					return false;
				}
			});
			return node;
		}
	});
	
	/*
	 * Tree组件扩展
	 */
	$.extend($.fn.tree.defaults, {
	  cascadeCheck:false,
	  checkbox:true,
	  dataType:'tree',//1树形结构,2列表(需要前台转换)
	  loader:function(param,success,error){
		  var self = this;
		  var mytree = $(this);
		  var opts = mytree.tree('options');
		  var id = opts.nodeId || 'id';
		  var pid = opts.nodePid || 'pid';
		  var text = opts.nodeText || 'text';
		  $ui.postEx(opts.url,param,function(retJson){
			  var nodes = retJson.data;
			  if(retJson.result){
				 if('list' == opts.dataType){
					 nodes = $ui.list2Tree(retJson.data,id,
							 pid,text);
				 }
				 //过滤选中节点
				 if(opts.values)
					 $ui.treeChecked(nodes,opts.values.split(','));
				 success.call(self,nodes);
			  }else error.call(self);
		  });
	  }
	});
	
});

