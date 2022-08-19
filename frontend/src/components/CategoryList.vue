<script setup>
const prop = defineProps({
  allCategory: {
    type: Array,
    require: true,
  },
});
</script>

<template>
  <div>
    <table class="table table-auto w-full">
      <!-- head -->
      <thead>
        <tr>
          <th>No.</th>
          <th>Category Name</th>
          <th>Description</th>
          <th>Duration</th>
        </tr>
      </thead>
      <tbody>
        <!-- row 1 -->
        <tr class="hover" v-for="(category, index) in prop.allCategory" :key="index">
          <th>{{ index + 1 }}</th>
          <td>{{ category.eventCategoryName }}</td>
          <td>{{ category.eventCategoryDescription }}</td>
          <td>{{ category.eventDuration }} mins</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style></style>
