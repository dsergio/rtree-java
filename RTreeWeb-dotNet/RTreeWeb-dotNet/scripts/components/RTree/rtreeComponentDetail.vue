<template>
    <div class="modal is-active">
        <div class="modal-background"></div>
        <div class="modal-card">
            <header class="modal-card-head">
                <h2 class="modal-card-title">RTree Detail</h2>
                <button class="delete" @click="cancel" aria-label="close"></button>
            </header>
            <section class="modal-card-body">
                <div class="content">
                    <h3>{{tree.name}}</h3>
                    <div v-if="tree.points.length != 0" id="threejsCanvas"></div>
                    <div v-if="tree.points.length != 0" id="canvasContainer2D"></div>


                    <article v-if="tree.points.length == 0" class="message is-info">
                        <div class="message-header">
                            Info
                        </div>
                        <div class="message-body">This R-Tree does not contain any points. Add points below.</div>
                    </article>

                    <article v-if="tree.numDimensions > 3 || tree.numberDimensions == 1" class="message is-info">
                        <div class="message-header">
                            Info
                        </div>
                        <div class="message-body">This R-Tree cannot be visualized. Only 2D and 3D R-Trees can be visualized. However, it can still hold N-Dimensional data.</div>
                    </article>

                    <h3 v-if="tree.points.length != 0">Points</h3>
                    <div v-if="tree.points.length != 0" style="overflow-y:auto;height:200px;">
                        <ul v-for="i in tree.points">
                            <li class="locationItem" :id="i.id">{{i.json}}</li>
                        </ul>
                    </div>

                    <h3 v-if="tree.points.length != 0">Rectangles</h3>
                    <div v-if="tree.points.length != 0" style="overflow-y:auto;height:200px;">
                        <ul v-for="i in tree.rectangles">
                            <li class="rectangle">{{i.json}}</li>
                        </ul>
                    </div>
                    <h3>Insert item</h3>
                    <div v-for="i in tree.numDimensions" :key="i" class="field">
                        <label class="label">Dimension {{i - 1}}</label>
                        <div class="control">
                            <input class="input" type="text" v-model="newItemDimensionArray[i - 1]" v-on:keyup="validate" />
                        </div>
                    </div>

                    <div v-if="errors.length" class="notification is-danger">
                        <p>
                            <b>Please correct the following error(s):</b>
                            <ul>
                                <li v-for="error in errors">{{ error }}</li>
                            </ul>
                        </p>
                    </div>

                    <spinner v-if="isUpdateLoading == true"></spinner>

                </div>
            </section>
            <footer class="modal-card-foot">
                <div class="field is-grouped">
                    <div class="control">
                        <button id="submit" v-if="isUpdateLoading == false && errors.length == 0"
                                class="button is-primary"
                                @click.once="save"
                                data-telemetry-action v-bind="telemetryActionAttributes()">
                            <font-awesome-icon icon="check-circle" /> &nbsp;
                            Submit
                        </button>
                    </div>
                    <div class="control">
                        <a v-if="isUpdateLoading == false" class="button" @click="cancel">
                            <font-awesome-icon icon="window-close" /> &nbsp;
                            Cancel
                        </a>
                    </div>
                </div>
            </footer>
        </div>
    </div>
</template>
<script lang="ts">
    import { Vue, Component, Prop, Emit } from 'vue-property-decorator'
    import { RTree, RTreeClient, LocationItem } from '../../api-client.g';
    import Spinner from 'vue-spinner-component/src/Spinner.vue';


    import {
        Telemetry,
        ITelemetryDataLayer, DataLayerTMSGeneric, DataLayerGTM, DataLayerTealium,
        ITelemetryPage, ITelemetryEvent,
        TelemetryEventTMSGeneric,
        TelemetryPageTMSGeneric, TelemetryPageGTM, TelemetryPageTealium, attachDataLayerObjects
    } from '../../analyticsDataLayer/analyticsDataLayer';

    declare var apiUrl: string;


    @Component({
        components: {
            Spinner
        }
    })
    export default class RTreeComponentDetail extends Vue {
        @Prop()
        tree: RTree;
        @Prop()
        telemetry: Telemetry;
        clonedTree: RTree = <RTree>{};
        newItemDimensionArray: Array<number>;
        isUpdateLoading: boolean = false;
        errors: any = [];

        constructor() {
            super();
            this.newItemDimensionArray = new Array<number>();
            let i = 0;
            for (i; i < this.tree.numDimensions; i++) {
                this.newItemDimensionArray.push(0);
            }
        }

        async mounted() {

            let event = new CustomEvent("detail-view", {
                bubbles: true,
                detail: {
                    N: () => this.tree.numDimensions,
                    numberPoints: () => this.tree.points.length
                }
            });
            document.dispatchEvent(event);

            this.initAnalyticsDataLayer();
        }

        initAnalyticsDataLayer(): void {

            let pageName = "rtree app detail: " + this.tree.name + " N:" + this.tree.numDimensions;
            let pageId = 'spa-pageview';

            let pageTMSGeneric: ITelemetryPage = new TelemetryPageTMSGeneric(pageName, pageId, true);
            let pageGTM: ITelemetryPage = new TelemetryPageGTM(pageName, pageId, true);
            let pageTealium: ITelemetryPage = new TelemetryPageTealium(pageName, pageId, true);

            this.telemetry.getDataLayer("Generic").setPage(pageTMSGeneric);
            this.telemetry.getDataLayer("GTM").setPage(pageGTM);
            this.telemetry.getDataLayer("Tealium").setPage(pageTealium);

            console.log("adding Event: ", this.telemetry.getDataLayer("Generic").getEvent("insert-" + this.tree.name));
            attachDataLayerObjects(this.telemetry);
        }

        telemetryActionAttributes(): object {

            let eventInsert: ITelemetryEvent = new TelemetryEventTMSGeneric("insert-" + this.tree.name);
            eventInsert.addEventAttribute(0, "rtree app detail");
            eventInsert.addEventAttribute(1, "insert submit");
            eventInsert.addEventAttribute(2, this.tree.name);
            this.telemetry.getDataLayer("Generic").addEvent(eventInsert);

            console.log("event... telemetry tree.name: ", this.tree.name);
            let i: number = 0;
            let obj: any = {};
            let id = "insert-" + this.tree.name;
            console.log("event... id: " + id + " getEvent: ", this.telemetry.getDataLayer("Generic").getEvent(id));
            if (this.telemetry.getDataLayer("Generic").getEvent(id) != null) {
                for (i = 0; i < this.telemetry.getDataLayer("Generic").getEvent(id).getTelementryEventAttributeCount(); i++) {
                    let str: string = "data-telemetry-attribute-" + i;
                    obj[str] = this.telemetry.getDataLayer("Generic").getEvent(id).getTelemetryEventAttribute(i);
                }
            }
            
            return obj;
        }

        validate() {

            let reValidNumber = /^\-?[0-9]+$/;

            this.errors = [];

            for (var i = 0; i < this.tree.numDimensions; i++) {
                if (!reValidNumber.test("" + this.newItemDimensionArray[i]) || this.newItemDimensionArray[i] < -10000 || this.newItemDimensionArray[i] > 10000) {

                    this.errors.push("Dimension " + i + " must be an integer between -10000 and 10000.");
                }
            }
            
        }

        @Emit('rtree-saved')
        async save() {

            this.validate();

            if (this.errors.length == 0) {

                this.isUpdateLoading = true;
                let client = new RTreeClient(apiUrl);
                let item = new LocationItem();
                item.dimensionArray = new Array<number>();
                let i = 0;
                for (i; i < this.tree.numDimensions; i++) {
                    item.dimensionArray.push(this.newItemDimensionArray[i]);
                }
                item.numberDimensions = this.tree.numDimensions;
                console.log("newItemDimensionArray: ", this.newItemDimensionArray);
                await client.insert(item, this.tree.name);


                let event = new CustomEvent("detail-close", {
                    bubbles: true,
                    detail: {
                        N: () => this.tree.numDimensions,
                        numberPoints: () => this.tree.points.length
                    }
                });
                document.dispatchEvent(event);

                this.isUpdateLoading = false;
            }
        }

        @Emit('rtree-closed')
        cancel() {
            let event = new CustomEvent("detail-close", {
                bubbles: true,
                detail: {
                    N: () => this.tree.numDimensions,
                    numberPoints: () => this.tree.points.length
                }
            });
            document.dispatchEvent(event);
        }
    }
</script>