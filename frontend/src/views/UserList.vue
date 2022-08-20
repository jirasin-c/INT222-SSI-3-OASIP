<script setup>
import { onBeforeMount, ref } from 'vue';
import { useRouter } from 'vue-router';
import UiAdd from '../components/UiAdd.vue';
const appRouter = useRouter()
const userList = ref([])
// const userDetail = ref([])
// const isDetail = ref(false)
const isEmpty = ref(false)

const getUser = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users`)
    userList.value = await res.json()
    if (res.status != 200) {
        isEmpty.value = true
        userList.value = []
    } else {
        isEmpty.value = false
    }
    //      if (res.status == 404) {
    //     isEmpty.value = true;
    //     event.value = []
    //   } else {
    //     isEmpty.value = false;
    //   }
}
const getDetail = async (name) => {


    // console.log(isDetail.value);
    // isDetail.value = true
    // const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users/${name}`)
    // userDetail.value = await res.json()
    // console.log(userDetail.value.name);
    // console.log(name);

    // appRouter.push({ name: 'UserDetail', params: userDetail.value })
    // appRouter.push({ name: 'UserDetail', params: { obj: userDetail.value } })
    appRouter.push({ name: 'UserDetail', params: { name: name } })
    // appRouter.push({ name: 'UserDetail' })
    // console.log(userDetail.value);
    // console.log(isDetail.value);

    //      if (res.status == 404) {
    //     isEmpty.value = true;
    //     event.value = []
    //   } else {
    //     isEmpty.value = false;
    //   }

    //   event.value.filter((e) => {
    //     const localDate = new Date(e.eventStartTime).toLocaleString("th-TH", {
    //       weekday: "short",
    //       month: "short",
    //       day: "numeric",
    //       year: "2-digit",
    //       hour: "numeric",
    //       minute: "numeric",
    //     });
    //     e.eventStartTime = localDate;
    //   });
}

onBeforeMount(async () => {
    await getUser()
    console.log(userList.value);
})
</script>
 
<template>
    <div
        class="mt-10 ml-16 text-3xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-purple-400 to-pink-600">
        USER LIST
    </div>
    <div class="flex items-center mt-8 justify-end mr-16">
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
                <div class="grid grid-cols-1  md:grid md:grid-cols-3 md:gap-6 ">
                    <!-- <div v-if="isDetail">
                        <div
                            class="grid break-inside-avoid rounded-xl p-4 mb-8 w-full break-words drop-shadow-md text-ellipsis overflow-hidden bg-gray-200 text-slate-500">
                            <p>Name: {{ userDetail.name }}</p>
                            <p>Email: {{ userDetail.email }}</p>
                            <p>Role: {{ userDetail.role }}</p>
                            <p>Created: {{ userDetail.createdOn }}</p>
                            <p>Updated: {{ userDetail.updatedOn }}</p>
                            <button class="btn btn-secondary" @click="getDetail(user.name)">See less</button>
                        </div>
                    </div> -->
                    <div v-for="(user, index) in userList" :key="index">
                        <div>
                            <div
                                class="grid break-inside-avoid rounded-xl p-4 mb-8 w-full break-words drop-shadow-md text-ellipsis overflow-hidden bg-gray-200 text-slate-500">
                                <!-- hover:bg-slate-500 hover:text-white grid break-inside-avoid rounded-xl p-4 mb-8 w-full break-words drop-shadow-md text-ellipsis overflow-hidden bg-gray-200 text-slate-500 -->
                                <!-- <div v-if="isDetail"> -->
                                <p>Name: {{ user.name }}</p>
                                <p>Email: {{ user.email }}</p>
                                <p>Role: {{ user.role }}</p>
                                <button class="btn btn-primary" @click="getDetail(user.name)">See
                                    more</button>
                                <button class="btn btn-secondary" @click="getDetail(user.name)">Delete</button>
                                <!-- </div> -->
                                <!-- <div v-else> -->

                                <!-- </div> -->
                            </div>
                            <!-- <div class="hover:h-[300px]">
                            <p>Name: {{user.name}}</p>
                                <p>Email: {{user.email}}</p>
                                <p>Role: {{user.role}}</p>
                            </div> -->
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</template>
 
<style>
</style>