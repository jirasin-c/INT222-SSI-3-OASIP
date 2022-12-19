<script setup>
import { get } from "color-string";
import { async } from "postcss-js";
import { onBeforeMount, ref } from "vue";
import { useUser } from "../stores/user";
const myUser = useUser()
const user = ref()
// const logout = () => {
//   localStorage.removeItem("token")
//   localStorage.removeItem("name")
//   myUser.setLogout()
//   location.reload()
// }
const logout = () => {
  if (confirm("Are you sure to sign out ?")) {
    myUser.setLogout()
  } else {
    return
  }
}
const getName = async () => {
  var userToLocal = localStorage.getItem("user")
  var userLocal = JSON.parse(userToLocal)
  // console.log(userLocal);

  if (localStorage.getItem("token") != null && userLocal != null) {
    // console.log(userLocal);
    myUser.setLogin()

    myUser.setUserEmail(userLocal.email)
    myUser.setUserRole(userLocal.role)
    //   const fetchName = userLocal.name
    //   var tokenToLocal = localStorage.getItem("token")
    //   var tokenLocal = JSON.parse(tokenToLocal)
    //   // console.log(localStorage.getItem("token"));
    //   // console.log(localStorage.getItem("name"));
    //   const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users/${fetchName}`, {
    //     method: "GET",
    //     headers: {
    //       "content-type": "application/json",
    //       "Authorization": `Bearer ${tokenLocal.accessToken}`
    //     },
    //   })

    //   if (res.status === 401) {
    //     var errText = await res.json()
    //     console.log(errText.message);
    //     if (errText.message == "JWT Token has expired") {
    //       var tokenToLocal = localStorage.getItem("token")
    //       var tokenLocal = JSON.parse(tokenToLocal)
    //       // var newAccessToken = ""
    //       const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/refreshtoken`, {
    //         method: "GET",
    //         headers: {
    //           "content-type": "application/json",
    //           "Authorization": `Bearer ${tokenLocal.refreshToken}`
    //         },
    //       })
    //       var tokenRes = await res.json()
    //       console.log(tokenRes.message);

    //       if (tokenRes.message == "JWT Refresh Token has expired") {
    //         myUser.setLogout()
    //         // appRouter.push({ name: "SignIn" })
    //       } else {
    //         // newAccessToken = await res.json()
    //         // console.log(newAccessToken);
    //         tokenLocal.accessToken = tokenRes.token
    //         localStorage.setItem('token', JSON.stringify(tokenLocal))
    //         await getName()
    //         // newAccessToken = await res.json()
    //         // console.log(newAccessToken);
    //         // tokenLocal.accessToken = newAccessToken.token
    //         // localStorage.setItem('token', JSON.stringify(tokenLocal))
    //         // getName()
    //       }
    //     }
    //   } else {
    //     // console.log(res.status);
    //     user.value = await res.json()
    //     // console.log(user.value);
    //     // user.value.map((e) => {
    //     //   // console.log(e);
    //     //   if (e.email == email.value) {
    //     myUser.setLogin()
    //     myUser.setUserName(user.value.name)
    //     myUser.setUserEmail(user.value.email)
    //     myUser.setUserRole(user.value.role)
    //     // localStorage.setItem('name', e.name)

    //     //   }
    //     // })
    //   }

  }
}

onBeforeMount(async () => {
  await getName()
})
</script>

<template>
  <!-- <header class="text-black bg-white body-font">
    <div class="container mx-auto flex flex-wrap p-5 flex-col md:flex-row items-center">
      <a class="flex title-font font-small items-center text-black mb-4 md:mb-0 text">
        <router-link :to="{ name: 'Home' }" class="btn btn-ghost normal-case text-xl">
          <div class="avatar pr-2">
            <div class="w-[40px] rounded-full">
              <img src="../assets/trimitr.png" />
            </div>
          </div>TRIMITR GROUP
        </router-link>
      </a>
      <nav class="md:ml-auto flex flex-wrap items-center text-base justify-center">
        <router-link :to="{ name: 'Booking' }" class="btn btn-ghost normal-case text-lg"
          v-show="myUser.userRole != 'lecturer'">BOOKS</router-link>
        <router-link :to="{ name: 'EventCategory' }" class="btn btn-ghost normal-case text-lg">CATEGORIES
        </router-link>
        <router-link :to="{ name: 'UserList' }" class="btn btn-ghost normal-case text-lg"
          v-show="myUser.userRole == 'admin'">USER LIST</router-link>
      </nav>
      <router-link :to="{ name: 'SignIn' }" class="btn  normal-case text-lg btn-accent ml-3">SIGN IN
      </router-link>
    </div>
    <router-view></router-view>
  </header> -->


  <header class="text-gray-700 body-font border-b border-gray-200">
    <div class="container mx-auto flex flex-wrap p-5 flex-col md:flex-row items-center">
      <router-link :to="{ name: 'Home' }" class="flex title-font font-medium items-center text-gray-900 mb-4 md:mb-0">
        <div class="w-[40px] rounded-full">
          <img src="../assets/trimitr.png" />
        </div>
        <span class="ml-3 text-xl">TRIMITR</span>
      </router-link>
      <nav class="md:ml-auto flex flex-wrap items-center text-base justify-center">
        <router-link :to="{ name: 'Booking' }" class="mr-5 hover:text-gray-900" v-show="myUser.userRole != 'lecturer'">Book</router-link>
        <router-link :to="{ name: 'EventCategory' }" class="mr-5 hover:text-gray-900">Categories</router-link>
        <router-link :to="{ name: 'UserList' }" class="mr-5 hover:text-gray-900" v-show="myUser.userRole == 'admin'">User list</router-link>
        <router-link :to="{ name: 'EventList' }" class="mr-5 hover:text-gray-900">Events</router-link>
      </nav>
      <div v-show="myUser.isLogin == true">
        <button id="dropdownDividerButton" data-dropdown-toggle="dropdownDivider"
          class="text-white bg-indigo-500 hover:bg-indigo-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-base px-4 py-2.5 text-center inline-flex items-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
          type="button">User<svg class="ml-2 w-4 h-4" aria-hidden="true" fill="none" stroke="currentColor"
            viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
          </svg></button>
        <div id="dropdownDivider"
          class="hidden z-10 w-44 bg-slate-50 rounded divide-y divide-gray-200 shadow dark:bg-gray-700 dark:divide-gray-600">
          <ul class="py-1 text-sm text-gray-700 dark:text-gray-200" aria-labelledby="dropdownDividerButton">
            <li class="flex justify-center">
              {{myUser.userEmail}}
            </li>
          </ul>
          <div class="py-1 flex justify-center">
            <button @click="logout"
          class="w-64 block py-2 px-4 text-sm rounded-lg text-white bg-rose-500 hover:bg-rose-700 ">SIGN
          OUT
        </button>
          </div>
        </div>
      </div>
      <div v-show="myUser.isLogin == false">
        <router-link :to="{ name: 'SignIn' }"
          class="text-white inline-flex items-center bg-green-400 border-0 px-4 py-2.5 focus:outline-none hover:bg-green-500 rounded-lg text-base mt-4 md:mt-0">
          Sign In
          <svg fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            class="w-4 h-4 ml-1" viewBox="0 0 24 24">
            <path d="M5 12h14M12 5l7 7-7 7"></path>
          </svg>
        </router-link>
      </div>
    </div>
    <router-view></router-view>
  </header>
</template>

<style>

</style>
