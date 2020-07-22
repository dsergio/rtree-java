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
                            <button id="submit" v-if="isUpdateLoading == false" class="button is-primary" @click.once="save">Submit</button>
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
    declare var apiUrl: string;

    @Component({
        components: {
            Spinner
        }
    })
    export default class RTreeComponentCreate extends Vue {
        @Prop()
        tree: RTree;
        clonedTree: RTreeCreate = <RTreeCreate>{};
        isUpdateLoading: boolean = false;

        constructor() {
            super();
        }

        async mounted() {
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