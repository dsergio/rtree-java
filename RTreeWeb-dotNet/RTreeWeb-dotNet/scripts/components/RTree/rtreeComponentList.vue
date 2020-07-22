<template>
    <div>
        <h2>RTree List</h2>
        <!--<button class="button is-secondary" @click="create()">Create New RTree</button>-->
        <table class="table">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>N</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="t in trees" :key="t.name">
                    <td>{{t.name}}</td>
                    <td>{{t.numDimensions}}</td>
                    <td>
                        <button data-telemetry-action :data-telemetry-action-id="t.name" v-bind="telemetryActionAttributes(t)" class="button is-primary" @click="edit(t)">Show/Edit</button>
                        <!--<button class="button" @click="deleteTree(t)">Delete</button>-->

                    </td>
                </tr>
            </tbody>
        </table>
        <!--<spinner v-if="isLoading == true"></spinner>-->
        <spinner v-if="isLoading == true"></spinner>
        <spinner v-if="trees == null"></spinner>
        <rtreeComponentDetail v-if="selectedTree != null"
                              :tree="selectedTree"
                              @rtree-saved="refresh()"></rtreeComponentDetail>
    </div>
</template>
<script lang="ts">
    import { Vue, Component } from 'vue-property-decorator';
    import Spinner from 'vue-spinner-component/src/Spinner.vue';
    //import { RTree2, RTreeClient2 } from '../../client';
    import { RTree, RTreeClient } from '../../api-client.g';
    import rtreeComponentDetail from './rtreeComponentDetail.vue';

    import { Telemetry, TelemetryConfigGA, TelemetryEventGA, TelemetryPageGA, ITelementryEvent } from '../../analyticsDataLayer/analyticsDataLayer';

    declare var apiUrl: string;

    //apiUrl = "http://dsergio-rtree-api-boot.azurewebsites.net";
    //apiUrl = "http://localhost:8080";

    @Component({
        components: {
            rtreeComponentDetail,
            Spinner
        }
    })
    export default class RTreeComponentList extends Vue {
        trees: RTree[] = null;
        selectedTree: RTree = null;
        rtreeClient: RTreeClient;
        isLoading: boolean = false;

        telemetry: Telemetry;

        constructor() {
            super();
            this.rtreeClient = new RTreeClient(apiUrl);
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

        async edit(tree: RTree) {
            this.isLoading = true;
            tree = await this.rtreeClient.get(tree.name);
            this.selectedTree = tree;
            this.isLoading = false;
        }

        async refresh() {
            this.trees = await this.rtreeClient.getAll();
            this.selectedTree = null;
        }
    }

</script>