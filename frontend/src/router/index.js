import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";
import Detail from "../views/Detail.vue";
import Booking from "../views/Booking.vue"
import EventCategory from "../views/EventCategory.vue"
import UserList from "../views/UserList.vue"
import UserDetail from "../views/UserDetail.vue"
const history = createWebHistory('/ssi3/');
// const history = createWebHistory();
const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/event-detail/:bookingId",
    name: "Detail",
    component: Detail,
  },
  {
    path: "/bookings/",
    name: "Booking",
    component: Booking,
  },
  {
    path: "/categories/",
    name: "EventCategory",
    component: EventCategory,
  },
  {
    path: "/user-list/",
    name: "UserList",
    component: UserList
  },
  {
    // path: "/user-detail/:name",
    path: "/user-detail/:name",
    name: "UserDetail",
    component: UserDetail
  }
];

const router = createRouter({ history, routes });
export default router;
