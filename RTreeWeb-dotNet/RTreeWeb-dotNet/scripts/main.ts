import '../styles/site.scss';
import Vue from 'vue';

import RTreeComponentList from './components/RTree/rtreeComponentList.vue';


document.addEventListener("DOMContentLoaded", async () => {
    if (document.getElementById('rtreeList')) {
        new Vue({
            render: h => h(RTreeComponentList)
        }).$mount('#rtreeList');
    }
});