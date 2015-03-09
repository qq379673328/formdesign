<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<html ng-app="app" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="edge" />
<title>表单设计系统</title>
<base href="<%=basePath%>" />
<link rel="stylesheet" type="text/css" href="css/font-awesome.min.css" />
<link rel="stylesheet" type="text/css" href="css/main.css" />

</head>
<body>
	<div class="main">
		<div class="top">
			<div class="userinfo">
				<div class="userinfo-left"></div>
				<div class="userinfo-right"></div>
			</div>
			<div class="logo">表单设计</div>
		</div>
		<div class="body">
			<div class="menus simple-scrollbar" id="main-menus">
				<div>
					<div>
						<div class="levelchild">
							<span class="menu-flag"></span>
							<a href="#formdesign">表单设计</a>
						</div>
					</div>
				</div>
			</div>
			<div class="content" id="main-content">
				<ng-view></ng-view>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="bower_components/angular/angular.js"></script>
	<script type="text/javascript" src="bower_components/angular-route/angular-route.js"></script>
	<script type="text/javascript" src="scripts/ext/page/page.js"></script>
	
	<!-- ue -->
	<script type="text/javascript" src="scripts/libs/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="scripts/libs/ueditor/ueditor.all.js"></script>
	<script type="text/javascript" src="scripts/libs/ueditor/lang/zh-cn/zh-cn.js"></script>
	<script type="text/javascript" src="scripts/libs/ueditor/formdesign/formdesign.v4.js"></script>
		
	<script type="text/javascript" src="scripts/app.js"></script>
	
	
	
</body>
</html>