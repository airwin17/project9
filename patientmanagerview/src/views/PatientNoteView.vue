<template>
    <NavBar/>
    <h1>Patient Note</h1>
    <patientNote :patientNoteUrl="patientNoteUrl"/>
</template>
<script setup>
import NavBar from '@/components/NavBar.vue';
import patientNote from '@/components/patientNote.vue';
const props = defineProps({
    patientNoteUrl: String
})
</script>
<style>
body {
    font-family: sans-serif;
    text-align: center;
}

h1 {
    font-size: 3em;
    color: #333;
}

p {
    font-size: 1.5em;
    color: #666;
}
</style>