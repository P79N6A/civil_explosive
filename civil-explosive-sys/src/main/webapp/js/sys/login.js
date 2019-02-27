
/*
 * 登录模块
 * */
define(function(require,exports,module){

	//创建表单
    function createForm() {
        var self = this;
        var inputWidth = 280;
        return {
            eName:"formEx",
            url:'sys/sysuser/login.do',
            alertFlag:false,
            onSave:function (retJson) {
                if(retJson.result){
                    self.jqDom.loadJsx("sys/index","js/sys/index");
                }else {
                	self.jqDom.find('#login_msg').text(retJson.info);
                	//重置
                	$(this).find('.checkcode').click();
                	$(this).findJq('code').textbox('setText','');
                }
            },
            elements:[{
                eName:'textbox',
                name:'userName',
                cls:'login-text-box',
                prompt:'用户名',
                required:true,
                iconCls:'icon-man',
                validType:'length[4,30]',
                tipPosition:"left",
                mTop:32, mBottom:24,height:32,
                width:inputWidth
            },{eName:'br'},{
                eName:'passwordbox',
                name:'password',
                prompt:'密码',
                required:true,
                validType:'length[4,30]',
                tipPosition:"left",
                height:32,
                width:inputWidth
            },{eName:'br'},{
                eName:'div',
                cssClass:'div_check_code',
                width:inputWidth,
                elements:[{
                	eName:'textbox',
                    name:'code',
                    prompt:'验证码',
                    required:true,
                    validType:'length[4,4]',
                    tipPosition:"left",
                    mTop:18, mBottom:8,height:32, 
                    width:inputWidth / 3
                },{
                	eName:'img',
                	src:'logincode.do?_' + new Date().getTime(),
                	cssClass:'checkcode',
                	onClick:function(){
                		$(this).attr("src","logincode.do?_" + new Date().getTime())
                	}
                }]
            },{eName1:'br'},{
                eName:'linkbutton',
                marginTop:12,
                text:'登录',
                plain:false,
                width:200,
                height:40,
                onClick:function () {
                    var jqForm = self.jqDom.findJq('formEx');
                    jqForm.formEx('submit');
                }
            },{
            	eName:'div',
            	id:'login_msg'
            }]
        }
    }
	//入口
	function main(jqDom,loadParams) {
		var self = this;
		self.jqDom = jqDom;
		var formUI = createForm.call(this);
		var jqFormDiv = jqDom.find('.login_form');
		jqFormDiv.createUI(formUI);
		jqFormDiv.findJq("userName").textbox("textbox").focus();
		var jqCodeBox = jqFormDiv.findJq("code");
		var jqCodeTxt = jqCodeBox.textbox('textbox');
		jqCodeTxt.keydown(function(e){
			var key = e.keyCode;
			if(key == 13){
				if(jqCodeBox.textbox('isValid')){
					 var jqForm = jqFormDiv.findJq('formEx');
	                 jqForm.formEx('submit');
				}
			}
		});
    }

    module.exports = main;

});
