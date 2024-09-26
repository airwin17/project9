<template>
    <NavBar/>
    <main>
        <h1  class="title">Patient data</h1>
        <patientTable :patientManagerUrl="patientManagerUrl"/>
    </main>
</template>
<script setup>
import NavBar from '../components/NavBar.vue';
import patientTable from '../components/patientTable.vue';
const props = defineProps({
    patientManagerUrl: String
})
</script>
<style lang="css">
    .title{
        color: var(--p-datatable-header-cell-color);
    }
</style>