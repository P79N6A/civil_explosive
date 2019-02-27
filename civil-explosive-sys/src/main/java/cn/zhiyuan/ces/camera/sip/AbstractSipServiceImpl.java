package cn.zhiyuan.ces.camera.sip;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.SubjectHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Message;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import cn.zhiyuan.ces.camera.biz.ICameraConfigBiz;
import cn.zhiyuan.ces.camera.entity.CameraBean;
import cn.zhiyuan.frame.CommonUtils;
import gov.nist.javax.sip.DialogTimeoutEvent;
import gov.nist.javax.sip.EventScanner;
import gov.nist.javax.sip.EventWrapper;
import gov.nist.javax.sip.ResponseEventExt;
import gov.nist.javax.sip.SipListenerExt;
import gov.nist.javax.sip.SipStackImpl;

public abstract class AbstractSipServiceImpl implements SipListenerExt {


	protected  Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	protected ICameraConfigBiz configBiz;
	
	abstract protected void onCameraStatus(SipDevice deviceBean);
	
	/*
	 * 当前在线设备
	 * */
	protected Map<String,SipDevice> deviceMap = new ConcurrentHashMap<>();

	protected final static String CMDTYPE_KEEPLIVE = "Keepalive";
	protected final static String CMDTYPE_CATALOG = "Catalog";

	protected SipFactory sipFactory;
	protected SipProvider sipProvider;
	private SipStack sipStack;
	private ListeningPoint lp;
	
	abstract protected void processBye(RequestEvent requestEvent);
	
	abstract protected void onGpsInfo(SipDevice sipDevice,String lat,String lng);
	
	/*
	 * client request客户端请求
	 * 设备心跳
	 * 设备注册
	 * */
	@Override
	public void processRequest(RequestEvent requestEvent) {
		Request request = requestEvent.getRequest();
		String method = request.getMethod();
		log.debug("client method : " + method + "," + getDeviceId(request));
		if("BYE".equals(method)) {//bye响应,结束拉流
			processBye(requestEvent);
			return;
		}
		SipDevice deviceBean = findDevice(request);
		if(deviceBean == null) return;
		switch(method) {
		case Request.REGISTER://注册
			if(regAuth(request)) {
				deviceBean.setLogin(true);
			}
			break;
		case Request.MESSAGE:
			onMessage(request,deviceBean);
			replyRequest(request,200);
			break;
		}
	}
	
	/*
	 * 查询设备连接的摄像头
	 * */
	protected void queryCamera(String deviceId) {
		SipDevice sipDevice = deviceMap.get(deviceId);
		if(sipDevice == null) {
			log.warn("平台设备不存在");
			return;
		}
		if(!sipDevice.getOnline()) return;
		log.debug("同步设备状态:" + deviceId);
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\"?>");
		sb.append("<Query>");
		sb.append("<CmdType>Catalog</CmdType>");
		sb.append("<SN>" + sipDevice.getSeqNum() + "</SN>");
		sb.append("<DeviceID>" +  deviceId + "</DeviceID>");
		sb.append("</Query>");
		SipParams params = new SipParamsBuilder(Request.MESSAGE,sipDevice.plusSeqNum(),false)
				.setFromParams(configBiz.getSipId(), configBiz.getSipIP()
						, configBiz.getSipPort())
				.setToParams(configBiz.getSipId(),sipDevice.getDeviceIp()
						,sipDevice.getDevicePort())
				.setTag(createTag(), null)
				.setContent("application", "MANSCDP+xml", sb.toString())
				.builder();
		sendSipMsg(params);
	}
	
	/*
	 * 客户端回复服务端的请求
	 * 客户端响应
	 * */
	@Override
	public void processResponse(ResponseEvent responseEvent) {
		Response response = responseEvent.getResponse();
		ResponseEventExt responseEventExt = (ResponseEventExt)responseEvent;
		int status = response.getStatusCode();
		if(200 != status) return;
		ToHeader toheader = (ToHeader)response.getHeader(ToHeader.NAME);
		Object[]objs = querySipCamera(responseEventExt.getRemoteIpAddress()
				,responseEventExt.getRemotePort(),toheader);
		if(objs == null) return;
		SipDevice sipDevice = (SipDevice)objs[0];
		SipCamera sipCamera = (SipCamera)objs[1];
		Dialog dialog = sipCamera.getDialog();
		log.debug(status + "," + sipDevice.info() + ",dialog = " + dialog);
		if(sipCamera.isVideoEnd() || dialog == null) return;
		try {
			dialog.sendAck(dialog.createAck(sipDevice.getSeqNum()));
			sipCamera.setVideoEnd(true);
		} catch (InvalidArgumentException | SipException e) {
			log.error(e.getMessage());
		}
	}
	
	//摄像头状态消息
	private void cameraStatus(SipDevice deviceBean,Document doc) {
		List<?> nodes = doc.selectNodes("Response/DeviceList/Item");
		for(Object obj : nodes) {//遍历节点
			Element node = (Element)obj;
			String cameraId = node.elementText("DeviceID");
			SipCamera sipCamera = deviceBean.findCamera(cameraId);
			if(sipCamera == null) {//创建新的
				sipCamera = deviceBean.addCamera(cameraId, node.elementText("Name"));
			}
			//更新状态
			String status = node.elementText("Status");
			log.debug(status + "," + deviceBean.getDeviceId() + "," + cameraId);
			boolean online = false;
			if("on".equalsIgnoreCase(status)) {
				online = true;
			}
			sipCamera.setOnline(online);
			//获取gps
			String lngStr = node.elementText("Longitude");
			String latStr = node.elementText("Latitude");
			//log.debug("gps : lat = " + latStr + ",lng = " + lngStr);
			onGpsInfo(deviceBean,latStr,lngStr);
		}
		onCameraStatus(deviceBean);
	}
	
	/*
	 * sip message消息处理
	 * */
	private void onMessage(Request request,SipDevice deviceBean) {
		byte[] body = (byte[])request.getContent();
		if(body == null) return;
		Document doc;
		try {
			doc = DocumentHelper.parseText(new String(body,"gb2312"));
			Element root = doc.getRootElement();
			String cmdType = root.elementText("CmdType");
			if(CMDTYPE_CATALOG.equalsIgnoreCase(cmdType)) {//设备状态
				cameraStatus(deviceBean,doc);
			}else if(CMDTYPE_KEEPLIVE.equals(cmdType)) {//心跳
				if(!deviceBean.isLogin()) {//保持登录
					log.debug("重新认证,保持在线");
					deviceBean.setLogin(true);
				}
				queryCamera(deviceBean.getDeviceId());//同步设备状态
			}
		} catch (UnsupportedEncodingException | DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 回调开启rtpd服务接收视频流
	 * 返回监听端口
	 * */
	protected abstract int onStartVideo(CameraBean cb);
	
	public boolean startVideo28181(CameraBean cb) {
		SipDevice sipDevice = deviceMap.get(cb.getDeviceId());
		if(sipDevice == null) {
			log.warn("平台设备不存在");
			return false;
		}
		SipCamera sipCamera = sipDevice.findCameraByIndex(cb.getCameraId());
		if(sipCamera == null) {
			log.warn("摄像头不存在" + cb.getCameraId());
			return false;
		}
		//不在线
		if(!sipCamera.isOnline()) {
			log.debug("摄像头不在线:" + cb.getCameraId());
			return false;
		}
		int port = onStartVideo(cb);
		if(port < 10000) {
			log.debug("port error");
			return false;
		}
		//发送sip命令invite
		sendInvite(sipDevice,sipCamera,port);
		return true;
	}
	
	public void stopVideo(CameraBean cb) {
		SipDevice sipDevice = deviceMap.get(cb.getDeviceId());
		if(sipDevice == null) {
			log.warn("平台设备不存在");
			return;
		}
		SipCamera sipCamera = sipDevice.findCameraByIndex(cb.getCameraId());
		if(sipCamera == null || sipCamera.getDialog() == null) {
			log.warn("摄像头不存在or无视频流");
			return;
		}
		try {
			Request request = sipCamera.getDialog().createRequest(Request.BYE);
			ViaHeader viaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
			viaHeader.setHost(configBiz.getSipIP());
			ClientTransaction client = sipProvider.getNewClientTransaction(request);
			sipCamera.getDialog().sendRequest(client);
		} catch (SipException | ParseException e) {
			log.error(CommonUtils.log4stack(e));
		}finally {
			sipCamera.closeVideo();//重置
		}
	}
	
	/*
	 * 发送Invite消息,开启视频会话
	 * */
	private void sendInvite(SipDevice sipDevice
			,SipCamera sipCamera,int rtpPort) {
		log.debug("sendInvite:" + sipCamera.getCameraId());
		List<String> contents = new ArrayList<>();
		contents.add("v=0");
		contents.add("o=" + sipCamera.getCameraId() + " 0 0 IN IP4 " + configBiz.getRtpdAddr());
		contents.add("s=Play");
		contents.add("c=IN IP4 " + configBiz.getRtpdAddr());
		contents.add("t=0 0");
		contents.add("m=video " + rtpPort + " RTP/AVP 96 97 98");
		contents.add("a=recvonly");
		contents.add("a=rtpmap:96 PS/90000");
		contents.add("a=rtpmap:97 MPEG4/90000");
		contents.add("a=rtpmap:98 H264/90000");
		sipCamera.setVideoEnd(false);//设置请求标志
		SipParams params = new SipParamsBuilder(Request.INVITE,sipDevice.plusSeqNum(),true)
				.setFromParams(configBiz.getSipId(), configBiz.getSipIP()
						, configBiz.getSipPort())
				.setToParams(sipCamera.getCameraId(),sipDevice.getDeviceIp()
						,sipDevice.getDevicePort())
				.setTag(createTag(), null)
				.setContent("application", "sdp", String.join("\r\n", contents) + "\n")
				.builder();
		//保持会话窗口
		sipCamera.setDialog(sendSipMsg(params));
	}
	
	//发送sip消息:message invite
	protected Dialog sendSipMsg(SipParams sipParams) {
		try {
			HeaderFactory hf = sipFactory.createHeaderFactory();
			MessageFactory mf = sipFactory.createMessageFactory();
			AddressFactory af = sipFactory.createAddressFactory();
			Request request = mf.createRequest(null);
			String fromTag = sipParams.getFromTag();
			long seq = sipParams.getSeq();
			String method = sipParams.getMethod();
			SipURI fromUri = af.createSipURI(sipParams.getFromSipId()
					, sipParams.getFromAddrPort());
			SipURI toUri = af.createSipURI(sipParams.getToSipId()
					, sipParams.getToAddrPort());
			//请求行
			request.setMethod(method);
			request.setRequestURI(toUri);
			//头文件
			FromHeader fromHeader = hf.createFromHeader(af.createAddress(fromUri)
					,fromTag);
			request.addHeader(fromHeader);
			ToHeader toHeader = hf.createToHeader(af.createAddress(toUri),sipParams.getToTag());
			request.addHeader(toHeader);
			if(sipParams.isSubobj()) {
				SubjectHeader subObject = hf.createSubjectHeader(sipParams.getToSipId() + ":1,"
							+sipParams.getFromSipId()+":1");
				request.addHeader(subObject);
			}
			request.addHeader(hf.createCSeqHeader(seq,method));
			request.addHeader(hf.createMaxForwardsHeader(70));
			request.addHeader(hf.createCallIdHeader(fromTag));
			
			ViaHeader viaHeader = hf.createViaHeader(sipParams.getFromAddr()
					,sipParams.getFromPort(), "udp",fromTag);
			request.addHeader(viaHeader);
			request.addHeader(hf.createContactHeader(af.createAddress(fromUri)));
			
			ContentTypeHeader ctHeader = hf.createContentTypeHeader(sipParams.getContentType(),
					sipParams.getContentSubType());
			
			request.setContent(sipParams.getContent(), ctHeader);
			//sipProvider.sendRequest(request);
			ClientTransaction trans = sipProvider.getNewClientTransaction(request);
			trans.sendRequest();
			return trans.getDialog();
		} catch (ParseException | SipException | InvalidArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//发送sip消息:bye ack
	protected boolean sendSipMsgEx(SipParams sipParams,String toTag) {
		try {
			HeaderFactory hf = sipFactory.createHeaderFactory();
			MessageFactory mf = sipFactory.createMessageFactory();
			AddressFactory af = sipFactory.createAddressFactory();
			Request request = mf.createRequest(null);
			String fromTag = sipParams.getFromTag();
			long seq = sipParams.getSeq();
			String method = sipParams.getMethod();
			SipURI fromUri = af.createSipURI(sipParams.getFromSipId()
					, sipParams.getFromAddrPort());
			SipURI toUri = af.createSipURI(sipParams.getToSipId()
					, sipParams.getToAddrPort());
			/*
			 * 请求行
			 * */
			request.setMethod(method);
			//地址URI未有sipid
			request.setRequestURI(af.createSipURI(null,sipParams.getToAddrPort()));
			//头文件
			FromHeader fromHeader = hf.createFromHeader(af.createAddress(fromUri)
					,fromTag);
			request.addHeader(fromHeader);
			ToHeader toHeader = hf.createToHeader(af.createAddress(toUri),toTag);
			request.addHeader(toHeader);
			
			request.addHeader(hf.createCSeqHeader(seq,method));
			request.addHeader(hf.createMaxForwardsHeader(70));
			request.addHeader(hf.createCallIdHeader(fromTag));
			
			ViaHeader viaHeader = hf.createViaHeader(sipParams.getFromAddr()
					,sipParams.getFromPort(), "udp",fromTag);
			request.addHeader(viaHeader);
			request.addHeader(hf.createContactHeader(af.createAddress(fromUri)));
			sipProvider.sendRequest(request);
		} catch (ParseException | SipException | InvalidArgumentException e) {
			e.printStackTrace();
		}
		return true;
	}
		
	//设备认证
	protected boolean regAuth(Request request){
		try {
			//获取request Auth header
			AuthorizationHeader authHeader = (AuthorizationHeader) request.getHeader(AuthorizationHeader.NAME);
			Response response = null;
			MessageFactory messageFactory = sipFactory.createMessageFactory();
			boolean ret = false;
			if(authHeader != null) {//注册认证成功
				response = messageFactory.createResponse(Response.OK, request);
				ret = true;
			}else {//返回认证信息
				response = messageFactory.createResponse(Response.UNAUTHORIZED, request);
				HeaderFactory headerFactory = sipFactory.createHeaderFactory();
				WWWAuthenticateHeader wwwAuth = headerFactory.createWWWAuthenticateHeader("Digest");
				wwwAuth.setRealm("1234");
				wwwAuth.setNonce("7104983f5477dd30a65167b28553df65");
				wwwAuth.setOpaque("f10efb1af3e8a362bcda7465284073ce");
				wwwAuth.setAlgorithm("MD5");
				response.addHeader(wwwAuth);
			}
			//设置to header
			ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
			toHeader.setTag(System.currentTimeMillis() / 1000 + "");
			sipProvider.sendResponse(response);
			return ret;
		}catch (Exception e) {
			log.error("regauth " + e.getMessage());
		}
		return false;
	}
		
	//回去请求
	protected void replyRequest(Request request,int statusCode) {
		MessageFactory messageFactory;
		try {
			messageFactory = sipFactory.createMessageFactory();
			Response response = messageFactory.createResponse(statusCode, request);
			//设置to header
			ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
			toHeader.setTag(System.currentTimeMillis() / 1000 + "");
			sipProvider.sendResponse(response);
		} catch (ParseException | SipException e) {
			e.printStackTrace();
		}
	}
		
	//提取:xx@中间的字符
	public static String extractVal(String str) {
		return CommonUtils.regx(str, ":([^:@]+)@");
	}
	
	protected void initSip(int servPort) {
		sipFactory = SipFactory.getInstance();
		//启动sip服务
		Properties properties = new Properties();
		properties.setProperty("javax.sip.STACK_NAME", "TextClient");
		properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "8");
		try {
			sipStack = sipFactory.createSipStack(properties);
			lp = sipStack.createListeningPoint("0.0.0.0",servPort, "udp");
			sipProvider = sipStack.createSipProvider(lp);
			sipProvider.addSipListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@PreDestroy
	public void over() {
		log.debug("关闭sip服务:" + sipStack);
		if(sipStack == null) return;
		SipStackImpl sipSI = (SipStackImpl)sipStack;
		EventScanner es = sipSI.getEventScanner();
		sipStack.stop();
		//BlockingQueue线程挂死了，必须激活,然后退出线程
		es.addEvent(new EventWrapper(null, null));
		sipStack = null;
	}
	
	public static String createTag() {
		return System.currentTimeMillis() + "";
	}
	
	@Override
	public void processTimeout(TimeoutEvent timeoutEvent) {
	}

	@Override
	public void processIOException(IOExceptionEvent exceptionEvent) {
	}

	@Override
	public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
	}

	@Override
	public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
	}

	@Override
	public void processDialogTimeout(DialogTimeoutEvent timeoutEvent) {
	}

	//解析请求sip id
	private String getDeviceId(Message message) {
		if(message == null) return null;
		FromHeader fromHeader = (FromHeader)message.getHeader(FromHeader.NAME);
		if(fromHeader == null) return null;
		return extractVal(fromHeader.toString());
	}
	
	//获取设备
	private SipDevice findDevice(Message message) {
		if(message == null) return null;
		FromHeader fromHeader = (FromHeader)message.getHeader(FromHeader.NAME);
		if(fromHeader == null) return null;
		String deviceId = extractVal(fromHeader.toString());
		if(deviceId == null) return null;
		//log.debug("deviceId:"+deviceId);
		SipDevice deviceBean = deviceMap.get(deviceId);
		if(deviceBean == null) return null;
		ViaHeader viaHeader  = (ViaHeader)message.getHeader(ViaHeader.NAME);
		deviceBean.setDeviceIp(viaHeader.getReceived());
		deviceBean.setDevicePort(viaHeader.getRPort());
		deviceBean.setTick(System.currentTimeMillis());
		return deviceBean;
	}
	
	/*
	 * 查询平台摄像头
	 * */
	private Object[] querySipCamera(String ip,int port,Header header) {
		if(header == null) return null;
		String cameraId = extractVal(header.toString());
		if(cameraId == null) return null;
		SipCamera sipCamera = null;
		for(SipDevice sipDevice : deviceMap.values()) {
			if(ip.equals(sipDevice.getDeviceIp()) 
					&& port == sipDevice.getDevicePort()) {
				sipCamera = sipDevice.findCamera(cameraId);
				if(sipCamera != null) {
					Object[] objs = new Object[2];
					objs[0] = sipDevice;
					objs[1] = sipCamera;
					return objs;
				}
				break;
			}
		}
		return null;
	}

	
}
