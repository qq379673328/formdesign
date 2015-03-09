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
<title>表单数据-<c:out value="${title }" />
</title>
<base href="<%=basePath%>" />
<style type="text/css">
.title {
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
	<span style="color: red;">${info }</span>
	<h1 class="title">${title }</h1>
	<div class="info">表单发布时间: <fmt:formatDate value="${dtFormCreate}" type="both"/></div>
	<div class="info">数据上报时间: <fmt:formatDate value="${dtFormDataCreate}" type="both"/></div>
	<input type="hidden" name="formid" value="${id }" />
	<div id="formdata" style="display: none;">${formdata }</div>
	
	
	<br />
	<div id="formcontent">${content }</div>

	<script type="text/javascript"
		src="bower_components/jquery/dist/jquery.min.js"></script>
	<script type="text/javascript">
		$(function() {
			var $formdata = $("#formdata"), 
				$content = $("#formcontent");
			var data = $formdata.html();
			$formdata.html("");

			//绑定数据
			data = JSON.parse(data);
			for (var name in data) {
				var value = data[name];
				var $tags = $content.find("[name=" + name + "]");
				$tags.each(function(idx, tag){
					var $tag = $(tag);
					if($tag.prop("tagName") == "INPUT"){//input
						var tagType = $tag.attr("type");
						if(tagType == "text"){//普通文本
							$tag.val(value);
						}else if(tagType == "radio"){//radio
							if($tag.val() == value){
								$tag.attr("checked", true);
							}
						}else if(tagType == "checkbox"){
							if($tag.val() == value){
								$tag.attr("checked", true);
							}
						}
					}else if($tag.prop("tagName") == "TEXTAREA"){//textarea
						$tag.html(value);
					}else if($tag.prop("tagName") == "SELECT"){//select
						$tag.val(value);
					}
				});
			}
			
			$content.find("input, select, textarea").attr("disabled", true);
		});
	</script>

</body>
</html>