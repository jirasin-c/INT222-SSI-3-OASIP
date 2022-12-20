<script setup>
import { onBeforeMount, ref } from "vue";
import CategoryCard from "../components/CategoryList.vue";

const eventCategory = ref([]);

onBeforeMount(async () => {
  await getEventCategory()
  // console.log(eventCategory.value);
})

const getEventCategory = async () => {
  const res = await fetch(
    `${import.meta.env.VITE_BASE_URL}api/event-categories`
  );
  eventCategory.value = await res.json();
};
</script>

<template>
  <div class="w-full h-ful">
    <CategoryCard :all-category="eventCategory" class="m-10 p-10" />
  </div>
</template>

<style>

</style>
