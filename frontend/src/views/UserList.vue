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
        // console.log(errText.message);
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
            // console.log(tokenRes.message);

            if (tokenRes.message == "JWT Refresh Token has expired") {
                myUser.setLogout()
                setTimeout(() => {
                    appRouter.push({ name: "SignIn" })
                }, 500)
            }else{
                // newAccessToken = await res.json()
                // console.log(newAccessToken);
                tokenLocal.accessToken = tokenRes.token
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
    <div
        class="mt-10 ml-16 text-3xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-purple-400 to-pink-600">
        USER LIST
    </div>
    <div class="flex items-center mt-8 justify-end mr-16">
        <router-link :to="{ name: 'MatchPass' }">
            <div class="w-4/8 shadow p-5 mr-5 bg-primary text-slate-300 rounded-2xl justify-self-start flex">
                <!-- <div class="text-xl font-extrabold m-2 hover:animate-bounce"> -->
                Match Pass
                <!-- </div> -->
            </div>
        </router-link>
        <router-link :to="{ name: 'AddUser' }">
            <div
                class="w-4/8 shadow p-5 rounded-2xl bg-gradient-to-r from-gray-500/20 to-slate-100/20 bg-opacity-20 justify-self-start flex">
                <div class="text-xl font-extrabold m-2 hover:animate-bounce">
                    <UiAdd class="inline-block" /> Creat new user
                </div>
            </div>
        </router-link>
    </div>
    <div class="flex flex-row h-screen">
        <div class="shadow-inner shadow-lg glass w-screen h-3/4 ml-16 mt-12 mr-16 rounded-2xl overflow-auto">
            <div class=" w-auto text-sm lg:w-[1700px] mx-auto space-y-6 pb-6 lg:text-2xl mt-10">
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
                <div class="grid grid-cols-1  md:grid md:grid-cols-3 md:gap-6 ">
                    <div v-for="(user, index) in userList" :key="index">
                        <div>
                            <div
                                class="grid break-inside-avoid rounded-xl p-4 mb-8 w-full break-words drop-shadow-md text-ellipsis overflow-hidden bg-gray-200 text-slate-500">
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
    </div>
</template>
 
<style>

</style>