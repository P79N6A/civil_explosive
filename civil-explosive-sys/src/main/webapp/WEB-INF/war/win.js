/*
	��������:
	���tomcat app���Զ�����
	����:
	1 ֹͣtomcat����
	2 ɾ��ԭapp war��
	3 ������app war��
	4 ��������
*/
//���ö���
var fso = new ActiveXObject("Scripting.FileSystemObject");
var wShell = new ActiveXObject("WScript.Shell");

/*
����:
	1 tomcat��Ŀ¼
	2 ��app����·�������tomcat��Ŀ¼
	3 tomcat��������
	4 app���� ��ʡ��
*/
var tomcatHome = null;
var warName = null;
var servName = null;
var appName = null;

main();//���

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
	//ֹͣ����
	wShell.run("net stop " + servName,0,true);
	//ɾ��
	path = tomcatHome + "\\webapps\\" + appName;
	if(fso.FolderExists(path)){
		fso.DeleteFolder(path,true);
	}
	path += ".war";
	if(fso.FileExists(path)){
		fso.DeleteFile(path,true);
	}
	//���� 
	fso.CopyFile(tomcatHome + warName, path);
	//��������
	wShell.run("net start " + servName,0,true);

}



