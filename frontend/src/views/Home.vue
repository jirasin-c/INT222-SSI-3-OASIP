<script setup>
import EventCard from "../components/EventCard.vue";
import ShowSelectedEvent from "../components/ShowSelectedEvent.vue";
import { useRouter } from 'vue-router';
import { onBeforeMount, onUpdated, ref } from "vue";
import { useUser } from "../stores/user";
const appRouter = useRouter()
const event = ref([]);
const eventFilter = ref([])
const eventCategory = ref([])
const selectCategory = ref('Select category')
const categoryStatus = ref('Select status')
const isEmpty = ref(false)
const isFindeNoByCategory = ref(false)
const isFindeNoByUpComing = ref(false)
const isFindeNoByPass = ref(false)
const isFindeNoByDate = ref(false)
const startTime = ref()
const filterType = ref("Select type")
const myHeader = ref()
const myUser = useUser()

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

const getEvent = async () => {
  if (localStorage.getItem('token') != null) {
    isFindeNoByCategory.value = false
    isFindeNoByUpComing.value = false;
    isFindeNoByPass.value = false
    isFindeNoByDate.value = false
    isEmpty.value = false
    createHeader()
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events`, {
      method: "GET",
      headers: myHeader.value
    });

    if (res.status === 404) {
      isEmpty.value = true;
      event.value = []

    }

    if (res.status === 401 || res.status === 500) {
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
            appRouter.push({ name: "Home" })
          }, 500)
        } else {
          // newAccessToken = await res.json()
          // console.log(newAccessToken);
          tokenLocal.accessToken = tokenRes.accessToken
          localStorage.setItem('token', JSON.stringify(tokenLocal))
          await getEvent()

        }

      }
    }
    else {
      event.value = await res.json();
      isEmpty.value = false;
    }

    event.value.filter((e) => {
      const localDate = new Date(e.eventStartTime).toLocaleString("th-TH", {
        weekday: "short",
        month: "short",
        day: "numeric",
        year: "2-digit",
        hour: "numeric",
        minute: "numeric",
      });
      e.eventStartTime = localDate;
    });
  } else {
    isEmpty.value = true;
    event.value = []
  }

}

const getEventCategory = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/event-categories`, {
    method: "GET",
    headers: {
      "content-type": "application/json",
    }
  });
  eventCategory.value = await res.json()
}

const filterCategory = async (ev) => {
  isFindeNoByCategory.value = false
  isFindeNoByUpComing.value = false;
  isFindeNoByPass.value = false
  isFindeNoByDate.value = false
  isEmpty.value = false
  if (selectCategory.value == 'All category') {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events`)
    event.value = await res.json()
    isFindeNoByCategory.value = false
    console.log(eventFilter.value);
    event.value.filter((e) => {
      const localDate = new Date(e.eventStartTime).toLocaleString("th-TH", {
        weekday: "short",
        month: "short",
        day: "numeric",
        year: "2-digit",
        hour: "numeric",
        minute: "numeric",
      });
      e.eventStartTime = localDate;
    });
  } else {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events/?categoryId=${selectCategory.value}`)
    if (res.status == 404) {
      isFindeNoByCategory.value = true;
      event.value = []
    } else {
      isFindeNoByCategory.value = false;
      event.value = await res.json()
      event.value.filter((e) => {
        const localDate = new Date(e.eventStartTime).toLocaleString("th-TH", {
          weekday: "short",
          month: "short",
          day: "numeric",
          year: "2-digit",
          hour: "numeric",
          minute: "numeric",
        });
        e.eventStartTime = localDate;
      });
    }
  }
}

const filterCategoryStatus = async () => {
  isFindeNoByCategory.value = false
  isFindeNoByUpComing.value = false;
  isFindeNoByPass.value = false
  isFindeNoByDate.value = false
  isEmpty.value = false
  if (categoryStatus.value == 'Up coming') {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events/upComing`)
    if (res.status == 404) {
      isFindeNoByUpComing.value = true;
      event.value = []
    } else {
      isFindeNoByUpComing.value = false;
      event.value = await res.json()
      event.value.filter((e) => {
        const localDate = new Date(e.eventStartTime).toLocaleString("th-TH", {
          weekday: "short",
          month: "short",
          day: "numeric",
          year: "2-digit",
          hour: "numeric",
          minute: "numeric",
        });
        e.eventStartTime = localDate;
      });
    }
  } else {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events/past`)
    // console.log(res.json());
    if (res.status == 404) {
      isFindeNoByPass.value = true
      event.value = []
    } else {
      isFindeNoByPass.value = false
      event.value = await res.json()
      event.value.filter((e) => {
        const localDate = new Date(e.eventStartTime).toLocaleString("th-TH", {
          weekday: "short",
          month: "short",
          day: "numeric",
          year: "2-digit",
          hour: "numeric",
          minute: "numeric",
        });
        e.eventStartTime = localDate;
      });
    }
    // console.log(event.value);
  }
}

const filterDay = async () => {
  isFindeNoByCategory.value = false
  isFindeNoByUpComing.value = false;
  isFindeNoByPass.value = false
  isFindeNoByDate.value = false
  isEmpty.value = false
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events/day/?dateEvent=${startTime.value}`)
  // console.log(res.json());
  if (res.status == 404) {
    isFindeNoByDate.value = true
    event.value = []
  } else {
    isFindeNoByDate.value = false
    event.value = await res.json()
    event.value.filter((e) => {
      const localDate = new Date(e.eventStartTime).toLocaleString("th-TH", {
        weekday: "short",
        month: "short",
        day: "numeric",
        year: "2-digit",
        hour: "numeric",
        minute: "numeric",
      });
      e.eventStartTime = localDate;
    });
  }
}

const resetFilter = () => {
  // if (filterType.value == 'Category') {
  //   console.log('cate');
  // }
  // filterType.value = 'Select type'
  selectCategory.value = "Select category"
  categoryStatus.value = "Select status"
  startTime.value = null
}

const clearFilter = async () => {
  filterType.value = 'Select type'
  selectCategory.value = "Select category"
  categoryStatus.value = "Select status"
  startTime.value = null
  await getEvent()
}

const getDetail = (id) => {
  // console.log(id);
  appRouter.push({ name: 'Detail', params: { bookingId: id.bookingId } })
}
const cancelEvent = async (id) => {
  id.event.stopPropagation()
  if (confirm(`Are you sure to delete Event name: ${id.deleteName} ?`)) {
    createHeader()
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events/${id.deleteId}`, {
      method: 'DELETE',
      headers: myHeader.value
    })
    if (res.status === 200) {
      event.value = event.value.filter((e) => e.id !== id.deleteId)
    } else {
      alert("Can't deleted")
    }
  }

}

onBeforeMount(async () => {
  await getEvent();
  await getEventCategory()
  // console.log(event.value);
});

onUpdated(() => {
  // console.log(selectCategory.value);
  // console.log(startTime.value);
  // console.log(filterType.value);
})
</script>

<template>
  <div>
    <div>
      <div
        class="mt-10 ml-16 text-3xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-purple-400 to-pink-600">
        CURRENT BOOKING</div>
      <div class="flex items-center mt-8 justify-end mr-16">
        <div
          class="w-4/8 shadow p-5 rounded-2xl bg-gradient-to-r from-gray-500/20 to-slate-100/20 bg-opacity-20 justify-self-start flex">
          <div class="text-2xl font-extrabold m-2">
            Filter by
          </div>
          <select class="select select-bordered w-36 max-w-xs mr-1" name="" id="" v-model="filterType"
            @change="resetFilter">
            <option select disabled>Select type</option>
            <option>Category</option>
            <option>Event status</option>
            <option>Date</option>
          </select>

          <div class="mr-1">
            <select class="select select-bordered w-full max-w-xs mr-1" name="" id="" v-model="selectCategory"
              @change="filterCategory" v-show="filterType == 'Category'">
              <option selected disabled>Select category</option>
              <option selected>All category</option>
              <option v-for="category in eventCategory" :key="category.id" :value="category.id">
                {{ category.eventCategoryName }}</option>
              <!-- <option value="" selected>All category</option>
                <option value="" >Client-side</option>
                <option value="" >All category</option> -->
            </select>
            <select class="select select-bordered w-full max-w-xs" name="" id="" v-model="categoryStatus"
              @change="filterCategoryStatus" v-show="filterType == 'Event status'">
              <option disabled selected>Select status</option>
              <option>Up coming</option>
              <option>Past</option>
              <!-- <option v-for="category in eventCategory" :key="category.id" :value="category.id">{{category.eventCategoryName}}</option> -->
              <!-- <option value="" selected>All category</option>
                <option value="" >Client-side</option>
                <option value="" >All category</option> -->
            </select>
            <input type="date" class="input input-bordered w-full max-w-xs text-lg" v-model="startTime" id="starttime"
              @change="filterDay" v-show="filterType == 'Date'">
          </div>
          <button class="btn btn-secondary" v-show="filterType !== 'Select type'" @click="clearFilter">CLEAR</button>
        </div>
      </div>
    </div>
    <div class="flex flex-row h-screen">
      <div class="shadow-inner shadow-lg glass w-screen h-3/4 ml-16 mt-12 mr-16 rounded-2xl overflow-auto">
        <div v-if="isEmpty" class="grid justify-items-center pt-72">
          <p class="text-2xl text-gray-400">List currently empty.</p>
        </div>
        <div v-else-if="isFindeNoByCategory" class="grid justify-items-center pt-72">
          <p class="text-2xl text-gray-400">No Schedule Events.</p>
        </div>
        <div v-else-if="isFindeNoByUpComing" class="grid justify-items-center pt-72">
          <p class="text-2xl text-gray-400">No On-Going or
            Upcoming Events.</p>
        </div>
        <div v-else-if="isFindeNoByPass" class="grid justify-items-center pt-72">
          <p class="text-2xl text-gray-400">No Past Events.</p>
        </div>
        <div v-else-if="isFindeNoByDate" class="grid justify-items-center pt-72">
          <p class="text-2xl text-gray-400">No Schedule Events.</p>
        </div>


        <!-- <div class=" columns-3 gap-6 w-[1700px] mx-auto space-y-6 pb-6 text-2xl mt-10 " id="style-1"> -->
        <div class=" w-auto text-sm lg:w-[1700px] mx-auto space-y-6 pb-6 lg:text-2xl mt-10">
          <EventCard :allBooking="event" @viewDetail="getDetail" @deleteEvent="cancelEvent"
            class="grid grid-cols-1  md:grid md:grid-cols-3 md:gap-6 " />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
::-webkit-scrollbar {
  width: 16px;

}

::-webkit-scrollbar-track {
  background-color: #e4e4e4;
  border-radius: 100px;
}

::-webkit-scrollbar-thumb {
  background-image: linear-gradient(180deg, #D0368A 0%, #708AD4 99%);
  box-shadow: inset 2px 2px 5px 0 rgba(#fff, 0.5);
  border-radius: 100px;
  border: 2px solid transparent;
  background-clip: content-box;
}

::-webkit-calendar-picker-indicator {
  filter: invert(1);
}
</style>
