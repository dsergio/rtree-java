<template>
    <div>
        <h2>RTree List</h2>

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
                              @rtree-saved="refresh()"
                              @rtree-closed="close()"></rtreeComponentDetail>
        <rtreeComponentCreate v-if="newTree != null"
                              :tree="newTree"
                              @rtree-created="refreshCreated()"
                              @rtree-closed="close()"></rtreeComponentCreate>
    </div>
</template>
<script lang="ts">
    import { Vue, Component } from 'vue-property-decorator';
    import Spinner from 'vue-spinner-component/src/Spinner.vue';
    import { RTree, RTreeClient } from '../../api-client.g';
    import rtreeComponentDetail from './rtreeComponentDetail.vue';
    import rtreeComponentCreate from './rtreeComponentCreate.vue';

    import { Telemetry, TelemetryConfigGA, TelemetryEventGA, TelemetryPageGA, ITelementryEvent } from '../../analyticsDataLayer/analyticsDataLayer';

    declare var apiUrl: string;


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
        newTree: RTree = null;
        rtreeClient: RTreeClient;
        isLoading: boolean = false;
        telemetry: Telemetry;
        searchQuery: string;

        constructor() {
            super();
            this.rtreeClient = new RTreeClient(apiUrl);
            this.searchQuery = "";
        }

        async mounted() {
            this.trees = await this.rtreeClient.getAll();
            this.initAnalyticsDataLayer();
            console.log("trees: ", this.trees);
        }

        initAnalyticsDataLayer(): void {

            let telemetryConfigGA: TelemetryConfigGA = new TelemetryConfigGA("UA-173118780-1");
            this.telemetry = new Telemetry(telemetryConfigGA);

            let page: TelemetryPageGA = new TelemetryPageGA("rtree app home", "123");
            this.telemetry.addPage(page);

            for (var i in this.trees) {
                let eventShow: ITelementryEvent = new TelemetryEventGA(this.trees[i].name, "rtree app", "show/edit", this.trees[i].name);
                this.telemetry.addEvent(eventShow);
            }
            
        }

        telemetryActionAttributes(t: RTree): object {
            console.log("telemetry attr: ", t);
            let i: number = 0;
            let obj: any = {};
            let id = t.name;
            if (this.telemetry.getEvent(id) != null) {
                for (i = 0; i < this.telemetry.getEvent(id).getTelementryEventAttributeCount(); i++) {
                    let str: string = "data-telemetry-attribute-" + i;
                    obj[str] = this.telemetry.getEvent(id).getTelemetryEventAttribute(i);
                }
            }
            
            return obj;
        }

        telemetryActionAttributes2(id: string): object {
            let i: number = 0;
            let obj: any = {};
            if (this.telemetry.getEvent(id) != null) {
                for (i = 0; i < this.telemetry.getEvent(id).getTelementryEventAttributeCount(); i++) {
                    let str: string = "data-telemetry-attribute-" + i;
                    obj[str] = this.telemetry.getEvent(id).getTelemetryEventAttribute(i);
                }
            }
            
            return obj;
        }

        create() {
            this.newTree = <RTree>{};
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

            this.selectedTree = await this.rtreeClient.get(tempTree.name);
            this.isLoading = false;
            tempTree = null;
        }

        async close() {
            this.selectedTree = null;
            this.newTree = null;
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