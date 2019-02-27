/*
	功能描述:
	完成tomcat app的自动部署
	步骤:
	1 停止tomcat服务
	2 删除原app war包
	3 复制新app war包
	4 启动服务
*/
//引用对象
var fso = new ActiveXObject("Scripting.FileSystemObject");
var wShell = new ActiveXObject("WScript.Shell");

/*
参数:
	1 tomcat根目录
	2 新app程序路径，相对tomcat根目录
	3 tomcat服务名称
	4 app名称 可省略
*/
var tomcatHome = null;
var warName = null;
var servName = null;
var appName = null;

main();//入口

function main(){
	try{
		if(false == init(WScript.Arguments))
			WScript.Quit(1);
		deploy();
	}catch(error){
		WScript.Echo("error:" + error);
	}
}

function init(args){
	var ret = true;
	if(args.length == 3){
		tomcatHome = args.item(0);
		warName = args.item(1);
		servName = "tomcat-" + args.item(2);
		appName = args.item(2);
	}else if(args.length == 4){
		tomcatHome = args.item(0);
		warName = args.item(1);
		servName = args.item(2);
		appName = args.item(3);
	}else{
		ret = false;
	}
	return ret;
}

function deploy(){
	var path = null;
	//停止服务
	wShell.run("net stop " + servName,0,true);
	//删除
	path = tomcatHome + "\\webapps\\" + appName;
	if(fso.FolderExists(path)){
		fso.DeleteFolder(path,true);
	}
	path += ".war";
	if(fso.FileExists(path)){
		fso.DeleteFile(path,true);
	}
	//复制 
	fso.CopyFile(tomcatHome + warName, path);
	//启动服务
	wShell.run("net start " + servName,0,true);

}



