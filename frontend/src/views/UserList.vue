<script setup>
import { onBeforeMount, onUpdated, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUser } from '../stores/user';
import UiAdd from '../components/UiAdd.vue';
const myUser = useUser()
const appRouter = useRouter()
const userList = ref([])
// const userDetail = ref([])
// const isDetail = ref(false)
const isEmpty = ref(false)
const isNotLogin = ref(false)
// const jwtToken = ref('')
const myHeader = ref()

const getUser = async () => {
    // console.log(jwtToken);
    createHeader()
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users`, {
        method: "GET",
        headers: myHeader.value
    })
    // console.log(res);

    if (res.status === 401) {
        var errText = await res.json()
        var startWithJwt = /^JWT expired/
        // console.log(startWithJwt.test(errText.message));
        // if (errText.message.match(startWithJwt)) {
        //     console.log('match');
        // }
        // console.log(errText.message);
        // if (errText.message == "JWT Token has expired") {
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
            console.log(tokenRes.message);

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
                await getUser()

            }

        }

        // isNotLogin.value = true
        // // console.log(isLogin);
        // return
    }

    if (res.status != 200) {
        isEmpty.value = true
        userList.value = []
    } else {
        isEmpty.value = false
        userList.value = await res.json()
    }
    userList.value.filter((ul) => {
        ul.role = capitalizeFirstLetter(ul.role)
    })
}
const getDetail = async (name) => {
    appRouter.push({ name: 'UserDetail', params: { name: name } })
}

const deleteUser = async (delName) => {
    // console.log(name);
    // console.log(myHeader.value);
    // console.log(delName);
    // console.log(myUser.userName);
    if (confirm(`Are you sure to delete user name: ${delName}`)) {
        // console.log('delete success');
        if (delName == myUser.userName) {
            myUser.setLogout()
        }
        const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users/${delName}`, {
            method: 'DELETE',
            headers: myHeader.value
        })
        if (res.status === 200) {
            if (delName == myUser.userName) {
                myUser.setLogout()
            }
            userList.value = userList.value.filter((u) => u.name !== delName)
        } else {
            alert("Can't deleted")
        }
    }
}

const capitalizeFirstLetter = (string) => {
    return string.charAt(0).toUpperCase() + string.slice(1)
}

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
onBeforeMount(async () => {
    // jwtToken.value = localStorage.getItem('token')
    // createHeader()
    // console.log(myHeader.value);
    await getUser()
    // console.log(userList.value);
})
</script>
 
<template>
    <div>
        <section class="text-gray-600 body-font">
            <div class="container px-5 py-24 mx-auto">
                <div class="flex flex-initial text-center w-full">
                    <h1 class="sm:text-3xl text-2xl font-medium title-font mb-4 text-gray-900">Users</h1>
                </div>
                <div>
                    <router-link :to="{ name: 'AddUser' }">
                        <button type="button"
                            class="focus:outline-none text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:ring-green-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-green-600 dark:hover:bg-green-700 dark:focus:ring-green-800">Create
                            User</button>
                    </router-link>
                    <router-link :to="{ name: 'MatchPass' }">
                        <button type="button"
                            class="focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-purple-600 dark:hover:bg-purple-700 dark:focus:ring-purple-900">Match Password</button>
                    </router-link>
                </div>
                <div class="flex flex-wrap -m-2">
                    <div class="p-2 lg:w-1/3 md:w-1/2 w-full" v-for="(user, index) in userList" :key="index">

                        <div class="h-full flex items-center border-gray-200 border p-4 rounded-lg">
                            <div class=" flex-grow">
                                <h2 class="text-gray-900 title-font font-medium">{{ user.name }}</h2>
                                <p class="text-gray-500">{{ user.email }}</p>
                                <span v-if="user.role == 'Admin'"
                                    class="bg-red-100 text-red-800 text-xs font-semibold mr-2 px-2.5 py-0.5 rounded dark:bg-red-200 dark:text-red-900">Admin</span>
                                <span v-else-if="user.role == 'Lecturer'"
                                    class="bg-green-100 text-green-800 text-xs font-semibold mr-2 px-2.5 py-0.5 rounded dark:bg-green-200 dark:text-green-900">
                                    {{ user.role }}</span>
                                <span v-else
                                    class="bg-blue-100 text-blue-800 text-xs font-semibold mr-2 px-2.5 py-0.5 rounded dark:bg-blue-200 dark:text-blue-800">{{
                                            user.role
                                    }}</span>


                            </div>

                            <div class="flex items-center p-6 space-x-4 rounded-lg">
                                <button class="justify-self-end flex" @click="getDetail(user.name)"><svg class="w-6 h-6"
                                        fill="none" stroke="currentColor" viewBox="0 0 24 24"
                                        xmlns="http://www.w3.org/2000/svg">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z">
                                        </path>
                                    </svg></button>

                                <button class="justify-self-end flex" @click="deleteUser(user.name)"><svg
                                        class="w-6 h-6" fill="none" stroke="red" viewBox="0 0 24 24"
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

    <!-- <div class="flex flex-row h-screen">
        <div class="w-screen h-3/4 ml-16 mt-12 mr-16 rounded-2xl overflow-auto">
            <div class="w-auto text-sm lg:w-[1700px] mx-auto space-y-6 pb-6 lg:text-2xl mt-10">
                <div v-if="isEmpty" class="grid justify-items-center pt-72">
                    <p class="text-2xl text-gray-400">No User.</p>
                </div>
                <div v-if="isNotLogin" class="grid justify-items-center pt-72">
                    <p class="text-2xl text-gray-400 mb-5">Please sign in to see the users.</p>
                    <router-link :to="{ name: 'SignIn' }" class="btn  normal-case text-lg btn-accent ml-3">Go to
                        SIGN
                        IN
                    </router-link>
                </div>
                <div class="grid md:grid md:grid-cols-3 md:gap-6 w-screen">
                    <div v-for="(user, index) in userList" :key="index">
                        <div>
                            <div
                                class="grid rounded-xl p-4 mb-8 w-full drop-shadow-md bg-gray-200 text-slate-500">
                                <p>Name: {{ user.name }}</p>
                                <p>Email: {{ user.email }}</p>
                                <p>Role: {{ user.role }}</p>
                                <button class="btn btn-primary justify-self-end flex" @click="getDetail(user.name)">See
                                    more</button>
                                <button class="btn btn-secondary justify-self-end flex"
                                    @click="deleteUser(user.name)">Delete</button>
                            </div>
                        </div>
                    </div>
                </div>


                

            </div>
        </div>
    </div> -->
</template>
 
<style>

</style>