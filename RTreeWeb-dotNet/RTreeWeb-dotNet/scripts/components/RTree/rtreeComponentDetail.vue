<template>
    <div class="modal is-active">
        <div class="modal-background"></div>
        <div class="modal-card">
            <header class="modal-card-head">
                <h2 class="modal-card-title">RTree Detail - {{tree.name}}</h2>
                <button class="delete" @click="cancel" aria-label="close"></button>
            </header>
            <section class="modal-card-body">
                <div class="content">

                    <div id="threejsCanvas"></div>
                    <div id="canvasContainer2D"></div>

                    <h3>Points</h3>
                    <div style="overflow-y:auto;height:200px;">
                        <ul v-for="i in tree.points">
                            <li class="locationItem" :id="i.id">{{i.json}}</li>
                        </ul>
                    </div>

                    <h3>Rectangles</h3>
                    <div style="overflow-y:auto;height:200px;">
                        <ul v-for="i in tree.rectangles">
                            <li class="rectangle">{{i.json}}</li>
                        </ul>
                    </div>
                    <h3>Insert item</h3>
                    <div v-for="i in tree.numDimensions" :key="i" class="field">
                        <label class="label">Dimension {{i - 1}}</label>
                        <div class="control">
                            <input class="input" type="text" v-model="newItemDimensionArray[i - 1]" />
                        </div>
                    </div>
                    <spinner v-if="isUpdateLoading == true"></spinner>

                    <div class="field is-grouped">

                        <div class="control">
                            <button id="submit" v-if="isUpdateLoading == false" class="button is-primary" @click.once="save">Submit</button>
                        </div>
                        <div class="control">
                            <a v-if="isUpdateLoading == false" class="button" @click="cancel">Cancel</a>
                        </div>
                        <!--
        <div class="control">
            <a class="button" @click="cancel">Close</a>
        </div>
            -->
                    </div>
                </div>
            </section>
        </div>
    </div>
</template>
<script lang="ts">
    import { Vue, Component, Prop, Emit } from 'vue-property-decorator'
    import { RTree, RTreeClient, LocationItem } from '../../api-client.g';
    import Spinner from 'vue-spinner-component/src/Spinner.vue';
    declare var apiUrl: string;


    @Component({
        components: {
            Spinner
        }
    })
    export default class RTreeComponentDetail extends Vue {
        @Prop()
        tree: RTree;
        clonedTree: RTree = <RTree>{};
        newItemDimensionArray: Array<number>;
        isUpdateLoading: boolean = false;

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
                    N: () => this.tree.numDimensions
                }
            });
            document.dispatchEvent(event);
        }

        @Emit('rtree-saved')
        async save() {
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
            
            this.isUpdateLoading = false;
        }

        @Emit('rtree-closed')
        cancel() {
        }
    }
</script>