<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html><%-- jsp加载的外壳，为了资源引用的统一 --%>
<html>
<head>
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta id="server_info" data-timestamp="<%=new java.util.Date().getTime()%>" data-timestr="${serverTimeStr}"/>
<meta http-equiv="X-UA-Compatible" content="IE=EDGE;IE=10;IE=9;IE=8"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>民用爆炸物品监管平台</title>
<%@ include file="/WEB-INF/reslib.jsp"%>
</head> 
<body data-js="${indexJs}">
<jsp:include page="${indexJsp}"/>
</body>
</html>   
