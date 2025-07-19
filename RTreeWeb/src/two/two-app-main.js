
import * as $ from 'jquery';

console.log("two-app-main.js loaded");

$(document).ready(function () {
	$(document).on("detail-view", function (e, data) {
		console.log("we're showing the detail view: ", data, data.N, e);
		if (data.N == 2) {

			var canvas = document.createElement('canvas');
			canvas.id = 'canvas';
			canvas.width = 800;
			canvas.height = 600;
			document.getElementById("canvasContainer2D").appendChild(canvas);

			var c = document.getElementById("canvas");
			var ctx = c.getContext("2d");

			drawTree(c, ctx, null, data.points, data.rectangles);
		}
	});
});

function clear2DRTree() {
	var myNode = document.getElementById("canvasContainer2D");
	while (myNode && myNode.firstChild) {
		myNode.removeChild(myNode.firstChild);
	}
}

function drawTree(c, ctx, id, points, rectangles) {

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

    console.log("points: ", points);
    if (points) {

        var x_min = 1000000;
        var x_max = -1000000;
        var y_min = 1000000;
        var y_max = -1000000;

        for (var i = 0; i < points.length; i++) {
            var obj = points[i]["item"];
            var x = obj["dimensionArray"][0];
            var y = obj["dimensionArray"][1];

            if (x < x_min) {
                x_min = x;
            }
            if (x > x_max) {
                x_max = x;
            }
            if (y < y_min) {
                y_min = y;
            }
            if (y > y_max) {
                y_max = y;
            }
        }

        for (var i = 0; i < points.length; i++) {
            var obj = points[i]["item"];
            console.log("obj: ", obj);
            console.log("point: ", obj["dimensionArray"][0], obj["dimensionArray"][1]);

            var x = obj["dimensionArray"][0];
            var y = obj["dimensionArray"][1];

            var x_normalized = (x - x_min) / (x_max - x_min);
            var y_normalized = (y - y_min) / (y_max - y_min);

            var x_scaled = x_normalized * (c.width - 100) + 50;
            var y_scaled = y_normalized * (c.height - 100) + 50;
            
            console.log("point x_normalized: ", x_normalized, "point y_normalized: ", y_normalized);
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
        console.log("rectangles: ", rectangles);

        for (var i = 0; i < rectangles.length; i++) {
            var obj = rectangles[i]["item"];
            console.log("rectangle: ", obj);

            var x1 = obj["dimensionArray1"][0];
            var y1 = obj["dimensionArray1"][1];
            var x2 = obj["dimensionArray2"][0];
            var y2 = obj["dimensionArray2"][1];

            var x1_normalized = (x1 - x_min) / (x_max - x_min);
            var y1_normalized = (y1 - y_min) / (y_max - y_min);
            var x2_normalized = (x2 - x_min) / (x_max - x_min);
            var y2_normalized = (y2 - y_min) / (y_max - y_min);

            var x1_scaled = x1_normalized * (c.width - 100) + 50;
            var y1_scaled = y1_normalized * (c.height - 100) + 50;
            var x2_scaled = x2_normalized * (c.width - 100) + 50;
            var y2_scaled = y2_normalized * (c.height - 100) + 50;

            var width = Math.abs(x2_scaled - x1_scaled);
            var height = Math.abs(y2_scaled - y1_scaled);

            console.log("rectangle x1: ", x1, "rectangle y1: ", y1);
            console.log("rectangle x2: ", x2, "rectangle y2: ", y2);
            console.log("rectangle x_normalized: ", x_normalized, "rectangle y_normalized: ", y_normalized);
            console.log("rectangle x_scaled: ", x_scaled, "rectangle y_scaled: ", y_scaled);
            console.log("rectangle width: ", width, "height: ", height);

            ctx.beginPath();
            ctx.rect(x1_scaled, y1_scaled, width, height);
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