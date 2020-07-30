﻿import '../styles/site.scss';
import Vue from 'vue';

import { library } from '@fortawesome/fontawesome-svg-core';
import { faUserSecret } from '@fortawesome/free-solid-svg-icons';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';
import { faWindowClose } from '@fortawesome/free-solid-svg-icons';
import { faFolderPlus } from '@fortawesome/free-solid-svg-icons';
import { faTree } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

library.add(faUserSecret);
library.add(faCheckCircle);
library.add(faWindowClose);
library.add(faFolderPlus);
library.add(faTree);

Vue.component('font-awesome-icon', FontAwesomeIcon);


//import VueRouter from 'vue-router';
//Vue.use(VueRouter);
//const Home = { template: '<div>This is Home</div>' }

import RTreeComponentList from './components/RTree/rtreeComponentList.vue';

//const router = new VueRouter({
//    mode: 'history',
//    base: '/RTree',
//    routes: [
//        { path: '*', component: Home }
//    ]
//});


document.addEventListener("DOMContentLoaded", async () => {
    if (document.getElementById('rtreeList')) {
        new Vue({
            //router,
            render: h => h(RTreeComponentList)
        }).$mount('#rtreeList');
    }
});

