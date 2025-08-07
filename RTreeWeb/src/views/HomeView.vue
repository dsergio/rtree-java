<template>
  <section class="section">
    <h1 class="title">Home</h1>

    <div class="columns">
        <div class="column">
            <h2 class="subtitle">About</h2>
            <div class="box">
                About
            </div>
        </div>
    </div>
    <AnalyticsDataLayer :pageName="pageName" :pageId="pageId" />
    
  </section>
</template>

<script setup lang="ts">

import { ref } from 'vue';

const pageName = ref('Home Page');
const pageId = ref('home-page');

</script>

<style scoped>
/* view-specific styles */
</style>
