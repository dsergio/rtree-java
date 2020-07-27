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
                            <input class="input" type="text" v-model="clonedTree.treeName" placeholder="TreeName" v-on:keyup="validate" />
                        </div>
                    </div>
                    <div class="field">
                        <label class="label">N</label>
                        <div class="control">
                            <input class="input" type="text" v-model="clonedTree.numDimensions" v-on:keyup="validate" />
                        </div>
                    </div>
                    <div class="field">
                        <label class="label">Max Children</label>
                        <div class="control">
                            <input class="input" type="text" v-model="clonedTree.maxChildren" v-on:keyup="validate" />
                        </div>
                    </div>
                    <div class="field">
                        <label class="label">Max Items</label>
                        <div class="control">
                            <input class="input" type="text" v-model="clonedTree.maxItems" v-on:keyup="validate" />
                        </div>
                    </div>

                    <spinner v-if="isUpdateLoading == true"></spinner>


                    <div v-if="errors.length" class="notification is-danger">
                        <p>
                            <b>Please correct the following error(s):</b>
                            <ul>
                                <li v-for="error in errors">{{ error }}</li>
                            </ul>
                        </p>
                    </div>

                    
                </div>
            </section>
            <footer class="modal-card-foot">
                <div class="field is-grouped">
                    <div class="control">
                        <button id="submit" v-if="isUpdateLoading == false && errors.length == 0 && clonedTree.treeName != null"
                                class="button is-primary"
                                @click.once="save"
                                data-telemetry-action v-bind="telemetryActionAttributes()">
                            Submit
                        </button>
                    </div>
                    <div class="control">
                        <a v-if="isUpdateLoading == false" class="button" @click="cancel">Cancel</a>
                    </div>
                </div>
            </footer>
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
        tree: RTreeCreate;
        @Prop()
        telemetry: Telemetry;
        @Prop()
        treeNames: string[];
        clonedTree: RTreeCreate = <RTreeCreate>{};
        isUpdateLoading: boolean = false;
        errors: any = [];

        constructor() {
            super();
            this.clonedTree.numDimensions = this.tree.numDimensions;
            this.clonedTree.maxItems = this.tree.maxItems;
            this.clonedTree.maxChildren = this.tree.maxChildren;
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

        validate() {

            let reValidNumber = /^[0-9]+$/;
            let reValidName = /^[a-zA-Z_0-9]+$/;

            this.errors = [];

            if (!reValidNumber.test("" + this.clonedTree.maxItems)) {
                this.errors.push("maxItems must be an integer between 2 and 10.");
            }
            if (!reValidNumber.test("" + this.clonedTree.maxChildren)) {
                this.errors.push("maxChildren must be an integer between 2 and 10.");
            }

            if (!reValidNumber.test("" + this.clonedTree.numDimensions)) {
                this.errors.push("numDimensions must be an integer between 1 and 20.");
            }

            if (!reValidName.test("" + this.clonedTree.treeName)) {
                this.errors.push("Tree Name must be an an alphanumeric string [a-zA-Z_0-9]");
            }


            if (this.clonedTree.maxItems < 2 || this.clonedTree.maxItems > 10) {
                this.errors.push("maxItems should be between 2 and 10.");
            }

            if (this.clonedTree.maxChildren < 2 || this.clonedTree.maxChildren > 10) {
                this.errors.push("maxChildren should be between 2 and 10.");
            }

            if (this.clonedTree.numDimensions < 1 || this.clonedTree.numDimensions > 20) {
                this.errors.push("N should be between 1 and 20.");
            }

            if (this.treeNames.indexOf(this.clonedTree.treeName) != -1) {
                this.errors.push(this.clonedTree.treeName + " is already taken.");
            }
        }

        @Emit('rtree-created')
        async save() {

            this.validate();

            if (this.errors.length == 0) {
                this.isUpdateLoading = true;
                let client = new RTreeClient(apiUrl);

                await client.newTree(this.clonedTree);
                this.tree.treeName = this.clonedTree.treeName;
                this.isUpdateLoading = false;
            }
            
        }

        @Emit('rtree-closed')
        cancel() {
        }
    }
</script>