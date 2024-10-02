<template>
    <form method="post" id="loginForm">
        <FloatLabel>
            <InputText v-model="username" />
            <label for="username">Username</label>
        </FloatLabel>
        <FloatLabel>
            <Password v-model="password" :feedback="false" inputId="password" />
            <label for="password" >Password</label>
        </FloatLabel>
        <Button label="Submit" icon="pi pi-sign-in" type="submit" @click="login($event)"/>
    </form>
</template>
<script setup>
import FloatLabel from 'primevue/floatlabel';
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import Button from 'primevue/button';
import { ref } from 'vue';
const username = ref('');
const password = ref('');
const props=defineProps({
    loginRoute: String
})
function login(e){
    console.log(username.value, password.value);
    
    e.preventDefault();
    const urlEncodedData = new URLSearchParams();
    urlEncodedData.append('username', username.value);
    urlEncodedData.append('password', password.value);
    fetch(props.loginRoute, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        method: 'POST',
        body: urlEncodedData
    })
    .then(response => {
        if (response.ok) {
            response.text().then(user => {
                localStorage.setItem('user', JSON.stringify(user));
                window.location.href = '/patientData';
            })
        } else {
            alert('Login failed');
        }
    })
    .catch(error => console.error('Error:', error));
}
</script>
<style>
form{
    display: flex ;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 30px;
}
</style>