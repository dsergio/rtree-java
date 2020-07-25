import '../styles/site.scss';
import Vue from 'vue';


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

