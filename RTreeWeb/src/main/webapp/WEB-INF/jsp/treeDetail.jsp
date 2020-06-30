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
	
	
	<script type = "text/javascript">
	
		$(document).ready(function() {
			
			var c = document.getElementById("canvas");
			var ctx = c.getContext("2d");
			
			for (var i = 50; i < 800; i+= 50) {
				
				ctx.beginPath();
				ctx.moveTo(i, 10);
				ctx.lineTo(i, 20);
				ctx.lineWidth = 1;
				ctx.strokeStyle = '#000000';
				ctx.stroke();
				ctx.fillText(i, i - 5, 30);
			}
			
			for (var i = 50; i < 800; i+= 50) {
				ctx.beginPath();
				ctx.moveTo(i, 50);
				ctx.lineTo(i, 600);
				ctx.lineWidth = 1;
				ctx.strokeStyle = '#caccc9';
				ctx.stroke();
			}
			for (var i = 50; i < 600; i+= 50) {
				ctx.beginPath();
				ctx.moveTo(50, i);
				ctx.lineTo(800, i);
				ctx.lineWidth = 1;
				ctx.strokeStyle = '#caccc9';
				ctx.stroke();
			}
			
			for (var i = 50; i < 600; i+= 50) {
				ctx.beginPath();
				ctx.moveTo(10, i);
				ctx.lineTo(20, i);
				ctx.lineWidth = 1;
				ctx.strokeStyle = '#000000';
				ctx.stroke();
				ctx.fillText(i, 30, i + 5);
			}
			
			$(".locationItem").each(function() {
				
				var obj = JSON.parse($(this).html());
				
				console.log("obj: " , obj);
				//console.log("obj.x: " , obj.x);
				
				ctx.beginPath();
				ctx.arc(obj.x, obj.y, 5, 0, 2 * Math.PI);
				ctx.lineWidth = 1;
				ctx.strokeStyle = '#000000';
				ctx.stroke();
				//ctx.fill();
			});
			
			$(".rectangle").each(function() {
				
				var obj = JSON.parse($(this).html());
				
				console.log("obj: " , obj);
				
				var width = Math.abs(obj.x2 - obj.x1);
				var height = Math.abs(obj.y2 - obj.y1);
				
				console.log("width: " + width + ", height: " + height);
				
				ctx.beginPath();
				ctx.rect(obj.x1, obj.y1, width, height);
				ctx.lineWidth = 1;
				ctx.strokeStyle = '#000000';
				ctx.stroke();
				
			});
			
			
			
		});
		
	</script>
	
	<style>
		#canvas {
		    border: 1px solid blue;
		} 
	</style>
	
</head>
<body>
<h1>RTree Detail - ${treeName}</h1>

<h3>Points</h3>
<ul>
<c:forEach items="${points}" var="point">
	<li class = "locationItem">${point}</li>
</c:forEach>
</ul>

<h3>Rectangles</h3>
<ul>
<c:forEach items="${rectangles}" var="r">
	<li class = "rectangle">${r}</li>
</c:forEach>
</ul>

<canvas id = "canvas" width = "800" height = "600"></canvas>

</body>
</html>