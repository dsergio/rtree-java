<template>
  <AnalyticsDataLayer :pageName="pageName" :pageId="pageId" />
  <section class="section">
    <h1 class="title">R-Tree Visualization</h1>
    <RTreeComponentList></RTreeComponentList>
  </section>
</template>

<script setup lang="ts">

import RTreeComponentList from '@/components/RTree/rtreeComponentList.vue';

import { onMounted, ref } from 'vue';
import { TelemetryPageTMSGeneric } from '@/analyticsDataLayer/pages/pages';

const pageName = ref('R-Tree Visualization');
const pageId = ref('rtree-visualization');

  onMounted(async () => {
      try {
        console.log('RTreeView mounted');

      } catch (err) {
        console.error('Failed to fetch data:', err);
      }
  });

</script>

<style scoped>
/* view-specific styles */
</style>
