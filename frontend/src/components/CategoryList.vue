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
    <div class="container mx-auto px-4 sm:px-8">
      <div class="py-8">
        <div>
          <h2 class="text-2xl font-semibold leading-tight">Event Categories</h2>
        </div>

        <div class="-mx-4 sm:-mx-8 px-4 sm:px-8 py-4 overflow-x-auto">
          <div class="inline-block min-w-full shadow rounded-lg overflow-hidden">
            <table class="min-w-full leading-normal">
              <thead>
                <tr>
                  <th
                    class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                    Category name
                  </th>
                  <th
                    class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                    Description
                  </th>
                  <th
                    class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                    Time
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(category, index) in prop.allCategory" :key="index">
                  <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                    <div class="flex items-center">
                      <div class="ml-3">
                        <p class="text-gray-900 whitespace-no-wrap">
                          {{ category.eventCategoryName }}
                        </p>
                      </div>
                    </div>
                  </td>
                  <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                    <p v-if="category.eventCategoryDescription !== ''" class="text-gray-900 whitespace-no-wrap">
                      {{ category.eventCategoryDescription }}
                    </p>
                    <p v-else class="text-gray-900 whitespace-no-wrap">
                      No Description.
                    </p>
                  </td>
                  <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                    <span class="relative inline-block px-5 py-1 font-semibold text-green-900 w-24">
                      <span aria-hidden class="absolute inset-0 bg-green-200 opacity-50 rounded-full"></span>
                      <span class="relative">{{ category.eventDuration }} mins</span>
                    </span>
                  </td>
                </tr>

              </tbody>
            </table>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style>

</style>
