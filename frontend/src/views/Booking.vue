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
    // if (isLarger10.value == true) {
    //     alert("The file size cannot be larget than 10 MB.")
    //     return
    // }
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
    if (confirm("Are you sure to remove this file?")) {
        document.getElementById('input').value = null
        file.value = {}
        isFileInputed.value = false
        size.value = 0
        imageURL.value = []
    } else {
        return
    }
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
        <section class="text-gray-600 body-font relative">
            <div class="container px-5 py-14 mx-auto">
                <div class="flex flex-col text-center w-full mb-12">
                    <h1 class="sm:text-3xl text-2xl font-medium title-font mb-4 text-gray-900">Booking</h1>
                    <p class="lg:w-2/3 mx-auto leading-relaxed text-base">Please fill out the data below</p>
                </div>
                <div class="lg:w-1/2 md:w-2/3 mx-auto">
                    <div class="flex flex-wrap -m-2">
                        <div class="p-2 w-full">
                            <div class="relative">
                                <label for="name" class="leading-7 text-sm text-gray-600">Category<span
                                        class="text-red-500 p-2">*</span></label>
                                <select
                                    class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out"
                                    id="
                                    category" v-model="selectedCategory">
                                    <option v-for="category in eventCategory " :key="category">
                                        {{ category.eventCategoryName }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="p-2 w-1/2">
                            <div class="relative">
                                <label for="name" class="leading-7 text-sm text-gray-600">Duration</label>
                                <input type="text" v-model="duration"
                                    class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-400 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out cursor-no-drop"
                                    disabled id="duration" />
                            </div>
                        </div>
                        <div class="p-2 w-1/2">
                            <div class="relative" id="outlined_error">
                                <label for="email" class="leading-7 text-sm text-gray-600">Start time<span
                                        class="text-red-500 p-2">*</span></label>
                                <span class="text-sm text-red-500 p-2"
                                    v-show="compareDate(startTime, currentTime)">Start time must be in the
                                    future.</span>
                                <input type="datetime-local"
                                    class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out"
                                    v-model="startTime" id="starttime" :min="currentTime">
                            </div>
                        </div>
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
                                <span class="text-sm text-yellow-500 pb-2" v-show="email.length == 100">** An email must
                                    be 1 - 100 characters. **</span>
                                <input v-if="myUser.isLogin == false" type="email"
                                    placeholder="example@mail.kmutt.ac.th"
                                    class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out"
                                    v-model="email" id="email" maxlength="100" />
                                <input v-else-if="myUser.userRole != 'admin'" type="email"
                                    placeholder="example@mail.kmutt.ac.th"
                                    class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out"
                                    v-model="email" id="email" maxlength="100" disabled />
                                <input v-else type="email" placeholder="example@mail.kmutt.ac.th"
                                    class="w-full bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-200 text-base outline-none text-gray-700 py-1 px-3 leading-8 transition-colors duration-200 ease-in-out"
                                    v-model="email" id="email" maxlength="100" />
                                <p class="text-sm opacity-50 text-right">{{ email.length }}/100</p>
                            </div>
                        </div>
                        <div class="p-2 w-full">
                            <div class="relative">
                                <label for="message" class="leading-7 text-sm text-gray-600">Notes</label>
                                <textarea id="notes" cols="30" rows="2" v-model="notes"
                                    class="w-full placeholder:opacity-50 bg-gray-100 bg-opacity-50 rounded border border-gray-300 focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-200 h-32 text-base outline-none text-gray-700 py-1 px-3 resize-none leading-6 transition-colors duration-200 ease-in-out"
                                    placeholder="Type something..." maxlength="500"></textarea>
                                <p class="text-sm opacity-50 text-right">{{ notes.length }}/500</p>
                            </div>
                        </div>
                        <div class="p-2 w-full">

                            <label class="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                                for="file_input">Upload file</label>
                            <input type="file"
                                class="block w-full text-sm text-gray-900 border border-gray-300 rounded-lg cursor-pointer bg-gray-50 dark:text-gray-400 focus:outline-none dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400"
                                @change="onFileSelected" id="input" />
                            <!-- <p class="mt-1 text-sm text-gray-500 dark:text-gray-300" id="file_input_help">SVG, PNG, JPG
                                or GIF (MAX. 800x400px).</p> -->


                            <span class="text-sm text-yellow-500 pb-2" v-show="isLarger10">** The file size cannot
                                be large than 10 MB. **</span>
                            <div class="flex p-8" v-if="file.type == 'image/png' || file.type == 'image/jpeg'">

                                <img class="mx-auto rounded-xl" :src="imageURL" alt="" width="200" height="200">

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
                        <div class="p-8 w-full">
                            <button
                                class="flex mx-auto text-white bg-indigo-500 border-0 py-2 px-8 focus:outline-none hover:bg-indigo-600 rounded-lg text-lg">SUBMIT</button>
                        </div>
                        <div class="p-2 w-full pt-8 mt-8 border-t border-gray-200 text-center">
                            <a class="text-indigo-500">napasuay@email.com</a>
                            <p class="leading-normal my-5">Napasorn, Jirasin, Naruebet
                                <br>School of Information Technology, KMUTT
                            </p>
                            <span class="inline-flex">
                                <a class="text-gray-500" href="https://www.instagram.com/_bamnapasorn_/" target="_blank">
                                    <svg fill="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                        stroke-width="2" class="w-5 h-5" viewBox="0 0 24 24">
                                        <path d="M18 2h-3a5 5 0 00-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 011-1h3z"></path>
                                    </svg>
                                </a>
                                <a class="ml-4 text-gray-500" href="https://www.instagram.com/_bamnapasorn_/" target="_blank">
                                    <svg fill="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                        stroke-width="2" class="w-5 h-5" viewBox="0 0 24 24">
                                        <path
                                            d="M23 3a10.9 10.9 0 01-3.14 1.53 4.48 4.48 0 00-7.86 3v1A10.66 10.66 0 013 4s-4 9 5 13a11.64 11.64 0 01-7 2c9 5 20 0 20-11.5a4.5 4.5 0 00-.08-.83A7.72 7.72 0 0023 3z">
                                        </path>
                                    </svg>
                                </a>
                                <a class="ml-4 text-gray-500" href="https://www.instagram.com/_bamnapasorn_/" target="_blank"> 
                                    <svg fill="none" stroke="currentColor" stroke-linecap="round"
                                        stroke-linejoin="round" stroke-width="2" class="w-5 h-5" viewBox="0 0 24 24">
                                        <rect width="20" height="20" x="2" y="2" rx="5" ry="5"></rect>
                                        <path d="M16 11.37A4 4 0 1112.63 8 4 4 0 0116 11.37zm1.5-4.87h.01"></path>
                                    </svg>
                                </a>
                                <a class="ml-4 text-gray-500" href="https://www.instagram.com/_bamnapasorn_/" target="_blank">
                                    <svg fill="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                        stroke-width="2" class="w-5 h-5" viewBox="0 0 24 24">
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



        <!-- <div class="container mx-auto h-screen bg-white flex justify-center items-center">
                <form class="w-full max-w-lg">
                    <div class="flex flex-wrap -mx-3 mb-6">
                        <div class="w-full md:w-1/2 px-3 mb-6 md:mb-0">
                            <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2"
                                for="grid-first-name">
                                First Name
                            </label>
                            <input
                                class="appearance-none block w-full bg-gray-200 text-gray-700 border border-red-500 rounded-full py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                id="grid-first-name" type="text" placeholder="Jane">
                            <p class="text-red-500 text-xs italic">Please fill out this field.</p>
                        </div>
                        <div class="w-full md:w-1/2 px-3">
                            <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2"
                                for="grid-last-name">
                                Last Name
                            </label>
                            <input
                                class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded-full py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500"
                                id="grid-last-name" type="text" placeholder="Doe">
                        </div>
                    </div>
                    <div class="flex flex-wrap -mx-3 mb-6">
                        <div class="w-full px-3">
                            <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2"
                                for="grid-password">
                                Password
                            </label>
                            <input
                                class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded-full py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white focus:border-gray-500"
                                id="grid-password" type="password" placeholder="******************">
                            <p class="text-gray-600 text-xs italic">Make it as long and as crazy as you'd like</p>
                        </div>
                    </div>


                    <div class="flex flex-wrap -mx-3 mb-2">
                        <div class="w-full md:w-1/3 px-3 mb-6 md:mb-0">
                            <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2"
                                for="grid-city">
                                City
                            </label>
                            <input
                                class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded-full py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500"
                                id="grid-city" type="text" placeholder="Albuquerque">
                        </div>

                        <div class="w-full md:w-1/3 px-3 mb-6 md:mb-0">
                            <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2"
                                for="grid-state">
                                State
                            </label>
                            <div class="relative">
                                <select
                                    class="block appearance-none w-full bg-gray-200 border border-gray-200 text-gray-700 py-3 px-4 pr-8 rounded-full leading-tight focus:outline-none focus:bg-white focus:border-gray-500"
                                    id="grid-state">
                                    <option>New Mexico</option>
                                    <option>Missouri</option>
                                    <option>Texas</option>
                                </select>
                                <div
                                    class="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
                                    <svg class="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg"
                                        viewBox="0 0 20 20">
                                        <path
                                            d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z" />
                                    </svg>
                                </div>
                            </div>
                        </div>


                        <div class="w-full md:w-1/3 px-3 mb-6 md:mb-0">
                            <label class="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2"
                                for="grid-zip">
                                Zip
                            </label>
                            <input
                                class="appearance-none block w-full bg-gray-200 text-gray-700 border border-gray-200 rounded-full py-3 px-4 leading-tight focus:outline-none focus:bg-white focus:border-gray-500"
                                id="grid-zip" type="text" placeholder="90210">
                        </div>

                    </div>

                    <div class="w-full px-3 mb-6 md:mb-0">
                        <div class="flex flex-wrap -mx-3 mb-6">

                            <label class="w-full block text-gray-500 font-bold">
                                <input class="mr-2 leading-tight" type="checkbox">
                                <span class="text-sm">
                                    Send me your newsletter!
                                </span>
                            </label>
                        </div>

                        <div class="flex flex-wrap -mx-3 mb-6">

                            <div class="w-full">
                                <button
                                    class="shadow-none bg-blue-700 hover:bg-blue-400 focus:shadow-outline focus:outline-none text-white font-bold py-4 px-10 rounded-full"
                                    type="button">
                                    Sign Up
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
            </div> -->
    </div>
</template>
 
<style scoped>

</style>