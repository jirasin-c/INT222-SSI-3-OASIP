<script setup>
import { onBeforeMount, reactive, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useUser } from '../stores/user';

const myUser = useUser()
let { params } = useRoute()
const appRouter = useRouter()
const name = params.name
const detail = ref([])
const localCreatedOn = ref()
const localUpdatedOn = ref()
const isEdit = ref(false)
const editName = ref('')
const editEmail = ref('')
const isNotEmail = ref(false)
const beforeEditName = ref('')
const beforeEditEmail = ref('')
const roles = ref(['Student', 'Lecturer', 'Admin', 'Guest'])
const editRole = ref('')
const beforeEditRole = ref('')
const falseInput = ref(false)
const alertText = ref('')
const user = ref([])
const success = ref(false)
const usedName = ref(false)
const usedEmail = ref(false)
const myHeader = ref()

const getUser = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users/`, {
    method: "GET",
    // headers: {
    // "content-type": "application/json",
    // "Authorization": `Bearer ${localStorage.getItem('token')}`,
    // },
    headers: myHeader.value
  })
  user.value = await res.json()
}

const checkUsedName = (checkedName) => {
  usedName.value = false
  if (beforeEditName.value == checkedName) {
    return
  }
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
  if (beforeEditEmail.value == checkedEmail) {
    return
  }
  user.value.filter((curr) => {
    if (curr.email === checkedEmail.trim()) {
      // console.log(`currentEmail: ${curr.email} == ${checkedEmail}`);
      usedEmail.value = true
      return
    }
    // else {
    //     return false
    // }
  })
}

const getDetail = async (newUser) => {
  // if (success.value == true) {
  // const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users/${newUser}`)
  // params.name = newUser
  // detail.value = await res.json()
  // } else {
  createHeader()
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users/${name}`, {
    method: "GET",
    // headers: {
    // "content-type": "application/json",
    // "Authorization": `Bearer ${localStorage.getItem('token')}`,
    // },
    headers: myHeader.value
  })
  detail.value = await res.json()
  // }
  // detail.value = await res.json()
  detail.value.role = capitalizeFirstLetter(detail.value.role)
  // console.log(detail.value.role);
  // capitalizeFirstLetter(detail.value.role)
  // localCreatedOn.value = new Date(detail.value.createdOn).to
  // localCreatedOn.value = new Date(detail.value.createdOn)
  // localCreatedOn.value = new Date(detail.value.updatedOn)

  localCreatedOn.value = new Date(detail.value.createdOn).toLocaleTimeString("th-TH", {
    weekday: "short",
    month: "short",
    day: "numeric",
    year: "2-digit",
    hour: "numeric",
    minute: "numeric",
    // timeZoneName: "short"
  }
  )
  localUpdatedOn.value = new Date(detail.value.updatedOn).toLocaleTimeString("th-TH", {
    weekday: "short",
    month: "short",
    day: "numeric",
    year: "2-digit",
    hour: "numeric",
    minute: "numeric",
    // timeZoneName: "short"
  }
  )
  editName.value = detail.value.name
  editEmail.value = detail.value.email
  editRole.value = detail.value.role
  beforeEditName.value = editName.value
  beforeEditEmail.value = editEmail.value
  beforeEditRole.value = editRole.value
  // console.log(localCreatedOn.value);
  // console.log(localUpdatedOn.value);

  //   event.value.filter((e) => {
  // const localDate = new Date(e.eventStartTime).toLocaleString("th-TH", {
  //   weekday: "short",
  //   month: "short",
  //   day: "numeric",
  //   year: "2-digit",
  //   hour: "numeric",
  //   minute: "numeric",
  // });
  //     e.eventStartTime = localDate;
  //   });
}


const capitalizeFirstLetter = (string) => {
  return string.charAt(0).toUpperCase() + string.slice(1)
}

const validateEmail = () => {
  const validRegex = /^(([^'<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  if (editEmail.value.match(validRegex) && editEmail.value.length > 0) {
    isNotEmail.value = false
  } else if (editEmail.value.length == 0 || editEmail.value == ' ') {
    isNotEmail.value = true
    return false
  } else {
    isNotEmail.value = true
    return true
  }
}

const updateUser = async () => {
  // console.log('Name: ' + editName.value, 'Email: ' + editEmail.value, 'Role: ' + editRole.value);
  // console.log(editName.value);
  // console.log(detail.value.name);

  if (editName.value.trim() === detail.value.name && editEmail.value.trim() === detail.value.email && editRole.value === detail.value.role) {
    // console.log('same');
    isEdit.value = false
    return
  } else {

    // console.log('not the same');
    if (editName.value == '' || editEmail.value == '') {
      alertText.value = ''
      falseInput.value = true
      if (editName.value == '') {
        if (editEmail.value != '') {
          alertText.value += ("name")
        } else {
          alertText.value += ("name, ")
        }
      }
      if (editEmail.value == '') {
        if (editName.value != '') {
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

      if (editRole.value.toLocaleLowerCase() == 'guest') {
        alert('Guest is invalid.')
        return
      }

      const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/users/${name}`, {
        method: 'PUT',
        headers: myHeader.value,
        // body: JSON.stringify(customEdit)
        body: JSON.stringify({
          name: editName.value.trim(),
          email: editEmail.value.trim(),
          role: editRole.value.toLocaleLowerCase()
        })
      })
      // console.log(res);
      if (res.status === 200) {
        // const e = editName.value.trim()
        success.value = true
        isEdit.value = false
        // alert("Event updated successfully")
        // name.value = ''
        // email.value = ''
        // selectedRole.value = 'Student'
        // getDetail(e)
        // setTimeout(() => appRouter.push({ name: 'UserDetail', params: { name: e } }), 1000)
        // appRouter.push({ name: 'UserDetail', params: { name: name } })
        // localStorage.setItem("name", editName.value.trim())
        // myUser.setUserName(editName.value.trim())
        setTimeout(() => appRouter.push({ name: 'UserList' }), 1000)
        // appRouter.push({ name: 'Home' })
      }
      else if (res.status === 400) {
        alert("This name and email are already used.")
        editRole.value == editRole.value
        // selectedRole.value == "Select your role"
      }
    } else {
      // console.log(editRole.value);
      if (editRole.value.toLocaleLowerCase() == 'guest') {
        alert('Guest is invalid.')
        return
      }
      alert("Invalid email address!");
      return
    }
  }
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
  // if (localStorage.getItem('token') != null) {
  //       var tokenToLocal = localStorage.getItem("token")
  //       var tokenLocal = JSON.parse(tokenToLocal)
  //       // console.log(tokenLocal);
  //       myHeader.value = new Headers({
  //           "content-type": "application/json",
  //           "Authorization": `Bearer ${tokenLocal.accessToken}`,
  //       })
  //   }
  await getDetail()
  await getUser()
})
</script>
 
<template>
  <div>
    <div class="px-6 py-4">
      <span
        class="inline-block bg-gray-100 rounded-full px-3 py-1 text-sm font-semibold text-gray-600 mr-2">#photography</span>
      <span
        class="inline-block bg-gray-100 rounded-full px-3 py-1 text-sm font-semibold text-gray-600 mr-2">#travel</span>
      <span class="inline-block bg-gray-100 rounded-full px-3 py-1 text-sm font-semibold text-gray-600">#winter</span>
    </div>
  </div>

  <div>
    <div class="min-h-screen bg-base-200">
      <div class="text-left">
        <div
          class="card w-auto lg:w-[1200px] h-full bg-gradient-to-r from-base-100 to-base-200 shadow-xl backdrop-blur-sm mb-12">
          <div class="card-body text-xl md:text-3xl place-self-center">
            <p v-if="isEdit == false"
              class="text-5xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-purple-300 to-pink-600 m-4 pb-1 text-center ">
              <span>{{ name }}</span>
            </p>
            <p v-else>
              <label>
                Name:
                <span class="text-sm text-red-500 pb-2" v-show="usedName">This name is already
                  used.</span>
                <span class="text-sm text-yellow-500 pb-2" v-show="editName.length == 100">** A name must be 1 -
                  100 characters. **
                  <br>
                  101st characters onwards will be cut off</span>
                <input type="text" class="input input-bordered input-secondary w-full max-w-xs self-center text-2xl"
                  v-model="editName" id="name" maxlength="100" @change="checkUsedName(editName)" />
                <label class="label">
                  <span class="label-text-alt"></span>
                  <span class="label-text-alt">{{ editName.length }}/100</span>
                </label>
              </label>
            </p>
            <p>
              <label>Email:
                <span v-if="isEdit == false">{{ detail.email }}</span>
                <span v-else>
                  <span class="text-sm text-red-500 pb-2" v-show="usedEmail">This email is already
                    used.</span>
                  <span class="text-sm text-red-500 pb-2" v-show="validateEmail()">Invalid email
                    address.</span>
                  <span class="text-sm text-yellow-500 pb-2" v-show="editEmail.length == 50">** An email must be
                    1
                    - 50 characters. **
                    <br>
                    51st characters onwards will be cut off</span>
                  <input type="email" v-model="editEmail"
                    class="input input-bordered input-secondary w-full max-w-xs self-center text-lg" id="email"
                    maxlength="50" @change="checkUsedEmail(editEmail)" />

                  <label class="label">
                    <span class="label-text-alt"></span>
                    <span class="label-text-alt">{{ editEmail.length }}/50</span>
                  </label>
                </span>
              </label>
            </p>
            <p>
              <label> Role:
                <span v-if="isEdit == false">{{ detail.role }}</span>
                <span v-else>
                  <select class="select select-secondary w-full max-w-xs  text-lg" id="role" v-model="editRole">
                    <option disabled selected>Select your role</option>
                    <option v-for="(role, index) in roles" :key="index">
                      {{ role }}</option>
                  </select>
                </span>
              </label>
            </p>
            <p>
              <!-- <IcPerson class="inline-block mr-5" /> -->
              <label> Created on:
                <!-- {{ detail.createdOn }} -->
                {{ localCreatedOn }}
              </label>
            </p>
            <p>
              <!-- <IcPerson class="inline-block mr-5" /> -->
              <label> Update on:
                <!-- {{ detail.updatedOn }} -->
                {{ localUpdatedOn }}
              </label>
            </p>
            <!-- <input type="text" v-model="selectedEvent.eventCategoryName"
                    class="input input-bordered input-secondary w-full max-w-xs self-center text-2xl" disabled
                  id="duration"  v-else/>
            <div class="divider"></div>
            <p>
              <IcPerson class="inline-block mr-5" /> 
              <label> Name: 
               <span v-if="isEdit==false">{{ selectedEvent.bookingName }}</span>
               <span v-else><input type="text" v-model="selectedEvent.bookingName"
                    class="input input-bordered input-secondary w-96 max-w-xs self-center text-lg" disabled
                  id="duration" /></span>
              </label> 
            </p>
            <br />
            <p>
              <IcTimer class="inline-block mr-5" />
              <label> Duration: 
               <span v-if="isEdit==false">{{ selectedEvent.eventDuration }} mins</span>
               <span v-else><input type="text" v-model="selectedEvent.eventDuration"
                    class="input input-bordered input-secondary w-84 max-w-xs self-center text-lg" disabled
                  id="duration" /></span>
              </label> 
            </p>
            <br />
            <p>
              <IcCalendar class="inline-block mr-5" />
              <label for="starttime">
                Start time  :
              </label>
            <p v-show="!isEdit" class="inline-block">{{ selectedEvent.eventStartTime }}</p> -->
            <!-- <span class="text-red-500 mr-2" v-show="isEdit" >*</span> -->
            <!-- <input v-show="isEdit" type="datetime-local"
              class="input input-bordered input-secondary w-auto max-w-xs text-lg" id="starttime" v-model="editDate" :min="currentTime"/>
            </p>
              <span class="text-sm text-yellow-500 pb-2 inline-block" v-show="compareDate(editDate,currentTime)">** Start time must be in the future.</span>
            <br/>
            <p>
              <IcEmail class="inline-block mr-5 " />
              <label> Email: 
               <span v-if="isEdit==false">{{ selectedEvent.bookingEmail }}</span>
               <span v-else><input type="text" v-model="selectedEvent.bookingEmail"
                    class="input input-bordered input-secondary w-full max-w-xs self-center text-lg" disabled
                  id="duration" /></span>
              </label> 
            </p>
            <br />
            <p>
              <IcNote class="inline-block mr-5" />
              <label for="notes">
                Notes: 
              </label>
            <p v-if="selectedEvent.eventNotes !=''" v-show="!isEdit" class="inline-block">{{ selectedEvent.eventNotes }}</p>
            <p v-else v-show="!isEdit" class="inline-block">NO MESSAGE.</p>
            <p>
              <div v-show="isEdit">
                <span class="text-sm text-yellow-500 pb-2" v-show="editNote.length == 500" >** A notes length must be 1 - 500 character.</span>
              </div>
              <textarea type="text" rows="2" v-show="isEdit" id="notes" v-model="editNote"
                class="textarea textarea-secondary text-xl w-full overflow-auto mt-3"
                maxlength="500"></textarea>
                  <label class="label">
                    <span class="label-text-alt"></span>
                    <span class="label-text-alt" v-show="isEdit">{{editNote.length}}/500</span>
                  </label>
                  </p>
            </p> -->
            <!-- <p v-else>
              <IcNote class="inline-block mr-5" />
              <label for="notes">
                Notes: 
              </label>
            <p v-show="!isEdit" class="inline-block">NO MESSAGE</p>
            <p>
              <span class="text-sm text-yellow-500 " v-show="editNote.length==500" >** A notes length must be 1 - 500 character.</span>
              <textarea type="text" rows="2" v-show="isEdit" id="notes" v-model="editNote"
                class="in textarea textarea-secondary text-lg w-full overflow-auto mt-3 "
                maxlength="500"></textarea>
                  <label class="label">
                    <span class="label-text-alt"></span>
                    <span class="label-text-alt" v-show="isEdit">{{editNote.length}}/500</span>
                  </label>
                </p>
            </p> -->
          </div>
          <div class="alert alert-error shadow-lg w-auto h-12 text-[16px] text-white self-center" v-show="falseInput">
            <div>
              <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6" fill="none"
                viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <span>Please fill {{ alertText }} field.</span>
            </div>
          </div>
          <div class="alert alert-success shadow-lg w-auto h-12 text-[16px] text-white self-center" v-show="success">
            <div>
              <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6" fill="none"
                viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <span>Update user success.</span>
            </div>
          </div>
          <div class="card-actions justify-end m-5">
            <button class="btn btn-secondary border-none " @click="isEdit = !isEdit" v-show="!isEdit">Edit</button>
            <button class="btn btn-accent border-none " @click="isEdit = true, updateUser()"
              v-show="isEdit">Apply</button>
            <!-- click="isEdit = !isEdit, editDate = beforeEditDate, editNote = beforeEditNote" -->
            <button class="btn btn-secondary border-none "
              @click="isEdit = !isEdit, editName = beforeEditName, editEmail = beforeEditEmail, editRole = beforeEditRole, usedEmail = false, usedName = false"
              v-show="isEdit">Cancel</button>
            <router-link :to="{ name: 'UserList' }"><button
                class="btn btn-secondary border-none bg-gradient-to-r from-yellow-500 to-orange-600 hover:from-pink-500 hover:to-yellow-500">Go
                back</button>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
 
<style>

</style>