import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

// https://vitejs.dev/config/
export default defineConfig({
  base: '/ssi3/',
  plugins: [vue()],
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080/",
        // target: "http://10.4.56.114:8080/",
        changeOrigin: true,
        secure: false,
        // rewrite: (path) => path.replace(/^/api/, '')
      },
    },
  },
});
