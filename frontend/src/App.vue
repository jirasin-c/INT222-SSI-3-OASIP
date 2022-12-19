<script setup>
import { ref } from "vue";
import Navbar from "./components/Navbar.vue";
</script>

<template>
  <div>
    <!-- <div :style="{'background-image':'url(\'./assets/img/bg.jpeg\')'}"> -->
    <Navbar/>
  </div>
  <div>
    <router-view></router-view>
  </div>
</template>

<style>
</style>
