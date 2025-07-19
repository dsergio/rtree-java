<template>
  <section class="section">
    <h1 class="title">Machine Learning for Physics and Astronomy</h1>
    <div class="columns">
      <div class="column">
        <div class="box">
          <h1 class="title">Exercises</h1>
          <table class="table is-striped">
            <thead>
              <tr>
                <th v-for="header in headers" :key="header">{{ header }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(row, rowIndex) in rows" :key="rowIndex">
                <td v-for="header in headers" :key="header">

                  <a v-if="header === 'Status' && row.Status === 'Done'" :href="`/public/mlpa/${row.Problem}/exercise_report.pdf`" target="_blank">View Report</a>
                  <span v-else>{{ row[header] }}</span>

                  <!-- <Notebook v-if="header === 'Status' && row.Status === 'Done'" :path="`/public/mlpa/${row.Problem}/notebook.html`" /> -->

                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">

import { ref, onMounted } from 'vue';
import Papa from 'papaparse';
import Notebook from '@/views/Notebook.vue';

const headers = ref<string[]>([]);
const rows = ref<Record<string, string>[]>([]);

onMounted(async () => {
  const response = await fetch('@/../mlpa.csv');
  const csvText = await response.text();

  const result = Papa.parse(csvText, {
    header: true,
    skipEmptyLines: true,
  });

  if (result.meta.fields) {
    headers.value = result.meta.fields;
    rows.value = result.data as Record<string, string>[];
  }
});



</script>

<style scoped>
/* view-specific styles */
.table {
  width: 100%;
  margin-top: 20px;
}
.table th, .table td {
  text-align: left;
  padding: 8px;
  width: 20%;
}
</style>
