//textbox组件
require(['garenjs/utils', 'garenjs/htmlui', 'garenjs/easyui'], function (utils, garenUI, easyUI) {

    easyUI.regFn({
        name: 'textbox',
        tag: 'input',
        onBeforeCreate: function (uiOpts) {//回调函数
            var jqDom = $(this);
            var clearFlag = uiOpts.clearFlag || $.fn.textbox.defaults.clearFlag;
            if (clearFlag) {
                uiOpts.icons = uiOpts.icons || [];
                uiOpts.icons.push({
                    iconCls: 'icon-clear',
                    handler: function (e) {
                        $(e.data.target).textbox('clear');
                    }
                });
            }
        },
        onCreate: function (jqObj, uiOpts) {
            var jqSpan = $.data(jqObj[0], 'textbox').textbox;
            if (uiOpts.mTop > 0) jqSpan.css('margin-top', uiOpts.mTop + 'px');
            if (uiOpts.mRight > 0) jqSpan.css('margin-right', uiOpts.mRight + 'px');
            if (uiOpts.mBottom > 0) jqSpan.css('margin-bottom', uiOpts.mBottom + 'px');
            if (uiOpts.mLeft > 0) jqSpan.css('margin-left', uiOpts.mLeft + 'px');
        }
    });
    $.extend($.fn.textbox.defaults, {
        clearFlag: false
    });
});


