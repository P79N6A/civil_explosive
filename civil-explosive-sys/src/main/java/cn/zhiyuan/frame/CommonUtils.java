package cn.zhiyuan.frame;

import java.awt.image.BufferedImage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;

public final class CommonUtils {

	protected static Log log = LogFactory.getLog(CommonUtils.class);
	
	public static final String TOMCAT_HOME_NAME = "catalina.home";
	public static final String TOMCAT_GAREN_DEPLOY= "TOMCAT_GAREN_DEPLOY";//项目发布标志,区别开发环境还是生成环境
	
	
	public static Date getFileCreateTime(String fullFileName) {
		Path path = Paths.get(fullFileName);
		BasicFileAttributeView basicview = Files.getFileAttributeView(path, BasicFileAttributeView.class,
				LinkOption.NOFOLLOW_LINKS);
		BasicFileAttributes attr;
		try {
			attr = basicview.readAttributes();
			Date createDate = new Date(attr.creationTime().toMillis());
			return createDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.set(1970, 0, 1, 0, 0, 0);
		return cal.getTime();
	}

	
	public static void xmlAddNodeTxt(Element node,String nodeName,Object nodeVal) {
		if(StringUtils.isEmpty(nodeName)) return;
		if(nodeVal == null) nodeVal = "";
		node.addElement(nodeName).addText(nodeVal.toString());
	}
	
	public static int str2int(String str,int num) {
		if(StringUtils.isEmpty(str)) return num;
		try {
			num = Integer.parseInt(str);
		}catch(NumberFormatException ex) {
		}
		return num;
	}
	/*
	 * 检测UDP端口是否被监听
	 * */
	public static boolean checkUDPPort(int port) {
		boolean result = false;
		try {
			DatagramSocket datagramSocket = new DatagramSocket(port);
			datagramSocket.close();
			result = true;
		} catch (IOException e) {
			log.error(e.getLocalizedMessage() + ":" + port);
		}
		return result;
	}
	public static Boolean isTrue(Boolean bl) {
		return bl == null?false:bl;
	}
	
	public static Document loadXML(String appPath) {
		try {
			return DocumentHelper.parseText(CommonUtils.readDataStr(CommonUtils.getAppPath(appPath)));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isWindows() {
		String osName = System.getProperty("os.name").toLowerCase();
		return osName.indexOf("windows") > -1 ? true:false;
	}
	
	public static boolean isLinux() {
		String osName = System.getProperty("os.name").toLowerCase();
		return osName.indexOf("linux") > -1 ? true:false;
	}

	//树结构父节点相等比较

	static public boolean comparePid(Integer pid1,Integer pid2){
		if(pid1 == null) {
			if(pid2 == null) return true;
		}else {
			if(pid1.equals(pid2)) return true;
		}
		return false;
	}
	
	//根据名称获取spring实例化的对象
	@SuppressWarnings("unchecked")
	public static  <T>T getSpringBean(ServletContext sc,String beanName,Class<? extends Object> T){
		try{
			WebApplicationContext wx = WebApplicationContextUtils
					.getRequiredWebApplicationContext(sc);
			if(wx == null) return null;
			Object obj = wx.getBean(beanName);
			return obj == null?null:(T)obj;
		}catch (NoSuchBeanDefinitionException e) {}
		return null;
	}
	
	//输出流到客户端
	public static void responseStream(HttpServletResponse response,
			byte [] bpost,String type){
		try {
			response.setContentType(type);
			if(bpost == null) bpost = new byte[0];
			response.setContentLength(bpost.length);
			OutputStream outs = response.getOutputStream();
			outs.write(bpost);
			outs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * map类型转换
	 * */
	public static JSONObject parseParams(Map<String,String[]> paramMap) {
		Map<String, String[]> params = paramMap;
		JSONObject json = new JSONObject();
		for (String key : params.keySet()) {
			String[] values = params.get(key);
			if(values.length == 1) {
				json.put(key, values[0]);
			}else {
				json.put(key, values);
			}
		}
		return json;
	}
	
	//判断浏览器是否为火狐Firefox
	public static boolean isFirefox(HttpServletRequest request){
		String userAgent = request.getHeader("User-Agent");
		return isMatches(userAgent,"firefox\\/\\d+");
	}
	
	private static char randomChar() {
		Random r = new Random();
		// String s = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789";
		String s = "0123456789";
		return s.charAt(r.nextInt(s.length()));
	}
	
	public static Object[] drawImg() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		String code = "";
		//log.debug("生成验证码1");
		for (int i = 0; i < 4; i++) {
			code += randomChar();
		}
		int width = 60;
		int height = 35;
		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		Font font = new Font("Times New Roman", Font.PLAIN, 20);
		Graphics2D g = bi.createGraphics();
		g.setFont(font);
		Color color = new Color(66, 2, 82);
		g.setColor(color);
		g.setBackground(new Color(226, 226, 240));
		g.clearRect(0, 0, width, height);
		//log.debug("生成验证码2");
		FontRenderContext context = g.getFontRenderContext();
		//log.debug("生成验证码20");
		Rectangle2D bounds = font.getStringBounds(code, context);
		double x = (width - bounds.getWidth()) / 2;
		double y = (height - bounds.getHeight()) / 2;
		//log.debug("生成验证码21");
		double ascent = bounds.getY();
		double baseY = y - ascent;
		g.drawString(code, (int) x, (int) baseY);
		g.dispose();
		//log.debug("生成验证码3");
		try {
			ImageIO.write(bi, "jpg", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//log.debug("生成验证码4");
		Object[] objs = new Object[2];
		objs[0] = code;
		objs[1] = output.toByteArray();
		try {
			output.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return objs;
	}

	//获取tomcat根目录 + 相对地址
	static public String getAppPath(String path){
		if(path == null) path = "";
		return System.getProperty(TOMCAT_HOME_NAME) + path;
	}
	
	/*
	 * 字节数据匹配查找，返回-1 或 匹配问题索引
	 * */
	public static int binarySearch(byte[] source,int offset,byte[] dest){
		int ret = -1;
		if(source == null || dest == null) return ret;
		int destLen = dest.length;
		int len = source.length - destLen;
		if(len < offset) return ret;
		for(int i = offset;i <= len;i++){
			int ii = i,j = 0;
			for(;j < destLen;j++){
				if(source[ii++] == dest[j]) continue;
				else break;
			}
			if(j == destLen) return i;//匹配成功
		}
		return ret;
	}
	 
	/*
	 * class资源递归遍历
	 * 查找定义包下class
	 * */
	public static void findClass(File f,Set<String>  set,String prefix){
		for(File subfile : f.listFiles()){
			if(subfile.isDirectory()) 
				findClass(subfile,set,prefix);
			else{
				String className = subfile.getName();
				if(className.endsWith(".class") &&
						prefix.equals(subfile.getParentFile().getName()))
					set.add(subfile.getPath());
			}
		}
	}
	
	/*
	 * SHA方式加密字符串
	 * @param inputStr
	 * @return
	 */
	static public String encryptSHA(String inputStr) {   
        byte[] inputData = inputStr.getBytes();
        MessageDigest sha =  null;
		try {
			sha = MessageDigest.getInstance("SHA");
			sha.update(inputData);
	        byte[] d = sha.digest();
	        BigInteger bigSha = new BigInteger(d); 
	        return bigSha.toString(32);  
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
    }   
	
	/*
	 * SHA方式加密字符串
	 * @param inputStr
	 * @return
	 */
	static public String encryptMD5(String inputStr) {   
        MessageDigest md5 =  null;
		try {
			byte[] inputData = inputStr.getBytes("utf-8");
			md5 = MessageDigest.getInstance("MD5");
	        return Base64.getEncoder().encodeToString(md5.digest(inputData));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }   
	

	public static String[] regMatches(String content, String regx) {
		List<String> strList = new ArrayList<>();
		if (content == null || regx == null) return new String[0];
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			for(int i = 1,size = matcher.groupCount();i <= size;i++)
				strList.add(matcher.group(i));
		}
		return strList.toArray(new String[0]);
	}
	/*
	 * 正则表达式匹配字符串 !
	 * @param content 源字符串
	 * @param regx 正则表达式
	 * @return 匹配的内容
	 */
	public static String regx(String content, String regx) {
		String[] results = regMatches(content,regx);
		return results.length == 0?null:results[0];
	}
	
	/*
	 * 正则表达式匹配字符串 !
	 * @param content 源字符串
	 * @param regx 正则表达式
	 * @return 匹配的内容
	 */
	public static boolean isMatches(String content, String regx) {
		if (content == null || regx == null) return false;
		Pattern p = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);
		return m.find();
	}
		
	
public static final String YMD= "yyyy-MM-dd";
	
	public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
	
	private static String[] weeks = {"天","一","二","三","四","五","六"};
	
	public static String getWeekStr(Date date){
		if(date == null) return null;
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		int w = cale.get(Calendar.DAY_OF_WEEK);
		return "星期" + weeks[w - 1];
	}
	/**
	 * 设置时间当天最大值
	 * @param date
	 * @return
	 */
	public static Date fullDate(Date date){
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		fullCale(cale);
		return cale.getTime();
	}
		
	/**
	 * 设置时间当天最大值
	 * @param cal
	 * @return
	 */
	public static Calendar fullCale(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		cal.set(Calendar.MILLISECOND,999);
		return cal;
	}
	/**
	 * 清零 时分秒毫秒
	 * @param date
	 * @return
	 */
	public static Date clearDate(Date date){
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		clearCale(cale);
		return cale.getTime();
	}
		
	
	/**
	 * 清零 时分秒毫秒
	 * @param cal
	 * @return
	 */
	public static Calendar clearCale(Calendar cal){
		//Calendar cal = Calendar.getInstance();
		//cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		return cal;
	}
		
	
	/**
	 * 日期格式为指定格式的字符串
	 * @param date 
	 * @param pattern 样式，如:yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws NullPointerException
	 */
	public static String date2str(Object obj, String pattern)
			throws NullPointerException {
		if(obj == null || (obj instanceof Date) == false) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String result = "";
		try {
			result = sdf.format((Date)obj);
		} catch (NullPointerException e) {
			throw e;
		}
		return result;
	}

	/**
	 * 字符串转换为日期 !
	 * @param date
	 * @param pattern 样式，如:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date str2date(String date, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date d  = null;
		if(StringUtils.isEmpty(date)) return null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	
private static final int HTTP_TIMEOUT = 1000 * 3;
	
	//获取文件扩展名
	public static String getExtName(String fileName){
		String ext = CommonUtils.regx(fileName, "(\\.[^\\.]+)$");
		return ext == null?null:ext.toLowerCase();
	}
	
	public static String createUUID() {
		return  UUID.randomUUID().toString().replaceAll("-", "");
	}
	/*
	 * 读取文件流
	 * */
	public static byte[] readData(String path){
		File f = new File(path);
		byte[] b = null;
		try {
			FileInputStream fin = new FileInputStream(f);
			b = new byte[fin.available()];
			fin.read(b);
			fin.close();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return b;
	}
	
	/*
	 * 读取字符文件,utf-8格式
	 * */
	public static String readDataStr(String path,String encode){
		if(encode == null) encode = "utf-8";
		try {
			byte[] data = readData(path);
			return data == null?null:new String(data,encode);
		} catch (UnsupportedEncodingException e) {
			log.debug(e.getMessage());
		}
		return "";
	}
	
	/*
	 * 读取字符文件,utf-8格式
	 * */
	public static String readDataStr(String path){
		return readDataStr(path,"utf-8");
	}
	
	/*
	 * 发送xml类型表单
	 * 接收xml类型
	 * */
	static public String postXML(String httpurl,String xmlStr) {
		byte bb[] = null;
		if(StringUtils.isEmpty(xmlStr)) return null;
		log.debug(httpurl);
		try {
			URL url = new URL(httpurl);
			HttpURLConnection urlconn = (HttpURLConnection)url.openConnection();
			urlconn.setRequestMethod("POST");
			urlconn.setDoOutput(true);
			urlconn.setConnectTimeout(HTTP_TIMEOUT);
			urlconn.setReadTimeout(HTTP_TIMEOUT);  
			urlconn.setRequestProperty("Content-Type", "application/xml");
			urlconn.connect();
			OutputStream os = urlconn.getOutputStream();
			os.write(xmlStr.getBytes("utf-8"));
			os.close();
			InputStream in = null;
			int code = urlconn.getResponseCode();
			if (code == 200) {
				in = urlconn.getInputStream();
			}else{
				in = urlconn.getErrorStream();
			}
			if(in != null){
				bb = readbodydata(in, 0);
				in.close();
			}
			urlconn.disconnect();
			return new String(bb,"utf-8");
		}catch (MalformedURLException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	/*
	 * 抓取网络信息
	 * */
	static public byte[] getHtml4Byte(String httpurl, byte b[]) {
		byte bb[] = null;
		try {
			URL url = new URL(httpurl);
			HttpURLConnection urlconn = (HttpURLConnection)url.openConnection();
			if(b == null){
				urlconn.setRequestMethod("GET");
			}else{
				urlconn.setRequestMethod("POST");
				urlconn.setDoOutput(true);
			}
			urlconn.setConnectTimeout(HTTP_TIMEOUT);
			urlconn.setReadTimeout(HTTP_TIMEOUT);  
			urlconn.setRequestProperty("Content-Type", "application/xml");
			urlconn.connect();
			if(b != null){
				OutputStream os = urlconn.getOutputStream();
				os.write(b);
				os.close();
			}
			InputStream in = null;
			int code = urlconn.getResponseCode();
			if (code == 200) {
				in = urlconn.getInputStream();
			}else{
				in = urlconn.getErrorStream();
			}
			if(in != null){
				bb = readbodydata(in, 0);
				in.close();
			}
			urlconn.disconnect();
		}catch (MalformedURLException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return bb;
	}
	/*
	 * read response body
	 */
	static public byte[] readbodydata(InputStream in, int size) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		final int DEFAULT_SIZE = 8192;
		try {
			if (size == 0)  size = DEFAULT_SIZE;
			byte[]  buf = new byte[size];
			int len = 0;
			while ((len = in.read(buf)) > 0) {
			    bout.write(buf, 0, len);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(null != in) in.close();
			} catch (IOException e) {}
		}
		return bout.toByteArray();
	}
	
	
	public static boolean writeFile(String path,byte[] body){
		File file = new File(path);
		File pfile =file.getParentFile();
		if(pfile.exists() == false)
			pfile.mkdirs();
		boolean ret = true;
		try {
			file.createNewFile();
			OutputStream out = new FileOutputStream(file);
			out.write(body);
			out.close();
		} catch (IOException e) {
			//e.printStackTrace();
			ret = false;
		}
		return ret;
	}
	
	public static boolean writeFile(String path,String body){
		try {
			return writeFile(path, body.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	

	public static int getIntValue(JSONObject jobj,String key){
		Integer x = null;
		try{
			x = jobj.getInteger(key);
		}catch(Exception ex){
			x = 0;
		}
		return x == null?0:x;
	}
	
	public static long getLongValue(JSONObject jobj,String key){
		Long x = getLong(jobj,key);
		return x == null?0:x;
	}
	
	public static Long getLong(JSONObject jobj,String key){
		Long x = null;
		try{
			x = jobj.getLong(key);
		}catch(Exception ex){
		}
		return x;
	} 
	
	
	public static float getFloatValue(JSONObject jobj,String key){
		Float x = getFloat(jobj,key);
		return x == null?0:x;
	}
	
	public static Float getFloat(JSONObject jobj,String key){
		Float x = null;
		try{
			x = jobj.getFloat(key);
		}catch(Exception ex){}
		return x;
	}
	/*
	 * 过滤json为新json
	 * */
	public static JSONObject filterJson(JSONObject srcJson,String strs) {
		JSONObject destJson = new JSONObject();
		for(String str : strs.split(",")) {
			destJson.put(str, srcJson.remove(str));
		}
		return destJson;
	}
	
	//json解析对象
	public static JSONObject getJSONObject(JSONObject jobj,String key) {
		if(jobj == null || key == null) return null;
		try{
			return jobj.getJSONObject(key);
		}catch(Exception ex){}
		return null;
	}

	/*
	 * 
	 */
	public static String log4stack(Exception ex){
		String ret = null;
		if(ex == null) return null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);
		ex.printStackTrace(ps);
		ret = new String(os.toByteArray());
		return ret;
	}
	/*
	 * 逗号分割的字符串,转化为set集合,flag true 转化为小写,false不转化
	 * */
	public static Set<String> str2Set(String strs){
		return str2Set(strs,false);
	}
	
	/*
	 * 逗号分割的字符串,转化为set集合,flag true 转化为小写,false不转化
	 * */
	public static Set<String> str2Set(String strs,boolean flag){
		Set<String> set = new HashSet<String>();
		if(StringUtils.isEmpty(strs)) return set;
		String [] aStrs = strs.split(",");
		for(String str : aStrs){
			if(StringUtils.isEmpty(str)) continue;
			if(flag) str = str.toLowerCase();
			set.add(str.trim());
		}
		return set;
	}
	
	//不分大小写遍历set
	public static boolean contains(Set<String> set,String key){
		boolean ret = false;
		if(StringUtils.isEmpty(key)) return false;
		for(String str : set){
			if(key.equalsIgnoreCase(str)){
				ret = true;
				break;
			}
		}
		return ret;
	}
		
	/*
	 * list数据转换逗号分割字符串
	 */
	public static String list2Str(List<? extends Object> list){
		StringBuilder sbStr = new StringBuilder();
		for(Object obj : list){
			if(sbStr.length() > 0)
				sbStr.append(",");
			sbStr.append(obj.toString());
		}
		return sbStr.toString();
	}
	
	/*
	 * 判断list是否为null或size == 0
	 * */
	public static boolean listIsNotNull(List<? extends Object> list){
		if(list==null) return false;
		else if(list.size() == 0) return false;
		return true;
	}
	
	/*
	 * 判断list是否为null或size == 0
	 * */
	public static boolean listIsNull(List<? extends Object> list){
		return !listIsNotNull(list);
	}
	
	
	/**
	  * 逗号分割的字符串，转换成string list
	  * @param strs
	  * @return
	  */
   public static  List<String> str2StrList(String strs){
       List<String> strlist = new ArrayList<String>();
       if(strs==null || "".equals(strs)) return strlist;
       for(String str : strs.split(",")){
           strlist.add(str);
       }
       return strlist;
   }

   /**
	  * 逗号分割的字符串，转换成Integer list
	  * @param strs
	  * @return
	  */
   public static  List<Integer> str2IntList(String strs){
       List<Integer> intlist = new ArrayList<Integer>();
       if(strs==null || "".equals(strs)) return intlist;
       for(String str : strs.split(",")){
           intlist.add(s2int(str));
       }
       return intlist;
   }
   
   /**
	  * 逗号分割的字符串，转换成long list
	  * @param strs
	  * @return
	  */
  public static  List<Long> str2longList(String strs){
      List<Long> intlist = new ArrayList<Long>();
      if(strs==null || "".equals(strs)) return intlist;
      for(String str : strs.split(",")){
          intlist.add(s2l(str));
      }
      return intlist;
  }
  
  
  /**
	 * 字符串数字格式化，错误返回0.
	 * @param snum
	 * @return
	 */
	public static int s2int(Object snum){
		int num = 0;
		if(snum == null) return num;
		try{
			num = Integer.parseInt(snum.toString());
		}catch(NumberFormatException ex){
		}
		return num;
	}
	
	/**
	 * 字符串数字格式化，错误返回0.
	 * @param snum
	 * @return
	 */
	public static Long s2l(String snum){
		Long num = 0L;
		if(snum == null) return num;
		try{
			num = Long.parseLong(snum);
		}catch(NumberFormatException ex){
		}
		return num;
	}
	
	
}
