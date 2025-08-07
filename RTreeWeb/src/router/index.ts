import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';
import RTreeView from '@/views/RTreeView.vue';
import PortfolioView from '@/views/PortfolioView.vue';
import AnalyticsView from '@/views/AnalyticsView.vue';
import NotFound from '@/views/NotFound.vue';

const routes = [
  { path: '/',      name: 'home',    component: HomeView, meta: { title: 'Home Page' }, },
  { path: '/rtree',  name: 'rtree', component: RTreeView, meta: { title: 'R-Tree Visualization' }, },
  { path: '/portfolio', name: 'portfolio', component: PortfolioView, meta: { title: 'Portfolio' }, },
  { path: '/analytics', name: 'analytics', component: AnalyticsView, meta: { title: 'Analytics' }, },
  { path: '/mlpa', name: 'mlpa', component: () => import('@/views/MLPAView.vue'), meta: { title: 'MLPA Exercises' }, },


  { path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFound },
];

export const router = createRouter({
  history: createWebHistory(),
  routes,
});
