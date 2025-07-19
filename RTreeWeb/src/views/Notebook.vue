<template>
  <div v-html="notebookContent"></div>
</template>

<script>
import { ref, onMounted } from 'vue';

export default {
  name: 'Notebook',
  props: {
    path: {
      type: String,
      required: true,
    },
  },
  setup(props) {
    const notebookContent = ref('');

    onMounted(async () => {
      const response = await fetch(props.path);
      if (response.ok) {
        notebookContent.value = await response.text();
      } else {
        console.error('Failed to load notebook:', response.status);
      }
    });

    return {
      notebookContent,
    };
  },
};
</script>

<style scoped>
/* Add any styles you need here */
</style>
