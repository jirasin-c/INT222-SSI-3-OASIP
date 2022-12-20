<script setup>
import MdiTrashCan from "./MdiTrashCan.vue";
import { onBeforeMount, ref } from "vue";
import RiDeleteBin6Line from "./RiDeleteBin6Line.vue";
const prop = defineProps({
  allBooking: {
    type: Array,
    require: true,
  },
});
const roleToLocal = localStorage.getItem("user")
const roleLocal = JSON.parse(roleToLocal)
// console.log(roleLocal.role != "lecturer");

defineEmits(["viewDetail", "deleteEvent"]);
</script>

<template>
  <div>

    <div >
      <section class="text-gray-600 body-font  flex justify-items-center">
        <div class="px-5 py-24 mx-auto">
          <!-- <div> -->
            <!-- <router-link :to="{ name: 'AddUser' }">
              <button type="button"
                class="focus:outline-none text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:ring-green-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-green-600 dark:hover:bg-green-700 dark:focus:ring-green-800">Create
                User</button>
            </router-link> -->
          <!-- </div> -->
          <div class="flex flex-wrap -m-2">
            <div class="p-2 lg:w-1/3 md:w-1/2 w-full" v-for="booking in prop.allBooking" :key="booking.id">

              <div class="h-full flex items-center border-gray-200 border p-4 rounded-lg bg-white"
                @click="$emit('viewDetail', { bookingId: booking.id, event: $event })">
                <div class=" flex-grow">
                  <h2 class="text-gray-900 title-font font-medium">{{ booking.eventCategoryName }}</h2>
                  <p class="text-gray-500">Name: {{ booking.bookingName }}</p>
                  <p>Date: {{ booking.eventStartTime }}</p>
                  <p>Duration: {{ booking.eventDuration }} mins.<br /></p>

                </div>

                <div class="flex items-center p-6 space-x-4 rounded-lg">
                  <button class="justify-self-end flex" @click="getDetail(user.name)"><svg class="w-6 h-6" fill="none"
                      stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z">
                      </path>
                    </svg></button>
                  <button v-show="roleLocal.role != 'lecturer'"
                    @click="$emit('deleteEvent', { deleteId: booking.id, deleteName: booking.bookingName, event: $event })"
                    class="justify-self-end flex"><svg class="w-6 h-6" fill="none" stroke="red" viewBox="0 0 24 24"
                      xmlns="http://www.w3.org/2000/svg">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16">
                      </path>
                    </svg></button>
                </div>

              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>

</template>

<style scoped>
.button2:hover {
  box-shadow: 0 12px 16px 0 rgba(0, 0, 0, 0.24),
    0 17px 50px 0 rgba(0, 0, 0, 0.19);
}

.cursor {
  cursor: pointer;
}
</style>
