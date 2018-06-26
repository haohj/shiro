<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改商品信息</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.form.min.js"></script>

<script type="text/javascript">
    
    
    
     
	//请求json响应json
	function request_json(){
		$.ajax({
			type:"post",
			url:"${pageContext.request.contextPath }/item/editItemSubmit_RequestJson.action",
			contentType:"application/json;charset=utf-8",
			data:'{"name":"测试商品","price":99.9}',
			success:function(data){
				alert(data);
			}
		});
	} 


	//请求form响应json
	/*  function response_json(){
		$.ajax({
			type:"post",
			url:"${pageContext.request.contextPath }/item/editItemSubmit_ResponseJson.action",
			data:"name=测试商品&price=99.9",
			success:function(data){
				alert(data);
			}
			
		});
	}  */

	 function response_json() {
		//form对象
		var formObj = $("#itemForm");
		//执行ajax提交
		formObj.ajaxSubmit({
			dataType : "json",//设置预期服务端返回json
			success : function(responseText) {
				alert(responseText);
			}
		});
	} 
</script>
</head>
<body>
<spring:hasBindErrors name="item">
<c:forEach items="${errors.allErrors}" var="error">
	<spring:message var="message" code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}"/>
	${message }<br/>
	${error.defaultMessage }<br/>
</c:forEach>
</spring:hasBindErrors>
<%-- <form:form commandName="item">
<form:errors path="*" cssStyle="color:red"></form:errors><br/>
</form:form> --%>
	<form id="itemForm"
		action="${pageContext.request.contextPath }/item/editItemSubmit.action"
		method="post" enctype="multipart/form-data">
		<input type="hidden" name="id" value="${item.id }" />
		<input type="hidden" name="pic" value="${item.pic }" />
		 修改商品信息：
		<table width="100%" border=1>
			<tr>
				<td>商品名称</td>
				<td><input type="text" name="name" value="${item.name }" /></td>
			</tr>
			<tr>
				<td>商品价格</td>
				<td><input type="text" name="price" value="${item.price }" /></td>
			</tr>
			<tr>
				<td>商品生产日期</td>
				<td><input type="text" name="createtime"
					value="<fmt:formatDate value="${item.createtime}" pattern="yyyy-MM-dd HH-mm-ss"/>" /></td>
			</tr>
			<%-- <tr>
				<td>商品图片</td>
				<td><c:if test="${item.pic !=null}">
						<img src="/pic/${item.pic}" width=100 height=100 />
						<br />
					</c:if> <input type="file" name="pictureFile" /></td>
			</tr> --%>
			
			<tr>
				<td>商品图片</td>
				<td>
				<c:forEach items="${item.piclist }" var="pic">
					<c:if test="${pic !=null}">
						<img src="/pic/${pic}" width=100 height=100 />
						<br />
					</c:if>
				</c:forEach>
				
				 <input type="file" name="pictureFile"/>
				 <input type="file" name="pictureFile"/>
				 <input type="file" name="pictureFile"/>
				 </td>
			</tr>
			<tr>
				<td>商品简介</td>
				<td><textarea rows="3" cols="30" name="detail">${item.detail }</textarea>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
				<input type="submit" value="提交" />
				<input type="button" value="ajax提交，提交json串" onclick="request_json()" />
				<input type="button" value="ajax提交，普通form提交" onclick="response_json()" />
				</td>
			</tr>
		</table>

	</form>
</body>

</html>