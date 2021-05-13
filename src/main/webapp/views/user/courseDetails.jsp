<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>WeLearn</title>
<style type="text/css">
<%@ include file="../css/style.css" %>
</style>
</head>
<body>
<%@ include file="../partials/header.jsp" %>
<div class="container">
<h2 class="title">WeLearn</h2>
<div class="confirm-course-container">
<h3>Hi ${sessionScope.name } !<br></h3>
<h4>Course Title: ${course.name }</h4>

<h3>Course Description :  ${course.description } </h3>
<h3>Happy Learning!</h3>
<a href="enrollcourse"><button>Back To course</button></a>
</div>
</div>

</body>
</html>