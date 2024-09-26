<template>
    <main>
        <h1>Signin</h1>
        <signinForm :signinRoute="signingRoute" />
    </main>
</template>
<script setup>
import signinForm from '../components/signinForm.vue'
defineProps({
    signinRoute: String
})
</script>