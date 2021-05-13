<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
 <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>WeLearn</title>
</head>
<style type="text/css">
<%@ include file="../views/css/style.css" %>
</style>
<body>
<h3 class="title">WeLearn</h3>
<hr>
<div class="main-container">
<h3 class="title">Create New Account Here!</h3>
<form:form modelAttribute="user" action="signup"  class="fomr-control">
<table>
<tr>
<td><label>Name</label>
<td><form:input path="name" /></td>
<td class="error"><form:errors path="name"></form:errors></td>
</tr>
<tr>
<td><label>Email</label></td>
<td><form:input path="email"/></td>
<td class="error"><form:errors path="email"></form:errors>${emailError }</td>
</tr>
<tr>
<td><label>Password</label></td>
<td><form:password path="password" /></td>
<td>
<div class="tooltip">i
  <span class="tooltiptext">Password should have Atleast<br> 2 uppercase letters<br>2 lowercase letters<br> 2 special characters!</span>
</div>
</td>
<td class="error"><form:errors path="password"></form:errors> ${passwordError }</td>
</tr>
</table>
<input type="submit" value="Sign Up" class="login-btn">
</form:form>
<a href="login" style="margin-left:90%;"><button>Back</button></a>
</div>
</body>
</html>