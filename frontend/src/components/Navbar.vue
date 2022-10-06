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

    const fetchName = userLocal.name
    var tokenToLocal = localStorage.getItem("token")
    var tokenLocal = JSON.parse(tokenToLocal)
    // console.log(localStorage.getItem("token"));
    // console.log(localStorage.getItem("name"));
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users/${fetchName}`, {
      method: "GET",
      headers: {
        "content-type": "application/json",
        "Authorization": `Bearer ${tokenLocal.accessToken}`
      },
    })

    if (res.status === 401) {
      var errText = await res.json()
      console.log(errText.message);
      if (errText.message == "JWT Token has expired") {
        var tokenToLocal = localStorage.getItem("token")
        var tokenLocal = JSON.parse(tokenToLocal)
        // var newAccessToken = ""
        const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/refreshtoken`, {
          method: "GET",
          headers: {
            "content-type": "application/json",
            "Authorization": `Bearer ${tokenLocal.refreshToken}`
          },
        })
        var tokenRes = await res.json()
        console.log(tokenRes.message);

        if (tokenRes.message == "JWT Refresh Token has expired") {
          myUser.setLogout()
          // appRouter.push({ name: "SignIn" })
        } else {
          // newAccessToken = await res.json()
          // console.log(newAccessToken);
          tokenLocal.accessToken = tokenRes.token
          localStorage.setItem('token', JSON.stringify(tokenLocal))
          await getName()
          // newAccessToken = await res.json()
          // console.log(newAccessToken);
          // tokenLocal.accessToken = newAccessToken.token
          // localStorage.setItem('token', JSON.stringify(tokenLocal))
          // getName()
        }
      }
    } else {
      // console.log(res.status);
      user.value = await res.json()
      // console.log(user.value);
      // user.value.map((e) => {
      //   // console.log(e);
      //   if (e.email == email.value) {
      myUser.setLogin()
      myUser.setUserName(user.value.name)
      myUser.setUserEmail(user.value.email)
      myUser.setUserRole(user.value.role)
      // localStorage.setItem('name', e.name)

      //   }
      // })
    }

  }
}

onBeforeMount(async () => {
  await getName()
})
</script>

<template>
  <div class="w-screen">
    <div
      class="navbar bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500 text-primary-content flex justify-between">
      <div>
        <div class="hidden md:block md:w-auto">
          <router-link :to="{ name: 'Home' }" class="btn btn-ghost normal-case text-xl">
            <div class="avatar pr-2">
              <div class="w-[40px] rounded-full">
                <img src="../assets/trimitr.png" />
              </div>
            </div>TRIMITR GROUP
          </router-link>
        </div>
        <div class="md:hidden">
          <router-link :to="{ name: 'Home' }" class="btn btn-ghost normal-case text-sm mb-2">
            <div class="w-[40px] rounded-full">
              <img src="../assets/trimitr.png" />
              trimitr
            </div>
          </router-link>
        </div>
        <div class="ml-5">
          <router-link :to="{ name: 'Booking' }" class="btn btn-ghost normal-case text-lg">BOOKS</router-link>
          <router-link :to="{ name: 'EventCategory' }" class="btn btn-ghost normal-case text-lg">CATEGORIES
          </router-link>
          <router-link :to="{ name: 'UserList' }" class="btn btn-ghost normal-case text-lg"
            v-show="myUser.userRole == 'admin' ">USER LIST</router-link>
          <!-- <a class="btn btn-ghost normal-case text-xl">TRIMITR GROUP</a> -->
        </div>
      </div>
      <div class="mr-20" v-if="myUser.isLogin == true">
        <router-link :to="{ name: 'UserDetail', params: { name: myUser.userName } }"
          class="btn btn-ghost normal-case text-lg hover:btn-primary ">
          Welcome: {{myUser.userName}}
        </router-link>
        <button @click="logout" class="btn  normal-case text-lg btn-accent ml-3">SIGN OUT
        </button>
      </div>
      <div class="mr-20" v-else>
        <!-- <router-link :to="{ name: 'AddUser' }" class="btn btn-ghost normal-case text-lg hover:btn-primary ">SIGN UP
        </router-link> -->
        <router-link :to="{ name: 'SignIn' }" class="btn  normal-case text-lg btn-accent ml-3">SIGN IN
        </router-link>
      </div>
    </div>
    <router-view></router-view>
  </div>
</template>

<style>

</style>
