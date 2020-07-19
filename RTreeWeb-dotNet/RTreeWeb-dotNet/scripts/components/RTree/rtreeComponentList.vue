<template>
    <div>
        <h2>RTree List</h2>
        <button class="button is-secondary" @click="create()">Create New RTree</button>
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
                        <button class="button is-primary" @click="edit(t)">Edit</button>
                        <button class="button" @click="deleteTree(t)">Delete</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <rtree-component-detail v-if="selectedTree != null"
                                :tree="selectedTree"
                                @rtree-saved="refresh()"></rtree-component-detail>
    </div>
</template>
<script lang="ts">
    import { Vue, Component } from 'vue-property-decorator';
    //import { RTree2, RTreeClient2 } from '../../client';
    import { RTree, RTreeClient } from '../../api-client.g';
    import rtreeComponentDetail from './rtreeComponentDetail.vue';
    declare var apiUrl: string;

    apiUrl = "http://localhost:8080";

    @Component({
        components: {
            rtreeComponentDetail
        }
    })
    export default class RTreeComponentList extends Vue {
        trees: RTree[] = null;
        selectedTree: RTree = null;
        rtreeClient: RTreeClient;

        constructor() {
            super();
            this.rtreeClient = new RTreeClient(apiUrl);
        }

        async mounted() {
            this.trees = await this.rtreeClient.getAll();
            console.log("trees: ", this.trees);
        }

        async edit(tree: RTree) {
            tree = await this.rtreeClient.get(tree.name);
            this.selectedTree = tree;
        }

        async refresh() {
            this.trees = await this.rtreeClient.getAll();
            this.selectedTree = null;
        }
    }
</script>