<script setup>
</script>

<template>
  <div>
    <div>
      <section class="text-gray-700 body-font">
        <div class="container mx-auto flex px-5 py-24 md:flex-row flex-col items-center">
          <div
            class="lg:flex-grow md:w-1/2 lg:pr-24 md:pr-16 flex flex-col md:items-start md:text-left mb-16 md:mb-0 items-center text-center">
            <h1 class="title-font sm:text-4xl text-3xl mb-4 font-medium text-gray-900">Talk with your instructors,
              <br class="hidden lg:inline-block">make your works better
            </h1>
            <p class="mb-8 leading-relaxed">The OASIP Application makes it simple for you to schedule a session with
              your teacher. Try out our features or click "Book Now" below to begin.</p>
            <div class="flex justify-center">
              <router-link :to="{ name: 'Booking' }"
                class="inline-flex text-white bg-indigo-500 border-0 py-2 px-6 focus:outline-none hover:bg-indigo-600 rounded-lg text-lg">
                BOOK
                NOW</router-link>
              <router-link :to="{ name: 'EventList' }"
                class="ml-4 inline-flex text-gray-700 bg-gray-200 border-0 py-2 px-6 focus:outline-none hover:bg-gray-300 rounded-lg text-lg">
                View Booking</router-link>
            </div>
          </div>
          <div class="lg:max-w-lg lg:w-full md:w-1/2 w-5/6">
            <img class="object-cover object-center rounded" alt="hero" src="../assets/undraw_educator_re_ju47.svg">
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped>
::-webkit-scrollbar {
  width: 16px;

}

::-webkit-scrollbar-track {
  background-color: #e4e4e4;
  border-radius: 100px;
}

::-webkit-scrollbar-thumb {
  background-image: linear-gradient(180deg, #D0368A 0%, #708AD4 99%);
  box-shadow: inset 2px 2px 5px 0 rgba(#fff, 0.5);
  border-radius: 100px;
  border: 2px solid transparent;
  background-clip: content-box;
}

::-webkit-calendar-picker-indicator {
  filter: invert(1);
}
</style>
