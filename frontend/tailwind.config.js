module.exports = {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}", 'node_modules/flowbite-vue/**/*.{js,jsx,ts,tsx}', "./node_modules/flowbite/**/*.js"],
  theme: {
    extend: {},
  },
  plugins: [require('flowbite/plugin')]

 // require("daisyui"), 
};
