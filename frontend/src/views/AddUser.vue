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

// onUpdated(() => {
//     console.log(isShowPassword.value);
// })
// const getUser = async () => {
//     const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users/`)
//     user.value = await res.json()
// }

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

        const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users`, {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            },
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
            setTimeout(() => appRouter.push({ name: 'UserList'}), 1000)
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
    <div class="hero min-h-screen bg-base-200">
        <div class="hero-content text-left ">
            <div
                class="card w-auto lg:w-[1000px] bg-gradient-to-r from-base-100 to-base-200 mb-16 shadow-xl backdrop-blur-sm">
                <div class="card-body text-3xl place-self-center pb-1">
                    <p
                        class="text-5xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-purple-300 to-pink-600 pb-1 text-center">
                        Create new user
                    </p>
                    <div class="divider"></div>
                    <div class="flex justify-center pb-2">
                        <div class="form-control w-full max-w-xs">
                            <label for="name" class="label">
                                <span class="label-text text-base font-semibold">
                                    Name: <span class="text-red-500">*</span>
                                </span>
                            </label>
                            <span class="text-sm text-red-500 pb-2" v-show="usedName">This name is already
                                used.</span>
                            <span class="text-sm text-yellow-500 pb-2" v-show="name.length == 100">** A name must be 1 -
                                100 characters. ** <br>
                                101st characters onwards will be cut off</span>
                            <input type="text" v-model="name" placeholder="Type yourname..."
                                class="input input-bordered input-secondary w-full max-w-xs text-lg"
                                id="name" maxlength="100" @change="checkUsedName(name)" />
                            <label class="label">
                                <span class="label-text-alt"></span>
                                <span class="label-text-alt">{{ name.length }}/100</span>
                            </label>

                            <label for="email" class="label">
                                <span class="label-text text-base font-semibold">
                                    Email : <span class="text-red-500">*</span>
                                </span>
                            </label>
                            <span class="text-sm text-red-500 pb-2" v-show="usedEmail">This email is already
                                used.</span>
                            <span class="text-sm text-red-500 pb-2" v-show="validateEmail()">Invalid email
                                address.</span>
                            <span class="text-sm text-yellow-500 pb-2" v-show="email.length == 50">** An email must be
                                1
                                - 50 characters. **<br>
                                51st characters onwards will be cut off</span>
                            <input type="email" placeholder="example@mail.kmutt.ac.th"
                                class="input input-bordered input-secondary w-full max-w-xs  text-lg" v-model="email"
                                id="email" maxlength="50" @change="checkUsedEmail(email)" />
                            <label class="label">
                                <span class="label-text-alt"></span>
                                <span class="label-text-alt">{{ email.length }}/50</span>
                            </label>

                            <label for="password" class="label">
                                <span class="label-text text-base font-semibold">
                                    Password : <span class="text-red-500">*</span>
                                </span>
                                <label class="swap">
                                    <input type="checkbox" v-model="isShowPassword" />
                                    <div class="swap-on text-sm">
                                        <svg width="24" height="24" viewBox="0 0 512 512" class="inline">
                                            <path fill="currentColor"
                                                d="m63.998 86.005l21.998-21.998L447.999 426.01l-21.998 21.998zM259.34 192.09l60.57 60.57a64.07 64.07 0 0 0-60.57-60.57Zm-6.68 127.82l-60.57-60.57a64.07 64.07 0 0 0 60.57 60.57Z" />
                                            <path fill="currentColor"
                                                d="M256 352a96 96 0 0 1-92.6-121.34l-69.07-69.08C66.12 187.42 39.24 221.14 16 256c26.42 44 62.56 89.24 100.2 115.18C159.38 400.92 206.33 416 255.76 416A233.47 233.47 0 0 0 335 402.2l-53.61-53.6A95.84 95.84 0 0 1 256 352Zm0-192a96 96 0 0 1 92.6 121.34L419.26 352c29.15-26.25 56.07-61.56 76.74-96c-26.38-43.43-62.9-88.56-101.18-114.82C351.1 111.2 304.31 96 255.76 96a222.92 222.92 0 0 0-78.21 14.29l53.11 53.11A95.84 95.84 0 0 1 256 160Z" />
                                        </svg> Hide
                                    </div>
                                    <div class="swap-off text-sm"><svg width="24" height="24" viewBox="0 0 512 512"
                                            class="inline">
                                            <circle cx="256" cy="256" r="64" fill="currentColor" />
                                            <path fill="currentColor"
                                                d="M490.84 238.6c-26.46-40.92-60.79-75.68-99.27-100.53C349 110.55 302 96 255.66 96c-42.52 0-84.33 12.15-124.27 36.11c-40.73 24.43-77.63 60.12-109.68 106.07a31.92 31.92 0 0 0-.64 35.54c26.41 41.33 60.4 76.14 98.28 100.65C162 402 207.9 416 255.66 416c46.71 0 93.81-14.43 136.2-41.72c38.46-24.77 72.72-59.66 99.08-100.92a32.2 32.2 0 0 0-.1-34.76ZM256 352a96 96 0 1 1 96-96a96.11 96.11 0 0 1-96 96Z" />
                                        </svg> Show</div>
                                </label>
                            </label>
                            <!-- <span class="text-sm text-red-500 pb-2" v-show="usedEmail">This email is already
                                used.</span>
                            <span class="text-sm text-red-500 pb-2" v-show="validateEmail()">Invalid email
                                address.</span> -->
                            <span class="text-sm text-yellow-500 pb-2"
                                v-show="0 < password.length && password.length < 8">** Password can be 8-14 characters
                                **</span>
                            <span class="text-sm text-red-500 pb-2" v-show="password.length > 14">** Password can be
                                8-14 characters
                                **</span>
                            <!-- <span class="text-sm text-red-500 pb-2" v-show="password.length == 14">** Exceed password
                                limit
                                ** <br>
                                15th characters onwards will be cut off</span> -->
                            <input v-if="!isShowPassword" type="password" placeholder="••••••••" v-model="password"
                                class="input input-bordered input-secondary w-full max-w-xs  text-2xl" id="password" />
                            <input v-else type="text" v-model="password"
                                class="input input-bordered input-secondary w-full max-w-xs  text-base" id="password" />
                            <label class="label">
                                <span class="label-text-alt"></span>
                                <!-- <span class="label-text-alt">{{ password.length }}/14</span> -->
                                <span class="label-text-alt">{{ password.length }} Characters.</span>
                            </label>

                            <label for="re-password" class="label">
                                <span class="label-text text-base font-semibold">
                                    Re-Password : <span class="text-red-500">*</span>
                                </span>
                                <label class="swap">
                                    <input type="checkbox" v-model="isShowRePassword" />
                                    <div class="swap-on text-sm">
                                        <svg width="24" height="24" viewBox="0 0 512 512" class="inline">
                                            <path fill="currentColor"
                                                d="m63.998 86.005l21.998-21.998L447.999 426.01l-21.998 21.998zM259.34 192.09l60.57 60.57a64.07 64.07 0 0 0-60.57-60.57Zm-6.68 127.82l-60.57-60.57a64.07 64.07 0 0 0 60.57 60.57Z" />
                                            <path fill="currentColor"
                                                d="M256 352a96 96 0 0 1-92.6-121.34l-69.07-69.08C66.12 187.42 39.24 221.14 16 256c26.42 44 62.56 89.24 100.2 115.18C159.38 400.92 206.33 416 255.76 416A233.47 233.47 0 0 0 335 402.2l-53.61-53.6A95.84 95.84 0 0 1 256 352Zm0-192a96 96 0 0 1 92.6 121.34L419.26 352c29.15-26.25 56.07-61.56 76.74-96c-26.38-43.43-62.9-88.56-101.18-114.82C351.1 111.2 304.31 96 255.76 96a222.92 222.92 0 0 0-78.21 14.29l53.11 53.11A95.84 95.84 0 0 1 256 160Z" />
                                        </svg> Hide
                                    </div>
                                    <div class="swap-off text-sm"><svg width="24" height="24" viewBox="0 0 512 512"
                                            class="inline">
                                            <circle cx="256" cy="256" r="64" fill="currentColor" />
                                            <path fill="currentColor"
                                                d="M490.84 238.6c-26.46-40.92-60.79-75.68-99.27-100.53C349 110.55 302 96 255.66 96c-42.52 0-84.33 12.15-124.27 36.11c-40.73 24.43-77.63 60.12-109.68 106.07a31.92 31.92 0 0 0-.64 35.54c26.41 41.33 60.4 76.14 98.28 100.65C162 402 207.9 416 255.66 416c46.71 0 93.81-14.43 136.2-41.72c38.46-24.77 72.72-59.66 99.08-100.92a32.2 32.2 0 0 0-.1-34.76ZM256 352a96 96 0 1 1 96-96a96.11 96.11 0 0 1-96 96Z" />
                                        </svg> Show</div>
                                </label>
                            </label>
                            <span class="text-sm text-red-500 pb-2" v-show="passwordUnmatch">Password are does not
                                match.</span>
                            <input v-if="!isShowRePassword" type="password" placeholder="••••••••" v-model="rePassword"
                                class="input input-bordered input-secondary w-full max-w-xs  text-2xl" id="re-password"
                                @change="passwordCheckMatch(rePassword)" />
                            <input v-else type="text" v-model="rePassword"
                                class="input input-bordered input-secondary w-full max-w-xs  text-base" id="re-password"
                                @change="passwordCheckMatch(rePassword)" />
                            <label class="label">
                                <span class="label-text-alt"></span>
                                <span class="label-text-alt">{{ rePassword.length }} Characters.</span>
                            </label>

                            <label for="role" class="label">
                                <span class="label-text text-base font-semibold">
                                    Role:
                                </span>
                            </label>
                            <select class="select select-secondary w-full max-w-xs  text-lg" id="role"
                                v-model="selectedRole">
                                <option disabled selected>Select your role</option>
                                <option v-for="(role, index) in roles" :key="index">
                                    {{ role }}</option>
                            </select>
                        </div>
                    </div>
                    <div class="alert alert-error shadow-lg w-auto h-12 text-[16px] text-white self-center"
                        v-show="falseInput">
                        <div>
                            <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6"
                                fill="none" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                            <span>Please fill {{ alertText }} field.</span>
                        </div>
                    </div>
                    <div class="alert alert-success shadow-lg w-auto h-12 text-[16px] text-white self-center"
                        v-show="success">
                        <div>
                            <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6"
                                fill="none" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                            <span>Create user success.</span>
                        </div>
                    </div>
                    <div class="card-actions justify-center">
                        <button @click="creatUser"
                            class="btn btn-secondary border-none bg-gradient-to-r from-yellow-500 to-orange-600 hover:from-pink-500 hover:to-yellow-500 mb-4 text-lg">Create
                            User</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>
 
<style>

</style>