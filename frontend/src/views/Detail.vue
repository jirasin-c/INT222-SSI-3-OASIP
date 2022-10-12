<script setup>
import { useRoute } from "vue-router";
import { ref, onBeforeMount, onUpdated } from "vue";
import IcPerson from "../icons/IcBaselinePerson.vue";
import IcEmail from "../icons/IcBaselineEmail.vue";
import IcTimer from "../icons/IcBaselineTimer.vue";
import IcCalendar from "../icons/BiCalendarDateFill.vue";
import IcNote from "../icons/PhNoteFill.vue";
import { useRouter } from 'vue-router';
const selectedEvent = ref([]);
const events = ref([])
const eventCategory = ref([])
const getEventCategoryName = ref("");
let { params } = useRoute();
const bookingId = params.bookingId;
const editDate = ref(null)
const beforeEditDate = ref(null)
const editNote = ref('')
const beforeEditNote = ref(null)
const isEdit = ref(false)
const currentTime = ref(null)
const isPast = ref(false)
const isOverlapped = ref(false)
const duration = ref()
const exceptDate = ref(null)

const appRouter = useRouter()
const compareDate = (editDate, currentTime) => {
  if (editDate > currentTime) {
    isPast.value = false
    return false
  } else if (editDate < currentTime) {
    isPast.value = true
    return true
  } else {
    isPast.value = false
    return false
  }

}
const myHeader = ref()
const createHeader = () => {
  if (localStorage.getItem('token') != null) {
    var tokenToLocal = localStorage.getItem("token")
    var tokenLocal = JSON.parse(tokenToLocal)
    // console.log(tokenLocal);
    myHeader.value = new Headers({
      "content-type": "application/json",
      "Authorization": `Bearer ${tokenLocal.accessToken}`,
    })
  }
}

onUpdated(() => {
  // currentTime.value = new Date().getFullYear() + '-' + ('0' + (new Date().getMonth() + 1)).slice(-2) + "-" + new Date().getDate() + 'T' + ('0' + new Date().getHours()).slice(-2) + ':' + ('0' + new Date().getMinutes()).slice(-2)
  currentTime.value = new Date().getFullYear() + '-' + ('0' + (new Date().getMonth() + 1)).slice(-2) + "-" + ('0' + (new Date().getDate())).slice(-2) + 'T' + ('0' + new Date().getHours()).slice(-2) + ':' + (String(new Date().getMinutes() + 1).padStart(2, '0'))

  // eventCategory.value.filter((findID) => {
  // if (findID.eventCategoryName === getEventCategoryName.value) {
  duration.value = eventCategory.value.eventDuration
  // }
  // });
})
const getEvents = async () => {
  createHeader()
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events`,{
      method: "GET",
      headers: myHeader.value
    })
  events.value = await res.json()
  // console.log(events.value);
}
const getEventCategory = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/event-categories/categoryName?categoryName=${selectedEvent.value.eventCategoryName}`)
  eventCategory.value = await res.json()
  // console.log(eventCategory.value);
}

const getDetailById = async () => {
  // console.log(bookingId);
  createHeader()
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events/${bookingId}`,{
    method: 'GET',
    headers: myHeader.value,
  }
  );
  if (res.status == 403) {
    return appRouter.push({name: "Home"})
  }
  selectedEvent.value = await res.json();
  // console.log(selectedEvent.value);
  // getEventCategoryName.value =
  //   selectedEvent.value.eventCategoryName;
  if (selectedEvent.value.eventNotes == null) {
    selectedEvent.value.eventNotes = ''
  } else {
    editNote.value = selectedEvent.value.eventNotes
  }
  beforeEditNote.value = editNote.value
  editDate.value = selectedEvent.value.eventStartTime.split('.')[0];
  beforeEditDate.value = editDate.value

  // console.log(selectedEvent.value.eventNotes);
  exceptDate.value = new Date(selectedEvent.value.eventStartTime).toISOString()
  const localDate = new Date(selectedEvent.value.eventStartTime).toLocaleString(
    "th-TH",
    {
      weekday: "short",
      month: "short",
      day: "numeric",
      year: "2-digit",
      hour: "numeric",
      minute: "numeric",
    }
  );
  selectedEvent.value.eventStartTime = localDate;
};

const updateEvent = async () => {
  isOverlapped.value = false
  const compareStartTime = new Date(editDate.value).toLocaleString()
  const compareStartTimeISO = new Date(editDate.value)

  events.value.filter((findOvl) => {
    const existingStartTime = new Date(findOvl.eventStartTime).toLocaleString()
    const existStartTimeToMillisec = new Date(findOvl.eventStartTime).getTime()
    const existDuration = findOvl.eventDuration * 60000
    const existingEndTime = new Date(existStartTimeToMillisec + existDuration).toLocaleString()

    const durationToMillisec = duration.value * 60000
    const alertExistEndTime = new Date(existStartTimeToMillisec + existDuration).getHours() + ":" + ('0' + new Date(existStartTimeToMillisec + existDuration).getMinutes()).slice(-2) + ":" + ('0' + new Date(existStartTimeToMillisec + existDuration).getSeconds()).slice(-2)
    const startTimeToMillisec = new Date(compareStartTimeISO).getTime()
    const startTimePlusDuration = startTimeToMillisec + durationToMillisec
    const compareEndTime = new Date(startTimePlusDuration).toLocaleString()
    // console.log(findOvl.eventCategoryID.id);
    if ((findOvl.eventCategoryName === selectedEvent.value.eventCategoryName)) {
      //  console.log(findOvl.id);
      //  console.log(selectedEvent.value.id);
      //     if (findOvl.id === selectedEvent.value.id) {
      //      if(((compareEndTime < existingEndTime) && (compareEndTime <= existingStartTime)) || ((compareStartTime < existingStartTime) &&(compareStartTime > existingEndTime)) || ((compareStartTime <= existingStartTime) &&(compareEndTime >= existingEndTime)))  {
      //    isOverlapped.value = true
      //    alert(`Sorry, the booking has Overlapped in ${existingStartTime} - ${alertExistEndTime}, Please select new date.`)
      //    }
      // }else 
      if (editDate.value == beforeEditDate.value) {
        isOverlapped.value = false
      } else if (((compareEndTime <= existingEndTime) && (compareEndTime > existingStartTime)) || ((compareStartTime > existingStartTime) && (compareStartTime < existingEndTime)) || ((compareStartTime <= existingStartTime) && (compareEndTime >= existingEndTime))) {
        isOverlapped.value = true
      }

      if (isOverlapped.value == true) {
        alert(`Sorry, the booking has Overlapped in ${existingStartTime} - ${alertExistEndTime}, Please select new date.`)
        return
      }

    }
  })
  if (isOverlapped.value == true) {
    return
  }
  if (isPast.value == true) {
    editDate.value = editDate.value
    alert("Start time must be in the future")
    return
  }
  // if(selectedEvent.value.eventStartTime == )
  if (confirm(`Are you sure to update the booking information ?`)) {
    createHeader()
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events/${selectedEvent.value.id}`, {
      method: 'PUT',
      headers: myHeader.value,
      body: JSON.stringify({
        eventStartTime: editDate.value,
        eventNotes: editNote.value,
      })
    })
    if (res.status === 200) {
      alert("Event updated successfully")
      getDetailById()
      isEdit.value = false
    } else {
      alert("Event can't updated, please fill the require field")
      isEdit.value = true
    }
  } else {
    isEdit.value = true
  }
}
onBeforeMount(async () => {
  await getDetailById();
  await getEventCategory()
  await getEvents()
  currentTime.value = new Date().getFullYear() + '-' + ('0' + (new Date().getMonth() + 1)).slice(-2) + "-" + new Date().getDate() + 'T' + ('0' + new Date().getHours()).slice(-2) + ':' + ('0' + new Date().getMinutes()).slice(-2)

});

window.onbeforeunload = function () {
  return true;
};

</script>

<template>
  <div>
    <div class="hero min-h-screen bg-base-200">
      <div class="hero-content text-left">
        <div
          class="card w-auto lg:w-[1200px] h-full bg-gradient-to-r from-base-100 to-base-200 shadow-xl backdrop-blur-sm mb-12">
          <div class="card-body text-xl md:text-3xl place-self-center">
            <p class="text-5xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-purple-300 to-pink-600 m-4 pb-1 text-center "
              v-if="isEdit == false">
              {{ selectedEvent.eventCategoryName }}
            </p>
            <input type="text" v-model="selectedEvent.eventCategoryName"
              class="input input-bordered input-secondary w-full max-w-xs self-center text-2xl" disabled id="duration"
              v-else />
            <div class="divider"></div>
            <p>
              <IcPerson class="inline-block mr-5" />
              <label> Name:
                <span v-if="isEdit == false">{{ selectedEvent.bookingName }}</span>
                <span v-else><input type="text" v-model="selectedEvent.bookingName"
                    class="input input-bordered input-secondary w-96 max-w-xs self-center text-lg" disabled
                    id="duration" /></span>
              </label>
            </p>
            <br />
            <p>
              <IcTimer class="inline-block mr-5" />
              <label> Duration:
                <span v-if="isEdit == false">{{ selectedEvent.eventDuration }} mins</span>
                <span v-else><input type="text" v-model="selectedEvent.eventDuration"
                    class="input input-bordered input-secondary w-84 max-w-xs self-center text-lg" disabled
                    id="duration" /></span>
              </label>
            </p>
            <br />
            <p>
              <IcCalendar class="inline-block mr-5" />
              <label for="starttime">
                Start time :
              </label>
            <p v-show="!isEdit" class="inline-block">{{ selectedEvent.eventStartTime }}</p>
            <!-- <span class="text-red-500 mr-2" v-show="isEdit" >*</span> -->
            <input v-show="isEdit" type="datetime-local"
              class="input input-bordered input-secondary w-auto max-w-xs text-lg" id="starttime" v-model="editDate"
              :min="currentTime" />
            </p>
            <span class="text-sm text-yellow-500 pb-2 inline-block" v-show="compareDate(editDate, currentTime)">** Start
              time must be in the future.</span>
            <br />
            <p>
              <IcEmail class="inline-block mr-5 " />
              <label> Email:
                <span v-if="isEdit == false">{{ selectedEvent.bookingEmail }}</span>
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
            <p v-if="selectedEvent.eventNotes != ''" v-show="!isEdit" class="inline-block">{{ selectedEvent.eventNotes
            }}
            </p>
            <p v-else v-show="!isEdit" class="inline-block">NO MESSAGE.</p>
            <p>
            <div v-show="isEdit">
              <span class="text-sm text-yellow-500 pb-2" v-show="editNote.length == 500">** A notes length must be 1 -
                500 character.</span>
            </div>
            <textarea type="text" rows="2" v-show="isEdit" id="notes" v-model="editNote"
              class="textarea textarea-secondary text-xl w-full overflow-auto mt-3" maxlength="500"></textarea>
            <label class="label">
              <span class="label-text-alt"></span>
              <span class="label-text-alt" v-show="isEdit">{{ editNote.length }}/500</span>
            </label>
            </p>
            </p>
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
            <button class="btn btn-accent border-none " @click="isEdit = true, updateEvent()"
              v-show="isEdit">Apply</button>
            <button class="btn btn-secondary border-none "
              @click="isEdit = !isEdit, editDate = beforeEditDate, editNote = beforeEditNote"
              v-show="isEdit">Cancel</button>
            <router-link :to="{ name: 'Home' }"><button
                class="btn btn-secondary border-none bg-gradient-to-r from-yellow-500 to-orange-600 hover:from-pink-500 hover:to-yellow-500">Go
                back</button>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
::-webkit-calendar-picker-indicator {
  filter: invert(1);
}
</style>
