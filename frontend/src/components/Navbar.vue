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
      <router-link :to="{name: 'Home'}" class="flex title-font font-medium items-center text-gray-900 mb-4 md:mb-0">
        <div class="w-[40px] rounded-full">
          <img src="../assets/trimitr.png" />
        </div>
        <span class="ml-3 text-xl">TRIMITR</span>
      </router-link>
      <nav class="md:ml-auto flex flex-wrap items-center text-base justify-center">
        <router-link :to="{ name: 'Booking' }" class="mr-5 hover:text-gray-900">Book</router-link>
        <router-link :to="{ name: 'EventCategory' }" class="mr-5 hover:text-gray-900">Categories</router-link>
        <router-link :to="{ name: 'UserList' }" class="mr-5 hover:text-gray-900">User list</router-link>
        <router-link :to="{ name: 'EventList' }" class="mr-5 hover:text-gray-900">Events</router-link>
      </nav>
      <router-link :to="{ name: 'SignIn' }"
        class="inline-flex items-center bg-green-300 border-0 py-1 px-3 focus:outline-none hover:bg-gray-300 rounded text-base mt-4 md:mt-0">Sign In
        <svg fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
          class="w-4 h-4 ml-1" viewBox="0 0 24 24">
          <path d="M5 12h14M12 5l7 7-7 7"></path>
        </svg>
      </router-link>
    </div>
    <router-view></router-view>
  </header>
</template>

<style>

</style>
