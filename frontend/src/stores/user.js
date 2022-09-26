import { defineStore, acceptHMRUpdate } from 'pinia'
import { ref, computed } from 'vue'
export const useUser = defineStore('user', () => {
  const isLogin = ref(false)
  const userID = ref(undefined)
  const userName = ref('')
  const userRole = ref('')
  const setLogin = () => (isLogin.value = true)
  const setLogout = () =>{
    isLogin.value = false
    userID.value = undefined
    userName.value = ''
    userRole.value = ''
    localStorage.removeItem("token")
    localStorage.removeItem("name")
    location.reload()
  }
  const setUserID = (setID) => (userID.value = setID)
  const setUserName = (setName) => (userName.value = setName)
  const setUserRole = (setRole) => (userRole.value = setRole)
  return { isLogin, userID, userName, userRole, setLogin, setLogout, setUserID, setUserName, setUserRole }
})
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUser, import.meta.hot))
}