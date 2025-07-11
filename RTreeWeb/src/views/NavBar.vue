<template>
  <nav class="navbar is-primary" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">

      <a
        role="button"
        class="navbar-burger"
        :class="{ 'is-active': isMenuOpen }"
        aria-label="menu"
        aria-expanded="false"
        @click="toggleMenu"
      >
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
      </a>
    </div>

    <div :class="['navbar-menu', { 'is-active': isMenuOpen }]">
      <div class="navbar-start">
        <a class="navbar-item" @click="$router.push('/')">Home</a>
        <a class="navbar-item" @click="$router.push('/portfolio')">Portfolio</a>

        <div class="navbar-item has-dropdown" :class="{ 'is-active': isSubmenuOpen }">
          <a class="navbar-link" @click="toggleSubmenu">Projects</a>

          <div class="navbar-dropdown">
            <a class="navbar-item" @click="$router.push('/rtree');toggleMenu();toggleSubmenu();">R-Tree</a>
            <a class="navbar-item">ML 1</a>
            <a class="navbar-item">ML 2</a>
            <a class="navbar-item" @click="$router.push('/analytics');toggleMenu();toggleSubmenu();">Analytics</a>
            <hr class="navbar-divider" />
            <a class="navbar-item" @click="$router.push('/portfolio');toggleMenu();toggleSubmenu();">All</a>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const isMenuOpen = ref(false);
const isSubmenuOpen = ref(false);

function toggleMenu() {
  isMenuOpen.value = !isMenuOpen.value;
}

function toggleSubmenu() {
  isSubmenuOpen.value = !isSubmenuOpen.value;
}
</script>

<style scoped>
.navbar-dropdown {
  pointer-events: auto;
}
</style>
