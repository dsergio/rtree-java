<template>
  <button class="base-button"
  :disabled="props.disabled"
  >
    <slot />
  </button>
</template>

<script setup lang="ts">

import { ref, defineProps } from 'vue';

// Define props
const props = defineProps<{
  disabled: boolean;
}>();

</script>

<style scoped>
.base-button {
  padding: 0.5em 1em;
  border: none;
  background: #3273dc;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 0.5em;
  margin-bottom: 0.5em;
  
}
.base-button:hover {
  background: #2759a1;
}
</style>
