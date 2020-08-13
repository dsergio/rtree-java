
import * as $ from 'jquery';
import * as THREE from 'three';
import { OrbitControls } from "three/examples/jsm/controls/OrbitControls";


var scene;
var camera;
var renderer;
var material;
var cubes;
var points;
var controls;
var clock;
var resolution;
var graph;

var colors = [
	0xed6a5a,
	0xf4f1bb,
	0x9bc1bc,
	0x5ca4a9,
	0xe6ebe0,
	0xf0b67f,
	0xfe5f55,
	0xd6d1b1,
	0xc7efcf,
	0xeef5db,
	0x50514f,
	0xf25f5c,
	0xffe066,
	0x247ba0,
	0x70c1b3
];

function init() {
	scene = new THREE.Scene();
	scene.background = new THREE.Color('white');

	//const color = 0xFFFFFF;
	//const intensity = 1;
	//const light = new THREE.DirectionalLight(color, intensity);
	//light.position.set(500, 500, 500);
	//scene.add(light);

	// var camera = new THREE.PerspectiveCamera( 75, window.innerWidth/window.innerHeight, 0.1, 1000 );
	camera = new THREE.PerspectiveCamera(100, 1, 0.1, 5000);

	renderer = new THREE.WebGLRenderer();

	var threejsWidth = 300;
	var threejsHeight = 300;
	if (document.getElementById("threejsCanvas")) {
		threejsWidth = document.getElementById("threejsCanvas").parentElement.clientWidth;
		threejsHeight = document.getElementById("threejsCanvas").parentElement.clientHeight;
	}
	
	renderer.setSize(threejsWidth * 0.8, threejsWidth * 0.8);
	//renderer.setSize(window.innerWidth / 4, window.innerWidth / 4);
	//document.body.appendChild( renderer.domElement );

	// var material = new THREE.MeshBasicMaterial( { color: 0x00ff00 } );

	//material = new THREE.MeshBasicMaterial({ color: 0xfefefe, wireframe: false, opacity: 0.5 });

	material = new THREE.MeshPhongMaterial({
		color: 0xca61ce,
		opacity: 0.5,
		transparent: true,
	});

	cubes = [];
	points = [];

	controls = new OrbitControls(camera, renderer.domElement);

	clock = new THREE.Clock();

	resolution = new THREE.Vector2(window.innerWidth, window.innerHeight);
	graph = new THREE.Object3D();
}



$(document).ready(function () {

	$(document).on("detail-view", function (e) {
		console.log("we're showing the detail view N: " + e.detail.N(), e);
		if (e.detail.N() == 3 && e.detail.numberPoints() !== 0) {

			init();
			render3DRTree();

		}
	});

	$(document).on("detail-close", function (e) {
		console.log("detail view close N: " + e.detail.N(), e);
		if (e.detail.N() == 3 && e.detail.numberPoints() !== 0) {
			clear3DRTree();
		}
	});
});

function clear3DRTree() {
	var myNode = document.getElementById("threejsCanvas");
	while (myNode && myNode.firstChild) {
		myNode.removeChild(myNode.firstChild);
	}
}

function render3DRTree() {

	document.getElementById("threejsCanvas").appendChild(renderer.domElement);

	$(".rectangle").each(function () {

		//material = new THREE.MeshPhongMaterial({
		//	color: 0xca61ce,
		//	opacity: 0.2,
		//	transparent: true,
		//});

		material = new THREE.LineBasicMaterial({ color: 0x000000, linewidth: 2 });

		var obj = JSON.parse($(this).html());
		var rectangleId = $(this).attr("id");
		var rectangleLevel = obj["level"];

		//if (rectangleLevel == 1) {
		//	material.color = new THREE.Color('red');
		//	material.opacity = 0.2;
		//} else if (rectangleLevel == 2) {
		//	material.color = new THREE.Color('orange');
		//	material.opacity = 0.3;
		//} else if (rectangleLevel == 3) {
		//	material.color = new THREE.Color('yellow');
		//	material.opacity = 0.4;
		//} else if (rectangleLevel == 4) {
		//	material.color = new THREE.Color('green');
		//	material.opacity = 0.5;
		//} else if (rectangleLevel == 5) {
		//	material.color = new THREE.Color('blue');
		//	material.opacity = 0.7;
		//} else if (rectangleLevel == 6) {
		//	material.color = 0x4B0082;
		//	material.opacity = 0.8;
		//} else {
		//	material.color = new THREE.Color('black');
		//	material.opacity = 0.9;
		//}
		console.log("rendering ", obj, " color: ", material.color);

		obj["x1"] = parseInt(obj["x1"]);
		obj["x2"] = parseInt(obj["x2"]);
		obj["y1"] = parseInt(obj["y1"]);
		obj["y2"] = parseInt(obj["y2"]);
		obj["z1"] = parseInt(obj["z1"]);
		obj["z2"] = parseInt(obj["z2"]);

		var width = Math.abs(obj["x2"] - obj["x1"]);
		var height = Math.abs(obj["y2"] - obj["y1"]);
		var depth = Math.abs(obj["z2"] - obj["z1"]);

		// var geometry = new THREE.BoxGeometry( 1, 1, 1 );
		var geometry = new THREE.BoxBufferGeometry(width, height, depth, 1, 1, 1);

		var geo = new THREE.EdgesGeometry(geometry);
		var wireframe = new THREE.LineSegments(geo, material);

		var cube = new THREE.Mesh(geometry, material);

		cube.position.x = (obj["x1"] + obj["x2"]) / 2;
		cube.position.y = (obj["y1"] + obj["y2"]) / 2;
		cube.position.z = (obj["z1"] + obj["z2"]) / 2;

		wireframe.position.x = (obj["x1"] + obj["x2"]) / 2;
		wireframe.position.y = (obj["y1"] + obj["y2"]) / 2;
		wireframe.position.z = (obj["z1"] + obj["z2"]) / 2;

		//scene.add(cube);
		scene.add(wireframe);

		cubes.push(cube);

	});

	$(".locationItem").each(function () {

		var obj = JSON.parse($(this).html());

		const radius = 2.5;
		const widthSegments = 11;
		const heightSegments = 10;
		const geometry = new THREE.SphereBufferGeometry(radius, widthSegments, heightSegments);

		material = new THREE.MeshBasicMaterial({ color: new THREE.Color('black'), wireframe: false, opacity: 1 });

		var point = new THREE.Mesh(geometry, material);
		point.position.x = obj["x"];
		point.position.y = obj["y"];
		point.position.z = obj["z"];

		points.push(point);

		scene.add(point);
	});

	var material = new THREE.LineBasicMaterial({ color: 0x0000ff });

	var lineGeo = new THREE.Geometry();
	lineGeo.vertices.push(new THREE.Vector3(0, 0, 0));
	lineGeo.vertices.push(new THREE.Vector3(800, 0, 0));
	
	var line = new THREE.Line(lineGeo, material);
	scene.add(line);

	var lineGeo = new THREE.Geometry();
	lineGeo.vertices.push(new THREE.Vector3(0, 0, 0));
	lineGeo.vertices.push(new THREE.Vector3(0, 800, 0));

	var line = new THREE.Line(lineGeo, material);
	scene.add(line);

	var lineGeo = new THREE.Geometry();
	lineGeo.vertices.push(new THREE.Vector3(0, 0, 0));
	lineGeo.vertices.push(new THREE.Vector3(0, 0, 800));

	var line = new THREE.Line(lineGeo, material);
	scene.add(line);

	//scene.add(graph);


	camera.position.z = 500;


	var animate = function () {

		requestAnimationFrame(animate);

		for (var i in cubes) {
			//cubes[i].rotation.x += 0.01;
			//cubes[i].rotation.y += 0.01;
		}
		for (var i in points) {
			//points[i].rotation.x += 0.01;
			//points[i].rotation.y += 0.01;
		}


		renderer.render(scene, camera);
	};

	animate();

};

