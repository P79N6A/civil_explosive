
/*
 * 代码生成模块
 * */
define(function(require,exports,module){

    //加载数据库表列表
    function loadTabes() {
        var self = this;
        $ui.postEx("ormjsons.do",function (retJson) {
            var htmlstr = "";
            $.each(retJson.data,function (i,e) {
                htmlstr += '<div class="divTable">' + e + '</div>';
            });
            var jqSrcTdDom = self.jqDom.find('#srcTable');
            jqSrcTdDom.html(htmlstr);
            //绑定事件
            //点击事件
            jqSrcTdDom.find(".divTable").click(function(){
                $(this).toggleClass('divTableFocus');
            });
        });
    }

    //绑定按钮事件
    function exportFiles() {
        var self = this;
        var bags = [];
        var savePath = self.jqDom.find('#txt_savepath').val();
        self.jqDom.find(".divPackage").each(function(){

            var packName = $(this).find('input').val();
            if(packName.length < 2){
                bags = null;//清空
                return false;
            }

            //遍历表
            $(this).find('.divTable').each(function(){
                var bag = {};
                bag.packName = packName;
                bag.savePath = savePath;
                bag.tableName = $(this).text();
                bags.push(bag);
            });
        });
        if(bags != null && bags.length > 0){
            var param = {'json':$.toJSON(bags)};
            $ui.postEx('convert.do',param,function(retJson){
                $ui.alert(retJson['info']);
            });
        }else{
            $ui.alert('包名称或包内容不能为空 !');
        }
    }
	//入口
	function main(jqDom,loadParams) {
        var self = this;
        this.jqDom = jqDom;
		var mainui = {
		    eName:'table',
            cssClass:'ormTable',
            elements:[{
		        eName:'tr',
                height:40,
                elements:[{
		            eName:'td',text:'表导出类设置',
                    cssStyle:'padding-left:8px;border:none;font-weight:700;font-size:14px;'
                },{
                    eName:'td',cssStyle:'border:none;'
                },{
                    eName:'td',
                    elements:[{eName:'span',cssStyle:'margin:5px;'},{
                        eName:'textbox',
                        width:220,
                        id:'txt_savepath',
                        label:'导出路径:',
                        value:'/home/wen/tmp/orm',
                        labelWidth:60
                    },{eName:'span',cssStyle:'margin:6px;'},{
                        eName:'linkbutton',
                        iconCls:'icon-save',
                        text:'导出',
                        onClick:function () {
                            exportFiles.call(self);
                        }
                    }]
                }]
            },{
                eName:'tr',
                height:40,
                elements:[{
                    eName:'td',
                    cssStyle:'padding-left:8px;font-weight:700;font-size:14px;',
                    elements:[{
                        eName:'linkbutton',
                        iconCls:'icon-add',
                        id:'btn_allTable',
                        text:'全选',
                        onClick:function () {
                            self.jqDom.find('#srcTable').find('.divTable').addClass('divTableFocus');
                        }
                    },{eName:'span',cssStyle:'margin:6px;'},{
                        eName:'linkbutton',
                        iconCls:'icon-add',
                        id:'btn_notAllTable',
                        text:'取反',
                        onClick:function () {
                            var jqObj = self.jqDom.find('#srcTable');
                            var select = jqObj.find('.divTableFocus');
                            jqObj.find('.divTable').addClass('divTableFocus');
                            select.removeClass('divTableFocus');
                        }
                    },{eName:'span',cssStyle:'margin:6px;'},{
                        eName:'searchbox',
                        searcher:function(q){
                            var jqObj = self.jqDom.find('#srcTable');
                            var select = jqObj.find('div:contains('+q+')');
                            jqObj.find('div').removeClass('divTable').hide();
                            select.addClass('divTable').show();
                        }
                    }]
                },{
                    eName:'td'
                },{
                    eName:'td',
                    cssStyle:'text-align: right;padding-right:8px;',
                    elements1:[{
                        eName:'linkbutton',
                        iconCls:'icon-add',
                        id:'btn_addBag',
                        text:'添加包'
                    },{eName:'span',cssStyle:'margin:6px;'},{
                        eName:'linkbutton',
                        iconCls:'icon-add',
                        id:'btn_delBag',
                        text:'删除包'
                    }]
                }]
            },{
                eName:'tr',
                cssClass:'orm_body',
                elements:[{
                    eName:'td',
                    width:610,
                    id:'srcTable'
                },{
                    eName:'td',
                    width:40,
                    elements:[{
                        eName:'img',
                        id:'left2right',
                        cssStyle:'cursor: pointer',
                        src:'images/Right.gif',
                        onClick:function () {
                            self.jqDom.find('#srcTable .divTableFocus')
                                .appendTo(self.jqDom.find('.divPackageFocus td:last'));
                        }
                    },{eName:'br'},{
                        eName:'img',
                        id:'right2left',
                        cssStyle:'cursor: pointer',
                        src:'images/Left.gif',
                        onClick:function () {
                            self.jqDom.find('.divPackageFocus .divTableFocus')
                                .appendTo(self.jqDom.find("#srcTable"));
                        }
                    }]
                },{
                    eName:'td',
                    width:400,
                    elements:{
                        eName:'table',
                        cssClass:'divPackage divPackageFocus',
                        id:"myBag",
                        elements:[{
                            eName:'tr',
                            elements:{
                                eName:'td',
                                cssStyle:'padding:5px;',
                                elements:[{
                                    eName:'span',text:'包名:'
                                },{
                                    eName:'textbox',
                                    width:220
                                }]
                            }
                        },{
                            eName:'tr',
                            elements:{eName:'td',cssClass:'cTable'}
                        }]
                    }
                }]
            }]
        };
        jqDom.createUI(mainui);
		loadTabes.call(this);
    }

    module.exports = main;
});
