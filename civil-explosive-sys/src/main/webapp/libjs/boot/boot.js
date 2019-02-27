
$(function(){
	var mainBody = $('body');
	mainBody.loadJs(mainBody.attr('data-js'),null,true);
});

//监控窗口大小变化
((function(){
	var resizeFlag = null;//延迟方法
    var htmlDom = $('html')
    $(window).resize(function(){
    	htmlDom.css('overflow','hidden');
    	window.clearTimeout(resizeFlag);//清空没有来得及执行的延时
        resizeFlag = setTimeout(function(){
        	var mainBody = $('body');
            //$ui.print("resize=" + $(document).width()+","+$(document).height());
            //$ui.print("body=" + mainBody.width()+","+ mainBody.height());
        	mainBody.find('.layoutExt').each(function(){
        		$(this).layoutExt('resize');
        	});
        	htmlDom.css('overflow','auto');
        },100);
    });
})());
