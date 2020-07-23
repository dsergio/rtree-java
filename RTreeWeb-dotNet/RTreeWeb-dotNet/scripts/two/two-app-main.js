

$(document).ready(function () {
	$(document).on("detail-view", function (e) {
		console.log("we're showing the detail view N: " + e.detail.N(), e);
		if (e.detail.N() == 2) {

			var canvas = document.createElement('canvas');
			canvas.id = 'canvas';
			canvas.width = 800;
			canvas.height = 600;
			document.getElementById("canvasContainer2D").appendChild(canvas);

			var c = document.getElementById("canvas");
			var ctx = c.getContext("2d");

			drawTree(c, ctx, null);
		}
	});
});


$(document).on("click", ".rectangle", function () {

	var c = document.getElementById("canvas");
	var ctx = c.getContext("2d");

	drawTree(c, ctx, $(this).attr("id"));
});

function drawTree(c, ctx, id) {

	console.log("drawTree: id: " + id);

	ctx.clearRect(0, 0, c.width, c.height);

	ctx.globalAlpha = 1;
	ctx.fillStyle = '#FFFFFF';
	ctx.fillRect(0, 0, c.width, c.height);
	ctx.fillStyle = '#000000';

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

	$(".locationItem").each(function () {

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

	$(".rectangle").each(function () {

		var obj = JSON.parse($(this).html());
		var rectangleId = $(this).attr("id");
		var rectangleLevel = obj["level"];

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