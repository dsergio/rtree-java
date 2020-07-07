<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="/RTreeWeb/css/styles.css" />

<!-- Bootstrap -->
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<meta charset="ISO-8859-1">
<title>RTree Detail</title>
</head>
<body>
	<h1>RTree Detail - ${treeName}</h1>

	<div class="container-fluid">
		<div class="row">
			<div class="col-md-6">
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
			</div>
			<div class="col-md-6">
				<div id = "threejsCanvas"></div>
			</div>
		</div>
	</div>
	
	

	<!-- JQuery -->
	<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
	
	<!-- Latest compiled and minified JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
	
	
	<script src="/RTreeWeb/js/three.js"></script>
	<script src="/RTreeWeb/js/OrbitControls.js"></script>
	<script src="/RTreeWeb/js/THREE.MeshLine.js"></script>
	
	<script type="text/javascript" src="/RTreeWeb/js/three-app-main.js"></script>


</body>
</html>











