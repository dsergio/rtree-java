

import { createApp } from 'vue';
import BaseButton from '@/components/BaseButton.vue';

import App from './app.vue';
import { router } from './router';
import 'bulma/css/bulma.css';

import "vue-search-select/dist/VueSearchSelect.css"

createApp(App)
  .component('BaseButton', BaseButton)
  .use(router)
  .mount('#app');

