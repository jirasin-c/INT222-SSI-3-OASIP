<script setup>
import { async } from "postcss-js";
import { onBeforeMount, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
let { params } = useRoute()
// console.log(params.email);
// console.log(params.password);


const email = ref("");
const password = ref("");
const isMatch = ref(false);
const isExist = ref(false);
const isChecked = ref(false);
const isShowPassword = ref(false);
const falseInput = ref(false);
const alertText = ref("");
const appRouter = useRouter();
const isNotSignedIn = ref(true);
const jwtToken = ref()

const checkMatch = async () => {
  isChecked.value = false;
  isMatch.value = false;
  isExist.value = false;
  falseInput.value = false;

  if (email.value == "" || password.value == "") {
    alertText.value = "";
    falseInput.value = true;
    if (email.value == "") {
      if (password.value != "") {
        alertText.value += "email";
      } else {
        alertText.value += "email, ";
      }
    }

    if (password.value == "") {
      if (email.value != "") {
        alertText.value += "password";
      } else {
        alertText.value += "password";
      }
    }
    return;
  }

  const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/login/`, {
    method: "POST",
    headers: {
      "content-type": "application/json",
    },
    body: JSON.stringify({
      email: email.value.trim(),
      password: password.value,
    }),
  });
  // const res = {
  //     status: 200
  // }
  if (res.status === 200) {
    isChecked.value = true;
    isMatch.value = true;
    isNotSignedIn.value = false;
    jwtToken.value = await res.json()
    // console.log(detail.value);
    localStorage.setItem('token', jwtToken.value.token)
    setTimeout(() => appRouter.push({ name: "Home" }), 1500);
    // isExist.value = true
  } else if (res.status === 401) {
    isChecked.value = true;
    isMatch.value = false;
    isExist.value = true;
    // detail.value = await res.json()
    // console.log(detail.value.message)
  } else if (res.status === 404) {
    isChecked.value = true;
    isMatch.value = false;
    isExist.value = false;
    // detail.value = await res.text()
    // console.log(detail.value);
  }

  // console.log(email.value);
  // console.log(password.value);
};

onBeforeMount(async () => {
  // if (params.email !== undefined && params.password !== undefined) {
  //   const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/login/`, {
  //     method: "POST",
  //     headers: {
  //       "content-type": "application/json",
  //     },
  //     body: JSON.stringify({
  //       email: params.email,
  //       password: params.password,
  //     }),
  //   });
  //   isNotSignedIn.value = false;
  //   jwtToken.value = await res.json()
  //   localStorage.setItem('token', jwtToken.value.token)
  //   setTimeout(() => appRouter.push({ name: "Home" }), 1500);
  // }
})
</script>

<template>
  <!-- <p>match pass</p> -->
  <div class="w-full h-full bg-gray-500/20 p-10 flex justify-center mt-10">
    <div class="card w-96 bg-base-100 shadow-xl">
      <!-- <figure class="px-10 pt-10">
                <img src="https://placeimg.com/400/225/arch" alt="Shoes" class="rounded-xl" />
            </figure> -->
      <div class="card-body items-center text-center">
        <h2 class="card-title text-2xl">Sign In</h2>
        <!-- <p>If a dog chews shoes whose shoes does he choose?</p> -->
        <div class="form-control w-full max-w-xs">
          <label for="email" class="label">
            <span class="label-text text-base font-semibold">
              Email : <span class="text-red-500">*</span>
            </span>
          </label>
          <!-- <span class="text-sm text-red-500 pb-2" v-show="usedEmail">This email is already
                                used.</span>
                            <span class="text-sm text-red-500 pb-2" v-show="validateEmail()">Invalid email
                                address.</span>
                            <span class="text-sm text-yellow-500 pb-2" v-show="email.length == 50">** An email must be
                                1
                                - 50 characters. **<br>
                                51st characters onwards will be cut off</span> -->
          <input type="email" placeholder="example@mail.kmutt.ac.th"
            class="input input-bordered input-secondary w-full max-w-xs text-lg mb-5" v-model="email" id="email" />
          <!-- <label class="label">
                        <span class="label-text-alt"></span>
                        <span class="label-text-alt">{{ email.length }}/50</span>
                    </label> -->

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
                </svg>
                Hide
              </div>
              <div class="swap-off text-sm">
                <svg width="24" height="24" viewBox="0 0 512 512" class="inline">
                  <circle cx="256" cy="256" r="64" fill="currentColor" />
                  <path fill="currentColor"
                    d="M490.84 238.6c-26.46-40.92-60.79-75.68-99.27-100.53C349 110.55 302 96 255.66 96c-42.52 0-84.33 12.15-124.27 36.11c-40.73 24.43-77.63 60.12-109.68 106.07a31.92 31.92 0 0 0-.64 35.54c26.41 41.33 60.4 76.14 98.28 100.65C162 402 207.9 416 255.66 416c46.71 0 93.81-14.43 136.2-41.72c38.46-24.77 72.72-59.66 99.08-100.92a32.2 32.2 0 0 0-.1-34.76ZM256 352a96 96 0 1 1 96-96a96.11 96.11 0 0 1-96 96Z" />
                </svg>
                Show
              </div>
            </label>
          </label>
          <input v-if="!isShowPassword" type="password" placeholder="••••••••" v-model="password"
            class="input input-bordered input-secondary w-full max-w-xs text-2xl mb-7" id="password" />
          <input v-else type="text" v-model="password"
            class="input input-bordered input-secondary w-full max-w-xs text-lg mb-7" id="password" />
          <!-- <label class="label">
                        <span class="label-text-alt"></span>
                        <span class="label-text-alt">{{ password.length }}/14</span>
                        <span class="label-text-alt">{{ password.length }} Characters.</span>
                     </label> -->
          <div v-show="isChecked" class="mb-3">
            <div class="alert alert-error shadow-lg w-auto h-12 text-[16px] text-white self-center" v-if="!isMatch">
              <div v-if="isExist">
                <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6" fill="none"
                  viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <span class="ml-3">Password incorrect!</span>
              </div>
              <div v-else>
                <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6" fill="none"
                  viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <span class="ml-9">A user with specified email does not existed.</span>
              </div>
            </div>
            <div class="alert alert-success shadow-lg w-auto h-12 text-[16px] text-gray-50 self-center" v-else>
              <div>
                <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6" fill="none"
                  viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <p class="ml-12">Login successful!</p>
              </div>
            </div>
          </div>

          <div class="alert alert-error shadow-lg w-auto h-12 text-[16px] text-white self-center mb-5"
            v-show="falseInput">
            <div>
              <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6" fill="none"
                viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <span>Please fill {{ alertText }} field.</span>
            </div>
          </div>
          
          <div class="card-actions justify-center">
            <button class="btn btn-primary" v-show="isNotSignedIn == true" @click="checkMatch">
              SIGN IN
            </button>
            <button v-show="isNotSignedIn == false" type="button"
              class="flex items-center rounded-lg bg-green-700 px-4 py-2 text-white" disabled>
              <svg class="mr-3 h-5 w-5 animate-spin text-white" xmlns="http://www.w3.org/2000/svg" fill="none"
                viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor"
                  d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                </path>
              </svg>
              <span class="font-medium"> Redirecting... </span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style>

</style>
