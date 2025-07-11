import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';
import RTreeView from '@/views/RTreeView.vue';
import PortfolioView from '@/views/PortfolioView.vue';
import AnalyticsView from '@/views/AnalyticsView.vue';

const routes = [
  { path: '/',      name: 'home',    component: HomeView },
  { path: '/rtree',  name: 'rtree', component: RTreeView },
  { path: '/portfolio', name: 'portfolio', component: PortfolioView },
  { path: '/analytics', name: 'analytics', component: AnalyticsView }
];

export const router = createRouter({
  history: createWebHistory(),
  routes,
});
