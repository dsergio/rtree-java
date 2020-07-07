<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="/RTreeWeb/css/styles.css" />
<meta charset="ISO-8859-1">
<title>RTree Detail</title>
</head>
<body>
	<h1>RTree Detail - ${treeName}</h1>

	<h3>Points</h3>
	<ul>
		<c:forEach items="${points}" var="point">
			<li class="locationItem" id="${point.id}">${point}</li>
		</c:forEach>
	</ul>

	<h3>Rectangles</h3>
	<ul>
		<%
			int c = 0;
		%>
		<c:forEach items="${rectangles}" var="r">
			<%
				c++;
			%>
			<li class="rectangle" id="rectangle_<%=c%>">${r}</li>
		</c:forEach>
	</ul>

	<canvas id="canvas" width="800" height="600"></canvas>

	<!-- JQuery -->
	<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
	
	
	<script type="text/javascript" src="/RTreeWeb/js/main.js"></script>

</body>
</html>











