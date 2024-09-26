<template>
    <div id="card">
        <Menubar :model="items"/>
    </div>
</template>
<script setup>
import { ref } from 'vue';
import Menubar from 'primevue/menubar';
import navMenu from '../assets/navMenu.json';
const items = ref(navMenu);
if(localStorage.getItem("darkMode") == "true") {
    document.querySelector('html').classList.toggle('my-app-dark');
}
items.value[items.value.length - 1].command = toggleDarkMode;
items.value[0].command=()=>{
    window.location.href = '/userTable';
};
items.value[1].command=()=>{
    window.location.href = '/patientData';
}
items.value[4].command=()=>{
    fetch('/api/logout', {
        method: 'POST'
    }).then(response => {
        if (response.ok) {
            console.log('Logout successful');
            window.location.href = '/login';
        } else {
            alert('Logout failed');
        }
    })
}
function toggleDarkMode() {
    if(localStorage.getItem("darkMode") == "true") {
        localStorage.setItem("darkMode", "false");
        items.value[items.value.length - 1].icon = 'pi pi-sun';
    } else {
        localStorage.setItem("darkMode", "true");
        items.value[items.value.length - 1].icon = 'pi pi-moon';
    }
    document.querySelector('html').classList.toggle('my-app-dark');
}
</script>
<style>
#card{
    display: flex; 
    justify-content: center;
}
@media (max-width: 960px) {
    #card{
        display: block;
    }
}
</style>