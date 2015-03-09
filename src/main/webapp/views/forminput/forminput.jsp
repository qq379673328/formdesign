<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>表单录入-<c:out value="${title }" /></title>
<base href="<%=basePath%>" />
<style type="text/css">
	.title{
		text-align: center;
	}
	.info{
		font-size: 12px;
		padding-left: 5px;
	}
	#formcontent{
		border: 2px solid #eee;
		padding: 5px;
		border-radius: 10px;
	}
	#formcontent input{
		border: none;
		background-color: #fff;
		border-bottom: 1px solid #CD2020;
	}
	#formcontent textarea{
		border: 1px solid #CD2020;
		background-color: #fff;
	}
	#formcontent select{
		border: none;
		background-color: #fff;
		border-bottom: 1px solid #CD2020;
	}
</style>
</head>
<body>
	<form action="formdesign/formdatasave" method="post">
		<h1 class="title">${title }</h1>
		<div class="info">发布时间:<fmt:formatDate value="${dtCreate}" type="both"/></div>
		<input type="hidden" name="formid" value="${id }" />
		<br />
		<div id="formcontent">${content }</div>
		<input type="submit" name="提交" />
		<input type="reset" name="重置" />
	</form>
</body>
</html>