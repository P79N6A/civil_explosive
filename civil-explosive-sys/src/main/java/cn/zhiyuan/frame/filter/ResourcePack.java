package cn.zhiyuan.frame.filter;

import javax.servlet.ServletContext;

import cn.zhiyuan.frame.CommonUtils;

/*
 * 打包css,js等资源，并流,文件的形式输出
 * */
public final class ResourcePack {
	
	private String appPath;
	
	private ServletContext sc;
	
	public ResourcePack( ServletContext sc) {
		this.sc = sc;
		this.appPath = sc.getRealPath("/");
	}

	//js代码打包
	public void packJsCss(){
		String jsFile = "libjs/pack.js";
		String cssFile = "themes/pack.css";
		CommonUtils.writeFile(appPath + jsFile,loadJs());
		CommonUtils.writeFile(appPath + cssFile,loadCss());
		//加入时间戳，防止缓存
		sc.setAttribute("PACK_JS",jsFile + "?_" + System.currentTimeMillis());
		sc.setAttribute("PACK_CSS",cssFile + "?_" + System.currentTimeMillis());
	}
	
	
	public  byte[] loadJs(){
        ResourceLoader lr = new ResourceLoader(appPath,".js");
        lr.load("libjs/module");
        lr.load("libjs/core/UICore.js",ResourceLoader.MODULE_NO);
        lr.load("libjs/easyui");
        lr.load("libjs/userdefined");
        lr.load("libjs/boot/orm.js",ResourceLoader.MODULE_AMD);
        lr.load("libjs/boot/deploy.js",ResourceLoader.MODULE_AMD);
        lr.load("js",ResourceLoader.MODULE_AMD);
        lr.load("libjs/boot/boot.js",ResourceLoader.MODULE_NO);
        return lr.toBytes();
	}
	
	public  byte[] loadCss(){
		ResourceLoader lr = new ResourceLoader(appPath,".css");
		lr.addexFile("pack.css");
		lr.load("css/main.css");
		lr.load("css");
		return lr.toBytes();
	}
	
}
