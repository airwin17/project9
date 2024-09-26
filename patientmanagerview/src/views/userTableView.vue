<template>
    <NavBar/>
    <main>
        <h1  class="title">user data</h1>
        <userTable :apiGatewayUser="apiGatewayUser"/>
    </main>
</template>
<script setup>
import userTable from '../components/userTable.vue'
import NavBar from '../components/NavBar.vue';
const props = defineProps({
    apiGatewayUser: String
})
console.log(props.apiGatewayUser)
</script>