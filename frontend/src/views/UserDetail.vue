<script setup>
import { onBeforeMount, ref } from 'vue';
import { useRoute } from 'vue-router';

let { params } = useRoute()
const name = params.name
const detail = ref([])
const localCreatedOn = ref()
const localUpdatedOn = ref()
const isEdit = ref(false)
const editName = ref('')
const editEmail = ref('')
const beforeEditName = ref('')
const beforeEditEmail = ref('')

const getDetail = (async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users/${name}`)
  detail.value = await res.json()
  detail.value.role = capitalizeFirstLetter(detail.value.role)
  // console.log(detail.value.role);
  // capitalizeFirstLetter(detail.value.role)
  // localCreatedOn.value = new Date(detail.value.createdOn).to
  localCreatedOn.value = new Date(detail.value.createdOn).toLocaleTimeString("th-TH", {
    weekday: "short",
    month: "short",
    day: "numeric",
    year: "2-digit",
    hour: "numeric",
    minute: "numeric",
  }
  )
  localUpdatedOn.value = new Date(detail.value.updatedOn).toLocaleTimeString("th-TH", {
    weekday: "short",
    month: "short",
    day: "numeric",
    year: "2-digit",
    hour: "numeric",
    minute: "numeric",
  }
  )

  editName.value = detail.value.name
  editEmail.value = detail.value.email
  beforeEditName.value = editName.value
  beforeEditEmail.value = editEmail.value
  console.log(localCreatedOn.value);
  console.log(localUpdatedOn.value);

  //   event.value.filter((e) => {
  // const localDate = new Date(e.eventStartTime).toLocaleString("th-TH", {
  //   weekday: "short",
  //   month: "short",
  //   day: "numeric",
  //   year: "2-digit",
  //   hour: "numeric",
  //   minute: "numeric",
  // });
  //     e.eventStartTime = localDate;
  //   });
})

const capitalizeFirstLetter = (string) => {
  return string.charAt(0).toUpperCase() + string.slice(1)
}
const updateUser = async () => {
  console.log('update');
}
onBeforeMount(async () => {
  await getDetail()

})
</script>
 
<template>
  <div>
    <div class="hero min-h-screen bg-base-200">
      <div class="hero-content text-left">
        <div
          class="card w-auto lg:w-[1200px] h-full bg-gradient-to-r from-base-100 to-base-200 shadow-xl backdrop-blur-sm mb-12">
          <div class="card-body text-xl md:text-3xl place-self-center">
            <p v-if="isEdit == false"
              class="text-5xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-purple-300 to-pink-600 m-4 pb-1 text-center ">
              {{ name }}
            </p>
            <input v-else type="text" v-model="editName"
              class="input input-bordered input-secondary w-full max-w-xs self-center text-2xl" id="name" />
            <p>
              <IcPerson class="inline-block mr-5" />
              <label>Email:
                <span v-if="isEdit == false">{{ detail.email }}</span>
                <span v-else><input type="text" v-model="editEmail"
                    class="input input-bordered input-secondary w-full max-w-xs self-center text-lg"
                    id="email" /></span>
              </label>
            </p>
            <p>
              <IcPerson class="inline-block mr-5" />
              <label> Role:
                {{ detail.role }}
              </label>
            </p>
            <p>
              <IcPerson class="inline-block mr-5" />
              <label> Created on:
                <!-- {{ detail.createdOn }} -->
                {{ localCreatedOn }}
              </label>
            </p>
            <p>
              <IcPerson class="inline-block mr-5" />
              <label> Update on:
                <!-- {{ detail.updatedOn }} -->
                {{ localUpdatedOn }}
              </label>
            </p>
            <!-- <input type="text" v-model="selectedEvent.eventCategoryName"
                    class="input input-bordered input-secondary w-full max-w-xs self-center text-2xl" disabled
                  id="duration"  v-else/>
            <div class="divider"></div>
            <p>
              <IcPerson class="inline-block mr-5" /> 
              <label> Name: 
               <span v-if="isEdit==false">{{ selectedEvent.bookingName }}</span>
               <span v-else><input type="text" v-model="selectedEvent.bookingName"
                    class="input input-bordered input-secondary w-96 max-w-xs self-center text-lg" disabled
                  id="duration" /></span>
              </label> 
            </p>
            <br />
            <p>
              <IcTimer class="inline-block mr-5" />
              <label> Duration: 
               <span v-if="isEdit==false">{{ selectedEvent.eventDuration }} mins</span>
               <span v-else><input type="text" v-model="selectedEvent.eventDuration"
                    class="input input-bordered input-secondary w-84 max-w-xs self-center text-lg" disabled
                  id="duration" /></span>
              </label> 
            </p>
            <br />
            <p>
              <IcCalendar class="inline-block mr-5" />
              <label for="starttime">
                Start time  :
              </label>
            <p v-show="!isEdit" class="inline-block">{{ selectedEvent.eventStartTime }}</p> -->
            <!-- <span class="text-red-500 mr-2" v-show="isEdit" >*</span> -->
            <!-- <input v-show="isEdit" type="datetime-local"
              class="input input-bordered input-secondary w-auto max-w-xs text-lg" id="starttime" v-model="editDate" :min="currentTime"/>
            </p>
              <span class="text-sm text-yellow-500 pb-2 inline-block" v-show="compareDate(editDate,currentTime)">** Start time must be in the future.</span>
            <br/>
            <p>
              <IcEmail class="inline-block mr-5 " />
              <label> Email: 
               <span v-if="isEdit==false">{{ selectedEvent.bookingEmail }}</span>
               <span v-else><input type="text" v-model="selectedEvent.bookingEmail"
                    class="input input-bordered input-secondary w-full max-w-xs self-center text-lg" disabled
                  id="duration" /></span>
              </label> 
            </p>
            <br />
            <p>
              <IcNote class="inline-block mr-5" />
              <label for="notes">
                Notes: 
              </label>
            <p v-if="selectedEvent.eventNotes !=''" v-show="!isEdit" class="inline-block">{{ selectedEvent.eventNotes }}</p>
            <p v-else v-show="!isEdit" class="inline-block">NO MESSAGE.</p>
            <p>
              <div v-show="isEdit">
                <span class="text-sm text-yellow-500 pb-2" v-show="editNote.length == 500" >** A notes length must be 1 - 500 character.</span>
              </div>
              <textarea type="text" rows="2" v-show="isEdit" id="notes" v-model="editNote"
                class="textarea textarea-secondary text-xl w-full overflow-auto mt-3"
                maxlength="500"></textarea>
                  <label class="label">
                    <span class="label-text-alt"></span>
                    <span class="label-text-alt" v-show="isEdit">{{editNote.length}}/500</span>
                  </label>
                  </p>
            </p> -->
            <!-- <p v-else>
              <IcNote class="inline-block mr-5" />
              <label for="notes">
                Notes: 
              </label>
            <p v-show="!isEdit" class="inline-block">NO MESSAGE</p>
            <p>
              <span class="text-sm text-yellow-500 " v-show="editNote.length==500" >** A notes length must be 1 - 500 character.</span>
              <textarea type="text" rows="2" v-show="isEdit" id="notes" v-model="editNote"
                class="in textarea textarea-secondary text-lg w-full overflow-auto mt-3 "
                maxlength="500"></textarea>
                  <label class="label">
                    <span class="label-text-alt"></span>
                    <span class="label-text-alt" v-show="isEdit">{{editNote.length}}/500</span>
                  </label>
                </p>
            </p> -->
          </div>
          <div class="card-actions justify-end m-5">
            <button class="btn btn-secondary border-none " @click="isEdit = !isEdit" v-show="!isEdit">Edit</button>
            <button class="btn btn-accent border-none " @click="isEdit = true, updateUser()"
              v-show="isEdit">Apply</button>
            <!-- click="isEdit = !isEdit, editDate = beforeEditDate, editNote = beforeEditNote" -->
            <button class="btn btn-secondary border-none "
              @click="isEdit = !isEdit, editName = beforeEditName, editEmail = beforeEditEmail"
              v-show="isEdit">Cancel</button>
            <router-link :to="{ name: 'UserList' }"><button
                class="btn btn-secondary border-none bg-gradient-to-r from-yellow-500 to-orange-600 hover:from-pink-500 hover:to-yellow-500">Go
                back</button>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
 
<style>
</style>