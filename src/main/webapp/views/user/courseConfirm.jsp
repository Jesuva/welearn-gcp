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
<h4>Thanks for Subscribing!</h4>
<h3>Hi <% out.print((String)session.getAttribute("name")); %> !<br>
You have enrolled for the <% out.print((String)session.getAttribute("course")); %> course!<br> Author Note: <% out.print((String)session.getAttribute("description")); %></h3>
<h1>Happy Learning!</h1>
<a href="/DWP/user/course-enroll"><button>Back To course</button></a>
</div>
</div>
</body>
</html>