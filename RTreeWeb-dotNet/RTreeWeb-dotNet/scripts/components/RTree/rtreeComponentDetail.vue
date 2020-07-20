<template>
    <div class="modal is-active">
        <div class="modal-background"></div>
        <div class="modal-card">
            <header class="modal-card-head">
                <h1>RTree Detail - {{tree.name}}</h1>
            </header>
            <section class="modal-card-body">
                <div class="content">
                    <h3>Points</h3>
                    <ul v-for="i in tree.points">
                        <li class="locationItem" :id="i.id">{{i.json}}</li>
                    </ul>

                    <h3>Rectangles</h3>
                    <ul v-for="i in tree.rectangles">
                        <li class="rectangle">{{i.json}}</li>
                    </ul>


                    <div id="threejsCanvas"></div>

                    <div class="field is-grouped">
                        <div class="control">
                            <button id="submit" class="button is-primary" @click.once="save">Submit</button>
                        </div>
                        <div class="control">
                            <a class="button" @click="cancel" >Cancel</a>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>
</template>
<script lang="ts">
    import { Vue, Component, Prop, Emit } from 'vue-property-decorator'
    import { RTree, RTreeClient } from '../../api-client.g';

    declare var apiUrl: string;

    //apiUrl = "http://dsergio-rtree-api-boot.azurewebsites.net";
    //apiUrl = "http://localhost:8080";

    @Component
    export default class RTreeComponentDetail extends Vue {
        @Prop()
        tree: RTree;
        clonedTree: RTree = <RTree>{};
        

        constructor() {
            super();
        }

        async mounted() {
        }

        @Emit('rtree-saved')
        async save() {
        }

        @Emit('rtree-saved')
        cancel() {
        }
    }
</script>