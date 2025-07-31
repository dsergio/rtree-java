
import * as $ from 'jquery';

console.log("two-app-main.js loaded");

$(document).ready(function () {
	$(document).on("detail-view", function (e, data) {
		console.log("we're showing the detail view: ", data, data.N, e);
		if (data.N == 2) {

            var treeName = data.name;
            var canvas;
            if (document.getElementById("canvas_2D_" + treeName) == null) {
                canvas = document.createElement('canvas');
                canvas.id = 'canvas_2D_' + treeName;
                canvas.width = 800;
                canvas.height = 600;
            } else {
                canvas = document.getElementById("canvas_2D_" + treeName);
            }
			document.getElementById("canvasContainer2D_" + treeName).appendChild(canvas);

			// var c = document.getElementById("canvas");
			var ctx = canvas.getContext("2d");

			drawTree(canvas, ctx, data.name, data.points, data.rectangles);
		}
	});
});

// function clear2DRTree() {
// 	var myNode = document.getElementById("canvasContainer2D");
// 	while (myNode && myNode.firstChild) {
// 		myNode.removeChild(myNode.firstChild);
// 	}
// }

function drawTree(c, ctx, id, points, rectangles) {

	console.log("drawTree: id: " + id);

	ctx.clearRect(0, 0, c.width, c.height);
    console.log("canvas width: ", c.width, "canvas height: ", c.height);

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

    console.log("points: ", points);


    if (points) {

        for (var i = 0; i < points.length; i++) {
            var obj = points[i]["item"];
            // console.log("obj: ", obj);
            console.log("point: ", obj["dimensionArray"][0], obj["dimensionArray"][1]);

            var x = obj["dimensionArray"][0];
            var y = obj["dimensionArray"][1];


            var x_scaled = x * (c.width - 0) + 0;
            var y_scaled = y * (c.height - 0) + 0;

            console.log("point x_scaled: ", x_scaled, "point y_scaled: ", y_scaled);
            ctx.beginPath();
            ctx.arc(x_scaled, y_scaled, 5, 0, 2 * Math.PI);
            ctx.lineWidth = 1;
            ctx.strokeStyle = '#000000';
            ctx.stroke();
            ctx.fillText(obj.type, x_scaled + 10, y_scaled + 10);
        }

    }

    if (rectangles) {
        // console.log("rectangles: ", rectangles);

        var maxDepth = 0;
        for (var i = 0; i < rectangles.length; i++) {
            var obj = rectangles[i]["item"];
            depth = obj["depth"];
            if (depth > maxDepth) {
                maxDepth = depth;
            }
        }

        for (var i = 0; i < rectangles.length; i++) {
            var obj = rectangles[i]["item"];
            // console.log("rectangle: ", obj);1

            var depth = obj["depth"];

            var x1 = obj["dimensionArray1"][0];
            var y1 = obj["dimensionArray1"][1];
            var x2 = obj["dimensionArray2"][0];
            var y2 = obj["dimensionArray2"][1];

            var x1_scaled = x1 * (c.width - 0) + 0;
            var y1_scaled = y1 * (c.height - 0) + 0;
            var x2_scaled = x2 * (c.width - 0) + 0;
            var y2_scaled = y2 * (c.height - 0) + 0;

            var width = Math.abs(x2_scaled - x1_scaled);
            var height = Math.abs(y2_scaled - y1_scaled);

            // console.log("rectangle x1: ", x1, "rectangle y1: ", y1);
            // console.log("rectangle x2: ", x2, "rectangle y2: ", y2);
            // console.log("rectangle x_scaled: ", x_scaled, "rectangle y_scaled: ", y_scaled);
            // console.log("rectangle width: ", width, "height: ", height);

            ctx.beginPath();
            ctx.fillStyle = 'rgba(0, 0, 0, ' + 0.25 * (depth / maxDepth) + ')';
            ctx.fillRect(x1_scaled, y1_scaled, width, height);
            ctx.lineWidth = 1;

            if (id === obj.id) {
                ctx.strokeStyle = '#e20909';
            } else {
                ctx.strokeStyle = '#000000';
            }

            ctx.stroke();
        }
    }

}