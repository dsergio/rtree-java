<template>
    <div>
        <h2 v-if="isLoading == false && trees != null">RTree List</h2>

        <spinner v-if="isLoading == true || trees == null"></spinner>

        <button v-if="isLoading == false && trees != null" class="button is-secondary" @click="create()">Create New RTree</button>
        <br /><br />
        <div class="field">
            <label v-if="isLoading == false && trees != null" class="label">Search</label>
            <div class="control">
                <input v-if="isLoading == false && trees != null" class="input" type="text" v-model="searchQuery" placeholder="Search" />
            </div>
        </div>

        <table v-if="isLoading == false" class="table">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>N</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="t in resultQuery()" :key="t.name">
                    <td>{{t.name}}</td>
                    <td>{{t.numDimensions}}</td>
                    <td>
                        <button v-if="isLoading == false" data-telemetry-action :data-telemetry-action-id="t.name" v-bind="telemetryActionAttributes(t)" class="button is-primary" @click="edit(t)">Show/Edit</button>
                        <!--<button class="button" @click="deleteTree(t)">Delete</button>-->

                    </td>
                </tr>
            </tbody>
        </table>

        <rtreeComponentDetail v-if="selectedTree != null"
                              :tree="selectedTree"
                              :telemetry="telemetry"
                              @rtree-saved="refresh()"
                              @rtree-closed="close()"></rtreeComponentDetail>
        <rtreeComponentCreate v-if="newTree != null"
                              :tree="newTree"
                              :telemetry="telemetry"
                              :treeNames="treeNames"
                              @rtree-created="refreshCreated()"
                              @rtree-closed="close()"></rtreeComponentCreate>
    </div>
</template>
<script lang="ts">
    import { Vue, Component } from 'vue-property-decorator';
    import Spinner from 'vue-spinner-component/src/Spinner.vue';
    import { RTree, RTreeClient, RTreeCreate } from '../../api-client.g';
    import rtreeComponentDetail from './rtreeComponentDetail.vue';
    import rtreeComponentCreate from './rtreeComponentCreate.vue';

    import {
        Telemetry,
        ITelemetryDataLayer, DataLayerTMSGeneric, DataLayerGTM, DataLayerTealium,
        ITelemetryPage, ITelemetryEvent,
        TelemetryEventTMSGeneric,
        TelemetryPageTMSGeneric, TelemetryPageGTM, TelemetryPageTealium, attachDataLayerObjects
    } from '../../analyticsDataLayer/analyticsDataLayer';

    declare var apiUrl: string;

    declare global {
        interface Window {
            dataLayerCollection: any;
            dataLayer: any;
            utag_data: any;
        }
    }

    @Component({
        components: {
            rtreeComponentDetail,
            rtreeComponentCreate,
            Spinner
        }
    })
    export default class RTreeComponentList extends Vue {
        trees: RTree[] = null;
        selectedTree: RTree = null;
        newTree: RTreeCreate = null;
        rtreeClient: RTreeClient;
        isLoading: boolean = false;
        telemetry: Telemetry;
        searchQuery: string;
        treeNames: string[] = [];

        constructor() {
            super();
            this.rtreeClient = new RTreeClient(apiUrl);
            this.searchQuery = "";
        }

        async mounted() {

            this.trees = await this.rtreeClient.getAll();

            for (var i in this.trees) {
                this.treeNames.push(this.trees[i].name);
            }

            let dataLayerTMSGeneric: ITelemetryDataLayer = new DataLayerTMSGeneric("Generic");
            let dataLayerGTM: ITelemetryDataLayer = new DataLayerGTM("GTM");
            let dataLayerTealium: ITelemetryDataLayer = new DataLayerTealium("Tealium");

            this.telemetry = new Telemetry("telemetryID");
            this.telemetry.addDataLayer(dataLayerTMSGeneric);
            this.telemetry.addDataLayer(dataLayerGTM);
            this.telemetry.addDataLayer(dataLayerTealium);

            this.initAnalyticsDataLayer();

            console.log("trees: ", this.trees);
        }

        initAnalyticsDataLayer(): void {

            let pageName = "rtree app home";
            let pageId = "rtree app home";

            let pageTMSGeneric: ITelemetryPage = new TelemetryPageTMSGeneric(pageName, pageId, false);
            let pageGTM: ITelemetryPage = new TelemetryPageGTM(pageName, pageId, false);
            let pageTealium: ITelemetryPage = new TelemetryPageTealium(pageName, pageId, false);

            this.telemetry.getDataLayer("Generic").setPage(pageTMSGeneric);
            this.telemetry.getDataLayer("GTM").setPage(pageGTM);
            this.telemetry.getDataLayer("Tealium").setPage(pageTealium);

            this.telemetry.getDataLayer("Generic").clearEvents();
            for (var i in this.trees) {
                let eventEditShow: ITelemetryEvent = new TelemetryEventTMSGeneric(this.trees[i].name);
                eventEditShow.addEventAttribute(0, "rtree app");
                eventEditShow.addEventAttribute(1, "show/edit");
                eventEditShow.addEventAttribute(2, this.trees[i].name);
                this.telemetry.getDataLayer("Generic").addEvent(eventEditShow);
                //console.log("created event: ");
                //eventEditShow.displayEvent();
            }

            attachDataLayerObjects(this.telemetry);
        }

        telemetryActionAttributes(t: RTree): object {
            //console.log("event... telemetry attr: ", t);
            let i: number = 0;
            let obj: any = {};
            let id = t.name;
            //console.log("event... id: " + id + " getEvent: ", this.telemetry.getDataLayer("TMSGenericID").getEvent(id));
            if (this.telemetry.getDataLayer("Generic").getEvent(id) != null) {
                for (i = 0; i < this.telemetry.getDataLayer("Generic").getEvent(id).getTelementryEventAttributeCount(); i++) {
                    let str: string = "data-telemetry-attribute-" + i;
                    obj[str] = this.telemetry.getDataLayer("Generic").getEvent(id).getTelemetryEventAttribute(i);
                }
            }
            
            return obj;
        }

        create() {
            this.newTree = <RTreeCreate>{};
            this.newTree.numDimensions = 3;
            this.newTree.maxChildren = 4;
            this.newTree.maxItems = 4;
        }

        async edit(tree: RTree) {
            this.isLoading = true;
            tree = await this.rtreeClient.get(tree.name);
            this.selectedTree = tree;
            this.isLoading = false;
        }

        async refresh() {
            let tempTree = this.selectedTree;
            this.selectedTree = null;
            this.isLoading = true;
            this.selectedTree = await this.rtreeClient.get(tempTree.name);
            this.isLoading = false;
            tempTree = null;
        }

        async refreshCreated() {
            let tempTree = this.newTree;
            this.selectedTree = null;
            this.newTree = null;
            this.isLoading = true;

            this.trees = await this.rtreeClient.getAll();
            this.initAnalyticsDataLayer();

            this.selectedTree = await this.rtreeClient.get(tempTree.treeName);
            this.isLoading = false;
            tempTree = null;
        }

        async close() {
            this.selectedTree = null;
            this.newTree = null;
            this.initAnalyticsDataLayer();
        }

        resultQuery() {

            if (this.searchQuery != "") {
                return this.trees.filter((item) => {
                    return this.searchQuery.toLowerCase().split(' ').every(v => item.name.toLowerCase().includes(v))
                });

            } else {
                return this.trees;
            }
        }
    }

</script>