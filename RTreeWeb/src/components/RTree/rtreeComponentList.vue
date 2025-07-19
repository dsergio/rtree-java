
<template>
    <div class="columns">
        <div class="column">
            <div class="rtree-component-list">
                <h2 class="title">R-Tree datasets</h2>
                <input type = "text" placeholder="New Tree Name" v-model="newTreeName" />
                <br /><br />
                <BaseButton @click="createRTree(newTreeName)">Add New R-Tree dataset</BaseButton>
                <br /><br />
                <table class="table is-striped">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>dim</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="component in rtree_list" :key="component.name">
                        <td>{{ component.item.name }}</td>
                        <td>{{ component.item.numDimensions }}</td>
                        <td>
                            <BaseButton @click="setActiveComponent(component)">View/Edit</BaseButton> &nbsp;
                            <BaseButton @click="deleteComponent(component.item.name)">Delete</BaseButton>
                        </td>
                    </tr>
                </tbody>
                </table>
            </div>
        </div>
        <div class="column">
            <div v-for="component in rtree_list" :key="component.item.name">
                <RTreeComponentDetail v-if="component.active" :treeName="component.item.name" :numDimensions="component.item.numDimensions" />
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">

import { ref, onMounted } from 'vue';
import { Configuration, RTreeDoubleApi, RTreeDouble } from '@/generated/TypeScriptClient';

import RTreeComponentDetail from '@/components/RTree/rtreeComponentDetail.vue';

const api = new RTreeDoubleApi(
  new Configuration({
    basePath: 'http://localhost:8080',
  })
);

const createRTree = async (name: string) => {
  try {

    var obj = {
        rtreeCreate: {
          maxChildren: 4,
          maxItems: 4,
          treeName: name,
          numDimensions: 2,
        }
      };

    console.log("new R-Tree object:", obj);
    const res = await api.rTreeDoubleNewTree(
      obj
    );

    console.log(`Created new R-Tree: ${name}`);

    init_treeList(); // Refresh the list after creating a new R-Tree
  } catch (error) {
    console.error('Failed to create new R-Tree:', error);
  }
};

const setActiveComponent = (component: RTreeDouble) => {
  rtree_list.value.forEach((item) => {
    item.active = item === component;
    // item.active = true;
  });
};

const deleteComponent = async (name: string) => {
  try {
    console.log(`Delete tree: ${name}`);

    api.rTreeDoubleDelete({
      treeName: name,
    }).then(() => {
      console.log(`Deleted tree: ${name}`);
      // Remove the deleted item from the list
      rtree_list.value = rtree_list.value.filter(item => item.item.name !== name);
    }).catch((error) => {
      console.error(`Error deleting tree: ${name}`, error);
    });

  } catch (error) {
    console.error(`Failed to delete tree: ${name}`, error);
  }
};

class RTreeDoubleItems {
  item: RTreeDouble;
  active?: boolean;

  constructor(item: RTreeDouble) {
    this.item = item;
    this.active = false; // Default to inactive
  }
}

var rtree_list = ref<RTreeDoubleItems[]>([]);

async function init_treeList() {
    const res = await api.rTreeDoubleGetAll();
    // console.log('Fetched data:', res);
    rtree_list.value = []; // Clear the list before adding new items
    
    for (const item of res) {
      rtree_list.value.push(new RTreeDoubleItems(item));
    }
    for (const item of rtree_list.value) {
      item.active = false; // Set all items to inactive initially
    }
    

    // rtree_list.value.push(...res.map(item => new RTreeDoubleItems(item)));

    console.log('RTree List:', rtree_list.value);
}

onMounted(async () => {
  try {
    await init_treeList();
  } catch (err) {
    console.error('Failed to fetch data:', err);
  }
});


</script>

<style scoped>
.rtree-component-list {
  padding: 1em;
  background: #f9f9f9;
  border: 1px solid #eee;
  border-radius: 4px;
}
.title {
  font-size: 1.5em;
  margin-bottom: 0.5em;
  color: black
}
</style>
