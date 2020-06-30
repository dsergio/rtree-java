<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>RTree Detail</title>
	<!-- JQuery -->
	<script src="https://code.jquery.com/jquery-3.3.1.min.js"
	integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
	crossorigin="anonymous"></script>
	
</head>
<body>
<h1>RTree Detail - ${treeName}</h1>

<ul>
<c:forEach items="${points}" var="point">
	<li> ${point}</li>
</c:forEach>

<c:forEach items="${rectangles}" var="r">
	<li> ${r}</li>
</c:forEach>
</ul>


</body>
</html>