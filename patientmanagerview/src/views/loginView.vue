
<template>
  <main>
    <h1>Login</h1>
    <loginForm :loginRoute="loginRoute" />
  </main>
</template>
<script setup>
import loginForm from '../components/loginForm.vue'
defineProps({
    loginRoute: String
})
</script>

