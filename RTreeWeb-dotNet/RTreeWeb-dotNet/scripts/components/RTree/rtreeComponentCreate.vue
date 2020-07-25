<template>
    <div class="modal is-active">
        <div class="modal-background"></div>
        <div class="modal-card">
            <header class="modal-card-head">
                <h2 class="modal-card-title">Create RTree</h2>
                <button class="delete" @click="cancel" aria-label="close"></button>
            </header>
            <section class="modal-card-body">
                <div class="content">

                    <div class="field">
                        <label class="label">Tree Name</label>
                        <div class="control">
                            <input class="input" type="text" v-model="clonedTree.treeName" />
                        </div>
                    </div>
                    <div class="field">
                        <label class="label">N</label>
                        <div class="control">
                            <input class="input" type="text" v-model="clonedTree.numDimensions" />
                        </div>
                    </div>
                    <div class="field">
                        <label class="label">Max Children</label>
                        <div class="control">
                            <input class="input" type="text" v-model="clonedTree.maxChildren" />
                        </div>
                    </div>
                    <div class="field">
                        <label class="label">Max Items</label>
                        <div class="control">
                            <input class="input" type="text" v-model="clonedTree.maxItems" />
                        </div>
                    </div>

                    <spinner v-if="isUpdateLoading == true"></spinner>

                    <div class="field is-grouped">
                        <div class="control">
                            <button id="submit" v-if="isUpdateLoading == false" 
                                    class="button is-primary" 
                                    @click.once="save"
                                    data-telemetry-action v-bind="telemetryActionAttributes()"
                                    >Submit</button>
                        </div>
                        <div class="control">
                            <a v-if="isUpdateLoading == false" class="button" @click="cancel">Cancel</a>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>
</template>
<script lang="ts">
    import { Vue, Component, Prop, Emit } from 'vue-property-decorator'
    import { RTree, RTreeClient, RTreeCreate } from '../../api-client.g';
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
    export default class RTreeComponentCreate extends Vue {
        @Prop()
        tree: RTree;
        @Prop()
        telemetry: Telemetry;
        clonedTree: RTreeCreate = <RTreeCreate>{};
        isUpdateLoading: boolean = false;

        constructor() {
            super();
        }

        async mounted() {
            this.initAnalyticsDataLayer();
        }

        initAnalyticsDataLayer(): void {

            let pageName = "rtree app create tree";
            let pageId = 'spa-pageview';

            let pageTMSGeneric: ITelemetryPage = new TelemetryPageTMSGeneric(pageName, pageId, true);
            let pageGTM: ITelemetryPage = new TelemetryPageGTM(pageName, pageId, true);
            let pageTealium: ITelemetryPage = new TelemetryPageTealium(pageName, pageId, true);

            this.telemetry.getDataLayer("Generic").setPage(pageTMSGeneric);
            this.telemetry.getDataLayer("GTM").setPage(pageGTM);
            this.telemetry.getDataLayer("Tealium").setPage(pageTealium);

            attachDataLayerObjects(this.telemetry);
        }

        telemetryActionAttributes(): object {

            let eventCreate: ITelemetryEvent = new TelemetryEventTMSGeneric("create-" + this.clonedTree.treeName);
            eventCreate.addEventAttribute(0, "rtree app create");
            eventCreate.addEventAttribute(1, "create submit");
            eventCreate.addEventAttribute(2, this.clonedTree.treeName + " N:" + this.clonedTree.numDimensions +
                " max children: " + this.clonedTree.maxChildren + " max items: " + this.clonedTree.maxItems);
            this.telemetry.getDataLayer("Generic").addEvent(eventCreate);

            //console.log("event... telemetry attr: ", t);
            let i: number = 0;
            let obj: any = {};
            let id = "create-" + this.clonedTree.treeName;
            //console.log("event... id: " + id + " getEvent: ", this.telemetry.getDataLayer("Generic").getEvent(id));
            if (this.telemetry.getDataLayer("Generic").getEvent(id) != null) {
                for (i = 0; i < this.telemetry.getDataLayer("Generic").getEvent(id).getTelementryEventAttributeCount(); i++) {
                    let str: string = "data-telemetry-attribute-" + i;
                    obj[str] = this.telemetry.getDataLayer("Generic").getEvent(id).getTelemetryEventAttribute(i);
                }
            }
            
            return obj;
        }

        @Emit('rtree-created')
        async save() {
            this.isUpdateLoading = true;
            let client = new RTreeClient(apiUrl);

            await client.newTree(this.clonedTree);
            this.tree.name = this.clonedTree.treeName;
            this.isUpdateLoading = false;
        }

        @Emit('rtree-closed')
        cancel() {
        }
    }
</script>