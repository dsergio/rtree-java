<template>
  <button class="base-button">
    <slot />
  </button>
</template>

<script setup lang="ts">
// No logic here: purely presentational
</script>

<style scoped>
.base-button {
  padding: 0.5em 1em;
  border: none;
  background: #3273dc;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}
.base-button:hover {
  background: #2759a1;
}
</style>
