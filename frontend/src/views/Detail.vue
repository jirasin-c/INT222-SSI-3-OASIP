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
const roleToLocal = localStorage.getItem("user")
const roleLocal = JSON.parse(roleToLocal)
const fileName = ref()
const showImg = ref()
const imageObjectURL = ref([])
const imageURL = ref([])
const beforeImageURL = ref([])
const file = ref({})
const beforeEditFile = ref({})
const isFileInputed = ref(false)
const isLarger10 = ref(false)
const size = ref(0)
const beforeSize = ref(0)
const currentPath = ref('')

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
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events`, {
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
  const res2 = await fetch(`${import.meta.env.VITE_BASE_URL}api/files/${bookingId}`, {
    method: 'GET',
    headers: myHeader.value,
  }
  );
  if (res2.status != 200) {
    fileName.value = ''
  } else {
    fileName.value = await res2.json()
    fileName.value = fileName.value.toString()
    console.log(fileName.value);
  }


  const res3 = await fetch(`${import.meta.env.VITE_BASE_URL}api/files/${bookingId}/${fileName.value}`, {
    method: 'GET',
    headers: myHeader.value,
  }
  );
  if (res3.status != 200) {
    showImg.value = ''
    // console.log(imageObjectURL.value);
  } else {
    showImg.value = await res3.blob()
    console.log(showImg.value);
    // console.log(showImg.value.type);

    imageObjectURL.value = URL.createObjectURL(showImg.value);
    console.log(imageObjectURL.value);
    imageURL.value[0] = (imageObjectURL.value)
    beforeImageURL.value = imageURL.value
    isFileInputed.value = true
    console.log(imageURL.value);
    size.value = formatBytes(showImg.value.size)
    beforeSize.value = size.value
    file.value = {
      name: fileName.value,
      type: showImg.value.type
    }
    beforeEditFile.value = file.value
  }



  const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events/${bookingId}`, {
    method: 'GET',
    headers: myHeader.value,
  }
  );
  if (res.status == 403 || res.status == 404 || res.status == 401) {
    return appRouter.push({ name: "Home" })
  } else if (res.status == 401) {
    var errText = await res.json()
    var startWithJwt = /^JWT expired/
    if (errText.message.match(startWithJwt)) {
      var tokenToLocal = localStorage.getItem("token")
      var tokenLocal = JSON.parse(tokenToLocal)
      // var newAccessToken = ""
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/refresh`, {
        method: "GET",
        headers: {
          "content-type": "application/json",
          "Authorization": `Bearer ${tokenLocal.refreshToken}`
        },
      })
      var tokenRes = await res.json()
      if (tokenRes.message == "Refresh token was expired. Please make a new signin request") {
        myUser.setLogout()
        setTimeout(() => {
          appRouter.push({ name: "SignIn" })
        }, 500)
      } else {
        // newAccessToken = await res.json()
        // console.log(newAccessToken);
        tokenLocal.accessToken = tokenRes.accessToken
        localStorage.setItem('token', JSON.stringify(tokenLocal))
        await getDetailById()

      }

    }
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
  console.log(editDate.value);
  console.log(beforeEditDate.value);
  console.log(editNote.value);
  console.log(beforeEditNote.value);
  if (editDate.value == beforeEditDate.value && editNote.value == beforeEditNote.value && beforeEditFile.value.name == file.value.name && beforeEditFile.value.type == file.value.type) {
    isLarger10.value = false
    isEdit.value = false
    return
  }
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
    // createHeader()
    const utc = new Date(editDate.value).toISOString()
    const formData = new FormData()
    if (localStorage.getItem('token') != null) {
      var tokenToLocal = localStorage.getItem("token")
      var tokenLocal = JSON.parse(tokenToLocal)
      // console.log(tokenLocal);
      myHeader.value = new Headers({
        "accept": "*/*",
        "Authorization": `Bearer ${tokenLocal.accessToken}`,
      })
    }
    formData.append('event', `{
                    "eventStartTime": "${utc}",
                    "eventNotes": "${notes.value}"
                  }`)
    if (Object.keys(file.value).length === 0 && file.value.constructor === Object) {
      formData.append('file', null)
    } else {
      formData.append('file', file.value)
    }
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events/${selectedEvent.value.id}`, {
      method: 'PATCH',
      headers: myHeader.value,
      body: formData
    })
    if (res.status === 200) {
      alert("Event updated successfully")
      getDetailById()
      isEdit.value = false
      isLarger10.value = false
    } else {
      alert("Event can't updated, please fill the require field")
      isEdit.value = true
    }
  } else {
    isEdit.value = true
  }
}

const onFileSelected = (e) => {
  // console.log(Object.keys(file.value).length === 0 && file.value.constructor === Object);
  isLarger10.value = false
  isFileInputed.value = false
  if (Object.keys(file.value).length === 0 && file.value.constructor === Object) {
    // console.log("ยังไม่มี");
    console.log(e.target.files[0]);
    file.value = e.target.files[0]
    currentPath.value = e.target.value
    // console.log(currentPath.value);
    if (file.value.size > 10485760) {
      // console.log("เกิน");
      e.target.value = null
      file.value = {}
      isFileInputed.value = false
      // console.log(e.target.files);
      isLarger10.value = true
      return
    }
    const newImgURL = []
    console.log(file.value);
    console.log(URL.createObjectURL(file.value));
    newImgURL.push(URL.createObjectURL(file.value))
    console.log(newImgURL);
    imageURL.value = newImgURL
    console.log(imageURL.value);
    isFileInputed.value = true
    size.value = formatBytes(file.value.size)
  } else {
    // console.log("มีแล้ว");
    // console.log(e.target.files.length == 0);
    if (e.target.files.length == 0) {
      isFileInputed.value = true
      return
    }

    let checkFile = {}
    checkFile = e.target.files[0]
    if (checkFile.size > 10485760) {
      // console.log("เกิน");
      // console.log(file.value);
      // console.log(e.target.files[0]);
      // console.log(currentPath.value);

      e.target.value = null
      // console.log(e.target.value);
      // file.value = {}
      isFileInputed.value = true
      // console.log(e.target.files);
      isLarger10.value = true
      return
    }
    file.value = checkFile
    const newImgURL = []
    newImgURL.push(URL.createObjectURL(file.value))
    // console.log(newImgURL);
    imageURL.value = newImgURL
    console.log(imageURL.value);
    isFileInputed.value = true
    size.value = formatBytes(file.value.size)
  }
  // console.log(file.value.size);
  // console.log(file.value.type);
  // console.log(e.target.files);
  // console.log(e.target.value);
  // isLarger10.value = false
  // file.value = e.target.files[0]
  // isFileInputed.value = true
  // console.log(e.target.files);
  // console.log(e.target.files[0]);
  // console.log(file.value);



  // if (file.value.size > 10485760) {
  //     e.target.value = null
  //     file.value = {}
  //     isFileInputed.value = false
  //     // console.log(e.target.files);
  //     isLarger10.value = true
  // } else {
  //     file.value = e.target.files[0]
  //     isFileInputed.value = true
  //     size.value = formatBytes(file.value.size)
  // }

  // console.log(size.value);
  // if (isDeleteFile.value == true) {
  //     e.target.value = null
  // }
  // if (file.value.length > 0) {
  //     isLarger10.value = true
  // }else 
  // if (file.value.size > 10485760) {
  //     e.target.value = null
  //     file.value = {}
  //     isFileInputed.value = false
  //     // console.log(e.target.files);
  //     isLarger10.value = true
  // }
  // console.log(file.value);
  // console.log(file.value.name);
}
const deleteFile = () => {
  // isDeleteFile.value = true
  // console.log(file.value);
  if (confirm("Are you sure to remove this file?")) {
    document.getElementById('input').value = null
    file.value = {}
    isFileInputed.value = false
    console.log(size.value);
    console.log(beforeSize.value);
    size.value = 0
    console.log(size.value);
    console.log(beforeSize.value);
    imageURL.value = []
  } else {
    return
  }

}
const formatBytes = (bytes, decimals = 2) => {
  if (!+bytes) return '0 Bytes'

  const k = 1024
  const dm = decimals < 0 ? 0 : decimals
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']

  const i = Math.floor(Math.log(bytes) / Math.log(k))

  return `${parseFloat((bytes / Math.pow(k, i)).toFixed(dm))} ${sizes[i]}`
}
onBeforeMount(async () => {
  await getDetailById();
  await getEventCategory()
  await getEvents()
  currentTime.value = new Date().getFullYear() + '-' + ('0' + (new Date().getMonth() + 1)).slice(-2) + "-" + new Date().getDate() + 'T' + ('0' + new Date().getHours()).slice(-2) + ':' + ('0' + new Date().getMinutes()).slice(-2)
  console.log(file.value);
  console.log(beforeEditFile.value);
});

window.onbeforeunload = function () {
  return true;
};

</script>

<template>


  <div>
    <div class="max-w-xl rounded-xl overflow-hidden shadow-lg mx-auto my-8 bg-white">
      <div class="px-6 py-4">
        <div class="font-bold text-xl mb-2">
          <p class="text-5xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-purple-300 to-pink-600 m-4 pb-1 text-center "
            v-if="isEdit == false">
            {{ selectedEvent.eventCategoryName }}
          </p>
          <p>
            <IcPerson class="inline-block mr-5" />
            <label>
              <span v-if="isEdit == false">{{ selectedEvent.bookingName }}</span>
              <span v-else><input type="text" v-model="selectedEvent.bookingName"
                  class="rounded-lg w-96 max-w-xs self-center text-lg" disabled
                  id="duration" /></span>
            </label>
          </p>
          <br />
          <p>
            <IcTimer class="inline-block mr-5" />
            <label>
              <span v-if="isEdit == false">{{ selectedEvent.eventDuration }} mins</span>
              <span v-else><input type="text" v-model="selectedEvent.eventDuration"
                  class="rounded-lg input-secondary w-84 max-w-xs self-center text-lg" disabled
                  id="duration" /></span>
            </label>
          </p>
          <br />
          <p>
            <IcCalendar class="inline-block mr-5" />
            <label for="starttime">
            </label>
          <p v-show="!isEdit" class="inline-block">{{ selectedEvent.eventStartTime }}</p>
          <!-- <span class="text-red-500 mr-2" v-show="isEdit" >*</span> -->
          <input v-show="isEdit" type="datetime-local"
            class="rounded-lg w-auto max-w-xs text-lg" id="starttime" v-model="editDate"
            :min="currentTime" />
          </p>
          <span class="text-sm text-yellow-500 pb-2 inline-block" v-show="compareDate(editDate, currentTime)">** Start
            time must be in the future.</span>
          <br />
          <p>
            <IcEmail class="inline-block mr-5 " />
            <label>
              <span v-if="isEdit == false">{{ selectedEvent.bookingEmail }}</span>
              <span v-else><input type="text" v-model="selectedEvent.bookingEmail"
                  class="rounded-lg w-full max-w-xs self-center text-lg" disabled
                  id="duration" /></span>
            </label>
          </p>
          <br />
          <p>
            <IcNote class="inline-block mr-5" />
            <label for="notes">
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
            class="rounded-lg text-xl w-full overflow-auto mt-3" maxlength="500"></textarea>
          <label class="label">
            <span class="label-text-alt"></span>
            <span class="label-text-alt text-base" v-show="isEdit">{{ editNote.length }}/500</span>
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
          <p class="" v-if="isEdit">
            <!-- <label>File name:</label> -->
            <label for="input" class="label">
              <span class="label-text text-base font-semibold text-secondary flex">
                Maximun 10 MB.
              </span>
            </label>
            <input type="file" class="flex rounded-lg  w-full max-w-xs" @change="onFileSelected"
              id="input" />
            <span class="flex text-sm text-yellow-500 pb-2" v-show="isLarger10">** The file size cannot
              be large than 10 MB. **</span>
          <div class="pt-5 avatar flex"
            v-if="(file.type == 'image/png' || file.type == 'image/jpeg') && imageURL.length > 0">
            <div class="flex w-96 h-64 rounded-xl">
              <!-- <img :src="imageObjectURL" width="300" height="300" alt="">
                                         -->
              <img :src="imageURL" alt="" width="400" height="400" class="rounded-lg">
            </div>
          </div>

          <!-- <p>{{file.name}}</p> -->
          <ul v-show="isFileInputed">
            <li class="text-sm flex justify-between mt-5">{{ file.name }}
              <span class="text-secondary">{{ size }}</span>
              <button @click="deleteFile">
                <svg width="32" height="32" viewBox="0 0 24 24">
                  <path fill="currentColor"
                    d="M15.59 7L12 10.59L8.41 7L7 8.41L10.59 12L7 15.59L8.41 17L12 13.41L15.59 17L17 15.59L13.41 12L17 8.41L15.59 7Z" />
                </svg>
              </button>
            </li>
          </ul>
          </p>
          <p class="flex" v-else>
            <!-- <IcEmail class="inline-block mr-5 " /> -->
            <label> File name:
              <!-- <a :href="imageObjectURL" target="_blank" class="flex text-lg link link-secondary" >{{ fileName }}</a> -->
              <a :href="imageObjectURL" class="flex text-lg link link-secondary" :download="fileName">{{ fileName
              }}</a>
              <span v-show="imageObjectURL.length == 0" class="text-secondary text-lg">No file.</span>
            </label>
          <div class="pl-5 avatar flex"
            v-show="imageObjectURL.length > 0 && (showImg.type == 'image/png' || showImg.type == 'image/jpeg')">
            <div class="w-96 h-64 rounded-xl">
              <img :src="imageObjectURL" width="300" height="300" alt="">
            </div>
          </div>

          </p>
          <div class="card-actions justify-end p-4">
            <button v-if="roleLocal.role != 'lecturer'" @click="isEdit = !isEdit" v-show="!isEdit" type="button"
              class="p-2 focus:outline-none text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:ring-green-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-green-600 dark:hover:bg-green-700 dark:focus:ring-green-800">
              Edit</button>
            <button @click="isEdit = true, updateEvent()" v-show="isEdit" class="mr-2 focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4
              focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-purple-600
              dark:hover:bg-purple-700 dark:focus:ring-purple-900">Apply</button>
            <button
              @click="isEdit = !isEdit, editDate = beforeEditDate, editNote = beforeEditNote, file = beforeEditFile, imageURL = beforeImageURL, size = beforeSize, fileName == '' ? isFileInputed = false : isFileInputed = true"
              v-show="isEdit"
              class="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900">Cancel</button>

            <router-link :to="{ name: 'EventList' }"><button
                class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800">Go
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
