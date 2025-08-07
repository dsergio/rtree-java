
<template>
    <div class="columns">
        <div class="column">
            <div class="rtree-component-list">
                <h2 class="title">R-Tree datasets</h2>

                <table class='table is-bordered'>
                  <tbody>
                    <tr>
                      <td><label class="label">Name</label></td>
                      <td><input class="input is-primary" type = "text" placeholder="New Tree Name" v-model="newTreeName" /></td>
                    </tr>

                    <tr>
                      <td><label class="label">Dataset</label></td>
                      <td>

                        <model-select :options="newTreeDatasetOptions"
                                v-model="newTreeDatasetItem"
                                :multiple="false"
                                :searchable="true"
                                :clearable="true"
                                @select="updateTreeName()"
                                >
                        </model-select>

                      </td>
                    </tr>

                    <tr>
                      <td><label class="label">Options</label></td>
                      <td>

                        <input type="checkbox" id="option_generate_random_items" v-model="option_generate_random_items" />
                        <label for="option_generate_random_items">Create tree and generate random items</label>

                      </td>
                    </tr>

                    <tr v-if="option_generate_random_items">
                      <td><label class="label">Number of randomly generated items</label></td>
                      <td>

                        <model-select :options="newTreeLocationCountOptions"
                                v-model="newTreeLocationCountItem"
                                :multiple="false"
                                :searchable="true"
                                :clearable="true"
                                :placeholder="'Select item'"
                                :label="'label'">
                        </model-select>
                      
                      </td>
                    </tr>
                    <tr>
                      <td><label class="label">Number of dimensions</label></td>
                      <td>

                        <model-select :options="newTreeNumDimensionsOptions"
                                v-model="newTreeNumDimensionsItem"
                                :multiple="false"
                                :searchable="true"
                                :clearable="true"
                                :placeholder="'Select item'"
                                :label="'label'">
                        </model-select>
                      
                      </td>
                    </tr>
                    <tr>
                      <td><label class="label">Max Children</label></td>
                      <td>

                        <model-select :options="newTreeMaxChildrenOptions"
                                v-model="newTreeMaxChildrenItem"
                                :multiple="false"
                                :searchable="true"
                                :clearable="true"
                                :placeholder="'Select item'"
                                :label="'label'">
                        </model-select>
                      
                      </td>
                    </tr>
                    <tr>
                      <td><label class="label">Max Items</label></td>
                      <td>

                        <model-select :options="newTreeMaxItemsOptions"
                                v-model="newTreeMaxItemsItem"
                                :multiple="false"
                                :searchable="true"
                                :clearable="true"
                                :placeholder="'Select item'"
                                :label="'label'">
                        </model-select>
                      
                      </td>
                    </tr>

                    
                  </tbody>
                </table>
                <BaseButton @click="createRTree(newTreeName)" :disabled="false">Create</BaseButton>

                <table class="table is-striped">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>dim</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="component in rtree_list" :key="component.item.name">
                        <td>{{ component.item.name }}</td>
                        <td>{{ component.item.numDimensions }}</td>
                        <td>
                            <BaseButton @click="setActiveComponent(component)" :disabled="false">View/Edit</BaseButton> &nbsp;
                            <BaseButton @click="deleteComponent(component.item.name)" :disabled="false">Delete</BaseButton>
                        </td>
                    </tr>
                </tbody>
                </table>
            </div>
        </div>
        <div class="column">
            <div v-for="component in rtree_list" :key="component.item.name">
                <RTreeComponentDetail v-if="component.active" 
                :treeName="component.item.name" 
                :numDimensions="component.item.numDimensions" 
                :dataSet="newTreeDatasetItem.value"
                :generateRandomData="option_generate_random_items && generateItems"
                :locationCount="newTreeLocationCountItem.value"
              />
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">

import { ref, onMounted, watch } from 'vue';
import { RTreeDouble } from '@/generated/TypeScriptClient';
import RTreeComponentDetail from '@/components/RTree/rtreeComponentDetail.vue';
import { api } from '@/config';
import { ModelSelect } from 'vue-search-select';

var newTreeLocationCountOptions = ref<Array<{ value: number; text: string }>>([]);
var newTreeLocationCountItem = ref<{ value: number; text: string }>({ value: 10, text: '10' });

var newTreeNumDimensionsOptions = ref<Array<{ value: number; text: string }>>([]);
var newTreeNumDimensionsItem = ref<{ value: number; text: string }>({ value: 2, text: '2' });

var newTreeMaxChildrenOptions = ref<Array<{ value: number; text: string }>>([]);
var newTreeMaxChildrenItem = ref<{ value: number; text: string }>({ value: 4, text: '4' });
var newTreeMaxItemsOptions = ref<Array<{ value: number; text: string }>>([]);
var newTreeMaxItemsItem = ref<{ value: number; text: string }>({ value: 4, text: '4' });

var generateItems = ref<boolean>(false);

var newTreeDatasetOptions = ref<Array<{ value: string; text: string }>>([
  { value: 'geo', text: 'Geographic 1 - WA cities' },
  { value: 'animal', text: 'Animal' },
]);

var newTreeDatasetItem = ref<{ value: string; text: string }>({ value: 'animal', text: 'Animal' });

var newTreeName = ref<string>('rtree-' + newTreeDatasetItem.value.value + '-' + Math.random().toString(36).substring(2, 10));

var option_generate_random_items = ref<boolean>(true);

for (let i = 1; i <= 10; i++) {
  newTreeLocationCountOptions.value.push({ value: i * 10, text: '' + i * 10});
  newTreeNumDimensionsOptions.value.push({ value: i, text: '' + i });
  newTreeMaxChildrenOptions.value.push({ value: i, text: '' + i });
  newTreeMaxItemsOptions.value.push({ value: i, text: '' + i });
}

class RTreeDoubleItems {
  item: RTreeDouble;
  active?: boolean;
  name: string;

  constructor(item: RTreeDouble) {
    this.item = item;
    this.active = false; // Default to inactive
    if (!item.name) {
      this.name = 'rtree-' + item.numDimensions + '-' + Math.random().toString(36).substring(2, 10);
    } else {
      this.name = item.name;
    }
  }
}

var rtree_list = ref<RTreeDoubleItems[]>([]);

const updateTreeName = () => {
  if (newTreeDatasetItem.value) {
    newTreeName.value = 'rtree-' + newTreeDatasetItem.value.value + '-' + Math.random().toString(36).substring(2, 10);
  } else {
    newTreeName.value = 'rtree-' + Math.random().toString(36).substring(2, 10);
  }
  // console.log('Updated newTreeName:', newTreeName.value);
};

watch(newTreeDatasetItem, (newValue) => {
  updateTreeName();
});

const createRTree = async (name: string) => {
  generateItems.value = option_generate_random_items.value; // Set generate items option based on checkbox

  try {

    if (!name) {
      name = 'rtree-' + Math.random().toString(36).substring(2, 10);
    }

    var obj = {
        'rTreeCreate': {
          maxChildren: newTreeMaxChildrenItem.value.value,
          maxItems: newTreeMaxItemsItem.value.value,
          treeName: name,
          numDimensions: newTreeNumDimensionsItem.value.value,
        }
      };

    console.log("new R-Tree object:", obj);

    const res = await api.rTreeDoubleNewTree(
      obj
    );

    console.log(`Created new R-Tree: ${name}`);
    const newTree = new RTreeDoubleItems(
     {
        name: name,
        numDimensions: newTreeNumDimensionsItem.value.value,
      }
    );

    rtree_list.value.push(newTree);

    updateTreeName(); // Reset the new tree name

    for (var i = 0; i < rtree_list.value.length; i++) {
      console.log('i: ' + i + ', name: ' + name + ', rtree_list.value[i].item.name: ' + rtree_list.value[i].item.name);
      if (rtree_list.value[i].item.name === name) {
        rtree_list.value[i].active = true;
      } else {
        rtree_list.value[i].active = false;
      }
    }

  } catch (error) {
    console.error('Failed to create new R-Tree:', error);
  }
};

const setActiveComponent = (component: RTreeDouble) => {
  generateItems.value = false; // Reset generate items option
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

.label {
  font-weight: bold;
  /* color: black; */
}

.table {
  width: 100%;
  margin-top: 20px;
}

label {
  padding: 10px;
}

</style>
