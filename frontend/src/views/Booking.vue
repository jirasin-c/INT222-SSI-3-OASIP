<script setup>
import { computed } from '@vue/reactivity';
import { ref, onBeforeMount, onUpdated } from 'vue';
import { useRouter } from 'vue-router';
import { useUser } from '../stores/user';
import MdiTrashCan from '../components/MdiTrashCan.vue';
const myUser = useUser()
const eventCategory = ref([])
const events = ref([])
const selectedCategory = ref('Project Management Clinic')
const startTime = ref(null)
const name = ref('')
const categoryID = ref()
const falseInput = ref(false)
const success = ref(false)
const email = ref('')
const notes = ref('')
const duration = ref()
const categoryName = ref('')
const appRouter = useRouter()
const currentTime = ref(null)
const isPast = ref(false)
const isOverlapped = ref(false)
const isNotEmail = ref(false)
const alertText = ref('')
const myHeader = ref()
const file = ref({})
const isFileInputed = ref(false)
const isLarger10 = ref(false)
const size = ref(0)
const currentPath = ref('')
const imageURL = ref([])
// const isDeleteFile = ref(false)
// const formData = new FormData()

const createHeader = () => {
    var userToLocal = localStorage.getItem("user")
    var userLocal = JSON.parse(userToLocal)
    var tokenToLocal = localStorage.getItem("token")
    var tokenLocal = JSON.parse(tokenToLocal)
    if (userLocal.role == "guest") {
        myHeader.value = new Headers({
            "content-type": "application/json",
        })
    }
    if (localStorage.getItem('token') != null) {
        // var tokenToLocal = localStorage.getItem("token")
        // var tokenLocal = JSON.parse(tokenToLocal)
        // console.log(tokenLocal);
        myHeader.value = new Headers({
            "content-type": "application/json",
            "Authorization": `Bearer ${tokenLocal.accessToken}`,
        })
    }
}

onUpdated(() => {
    currentTime.value = new Date().getFullYear() + '-' + ('0' + (new Date().getMonth() + 1)).slice(-2) + "-" + ('0' + (new Date().getDate())).slice(-2) + 'T' + ('0' + new Date().getHours()).slice(-2) + ':' + (String(new Date().getMinutes() + 1).padStart(2, '0'))
    // console.log(currentTime.value);
    // console.log(startTime.value);
    // const d = String(new Date().getMinutes())
    // console.log(d.padStart(2,'0'));
    // console.log(categoryID.value);
    // console.log(selectedCategory.value);
    // console.log(eventCategory.value);
    // console.log(selectedCategory.value);
    eventCategory.value.filter((findID) => {
        // console.log(findID.eventCategoryName);
        if (findID.eventCategoryName.trim() === selectedCategory.value) {
            // console.log('ตรง');
            categoryID.value = findID.id
            // duration.value = eventCategory.value.eventDuration
            duration.value = findID.eventDuration
            categoryName.value = findID.eventCategoryName
            // console.log(categoryID.value);
            // console.log(duration.value);
            // console.log(categoryName.value);
        }
    });
    // console.log(categoryID.value);
    // console.log(selectedCategory.value);
    // console.log(categoryName.value);
    // validateOverlapped()
    // console.log(isOverlapped.value);
})
onBeforeMount(async () => {
    await getEventCategory()
    await getEvents()
    // currentTime.value = new Date().getFullYear() + '-' + ('0' + (new Date().getMonth() + 1)).slice(-2) + "-" + new Date().getDate() + 'T' + ('0' + new Date().getHours()).slice(-2) + ':' + (String(new Date().getMinutes() + 1).padStart(2, '0'))
    currentTime.value = new Date().getFullYear() + '-' + ('0' + (new Date().getMonth() + 1)).slice(-2) + "-" + ('0' + (new Date().getDate())).slice(-2) + 'T' + ('0' + new Date().getHours()).slice(-2) + ':' + (String(new Date().getMinutes() + 1).padStart(2, '0'))
    // currentTime.value = new Date().getFullYear()+'-'+('0'+(new Date().getMonth()+1)).slice(-2)+"-"+new Date().getDate()+'T'+('0'+new Date().getHours()).slice(-2)+':'+('0'+new Date().getMinutes()).slice(-2)
    // console.log(myUser.userName);
    // console.log(myUser.userEmail);
    // console.log(myUser.userRole);
    // console.log(myUser.isLogin);
    email.value = myUser.userEmail
})

const compareDate = (startTime, currentTime) => {
    if (startTime > currentTime) {
        isPast.value = false
        return false
    } else if (startTime < currentTime) {
        isPast.value = true
        return true
    } else {
        isPast.value = false
        return false
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

// const validateOverlapped = ()=>{
//     isOverlapped.value = false
//     const compareStartTime = new Date(startTime.value).toLocaleString()
//     const compareStartTimeISO = new Date(startTime.value)

//     events.value.filter((findOvl)=>{
//     const existingStartTime = new Date(findOvl.eventStartTime).toLocaleString()
//     const existStartTimeToMillisec = new Date(findOvl.eventStartTime).getTime()
//     const durationToMillisec = duration.value* 60000
//     const existDuration = findOvl.eventDuration * 60000
//     const existingEndTime = new Date(existStartTimeToMillisec+existDuration).toLocaleString()
//     const alertExistEndTime = new Date(existStartTimeToMillisec+existDuration).getHours()+":"+('0'+new Date(existStartTimeToMillisec+existDuration).getMinutes()).slice(-2)+":"+('0'+ new Date(existStartTimeToMillisec+existDuration).getSeconds()).slice(-2)
//     const startTimeToMillisec = new Date(compareStartTimeISO).getTime()
//     const startTimePlusDuration = startTimeToMillisec + durationToMillisec
//     const compareEndTime = new Date(startTimePlusDuration).toLocaleString()
//     // console.log(existingStartTime.value);
//     // console.log(alertExistEndTime.value);
//        if ((findOvl.eventCategoryID.id === categoryID.value)) {
//         //    console.log(existingEndTime);
//         //    if(((compareEndTime <= existingEndTime) && (compareEndTime >= existingStartTime)) || ((compareStartTime >= existingStartTime) &&(compareStartTime < existingEndTime)))  {
//             if(((compareEndTime <= existingEndTime) && (compareEndTime > existingStartTime)) || ((compareEndTime <= existingEndTime) && (compareEndTime > existingStartTime))|| ((compareStartTime > existingStartTime) && (compareStartTime < existingEndTime)) || ((compareStartTime <= existingStartTime) &&(compareEndTime >= existingEndTime))) {
//             // if(((compareStartTime == existingStartTime)&&(compareEndTime == existingEndTime)) || ((compareStartTime > existingStartTime) &&(compareStartTime < existingEndTime)) || ((compareStartTime <= existingStartTime) &&(compareEndTime >= existingEndTime))) {
//             // if(((compareStartTime<existingEndTime)&&(compareEndTime>=existingEndTime)) && ((compareStartTime>=existingStartTime) && (compareEndTime>=existingStartTime))) {
//         //    if((existingStartTime<compareStartTime&&compareStartTime<existingEndTime)||(existingStartTime<compareEndTime&&compareEndTime<existingEndTime))  {
//             //    console.log(compareEndTime);
//            isOverlapped.value = true
//            alert(`Sorry, the booking has Overlapped in ${existingStartTime} - ${alertExistEndTime}, Please select new date.`)
//            }
//        }
//     })
// }

const createEvent = async () => {

    isOverlapped.value = false
    const compareStartTime = new Date(startTime.value).toLocaleString()
    const compareStartTimeISO = new Date(startTime.value)

    events.value.filter((findOvl) => {
        const existingStartTime = new Date(findOvl.eventStartTime).toLocaleString()
        const existStartTimeToMillisec = new Date(findOvl.eventStartTime).getTime()
        const existDuration = findOvl.eventDuration * 60000
        const existingEndTime = new Date(existStartTimeToMillisec + existDuration).toLocaleString()

        const durationToMillisec = duration.value * 60000
        const alertExistEndTime = new Date(existStartTimeToMillisec + existDuration).getHours() + ":" + ('0' + new Date(existStartTimeToMillisec + existDuration).getMinutes()).slice(-2) + ":" + ('0' + new Date(existStartTimeToMillisec + existDuration).getSeconds()).slice(-2)
        const startTimeToMillisec = new Date(compareStartTimeISO).getTime()
        const startTimePlusDuration = startTimeToMillisec + durationToMillisec
        const compareEndTime = new Date(startTimePlusDuration).toLocaleString()
        // console.log(findOvl.eventCategoryName)
        // console.log(categoryName.value);
        if ((findOvl.eventCategoryName === categoryName.value)) {
            //    console.log(existingEndTime);
            //    if(((compareEndTime <= existingEndTime) && (compareEndTime >= existingStartTime)) || ((compareStartTime >= existingStartTime) &&(compareStartTime < existingEndTime)))  {
            // if(((compareStartTime == existingStartTime)&&(compareEndTime == existingEndTime)) || ((compareEndTime <= existingEndTime) && (compareEndTime > existingStartTime)) || ((compareStartTime > existingStartTime) &&(compareStartTime < existingEndTime)) || ((compareStartTime <= existingStartTime) &&(compareEndTime >= existingEndTime))) {
            // if(((compareEndTime <= existingEndTime) && (compareEndTime > existingStartTime)) || ((compareEndTime <= existingEndTime) && (compareEndTime > existingStartTime))|| ((compareStartTime > existingStartTime) && (compareStartTime < existingEndTime)) || ((compareStartTime <= existingStartTime) &&(compareEndTime >= existingEndTime))) {
            if (((compareEndTime <= existingEndTime) && (compareEndTime > existingStartTime)) || ((compareStartTime > existingStartTime) && (compareStartTime < existingEndTime)) || ((compareStartTime <= existingStartTime) && (compareEndTime >= existingEndTime))) {
                // if(((compareStartTime<existingEndTime)&&(compareEndTime>=existingEndTime)) && ((compareStartTime>=existingStartTime) && (compareEndTime>=existingStartTime))) {
                //    if((existingStartTime<compareStartTime&&compareStartTime<existingEndTime)||(existingStartTime<compareEndTime&&compareEndTime<existingEndTime))  {
                //    console.log(compareEndTime);
                isOverlapped.value = true
                alert(`Sorry, the booking has Overlapped in ${existingStartTime} - ${alertExistEndTime}, Please select new date.`)
            }
        }
    })
    if (startTime.value == '') {
        startTime.value = null
    }
    // console.log(startTime.value);
    if (name.value == '' || email.value == '' || startTime.value == null) {
        alertText.value = ''
        //  console.log(startTime.value);
        startTime.value = startTime.value
        //  console.log(startTime.value);
        falseInput.value = true

        if (name.value == '') {
            if (email.value != '') {
                alertText.value += ("name, ")
            } else {
                alertText.value += ("name, ")
            }
        }
        if (email.value == '') {
            if (startTime.value != null) {
                alertText.value += ("email ")
            } else {
                alertText.value += ("email, ")
            }
        }
        if (startTime.value == null) {
            alertText.value += ("start time ")
        }

        return
    }

    if (isOverlapped.value == true) {
        // alert(`Sorry, the booking has Overlapped in ${existingStartTime} - ${alertExistEndTime}, Please select new date.`)
        return
    }

    if (isPast.value == true) {
        startTime.value = startTime.value
        alert("Start time is in the past, Please select new date")
        return
    }
    if (isLarger10.value == true) {
        alert("The file size cannot be larget than 10 MB.")
        return
    }
    // if (name.value.length == 100 || notes.value.length == 500 || email.value.length == 100 ) {
    //     startTime.value = startTime.value
    //     alert("Field longer string can't be event.")
    // }else{
    if (isNotEmail.value == false) {
        falseInput.value = false
        console.log(startTime.value);
        // const utc = new Date(startTime.value).toISOString()
        // startTime.value = utc
        // console.log(startTime.value);
        if (confirm(`Are you sure to the create the event ?`)) {
            const utc = new Date(startTime.value).toISOString()
            // startTime.value = utc
            // console.log(myUser.userID); 
            var userToLocal = localStorage.getItem("user")
            var userLocal = JSON.parse(userToLocal)
            createHeader()
            // if (userLocal.role == "guest") {
            const formData = new FormData()
            myHeader.value = new Headers({
                "accept": "*/*",
                // "content-Type": "multipart/form-data"
            })
            formData.append('event', `{"bookingName": "${name.value}",
                    "bookingEmail": "${email.value}",
                    "eventStartTime": "${utc}",
                    "eventDuration": ${duration.value},
                    "eventNotes": "${notes.value}",
                    "eventCategoryID": ${categoryID.value}}`)
            formData.append('file', file.value)
            // }
            // const formData = new FormData()

            const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events`, {
                method: 'POST',
                headers: myHeader.value,
                body: formData
                // body: JSON.stringify({
                //     // userID: myUser.userID,
                //     // userID: userLocal.id,
                //     bookingName: name.value,
                //     bookingEmail: email.value,
                //     eventStartTime: utc,
                //     eventDuration: duration.value,
                //     eventNotes: notes.value,
                //     eventCategoryID: categoryID.value,
                // },
                // )
            })
            if (res.status === 200) {
                startTime.value = null
                name.value = ''
                email.value = ''
                notes.value = ''
                // alert("Event created successfully")
                success.value = true
                setTimeout(() => appRouter.push({ name: 'Home' }), 1000)
                // appRouter.push({ name: 'Home' })
            } else {
                startTime.value = startTime.value
                alert("Event can't created")
            }
        }
    } else {
        alert("Invalid email address!");
        return
    }
}
const getEventCategory = async () => {
    createHeader()
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/event-categories`, {
        method: "GET",
        headers: myHeader.value
    })
    eventCategory.value = await res.json()
    // console.log(eventCategory.value);
}
const getEvents = async () => {
    createHeader()
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}api/events`, {
        method: "GET",
        headers: myHeader.value
    })
    if (res.status == 401) {
        events.value = []
    } else {
        events.value = await res.json()
    }

    // console.log(events.value);
}

const onFileSelected = (e) => {
    // console.log(Object.keys(file.value).length === 0 && file.value.constructor === Object);
    isLarger10.value = false
    isFileInputed.value = false
    if (Object.keys(file.value).length === 0 && file.value.constructor === Object) {
        // console.log("ยังไม่มี");
        file.value = e.target.files[0]
        currentPath.value = e.target.value
        // console.log(currentPath.value);
        if (file.value.size > 10485760) {
            // console.log("เกิน");
            e.target.value = null
            file.value = {}
            isFileInputed.value = false
            // console.log(e.target.files);
            isLarger10.value = true
            return
        }
        const newImgURL = []
        newImgURL.push(URL.createObjectURL(file.value))
        // console.log(newImgURL);
        imageURL.value = newImgURL
        // console.log(imageURL.value);
        isFileInputed.value = true
        size.value = formatBytes(file.value.size)
    } else {
        // console.log("มีแล้ว");
        // console.log(e.target.files.length == 0);
        if (e.target.files.length == 0) {
            isFileInputed.value = true
            return
        }

        let checkFile = {}
        checkFile = e.target.files[0]
        if (checkFile.size > 10485760) {
            // console.log("เกิน");
            // console.log(file.value);
            // console.log(e.target.files[0]);
            // console.log(currentPath.value);

            e.target.value = null
            // console.log(e.target.value);
            // file.value = {}
            isFileInputed.value = true
            // console.log(e.target.files);
            isLarger10.value = true
            return
        }
        file.value = checkFile
        const newImgURL = []
        newImgURL.push(URL.createObjectURL(file.value))
        // console.log(newImgURL);
        imageURL.value = newImgURL
        // console.log(imageURL.value);
        isFileInputed.value = true
        size.value = formatBytes(file.value.size)
    }
    // console.log(file.value.size);
    // console.log(file.value.type);
    // console.log(e.target.files);
    // console.log(e.target.value);
    // isLarger10.value = false
    // file.value = e.target.files[0]
    // isFileInputed.value = true
    // console.log(e.target.files);
    // console.log(e.target.files[0]);
    // console.log(file.value);



    // if (file.value.size > 10485760) {
    //     e.target.value = null
    //     file.value = {}
    //     isFileInputed.value = false
    //     // console.log(e.target.files);
    //     isLarger10.value = true
    // } else {
    //     file.value = e.target.files[0]
    //     isFileInputed.value = true
    //     size.value = formatBytes(file.value.size)
    // }

    // console.log(size.value);
    // if (isDeleteFile.value == true) {
    //     e.target.value = null
    // }
    // if (file.value.length > 0) {
    //     isLarger10.value = true
    // }else 
    // if (file.value.size > 10485760) {
    //     e.target.value = null
    //     file.value = {}
    //     isFileInputed.value = false
    //     // console.log(e.target.files);
    //     isLarger10.value = true
    // }
    // console.log(file.value);
    // console.log(file.value.name);
}
const deleteFile = () => {
    // isDeleteFile.value = true
    document.getElementById('input').value = null
    file.value = {}
    isFileInputed.value = false
    size.value = 0
    imageURL.value = []
}
const formatBytes = (bytes, decimals = 2) => {
    if (!+bytes) return '0 Bytes'

    const k = 1024
    const dm = decimals < 0 ? 0 : decimals
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']

    const i = Math.floor(Math.log(bytes) / Math.log(k))

    return `${parseFloat((bytes / Math.pow(k, i)).toFixed(dm))} ${sizes[i]}`
}

</script>
 
<template>
    <div>
        <div class="hero min-h-screen bg-base-200">
            <div class="hero-content text-left ">
                <div
                    class="card w-auto lg:w-[1000px] bg-gradient-to-r from-base-100 to-base-200 mb-16 shadow-xl backdrop-blur-sm">
                    <div class="card-body text-3xl place-self-center pb-1">
                        <p
                            class="text-5xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-purple-300 to-pink-600 pb-1 text-center">
                            Booking Event
                        </p>
                        <div class="divider"></div>
                        <div class="flex justify-center pb-2">
                            <div class="form-control w-full max-w-xs">
                                <label for="category" class="label">
                                    <span class="label-text text-base font-semibold">
                                        Category: <span class="text-red-500">*</span>
                                    </span>
                                </label>
                                <select class="select select-secondary w-full max-w-xs  text-lg" id="category"
                                    v-model="selectedCategory">
                                    <option v-for="category in eventCategory " :key="category">
                                        {{ category.eventCategoryName }}</option>
                                </select>
                                <label for="duration" class="label">
                                    <span class="label-text text-base font-semibold">
                                        Duration:
                                    </span>
                                </label>
                                <input type="text" v-model="duration"
                                    class="input input-bordered input-secondary w-full max-w-xs text-gray-50" disabled
                                    id="duration" />
                                <label for="starttime" class="label">
                                    <span class="label-text text-base font-semibold">
                                        Start time: <span class="text-red-500">*</span>
                                    </span>
                                </label>
                                <span class="text-sm text-red-500 pb-2"
                                    v-show="compareDate(startTime, currentTime)">Start time must be in the
                                    future.</span>
                                <!-- <span class="text-sm text-red-500 pb-2" v-show="isOverlapped">Start time is overlapped.</span> -->
                                <input type="datetime-local" placeholder="Type here"
                                    class="input input-bordered input-secondary w-full max-w-xs text-lg"
                                    v-model="startTime" id="starttime" :min="currentTime">
                                <label for="name" class="label">
                                    <span class="label-text text-base font-semibold">
                                        Name : <span class="text-red-500">*</span>
                                    </span>
                                </label>
                                <span class="text-sm text-yellow-500 pb-2" v-show="name.length == 100">** A name must be
                                    1
                                    - 100 characters. **</span>
                                <input type="text" placeholder="Type yourname..."
                                    class="input input-bordered input-secondary w-full max-w-xs text-lg" v-model="name"
                                    id="name" maxlength="100" />
                                <label class="label">
                                    <span class="label-text-alt"></span>
                                    <span class="label-text-alt">{{ name.length }}/100</span>
                                </label>
                                <label for="email" class="label">
                                    <span class="label-text text-base font-semibold">
                                        Email : <span class="text-red-500">*</span>
                                    </span>
                                </label>
                                <span class="text-sm text-red-500 pb-2" v-show="validateEmail()">Invalid email
                                    address.</span>
                                <span class="text-sm text-yellow-500 pb-2" v-show="email.length == 100">** An email must
                                    be 1 - 100 characters. **</span>
                                <input v-if="myUser.isLogin == false" type="email"
                                    placeholder="example@mail.kmutt.ac.th"
                                    class="input input-bordered input-secondary w-full max-w-xs  text-lg"
                                    v-model="email" id="email" maxlength="100" />
                                <input v-else-if="myUser.userRole != 'admin'" type="email"
                                    placeholder="example@mail.kmutt.ac.th"
                                    class="input input-bordered input-secondary w-full max-w-xs  text-lg text-gray-50"
                                    v-model="email" id="email" maxlength="100" disabled />
                                <input v-else type="email" placeholder="example@mail.kmutt.ac.th"
                                    class="input input-bordered input-secondary w-full max-w-xs  text-lg"
                                    v-model="email" id="email" maxlength="100" />
                                <label class="label">
                                    <span class="label-text-alt"></span>
                                    <span class="label-text-alt">{{ email.length }}/100</span>
                                </label>
                                <label for="notes" class="label">
                                    <span class="label-text text-base font-semibold">
                                        Notes :
                                    </span>
                                </label>
                                <span class="text-sm text-yellow-500 pb-2" v-show="notes.length == 500">** A notes must
                                    be
                                    1 - 500 characters. **</span>
                                <textarea id="notes" cols="30" rows="2" v-model="notes"
                                    class="textarea textarea-secondary  text-lg w-full overflow-auto"
                                    placeholder="Type something..." maxlength="500"></textarea>
                                <label class="label">
                                    <span class="label-text-alt"></span>
                                    <span class="label-text-alt">{{ notes.length }}/500</span>
                                </label>
                                <label for="input" class="label">
                                    <span class="label-text text-base font-semibold text-secondary">
                                        Maximun 10 MB.
                                    </span>
                                </label>
                                <input type="file" class="file-input file-input-bordered w-full max-w-xs"
                                    @change="onFileSelected" id="input" />
                                <span class="text-sm text-yellow-500 pb-2" v-show="isLarger10">** The file size cannot
                                    be large than 10 MB. **</span>
                                <div class="pt-5 avatar flex"  v-if="file.type == 'image/png' || file.type == 'image/jpeg'">
                                    <div class="w-96 h-64 rounded-xl">
                                        <!-- <img :src="imageObjectURL" width="300" height="300" alt="">
                                         -->
                                        <img :src="imageURL" alt="" width="400" height="400">
                                    </div>
                                </div>

                                <!-- <p>{{file.name}}</p> -->
                                <ul v-show="isFileInputed">
                                    <li class="text-sm flex justify-between mt-5">{{ file.name }}
                                        <span class="text-secondary">{{ size }}</span>
                                        <button @click="deleteFile">
                                            <svg width="32" height="32" viewBox="0 0 24 24">
                                                <path fill="currentColor"
                                                    d="M15.59 7L12 10.59L8.41 7L7 8.41L10.59 12L7 15.59L8.41 17L12 13.41L15.59 17L17 15.59L13.41 12L17 8.41L15.59 7Z" />
                                            </svg>
                                        </button>
                                    </li>
                                </ul>

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
                                <span>Create event success.</span>
                            </div>
                        </div>
                        <div class="card-actions justify-center">
                            <button @click="createEvent"
                                class="btn btn-secondary border-none bg-gradient-to-r from-yellow-500 to-orange-600 hover:from-pink-500 hover:to-yellow-500 mb-4 text-lg">Book
                                event</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>
 
<style scoped>
::-webkit-calendar-picker-indicator {
    filter: invert(1);
}
</style>