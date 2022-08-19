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
defineEmits(["viewDetail", "deleteEvent"]);
</script>

<template>
  <div>
    <div v-for="booking in prop.allBooking" :key="booking.id">
      <!-- <router-link :to="{ name: 'Detail', params: { bookingId: booking.id } }"> -->
      <div @click="$emit('viewDetail', { bookingId: booking.id, event: $event })" class="cursor">
        <div
          class="hover:bg-slate-500 hover:text-white grid break-inside-avoid rounded-xl p-4 mb-8 w-full break-words drop-shadow-md text-ellipsis overflow-hidden bg-gray-200 text-slate-500">
          <div class="badge badge badge-accent badge-outline mb-1">
            {{ booking.eventCategoryName }}
          </div>
          <div class="flex flex-row">
            <div>
              <span><b>Name: {{ booking.bookingName }}</b><br />
                <b>Category: {{ booking.eventCategoryName }}</b></span>

              <br />
              Date: {{ booking.eventStartTime }}<br />
              Duration: {{ booking.eventDuration }} mins.<br />

            </div>
          </div>
          <div class="justify-self-end flex bg-slate-100 hover:bg-slate-600 rounded-full  w-9 h-9 lg:w-auto lg:h-14">
            <button
              @click="$emit('deleteEvent', { deleteId: booking.id, deleteName: booking.bookingName, event: $event })">
              <RiDeleteBin6Line class=" mx-2 lg:mx-3" />
            </button>
          </div>
        </div>
      </div>
      <!-- </router-link> -->
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
