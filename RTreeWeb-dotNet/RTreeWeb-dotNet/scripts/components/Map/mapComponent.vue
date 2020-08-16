<template>
    <div id="mapContainer">
        <canvas id="mapCanvas"></canvas>
    </div>
</template>
<script lang="ts">
    import { Vue, Component } from 'vue-property-decorator';
    import Spinner from 'vue-spinner-component/src/Spinner.vue';
    import { RTreeDouble, RTreeDoubleClient, LocationItemDouble, RectangleDouble } from '../../api-client.g';


    declare var apiUrl: string;

    @Component({
        components: {
            Spinner
        }
    })
    export default class MapComponent extends Vue {
        tree: RTreeDouble = null;
        rtreeClient: RTreeDoubleClient;
        c: any;
        ctx: any;

        latitude1: number;
        longitude1: number;
        latitude2: number;
        longitude2: number;
        points: LocationItemDouble[];
        searchResults: LocationItemDouble[];
        searchQuery: RectangleDouble;
        treeName: string;
        mapWidth: number;
        mapHeight: number;

        mouseMoveStartX: number;
        mouseMoveStartY: number;

        coordsChanged: boolean = true;

        x_offset: number = 0;
        y_offset: number = 0;
        isMouseDown: boolean = false;

        constructor() {
            super();
            this.treeName = "wacities1";
           
            this.latitude1 = 47.301960;
            this.latitude2 = 47.969599;
            this.longitude1 = -117.882374;
            this.longitude2 = -117.069398;
            

            this.rtreeClient = new RTreeDoubleClient(apiUrl);
            
        }

        async mounted() {
            this.c = document.getElementById("mapCanvas");
            this.ctx = this.c.getContext("2d");

            this.mapWidth = document.getElementById("mapContainer").clientWidth * 0.8;
            this.mapHeight = window.innerHeight * 0.7;

            this.c.width = this.mapWidth;
            this.c.height = this.mapHeight;

            this.displayMap();
            this.c.addEventListener("wheel", this.zoom, false);

            this.c.addEventListener("mousedown", this.mouseDown, false);
            this.c.addEventListener("mouseup", this.mouseUp, false);
            this.c.addEventListener("mousemove", this.mouseMove, false);

            setInterval(this.displayMap, 500);


        }

        async displayMap() {

            if (!this.coordsChanged) {
                return;
            }
            this.coordsChanged = false;

            this.searchQuery = new RectangleDouble({
                "dimensionArray1": [this.longitude1, this.latitude1],
                "dimensionArray2": [this.longitude2, this.latitude2],
                "numberDimensions": 2
            });

            //this.tree = await this.rtreeClient.get(this.treeName);
            this.searchResults = await this.rtreeClient.search(this.searchQuery, this.treeName)
            this.redraw();
        }
        redraw() {

            var latRange = this.latitude2 - this.latitude1;
            var longRange = this.longitude2 - this.longitude1;


            this.ctx.clearRect(0, 0, this.c.width, this.c.height);

	        this.ctx.globalAlpha = 1;
	        this.ctx.fillStyle = '#FFFFFF';
	        this.ctx.fillRect(0, 0, this.c.width, this.c.height);
	        this.ctx.fillStyle = '#000000';

            for (var i = 50; i < this.mapWidth; i += 50) {

		        this.ctx.beginPath();
		        this.ctx.moveTo(i, 10);
		        this.ctx.lineTo(i, 20);
		        this.ctx.lineWidth = 1;
		        this.ctx.strokeStyle = '#000000';
                this.ctx.stroke();
                var longi = this.longitude1 + longRange * (i / this.mapHeight);
                this.ctx.fillText(longi.toFixed(3), i - 5, 30);
	        }

            for (var i = 50; i < this.mapWidth; i += 50) {
		        this.ctx.beginPath();
                this.ctx.moveTo(i, 50);
                this.ctx.lineTo(i, this.mapHeight);
		        this.ctx.lineWidth = 1;
		        this.ctx.strokeStyle = '#caccc9';
		        this.ctx.stroke();
            }
            for (var i = 50; i < this.mapHeight; i += 50) {
		        this.ctx.beginPath();
                this.ctx.moveTo(50, i);
                this.ctx.lineTo(this.mapWidth, i);
		        this.ctx.lineWidth = 1;
		        this.ctx.strokeStyle = '#caccc9';
		        this.ctx.stroke();
	        }

            for (var i = 50; i < this.mapHeight; i += 50) {
		        this.ctx.beginPath();
		        this.ctx.moveTo(10, i);
		        this.ctx.lineTo(20, i);
		        this.ctx.lineWidth = 1;
		        this.ctx.strokeStyle = '#000000';
                this.ctx.stroke();
                var lati = this.latitude2 - latRange * (i / this.mapHeight);
                this.ctx.fillText(lati.toFixed(3), 30, i + 5);
            }

             //console.log("searchResults: ", this.searchResults);


            for (var j in this.searchResults) {

                
                var item: LocationItemDouble = this.searchResults[j];
                //console.log("city: " + item.type + " longitude: " + item.dimensionArray[0] + " latitude: " + item.dimensionArray[1]);

                var x = ((item.dimensionArray[0] - this.longitude1) / longRange) * this.mapWidth;
                var y = this.mapHeight - ((item.dimensionArray[1] - this.latitude1) / latRange) * this.mapHeight;


                x += this.x_offset;
                y += this.y_offset;

                this.ctx.beginPath();
		        this.ctx.arc(x, y, 5, 0, 2 * Math.PI);
		        this.ctx.lineWidth = 1;


		        this.ctx.strokeStyle = '#000000';
		        this.ctx.stroke();
		        this.ctx.fillText(item.type, x + 10, y + 10);
            }

            
        }

        zoom(e: WheelEvent) {
            e.preventDefault();
            console.log("event: ", e);

            

            if (e.deltaY < 0) {
                this.latitude1 += 0.15;
                this.latitude2 -= 0.15;
                this.longitude1 += 0.15;
                this.longitude2 -= 0.15;
            } else {
                this.latitude1 -= 0.15;
                this.latitude2 += 0.15;
                this.longitude1 -= 0.15;
                this.longitude2 += 0.15;
            }

            

            this.coordsChanged = true;

        }

        mouseMove(e: MouseEvent) {
            e.preventDefault();

            if (this.isMouseDown) {

                this.x_offset = e.x - this.mouseMoveStartX;
                this.y_offset = e.y - this.mouseMoveStartY;

                this.redraw();
            }
            
        }

        mouseDown(e: MouseEvent) {
            e.preventDefault();
            this.mouseMoveStartX = e.x;
            this.mouseMoveStartY = e.y;

            this.isMouseDown = true;

            //console.log("mousedown - event: ", e);
            
        }
        mouseUp(e: MouseEvent) {
            e.preventDefault();
            var x_moved = e.x - this.mouseMoveStartX;
            var y_moved = e.y - this.mouseMoveStartY;

            this.isMouseDown = false;
            this.x_offset = 0;
            this.y_offset = 0;

            var latRange = this.latitude2 - this.latitude1;
            var longRange = this.longitude2 - this.longitude1;

            var longChange = (x_moved / this.mapWidth) * longRange;
            this.longitude1 -= longChange;
            this.longitude2 -= longChange;

            var latChange = (y_moved / this.mapHeight) * latRange;
            this.latitude1 += latChange;
            this.latitude2 += latChange;

            this.coordsChanged = true;
            console.log("mouseup - event: ", e);
            
        }
    }

</script>