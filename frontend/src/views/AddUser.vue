<script setup>
import { onBeforeMount, onUpdated, ref } from 'vue';
import { useRouter } from 'vue-router';
const appRouter = useRouter()
const user = ref([])
const name = ref('')
const email = ref('')
const roles = ref(['Student', 'Lecturer', 'Admin', 'Guest'])
const selectedRole = ref('Select your role')
const isNotEmail = ref(false)
const falseInput = ref(false)
const alertText = ref('')
const success = ref(false)
const usedName = ref(false)
const usedEmail = ref(false)
const isShowPassword = ref(false)
const isShowRePassword = ref(false)
const password = ref('')
const rePassword = ref('')
const passwordUnmatch = ref(false)
const errText = ref('')
const myHeader = ref()
// onUpdated(() => {
//     console.log(isShowPassword.value);
// })
// const getUser = async () => {
//     const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users/`)
//     user.value = await res.json()
// }

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

const passwordCheckMatch = (checkedPass) => {
    // console.log(checkedPass);
    if (checkedPass != password.value) {
        passwordUnmatch.value = true
    } else {
        passwordUnmatch.value = false
    }
}
const validateEmail = () => {
    const validRegex = /^(([^'<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    if (email.value.match(validRegex) && email.value.length > 0) {
        isNotEmail.value = false
    } else if (email.value.length == 0 || email.value == ' ') {
        isNotEmail.value = true
        return false
    } else {
        isNotEmail.value = true
        return true
    }
}

const checkUsedName = (checkedName) => {
    usedName.value = false
    // console.log('check name');
    user.value.filter((curr) => {
        // console.log(`currentName: ${curr.name} == ${checkedName}`);
        if (curr.name == checkedName.trim()) {
            // console.log('ตรงกัน');
            usedName.value = true
            return
        }
        // else {
        //     // usedName.value = false
        //     return
        // }
    })
}
const checkUsedEmail = (checkedEmail) => {
    usedEmail.value = false
    user.value.filter((curr) => {
        if (curr.email === checkedEmail.trim()) {
            usedEmail.value = true
            return
        }
        // else {
        //     return false
        // }
    })
}

const creatUser = async () => {
    // if (name.value.length == 100 || email.value.length == 50) {
    //     if (confirm("!!! Warning check your name before submit. If the name or email characters exceed the limit, the information will cut off ")) {

    //     }
    // }
    // console.log(`Trim:` + name.value.trim());
    // console.log('Name: ' + name.value, 'Email: ' + email.value, 'Role:' + selectedRole.value,);
    if (name.value == '' || email.value == '' || password.value == '' || rePassword.value == '') {
        alertText.value = ''
        falseInput.value = true
        if (name.value == '') {
            if (email.value != '') {
                alertText.value += ("name, ")
            } else {
                alertText.value += ("name")
            }
            // alertText.value += ("name")

        }
        if (email.value == '') {
            if (name.value != '') {
                alertText.value += ("email")
            } else {
                alertText.value += (", email")
            }
        }

        if (password.value == '') {
            if (email.value != '') {
                alertText.value += ("password")
            } else {
                alertText.value += (", password")

            }

        }
        if (rePassword.value == '') {
            if (name.value != '' && email.value != '' && password.value != '') {
                alertText.value += ("re-password")
            } else {
                alertText.value += (", re-password")
            }
        }
        return
    }
    if (isNotEmail.value == false) {
        falseInput.value = false
        if (passwordUnmatch.value == true) {
            alert("Password are does not match, please edit your re-password.")
            return
        }
        if (password.value.length > 14 || password.value.length < 8) {
            alert("Password can be 8-14 characters, please edit your password.")
            return
        }
        if (usedName.value == true && usedEmail.value == true) {
            alert("This name and email already used.")
            return
        } else if (usedName.value == true) {
            alert("This name already used.")
            return
        } else if (usedEmail.value == true) {
            alert("This email already used.")
            return
        }

        if (selectedRole.value.toLocaleLowerCase() == 'guest') {
            alert('Guest is invalid.')
            return
        }
        // if (confirm(`Are you sure to the create user ?`)) {
        // const utc = new Date(startTime.value).toISOString()
        // startTime.value = utc
        if (selectedRole.value == "Select your role") {
            selectedRole.value = "Student"
        }
        // console.log('Name: ' + name.value.trim(), 'Email: ' + email.value, 'Role:' + selectedRole.value,);
        // success.value = true
        createHeader()
        const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users`, {
            method: 'POST',
            headers: myHeader.value,
            body: JSON.stringify({
                name: name.value.trim(),
                email: email.value.trim(),
                password: password.value,
                role: selectedRole.value.toLocaleLowerCase()
            })
        })
        // console.log(res);
        if (res.status === 200) {
            // setTimeout(() => appRouter.push({ name: 'MatchPass', params: { email: email.value.trim(), password: password.value } }), 1000)
            setTimeout(() => appRouter.push({ name: 'UserList' }), 1000)
            // name.value = ''
            // email.value = ''
            selectedRole.value = 'Student'
            success.value = true
            // appRouter.push({ name: 'Home' })
        }
        else if (res.status === 400) {
            errText.value = await res.json()
            console.log(errText.value);
            alert(errText.value.message)
            // alert("This name and email are already used.")
            selectedRole.value == "Select your role"
        }
    } else {
        if (selectedRole.value.toLocaleLowerCase() == 'guest') {
            alert('Guest is invalid.')
            return
        }
        alert("Invalid email address!");
        return
    }
}

onBeforeMount(async () => {
    // await getUser()
    // console.log(user.value);
})
</script>
 
<template>
    <section class="text-gray-600 body-font relative">
        <div class="container px-5 py-14 mx-auto">
            <div class="flex flex-col text-center w-full mb-12">
                <h1 class="sm:text-3xl text-2xl font-medium title-font mb-4 text-gray-900">Create user</h1>
                <p class="lg:w-2/3 mx-auto leading-relaxed text-base">Please fill out the data below</p>
            </div>
            <div class="lg:w-1/2 md:w-2/3 mx-auto">
                <div class="flex flex-wrap -m-2">
                    <div class="p-2 w-1/2">
                        <div class="relative">
                            <label for="email" class="leading-7 text-sm text-gray-600">Name<span
                                    class="text-red-500 p-2">*</span></label>
                            <span class="text-sm text-yellow-500 pb-2" v-show="name.length == 100">** A name must be
                                1
                                - 100 characters. **</span>
                            <input type="text" placeholder="Type yourname..."
                                class="w-full placeholder:opacity-50 bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out"
                                v-model="name" id="name" maxlength="100" />
                            <p class="text-sm opacity-50 text-right">{{ name.length }}/100</p>
                        </div>
                    </div>
                    <div class="p-2 w-1/2">
                        <div class="relative">
                            <label for="email" class="leading-7 text-sm text-gray-600">Email<span
                                    class="text-red-500 p-2">*</span></label>
                            <span class="text-sm text-red-500 pb-2" v-show="validateEmail()">Invalid email
                                address.</span>
                            <span class="text-sm text-yellow-500 pb-2" v-show="email.length == 50">** An email must
                                be 1 - 50 characters. **</span>
                            <input type="email" placeholder="example@mail.kmutt.ac.th"
                                class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out"
                                v-model="email" id="email" maxlength="50" />
                            <p class="text-sm opacity-50 text-right">{{ email.length }}/50</p>
                        </div>
                    </div>
                    <div class="p-2 w-1/2">
                        <div class="relative">
                            <label for="email" class="leading-7 text-sm text-gray-600">Password<span
                                    class="text-red-500 p-2">*</span></label>
                            <span class="text-sm text-yellow-500 pb-2"
                                v-show="0 < password.length && password.length < 8">** Password can be 8-14 characters
                                **</span>
                            <span class="text-sm text-red-500 pb-2" v-show="password.length > 14">** Password can be
                                8-14 characters
                                **</span>
                            <input type="password" placeholder="••••••••"
                                class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out"
                                v-model="password" id="password"/>
                            <p class="text-sm opacity-50 text-right">{{ password.length }} Characters.</p>
                        </div>
                    </div>
                    <div class="p-2 w-1/2">
                        <div class="relative">
                            <label for="email" class="leading-7 text-sm text-gray-600">Re-Password<span
                                    class="text-red-500 p-2">*</span></label>
                                    <span class="text-sm text-red-500 pb-2" v-show="passwordUnmatch">Password are does not
                                match.</span>
                            <input type="password" placeholder="••••••••" @change="passwordCheckMatch(rePassword)" 
                                class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out"
                                v-model="rePassword" id="rePassword"/>
                            <p class="text-sm opacity-50 text-right">{{ rePassword.length }} Characters.</p>
                        </div>
                    </div>
                    <div class="p-2 w-full">
                        <div class="relative">
                            <label for="role" class="label">
                                <span class="label-text text-base font-semibold">
                                    Role:
                                </span>
                            </label>
                            <select class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out" id="role"
                                v-model="selectedRole">
                                <option disabled selected>Select your role</option>
                                <option v-for="(role, index) in roles" :key="index">
                                    {{ role }}</option>
                            </select>
                        </div>
                    </div>

                    <div class="alert alert-error shadow-lg w-screen h-12 py-3 text-[16px] text-white self-center"
                        v-show="falseInput">
                        <div class=" flex p-4 mb-4 text-sm text-red-700 bg-red-100 rounded-lg dark:bg-red-200 dark:text-red-800"
                            role="alert">
                            <svg aria-hidden="true" class="flex-shrink-0 inline w-5 h-5 mr-3" fill="currentColor"
                                viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                                <path fill-rule="evenodd"
                                    d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z"
                                    clip-rule="evenodd"></path>
                            </svg>
                            <span class="sr-only">Info</span>
                            <div>
                                lease fill {{ alertText }} field.
                            </div>
                        </div>

                    </div>

                    <div class="alert alert-success shadow-lg w-screen h-12 text-[16px] text-white self-center"
                        v-show="success">
                        <div class="flex p-4 mb-4 text-sm text-green-700 bg-green-100 rounded-lg dark:bg-green-200 dark:text-green-800"
                            role="alert">
                            <svg aria-hidden="true" class="flex-shrink-0 inline w-5 h-5 mr-3" fill="currentColor"
                                viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                                <path fill-rule="evenodd"
                                    d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z"
                                    clip-rule="evenodd"></path>
                            </svg>
                            <span class="sr-only">Info</span>
                            <div>
                                Create user success.
                            </div>
                        </div>

                    </div>
                    <div class="p-8 w-full">
                        <button @click="creatUser"
                            class="flex mx-auto text-white bg-indigo-500 border-0 py-2 px-8 focus:outline-none hover:bg-indigo-600 rounded-lg text-lg">SUBMIT</button>
                    </div>

                    <div class="p-2 w-full pt-8 mt-8 border-t border-gray-200 text-center">
                        <a class="text-indigo-500">napasuay@email.com</a>
                        <p class="leading-normal my-5">Napasorn, Jirasin, Naruebet
                            <br>School of Information Technology, KMUTT
                        </p>
                        <span class="inline-flex">
                            <a class="text-gray-500" href="https://www.instagram.com/_bamnapasorn_/" target="_blank">
                                <svg fill="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    class="w-5 h-5" viewBox="0 0 24 24">
                                    <path d="M18 2h-3a5 5 0 00-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 011-1h3z"></path>
                                </svg>
                            </a>
                            <a class="ml-4 text-gray-500" href="https://www.instagram.com/_bamnapasorn_/"
                                target="_blank">
                                <svg fill="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    class="w-5 h-5" viewBox="0 0 24 24">
                                    <path
                                        d="M23 3a10.9 10.9 0 01-3.14 1.53 4.48 4.48 0 00-7.86 3v1A10.66 10.66 0 013 4s-4 9 5 13a11.64 11.64 0 01-7 2c9 5 20 0 20-11.5a4.5 4.5 0 00-.08-.83A7.72 7.72 0 0023 3z">
                                    </path>
                                </svg>
                            </a>
                            <a class="ml-4 text-gray-500" href="https://www.instagram.com/_bamnapasorn_/"
                                target="_blank">
                                <svg fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                    stroke-width="2" class="w-5 h-5" viewBox="0 0 24 24">
                                    <rect width="20" height="20" x="2" y="2" rx="5" ry="5"></rect>
                                    <path d="M16 11.37A4 4 0 1112.63 8 4 4 0 0116 11.37zm1.5-4.87h.01"></path>
                                </svg>
                            </a>
                            <a class="ml-4 text-gray-500" href="https://www.instagram.com/_bamnapasorn_/"
                                target="_blank">
                                <svg fill="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    class="w-5 h-5" viewBox="0 0 24 24">
                                    <path
                                        d="M21 11.5a8.38 8.38 0 01-.9 3.8 8.5 8.5 0 01-7.6 4.7 8.38 8.38 0 01-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 01-.9-3.8 8.5 8.5 0 014.7-7.6 8.38 8.38 0 013.8-.9h.5a8.48 8.48 0 018 8v.5z">
                                    </path>
                                </svg>
                            </a>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </section>

</template>
 
<style>

</style>