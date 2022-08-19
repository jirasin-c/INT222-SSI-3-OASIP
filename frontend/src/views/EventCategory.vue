<script setup>
import { onBeforeMount, ref } from "vue";
import CategoryCard from "../components/CategoryList.vue";

const eventCategory = ref([]);

onBeforeMount(async()=>{
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
  <div>
    <div>
      <div
        class="mt-10 ml-16 text-3xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-purple-400 to-pink-600"
      >
        EVENT CATEGORY
      </div>
    </div>
    <div class="w-full h-full bg-gray-500/20">
      <CategoryCard :all-category="eventCategory" class="m-10 p-10"/>
    </div>
  </div>
</template>

<style></style>
