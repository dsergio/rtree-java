<template
    lang="html">
    <div class="box">
        <h1 class="title">{{ props.treeName }} Detail</h1>
        <p>Tree Name: [{{ props.treeName }}], numDimensions: {{ props.numDimensions }}</p>

        Canvas: <div id = "canvasContainer2D"></div>

        <!-- <p>{{ pointsRef }}</p>
        <p>{{ rectanglesRef }}</p> -->
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
import { Configuration, RTreeDoubleApi } from '@/generated/TypeScriptClient';

import * as $ from 'jquery';
import * as twoAppMain from '@/two/two-app-main.js';

// Define props
const props = defineProps<{
  treeName: string;
  numDimensions?: number;
}>();

props.treeName = 'tree1';



class RTreeComponentDetail {
    name: string;
    numDimensions: number;

    constructor(name: string, numDimensions: number) {
        this.name = name;
        this.numDimensions = numDimensions;
        
    }


}


const api = new RTreeDoubleApi(
  new Configuration({
    basePath: 'http://localhost:8080',
  })
);

const rtreeDetail = ref<RTreeComponentDetail | null>(null);
const pointsRef = ref<Object[]>([]);
const rectanglesRef = ref<Object[]>([]);

async function init_tree() {
    console.log('Fetching RTree details for:', props.treeName);
        const res = await api.rTreeDoubleGet(props);

        console.log(props.treeName + ' details:', res);

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
            pointsRef.value = res.points.map(point => ({ item: point}));
            rectanglesRef.value = res.rectangles.map(rectangle => ({ item: rectangle}));

            try {

                // emit detail-view
                // emit('detail-view', {
                //     name: rtreeDetail.value.name,
                //     numDimensions: rtreeDetail.value.numDimensions,
                //     points: pointsRef.value
                // });

                var eventObject = {
                    name: rtreeDetail.value.name,
                    N: props.numDimensions,
                    points: pointsRef.value,
                    rectangles: rectanglesRef.value
                };

                console.log('Emitting detail-view event with:', eventObject, eventObject.N);


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

onMounted(async () => {

    try {

        init_tree();

        $(document).on('click', '#canvasContainer2D', (event: MouseEvent) => {
            const x = event.pageX - $('#canvasContainer2D').offset().left;
            const y = event.pageY - $('#canvasContainer2D').offset().top;

            const x_normalized = x / $('#canvasContainer2D').width();
            const y_normalized = y / $('#canvasContainer2D').height();

            console.log(`Clicked at: (${x}, ${y})`);
            console.log(`Normalized coordinates: (${x_normalized}, ${y_normalized})`);


            const res = api.rTreeDoubleInsert({
                treeName: props.treeName,
                itemToInsert:{
                    dimensionArray: [x_normalized, y_normalized],
                    id: 'point-' + Date.now(), // Unique ID for the point
                    type: 'web-insert',
                    numberDimensions: props.numDimensions as number,
                    itemProperties: {}

                }
            });

            if (res instanceof Promise) {
                init_tree();
            } else {
                console.error('Insert operation did not return a Promise');
            }

            console.log('Insert response:', res);
            
        });

    } catch (error) {
        console.error('Failed to create API instance:', error);
    }
    
    

});

</script>

<style scoped>
</style>