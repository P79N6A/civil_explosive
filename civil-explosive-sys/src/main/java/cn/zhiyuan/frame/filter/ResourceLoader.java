package cn.zhiyuan.frame.filter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.zhiyuan.frame.CommonUtils;

//加载资源js,css等
public final class ResourceLoader {

	private static Log log = LogFactory.getLog(ResourceLoader.class);   
	
	public static int MODULE_NO = 1;//无封装
	public static int MODULE_AMD = 3;//新模块 AMD规范
	
	private final byte[] define = "define(".getBytes();
	
	//基础路径
	private String basePath = "";
	private String fileType;//资源扩展名
	private Set<String> exFileSet;//过滤的文件
	
	//资源流
	private ByteArrayOutputStream stream;
	
	public ResourceLoader(String basePath,String fileType){
		this.basePath = basePath == null?"":basePath;
		this.fileType = fileType == null?"":fileType;
		stream = new ByteArrayOutputStream();//初始化
		exFileSet = new HashSet<>();
	}
	
	//输出资源
	public byte[] toBytes(){
		return stream == null?new byte[0]:stream.toByteArray();
	}
	//加载文件夹
	public void load(String folderPath){
		load(folderPath,MODULE_NO);
	}
	//加载文件夹
	public void load(String folderPath,int isModule){
		File folderFile = new File(basePath + folderPath);
		if(folderFile.exists() == false) return;
		if(folderFile.isDirectory() == false) {
			loadFile(folderPath,isModule);
			return;
		}
		readFolder(folderFile,isModule);
	}
	
	//加载例外资源
	public ResourceLoader addexFile(String exFile){
		exFileSet.add(exFile);
		return this;
	}
	
	//删除例外资源
	public ResourceLoader delExFile(String exFile){
		exFileSet.remove(exFile);
		return this;
	}
	
	//遍历文件夹
	private void  readFolder(File folder,int isModule){
		File [] files = folder.listFiles();
		for(File f : files){
			if(f.isDirectory()) readFolder(f,isModule);
			else readFile(f,isModule);
		}
	}
	//判断文件扩展名是否匹配
	private boolean isEqual(File file){
		if(fileType == null) return true;
		String fileName = file.getName();
		String extName = CommonUtils.getExtName(fileName);
		return fileType.equals(extName);
	}
	//加载文件
	private void loadFile(String filePath,int isModule){
		File file = new File(basePath + filePath);
		if(file.isDirectory() || file.exists() == false) return;
		readFile(file,isModule);
		exFileSet.add(filePath);//加载过的
	}
	/*
	 * 读取文件内容
	 * 加载文件内存注释
	 * isModule 
	 * 	true 模块封装
	 *  false 不封装为模块
	 * */
	private void readFile(File file, int isModule) {
		String fileName = file.getName();
		if (false == isEqual(file))
			return;
		String path = file.getAbsolutePath().replace(basePath, "");
		path = path.replace("\\", "/");// window系统文件路经\替换为/
		if (exFileSet.contains(path))
			return;
		try {
			// 读取文件流
			byte[] fileBody = CommonUtils.readbodydata(new FileInputStream(file), 0);
			stream.write(("\r\n/***" + fileName + "***/\r\n").getBytes());
			if (isModule == MODULE_AMD) {// 模块封装 AMD规范
				if (lastAMD(path, fileBody)) return;
			} else if (MODULE_NO == isModule) {
				stream.write(fileBody);
				stream.write("\r\n".getBytes());
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	//验证AMD封装方式是否合法
	private boolean lastAMD(String filePath,byte [] fileBody) throws IOException{
		boolean ret = false;
		byte b;
		int offset = CommonUtils.binarySearch(fileBody,0,define);
		if(offset < 1) return ret;
		b = fileBody[offset + define.length];
		if(b == 34 || b == 39) return ret;
		b = fileBody[offset - 1];
		if(b == 10 || b == 13) {
			//log.debug("符合条件");
			stream.write(fileBody, 0, offset + define.length);
			String str = "\"" + filePath.replaceFirst("\\.js$","") + "\",";
			stream.write(str.getBytes());
			stream.write(fileBody, offset  + define.length,
					fileBody.length - offset - define.length);
			stream.write("\r\n".getBytes());
			ret = true;
		}
		return ret;
	}

		
}
