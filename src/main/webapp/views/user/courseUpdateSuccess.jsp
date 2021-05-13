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
<h4>Thanks for Updating the Course!</h4>
<h3>Hi ${sessionScope.name } !<br>
Thanks for Updating the course titled as " ${name } "!</h3>
<h1>Happy Teaching!</h1>
<a href="../user/view-course-created-by-you"><button>Back</button></a>
</div>
</div>
</body>
</html>