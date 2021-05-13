<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>WeLearn</title>
</head>
<style type="text/css">
<%@ include file="../css/style.css" %>
</style>
<body>
<%@ include file="../partials/header.jsp" %>
<div class="container">
<h2 class="title">WeLearn</h2>
<div class="confirm-course-container">
<h4>You have UnEnrolled from a Course!</h4>
<h3>Hi ${sessionScope.name } !<br>
You have unenrolled from a  course titled as " ${name } " from your Learning List!</h3>
<h1>Enroll in some other Course!Happy Learning!</h1>
<a href="/user/enrollcourse"><button>View All Course</button></a>
</div>
</div>
</body>
</html>