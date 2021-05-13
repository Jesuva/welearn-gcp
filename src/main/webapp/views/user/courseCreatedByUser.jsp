<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<h3 class="title">Course Created By You</h3>
<c:if test="${not empty courses }">
<form action="update-course" method="get">
<table style="margin:auto;">
<tr>
<th>Si.No</th>
<th>Course Name</th>
<th>Course Description</th>
<th>Chapters</th>
<th>Price</th>
<th>Action</th>
</tr>
<c:forEach var="course" items="${courses }" varStatus="counter">
<tr>
<td>${counter.count }</td>
<td>${course.name }</td>
<td>${course.description }</td>
<td>${course.chapters }</td>
<td>${course.price }</td>
<td><a href="../user/${course.name }">View Details</a></td>
<td><button type="submit" name="selectedCourse" value="${course.courseId }">Edit</button></td>
</tr>
</c:forEach>
</table>
</form>
</c:if>
<c:if test="${empty courses}">
   <h3 class="title">You have not created a course yet!</h3>
</c:if>

<a href="enrollcourse" style="float:right;margin-right:10px;"><button>Back</button></a>
</div>

<br><br>
</body>
</html>