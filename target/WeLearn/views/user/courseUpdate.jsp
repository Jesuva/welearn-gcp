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
<div class="container" >
<h3 class="title">Update Course Details</h3>
<form action="update-course" method="post">
<table>
<tr>
<td><label>Course Name</label></td>
<td><input type="text" name="courseName" value="${course.name }"required></td>
<td><p class="error"> ${courseNameError }</p></td>
</tr>
<tr>
<td><label>Chapters</label></td>
<td><input type="number" min="1" name="chapters" value="${course.chapters }" required></td>
</tr>
<tr>
<td><label>Price</label></td>
<td><input type="number" min="100" name="price" value="${course.price }" required></td>
</tr>
<tr>
<td><label>Course Description</label></td>
<td><textarea style="resize:none" name="courseDescription" required>${course.description} </textarea></td>
</tr>
</table>
<input type="hidden" value="${course.courseId }" name="courseId">
<input type="submit" value="Update Course" style="margin-left:25%;">
</form>
<a href="../user/view-course-created-by-you" style="float:right;"><button>Back</button></a>
</div>
</body>
</html>