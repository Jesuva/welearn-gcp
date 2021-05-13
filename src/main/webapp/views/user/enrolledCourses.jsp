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
<h3 class="title">Courses You Enrolled!</h3>
<c:if test="${not empty courses }">
<form action="course-uneroll" method="post">
<table style="margin:auto;">
<tr>
<th>Si.No</th>
<th>Course Name</th>
<th>Course Description</th>
<th>Chapters</th>
<th>Price</th>
<th colspan="2">Action</th>
</tr>
<c:forEach var="course" items="${courses }" varStatus="counter">
<tr>
<td>${counter.count }</td>
<td>${course.properties.courseName }</td>
<td>${course.properties.description }</td>
<td>${course.properties.chapters }</td>
<td>${course.properties.price }</td>
<td><a href="../user/${course.properties.courseName }">View Details</a></td>
<td><button type="submit" name="selectedCourse" value="${course.getKey().getId() }">Unenroll</button></td>
</tr>
</c:forEach>
</table>
</form>
</c:if>
<c:if test="${empty courses }">
<h2 class="title">No Courses Available at this Time!Please Come Back Later!</h2>
</c:if>
<a href="enrollcourse" style="float:right;margin-right:10px;"><button>Back</button></a>
</div>

<br><br>
</body>
</html>