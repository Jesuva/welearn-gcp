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
<h3 class="title">Available Courses</h3>
<c:if test="${not empty courses }">
<form action="course-enroll" method="post">
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
<td>${course.properties.courseName }</td>
<td>${course.properties.description }</td>
<td>${course.properties.chapters }</td>
<td>${course.properties.price }</td>
<td><a href="../user/${course.getKey().getId() }">View Details</a></td>
<td><button type="submit" name="selectedCourse" value="${course.getKey().getId() }">Enroll</button></td>
</tr>
</c:forEach>
</table>
</form>
</c:if>
<c:if test="${empty courses }">
<h2 class="title">No Courses Available at this Time!Please Come Back Later!</h2>
</c:if>
<div style="margin-top:20px;">
<a href="../user/view-course-created-by-you" style="float:right;margin-left:10px;"><button>View Courses Created By You</button></a>
<a href="enrolled-courses" style="float:right;"><button>View Enrolled Courses</button></a>
<a href="add-course" style="float:right;margin-right:10px;"><button>Add Course</button></a>
</div>
</div>

<br><br>
</body>
</html>