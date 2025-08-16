<template
    lang="html">
    <div class="box">
        <h1 class="title">{{ props.numDimensions }}-D {{ props.treeName }}</h1>

        <BaseButton :disabled="loading" class="button is-primary" @click="generate_random_data">
            <span class="spinner" v-if="loading"></span> 
            <span v-if="loading">Generating {{ gen_count }} random location items</span>
            <span v-else>Generate random location items</span>
        </BaseButton>

        <div :id = "'canvasContainer2D_' + props.treeName"></div>
        
        <div v-if="props.numDimensions == 3" id="threejsCanvas"></div>

        <ul class="list">
            <li class ="list-item">
               generate: {{ props.generateRandomData }}
            </li>
            <li class ="list-item">
                dataSet: {{ props.dataSet }}
            </li>
            <li class ="list-item">
                numDimensions: {{ props.numDimensions }}
            </li>
            <li class ="list-item">
                treeName: {{ props.treeName }}
            </li>
            <li class ="list-item">
                locationCount: {{ props.locationCount }}
            </li>
        </ul>

        <h3 v-if="pointsRef.length != 0">Points</h3>
        <div v-if="pointsRef.length != 0" style="overflow-y:auto;height:200px;">
            <ul v-for="i in pointsRef" :key="i.id">
                <li class="locationItem" :id="i.id">{{i.item}}</li>
            </ul>
        </div>
        <h3 v-if="rectanglesRef.length != 0">Rectangles</h3>
        <div v-if="rectanglesRef.length != 0" style="overflow-y:auto;height:200px;">
            <ul v-for="i in rectanglesRef" :key="i.id">
                <li class="rectangle">{{i.item}}</li>
            </ul>
        </div>
        
    </div>
</template>

<script setup lang="ts">

import { ref, onMounted, defineProps, defineComponent } from 'vue';

import { api } from '@/config';
import BaseButton from '../BaseButton.vue';

import * as $ from 'jquery';
import * as twoAppMain from '@/two/two-app-main.js';

// Define props
const props = defineProps<{
  treeName: string;
  numDimensions?: number;
  dataSet?: string;
  generateRandomData?: boolean;
  locationCount: number;
}>();

class RTreeComponentDetail {
    name: string;
    numDimensions: number;

    constructor(name: string, numDimensions: number) {
        this.name = name;
        this.numDimensions = numDimensions;
    }

}

class Point {
    id: string;
    item: Object;

    constructor(id: string, item: Object) {
        this.id = id;
        this.item = item;
    }
}

class Rectangle {
    id: string;
    item: Object;

    constructor(id: string, item: Object) {
        this.id = id;
        this.item = item;
    }
}

const rtreeDetail = ref<RTreeComponentDetail | null>(null);
const pointsRef = ref<Point[]>([]);
const rectanglesRef = ref<Rectangle[]>([]);

const loading = ref<boolean>(false);
var gen_count = ref<number>(0);

async function init_tree(res?: any) {
    console.log('props:', props);

    if (!res) {

        try {
            res = await api.rTreeDoubleGet(props);
        } catch (error) {
            console.error('Failed to fetch RTree details:', error);
            return;
        }
    } else {
        res = await res;
    }
    

    console.log('tree ' + props.treeName + ' res:', res);

    if (res) {
        rtreeDetail.value = new RTreeComponentDetail(props.treeName, props.numDimensions as number);

        if (res.points === undefined || res.points === null) {
            console.error('Points data is undefined or null');
            pointsRef.value = [];
            return;
        }
        if (res.rectangles === undefined || res.rectangles === null) {
            console.error('Rectangles data is undefined or null');
            rectanglesRef.value = [];
            return;
        }
        pointsRef.value = [];
        rectanglesRef.value = [];
        pointsRef.value = res.points.map((point: any) => ({ item: point}));
        rectanglesRef.value = res.rectangles.map((rectangle: any) => ({ item: rectangle}));

        try {

            var eventObject = {
                name: rtreeDetail.value.name,
                N: props.numDimensions,
                points: pointsRef.value,
                rectangles: rectanglesRef.value
            };

            // console.log('Emitting detail-view event with:', eventObject, eventObject.N);
            
            $(document).trigger('detail-view', eventObject);

        } catch (error) {
            console.error('Failed to emit detail-view event:', error);
        }

    } else {
        console.error('Failed to fetch RTree details');
        rtreeDetail.value = null; // Reset if no data is returned
        return;
    }
};

const generate_random_data = async () => {

    gen_count.value = props.locationCount;

    if (props.generateRandomData) {
        
        let numPoints = props.locationCount;

        let count = 0;

        loading.value = true;

        const intervalId = setInterval(async () => {
            count++;
            console.log(`Count: ${count}`);
            gen_count.value = numPoints - count;

            var obj = {

                treeName: props.treeName,
                locationItemDouble: {
                    dimensionArray: [Math.random(), Math.random()],
                    id: 'point-' + Date.now(), // Unique ID for the point
                    type: 'web-insert',
                    numberDimensions: 2,
                    itemProperties: {}
                }

            };
            var res;
            if (props.dataSet == "geo") {
                res = await api.rTreeDoubleInsertGeo(obj);
            } else {
                res = await api.rTreeDoubleInsert(obj);
            }

            

            init_tree();

            if (count >= numPoints) {
                clearInterval(intervalId);
                loading.value = false;
            }
        }, 500);
        
    }
};

onMounted(async () => {

    try {

        if (props.generateRandomData) {
            
            console.log("Detail view for geographical R-Tree:", props.treeName);
            generate_random_data();

        } else {

            init_tree();

            $(document).on('click', '#canvasContainer2D_' + props.treeName, (event: MouseEvent) => {

                var c = document.getElementById("canvas_2D_" + props.treeName);
                var canvas_height = 0;
                var canvas_width = 0;
                if (c != null) {
                    canvas_height = c.clientHeight;
                    canvas_width = c.clientWidth;
                } else {
                    console.error('Canvas element not found');
                    return;
                }

                const x = event.pageX - $('#canvasContainer2D_' + props.treeName).offset().left;
                const y = event.pageY - $('#canvasContainer2D_' + props.treeName).offset().top;

                const x_normalized = x / canvas_width;
                const y_normalized = y / canvas_height;

                console.log(`[INSERT Web] Clicked at: (${x}, ${y})`);
                console.log(`[INSERT Web] Normalized coordinates: (${x_normalized}, ${y_normalized})`);
                console.log('[INSERT Web] test: ', canvas_height, canvas_width);
                console.log(`[INSERT Web] props: `, props);

                try {

                    var obj = {
                        treeName: props.treeName,
                        locationItemDouble: {
                            dimensionArray: [x_normalized, y_normalized],
                            id: 'point-' + Date.now(), // Unique ID for the point
                            type: 'web-insert',
                            numberDimensions: props.numDimensions as number,
                            itemProperties: {}

                        }
                    }

                    var res;
                    if (props.dataSet == "geo") {
                        res = api.rTreeDoubleInsertGeo(obj);
                    } else {
                        res = api.rTreeDoubleInsert(obj);
                    }
                    
                    if (res instanceof Promise) {
                        init_tree(res);
                    } else {
                        console.error('[INSERT Web] Insert operation did not return a Promise');
                    }

                    console.log('[INSERT Web] Insert response:', res);

                } catch (error) {
                    console.error('[INSERT Web] Failed to insert point:', error);
                    return;
                }

            });

        }

    } catch (error) {
        console.error('Failed to create API instance:', error);
    }

});

</script>

<style scoped>

.spinner {
            border: 2px solid #f3f3f3; /* Light grey */
            border-top: 2px solid #3498db; /* Blue */
            border-radius: 50%;
            width: 16px;
            height: 16px;
            animation: spin 0.6s linear infinite;
            display: inline-block;
            margin-right: 5px; /* Space between spinner and text */
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

</style>