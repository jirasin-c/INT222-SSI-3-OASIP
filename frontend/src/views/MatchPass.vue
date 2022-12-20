<script setup>
import { onBeforeMount, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
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
const myHeader = ref()


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
  createHeader()
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/match`, {
    method: "POST",
    headers: myHeader.value,
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
    // isNotSignedIn.value = false;
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
</script>
    
<template> 
<section class="bg-gray-50 dark:bg-gray-900">
  <div class="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
    <div
      class="w-full bg-white rounded-lg shadow dark:border md:mt-10 sm:max-w-md xl:p-0 dark:bg-gray-800 dark:border-gray-700">
      <div class="p-6 space-y-4 md:space-y-6 sm:p-8">
        <h1 class="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
          Match Password
        </h1>
        <form class="space-y-4 md:space-y-6" @submit.prevent="checkMatch">
          <div>
            <label for="email" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Your email</label>
            <input type="email" name="email" id="email" v-model="email" @keypress.enter="checkMatch"
              class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
              placeholder="example@mail.kmutt.ac.th" required="">
          </div>
          <div>
            <label for="password"
              class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Password</label>
            <input type="password" name="password" id="password" placeholder="••••••••" v-model="password"
              class="bg-gray-50 border border-gray-300 text-gray-900 text-xl rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
              required="">
          </div>
          <button type="submit"  @click="checkMatch"
            class="w-full text-white bg-blue-500 hover:bg-blue-600 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800">
            Check</button>
            

          <div v-show="isChecked" class="mb-3">
            <div class="alert alert-error shadow-lg w-auto h-12 text-[16px] text-white self-center" v-if="!isMatch">
              <!-- <div v-if="isExist">
                <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6" fill="none"
                  viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <span class="ml-10">Password incorrect!</span>
              </div> -->
              <div v-if="isExist"
                class="flex p-4 mb-4 text-sm text-red-700 bg-red-100 rounded-lg dark:bg-red-200 dark:text-red-800"
                role="alert">
                <svg aria-hidden="true" class="flex-shrink-0 inline w-5 h-5 mr-3" fill="currentColor"
                  viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                  <path fill-rule="evenodd"
                    d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z"
                    clip-rule="evenodd"></path>
                </svg>
                <span class="sr-only">Info</span>
                <div>
                  Password not match.
                </div>
              </div>
              <div v-else
                class="flex p-4 mb-4 text-sm text-red-700 bg-red-100 rounded-lg dark:bg-red-200 dark:text-red-800"
                role="alert">
                <svg aria-hidden="true" class="flex-shrink-0 inline w-5 h-5 mr-3" fill="currentColor"
                  viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                  <path fill-rule="evenodd"
                    d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z"
                    clip-rule="evenodd"></path>
                </svg>
                <span class="sr-only">Info</span>
                <div>
                  A user with specified email does not existed.
                </div>
              </div>
            </div>

            <div v-else
              class="flex p-4 mb-4 text-sm text-green-700 bg-green-100 rounded-lg dark:bg-green-200 dark:text-green-800"
              role="alert">
              <svg aria-hidden="true" class="flex-shrink-0 inline w-5 h-5 mr-3" fill="currentColor"
                viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                <path fill-rule="evenodd"
                  d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z"
                  clip-rule="evenodd"></path>
              </svg>
              <span class="sr-only">Info</span>
              <div>
                Password Match.
              </div>
            </div>

          </div>
        </form>
      </div>
    </div>
  </div>
</section>

</template>
    
<style>

</style>
    