<script setup>
import Navbar from "./components/Navbar.vue";
</script>

<template>
  <div>
    <!-- <div :style="{'background-image':'url(\'./assets/img/bg.jpeg\')'}"> -->
    <Navbar />
  </div>
  <div>
    <router-view></router-view>
  </div>
</template>

<style>
body {
  background-image: url('./assets/illust.png');
  background-size: 300% 300%;
  background-repeat: no-repeat;
  background-attachment: fixed;

}
</style>
