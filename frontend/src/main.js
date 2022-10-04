import { createApp } from "vue";
import router from "./router";
import App from "./App.vue";
import {createPinia} from 'pinia'
import "./index.css";

createApp(App).use(createPinia()).use(router).mount("#app");

// const app = createApp(App)
// app.use(createPinia()).use(router)
// app.mount('#app')


