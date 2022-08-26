<script setup>
import { onBeforeMount, ref } from 'vue';
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

const getUser = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users/`)
    user.value = await res.json()
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

// const checkUsedUserName = (signUpUserName) => {
//     // console.log(signUpUserName);
//     accounts.value.filter((current) => {
//         if (current.userName === signUpUserName) {
//             userUsed.value = true
//         }
//     }
//     );

// }
const creatUser = async () => {
    // if (name.value.length == 100 || email.value.length == 50) {
    //     if (confirm("!!! Warning check your name before submit. If the name or email characters exceed the limit, the information will cut off ")) {

    //     }
    // }
    // console.log(`Trim:` + name.value.trim());
    // console.log('Name: ' + name.value, 'Email: ' + email.value, 'Role:' + selectedRole.value,);
    if (name.value == '' || email.value == '') {
        alertText.value = ''
        falseInput.value = true
        if (name.value == '') {
            if (email.value != '') {
                alertText.value += ("name")
            } else {
                alertText.value += ("name, ")
            }
        }
        if (email.value == '') {
            if (name.value != '') {
                alertText.value += ("email")
            } else {
                alertText.value += ("email")
            }
        }
        return
    }
    if (isNotEmail.value == false) {
        falseInput.value = false
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

        const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users/`, {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            },
            body: JSON.stringify({
                name: name.value.trim(),
                email: email.value.trim(),
                role: selectedRole.value.toLocaleLowerCase()
            })
        })
        // console.log(res);
        if (res.status === 200) {
            name.value = ''
            email.value = ''
            selectedRole.value = 'Student'
            success.value = true
            setTimeout(() => appRouter.push({ name: 'UserList' }), 1000)
            // appRouter.push({ name: 'Home' })
        }
        else if (res.status === 400) {
            alert("This name and email are already used.")
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
    await getUser()
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
                                class="input input-bordered input-secondary w-full max-w-xs text-lg text-gray-50"
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