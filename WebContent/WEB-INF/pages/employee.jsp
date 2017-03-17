<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="script/jquery-1.7.2.js">
</script>
<script src="script/jquery.blockUI.js"></script>
<script type="text/javascript">
	$(function() {
		$(document).ajaxStart(function() {
			$.blockUI({
				message : $('#loading'),
				css : {
					top : ($(window).height() - 200) / 2 + 'px',
					left : ($(window).width() - 200) / 2 + 'px',
					width : '200px',
					border : 'none'
				},
				overlayCSS : {
					backgroundColor : '#fff'
				}
			})
		}).ajaxStop($.unblockUI);
		/*
		1.获取#city节点，添加onchange响应事件
		2.使#department只保留第一个option子节点
		3.获取#city选择的值，若该值为""，即选择的是"请选择..."，此时不需要发送ajax请求
		4.若该值不为""，说明的确是city值发生了改变，准备ajax请求
		4.1 url:EmployeeServlet?method=listDepartments
		4.2 args:locationId,time
		5.返回的是一个Json数组
		5.1若返回的数组中的元素为0：提示："当前城市没有部门"
		5.2若返回的数组中的元素不为0：遍历，创建<option value="departmentId">departmentName</option>
		并把新创建的option节点加为#department的子节点
		*/
		$("#city").change(
				function() {
					$("#empdetails").css({
						"display" : "none"
					});
					$("#department option:not(:first)").remove();
					$("#employee option:not(:first)").remove();
					var city = $(this).val();
					if (city != "") {
						var url = "EmployeeServlet?method=listDepartments";
						var args = {
							"locationId" : city,
							"time" : new Date()
						};
						$.getJSON(url, args, function(data) {
							if (data.length == "") {
								alert("当前城市没有部门");
							} else {
								$(data).each(function(i,k){
									var depId = k.departmentId;
									var depName = k.departmentName;
									$("#department").append(
											"<option value='"+depId+"'>"
													+ depName + "</option>");
								})
							}
						})
					}
				})
		$("#department").change(
				function() {
					$("#empdetails").css({
						"display" : "none"
					});
					$("#employee option:not(:first)").remove();
					var department = $(this).val();
					if (department != "") {
						var url = "EmployeeServlet?method=listEmployees";
						var args = {
							"departmentId" : department,
							"time" : new Date()
						};
						$.getJSON(url, args, function(data) {
							if (data.length == 0) {
								alert("该部门没有员工！");
							} else {
								for ( var i = 0; i < data.length; i++) {
									var employeeId = data[i].employeeId;
									var lastName = data[i].lastName;
									$("#employee").append(
											"<option value='"+employeeId+"'>"
													+ lastName + "</option>");
								}
							}
						})
					}
				});
		$("#employee").change(function() {
			var employ = $(this).val();
			if (employ != "") {
				url = "EmployeeServlet?method=listPerson";
				args = {
					"employeeId" : employ,
					"time" : new Date()
				};
				$.getJSON(url, args, function(data) {
					var email = data.email;
					var salary = data.salary;
					var employeeId = data.employeeId;
					var name = data.lastName;
					$("#empdetails").css({
						"display" : "inline-block"
					});
					$("#id").text(employeeId);
					$("#name").text(name);

					$("#email").text(email);
					$("#salary").text(salary);
				})
			} else {
				$("#empdetails").css({
					"display" : "none"
				});
			}
		})
	})
</script>
</head>
<body>
	<img id="loading" src="images/loading.gif" style="display: none">
	<center>
		<br> <br> 
		City:<select id="city">
				<option value="">请选择...</option>
				<c:forEach items="${requestScope.locations}" var="location">
					<option value="${location.locationId}">${location.city}</option>
				</c:forEach>
			</select> 
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			
		Department:<select id="department">
						<option value="">请选择...</option>
					</select> 
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			
		Employee:<select id="employee">
					<option value="">请选择...</option>
				</select> 
		<br> <br>

		<table id="empdetails" border="1" cellspacing="0" cellpadding="5"
			style="display: none">
			<tr>
				<th>Id</th>
				<th>Name</th>
				<th>Email</th>
				<th>Salary</th>
			</tr>
			<tr>
				<td id="id"></td>
				<td id="name"></td>
				<td id="email"></td>
				<td id="salary"></td>
			</tr>

		</table>

	</center>
</body>
</html>