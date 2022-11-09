import { createRouter, createWebHistory } from "vue-router";
import { useUser } from "../stores/user";
import Home from "../views/Home.vue";
import Detail from "../views/Detail.vue";
import Booking from "../views/Booking.vue";
import EventCategory from "../views/EventCategory.vue";
import UserList from "../views/UserList.vue";
import UserDetail from "../views/UserDetail.vue";
import AddUser from "../views/AddUser.vue";
import SignIn from "../views/SignIn.vue";
import MatchPass from "../views/MatchPass.vue";
const history = createWebHistory("/ssi3/");
// const history = createWebHistory();
// async function removeQueryParams(to) {
//   // const myUser = useUser();
//   var userToLocal = localStorage.getItem("user");
//   var userLocal = JSON.parse(userToLocal);
//   // console.log(myUser);
//   // console.log(localStorage.getItem("user"));
//   // console.log(to);
//   // console.log(userLocal.role);
//   // console.log(myUser.userRole);
//   // if (Object.keys(to.query).length){
//   //   return { path: to.path, query: {}, hash: to.hash }
//   if (localStorage.getItem("user") == null) {
//     return { path: "/", query: {}, hash: to.hash };
//   }
//   if (localStorage.getItem("user") != null && userLocal.role != "admin") {
//     return { path: "/", query: {}, hash: to.hash };
//   }
// }
// function removeHash(to) {
//   if (to.hash) return { path: to.path, query: to.query, hash: "" };
// }

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
    beforeEnter: (to, form) => {
      var userLocal = {
        // id: null,
        // name: "",
        email: "",
        role: "",
      };
      if (localStorage.getItem("token") == null) {
        // myUser.setLogin();
        // myUser.setUserEmail(email);
        // myUser.setUserRole("guest");
        // userLocal.email = email;
        userLocal.role = 'guest';
        localStorage.setItem("user", JSON.stringify(userLocal));
      }
    },
  },
  {
    path: "/event-detail/:bookingId",
    name: "Detail",
    component: Detail,
    beforeEnter: (to, form) => {
      // const myUser = useUser();
      // console.log(myUser.userRole);
      // if (myUser.userRole != "admin"){
      //   return {name: "Home"}
      // }
      var userToLocal = localStorage.getItem("user");
      var userLocal = JSON.parse(userToLocal);
      if (localStorage.getItem("user") == null) {
        return { name: "Home" };
      }
      // if (localStorage.getItem("user") != null && userLocal.role == "lecturer") {
      //   return { name: "Home" };
      // }
    },
  },
  {
    path: "/bookings/",
    name: "Booking",
    component: Booking,
    beforeEnter: (to, form) => {
      // const myUser = useUser();
      // console.log(myUser.userRole);
      // if (myUser.userRole != "admin"){
      //   return {name: "Home"}
      // }
      var userToLocal = localStorage.getItem("user");
      var userLocal = JSON.parse(userToLocal);
      if (localStorage.getItem("user") != null && userLocal.role == "lecturer") {
        return { name: "Home" };
      }
    },
  },
  {
    path: "/categories/",
    name: "EventCategory",
    component: EventCategory,
  },
  {
    path: "/user-list/",
    name: "UserList",
    component: UserList,
    beforeEnter: (to, from) => {
      // const myUser = useUser();
      // var userToLocal = localStorage.getItem("user");
      // var userLocal = JSON.parse(userToLocal);
      // if (myUser.userRole != "admin" && userLocal == null) {
      //   return { name: "SignIn" };
      // }
      // if (myUser.userRole != "admin" && userLocal != null) {
      //   return { name: "Home" };
      // }
      var userToLocal = localStorage.getItem("user");
      var userLocal = JSON.parse(userToLocal);
      if (localStorage.getItem("user") == null) {
        return { name: "SignIn" };
      }
      if (localStorage.getItem("user") != null && userLocal.role != "admin") {
        return { name: "Home" };
      }
    },
  },
  {
    // path: "/user-detail/:name",
    path: "/user-detail/:name",
    name: "UserDetail",
    component: UserDetail,
    beforeEnter: (to, form) => {
      // const myUser = useUser();
      // console.log(myUser.userRole);
      // if (myUser.userRole != "admin"){
      //   return {name: "Home"}
      // }
      var userToLocal = localStorage.getItem("user");
      var userLocal = JSON.parse(userToLocal);
      if (localStorage.getItem("user") == null) {
        return { name: "Home" };
      }
      if (localStorage.getItem("user") != null && userLocal.role != "admin") {
        return { name: "Home" };
      }
    },
    // beforeEnter: [removeQueryParams, removeHash],
  },
  {
    path: "/sign-up/",
    name: "AddUser",
    component: AddUser,
    beforeEnter: (to, form) => {
      // const myUser = useUser();
      // if (myUser.userRole != "admin") {
      //   return { name: "Home" };
      // }
      var userToLocal = localStorage.getItem("user");
      var userLocal = JSON.parse(userToLocal);
      if (localStorage.getItem("user") == null) {
        return { name: "Home" };
      }
      if (localStorage.getItem("user") != null && userLocal.role != "admin") {
        return { name: "Home" };
      }
    },
  },
  {
    path: "/sign-in/",
    name: "SignIn",
    component: SignIn,
  },
  {
    path: "/match-pass/",
    name: "MatchPass",
    component: MatchPass,
    beforeEnter: (to, from) => {
      // const myUser = useUser()
      // if (myUser.userRole != "admin") {
      //     return { name: "Home" };
      // }
      var userToLocal = localStorage.getItem("user");
      var userLocal = JSON.parse(userToLocal);
      if (localStorage.getItem("user") == null) {
        return { name: "Home" };
      }
      if (localStorage.getItem("user") != null && userLocal.role != "admin") {
        return { name: "Home" };
      }
    },
  },
];

const router = createRouter({ history, routes });
// router.beforeEach(async(to,form)=>{
// const myUser = useUser()

//   if (to.name == "UserList" && myUser.isLogin == false) {
//     return {name: "MatchPass"}
//   }
// })
export default router;
