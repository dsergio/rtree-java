$(document).ready(function() {

	var c = document.getElementById("canvas");
	var ctx = c.getContext("2d");

	drawTree(c, ctx, null);

});

$(document).on("click", ".rectangle", function() {

	var c = document.getElementById("canvas");
	var ctx = c.getContext("2d");

	drawTree(c, ctx, $(this).attr("id"));
});

function drawTree(c, ctx, id) {

	console.log("drawTree: id: " + id);

	ctx.clearRect(0, 0, c.width, c.height);

	for (var i = 50; i < 800; i += 50) {

		ctx.beginPath();
		ctx.moveTo(i, 10);
		ctx.lineTo(i, 20);
		ctx.lineWidth = 1;
		ctx.strokeStyle = '#000000';
		ctx.stroke();
		ctx.fillText(i, i - 5, 30);
	}

	for (var i = 50; i < 800; i += 50) {
		ctx.beginPath();
		ctx.moveTo(i, 50);
		ctx.lineTo(i, 600);
		ctx.lineWidth = 1;
		ctx.strokeStyle = '#caccc9';
		ctx.stroke();
	}
	for (var i = 50; i < 600; i += 50) {
		ctx.beginPath();
		ctx.moveTo(50, i);
		ctx.lineTo(800, i);
		ctx.lineWidth = 1;
		ctx.strokeStyle = '#caccc9';
		ctx.stroke();
	}

	for (var i = 50; i < 600; i += 50) {
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

		console.log("obj: ", obj);
		//console.log("obj.x: " , obj.x);

		ctx.beginPath();
		ctx.arc(obj.x, obj.y, 5, 0, 2 * Math.PI);
		ctx.lineWidth = 1;


		ctx.strokeStyle = '#000000';
		ctx.stroke();
		ctx.fillText(obj.type, obj.x + 10, obj.y + 10);
		//ctx.fill();
	});

	$(".rectangle").each(function() {

		var obj = JSON.parse($(this).html());
		var rectangleId = $(this).attr("id");

		console.log("obj: ", obj);

		var width = Math.abs(obj.x2 - obj.x1);
		var height = Math.abs(obj.y2 - obj.y1);

		var x1 = obj.x1;
		var y1 = obj.y1;

		var depth = "N/A";
		if (typeof (obj.z1) != "undefined") {
			depth = Math.abs(obj.z2 - obj.z1);
		}

		console.log("width: " + width + ", height: " + height + ", depth: " + depth);

		ctx.beginPath();
		ctx.rect(obj.x1, obj.y1, width, height);
		ctx.lineWidth = 1;

		if (id === rectangleId) {
			ctx.strokeStyle = '#e20909';
		} else {
			ctx.strokeStyle = '#000000';
		}

		ctx.stroke();

	});

}

function drawCuboid(c, ctx, x1, x2, y1, y2, z1, z2) {

	var d = 100;
	var x1prime = x1 * (d / z1);
	var x2prime = x2 * (d / z2);

	var y1prime = y1 * (d / z1);
	var y2prime = y2 * (d / z2);

	ctx.beginPath();
	ctx.moveTo(x1prime, y1prime);
	ctx.lineTo(i, 20);
	ctx.lineWidth = 1;
	ctx.strokeStyle = '#000000';
	ctx.stroke();


}