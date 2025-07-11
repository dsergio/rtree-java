<template>
  <div id="app">
    <NavBar></NavBar>
    <router-view />  <!-- renders current view -->
  </div>
</template>

<script setup lang="ts">
import NavBar from '@/views/NavBar.vue';
</script>

<style>
/* global styles */
</style>


