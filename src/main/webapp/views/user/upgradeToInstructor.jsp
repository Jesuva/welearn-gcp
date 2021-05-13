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
<div class="main-container">
<h3>Request to Upgrade your Account to Instructor Account</h3>
<p>Confirm Your Password to send the Request!</p>
<form action="upgradeRequest" method="post">
<label>Name</label><br>
<input type="text" value="${sessionScope.name }" name="userName" disabled><br>
<label>Password</label><br>
<input type="password" name="password" required><br>
<input type="submit" value="Confirm">
</form>
<a href="course-enroll" style="margin-left:90%"><button>Back</button></a>
</div>

</body>
</html>