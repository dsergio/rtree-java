<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/styles.css"/>
<meta charset="ISO-8859-1">
<title>RTree List</title>
</head>
<body>
<h1>RTree List</h1>

<ul>
<c:forEach items="${result}" var="item">
	<li> <a href = "/RTreeWeb/get/${item.name}">${item}</a></li>
</c:forEach>
</ul>
</body>
</html>