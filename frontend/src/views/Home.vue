<script setup>
import EventCard from "../components/EventCard.vue";
import ShowSelectedEvent from "../components/ShowSelectedEvent.vue";
import { useRouter } from 'vue-router';
import { onBeforeMount, onUpdated, ref } from "vue";
import { useUser } from "../stores/user";
import { Breadcrumb } from 'flowbite-vue'
import router from "../router";
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
      <section class="text-gray-700 body-font">
        <div class="container mx-auto flex px-5 py-24 md:flex-row flex-col items-center">
          <div
            class="lg:flex-grow md:w-1/2 lg:pr-24 md:pr-16 flex flex-col md:items-start md:text-left mb-16 md:mb-0 items-center text-center">
            <h1 class="title-font sm:text-4xl text-3xl mb-4 font-medium text-gray-900">Talk with your instructors,
              <br class="hidden lg:inline-block">make your works better
            </h1>
            <p class="mb-8 leading-relaxed">The OASIP Application makes it simple for you to schedule a session with
              your teacher. Try out our features or click "Book Now" below to begin.</p>
            <div class="flex justify-center">
              <router-link :to="{name: 'Booking'}"
                class="inline-flex text-white bg-sky-400 border-0 py-2 px-6 focus:outline-none hover:bg-indigo-600 rounded text-lg">BOOK
                NOW</router-link>
              <router-link :to="{name: 'EventList'}"
                class="ml-4 inline-flex text-gray-700 bg-gray-200 border-0 py-2 px-6 focus:outline-none hover:bg-gray-300 rounded text-lg">View Booking</router-link>
            </div>
          </div>
          <div class="lg:max-w-lg lg:w-full md:w-1/2 w-5/6">
            <img class="object-cover object-center rounded" alt="hero" src="../assets/undraw_educator_re_ju47.svg">
          </div>
        </div>
      </section>
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
