<template>
    <Dialog class="userShow" v-model:visible="visible" :header="title" :style="{ width: '25rem' }" position="top"
        :modal="true" :draggable="false">
        <span class="text-surface-500 dark:text-surface-400 block mb-8">Enter new user data </span>
        <form>
            <div>
                <FloatLabel>
                    <InputText id="username" v-model="username" />
                    <label for="username">Username</label>
                </FloatLabel>
                <span style="text-align: left; display: block;">{{ usernameErrorMsg }}</span>
            </div>
            <div>
                <FloatLabel>
                    <Password v-model="password" id="password" toggleMask />
                    <label for="password">Password</label>
                </FloatLabel>
                <span style="text-align: left; display: block;">{{ passwordErrorMsg }}</span>
            </div>
            <div class="role-div">
                <span>User</span>
                <ToggleSwitch v-model="checked" />
                <span>Admin</span>
            </div>
            <div>
                <Button type="button" label="Cancel" severity="secondary" @click="visible = false"></Button>
                <Button type="button" label="Save" @click="onSave()"></Button>
            </div>
        </form>
    </Dialog>
</template>

<script setup>
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import FloatLabel from 'primevue/floatlabel';
import Password from 'primevue/password';
import ToggleSwitch from 'primevue/toggleswitch';
import { ref, watch } from 'vue';
let props = defineProps({
    user: Object,
    apiGatewayUser: {
        type: String,
        required: true
    },
    title: String
})
let usernameErrorMsg = ref();
let passwordErrorMsg = ref();
let visible = defineModel("visibility", { type: Boolean, default: false });
let editmode = defineModel("editmode", { type: Boolean, default: false });
let username=ref();
let checked=defineModel();
let password=ref();
function onSave() {
    fetch(props.apiGatewayUser+"/add", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            userid: editmode.value ? props.user.userid : null,
            username: username.value,
            password: password.value,
            authority: checked.value==true ? "ADMIN" : "USER"
        })
    }).then(res => {
            if (res.ok) {
                visible.value = false;
                location.reload();
            }else {
                res.json().then(data => {
                    passwordErrorMsg.value = data.password
                    usernameErrorMsg.value = data.username
                })
            }
        })    
}

function initEditMode(t) {
    console.log(props.user)
    if (t) {
        checked.value = props.user.authority === "ADMIN" ? true : false;
        username.value = props.user.username;
    } else {
        checked.value = false;
        username.value = "";
    }
}
watch(visible, newVal => {
    if (newVal) {
        initEditMode(editmode.value);
        usernameErrorMsg.value = "";
        passwordErrorMsg.value = "";
    }
})

</script>

<style>
.userShow button {
    margin: 10px;
}
.role-div {
    display: flex;
    justify-content: center;
    align-items: center;
}
.role-div > span {
    margin: 10px;
    text-align: center;
}
.userShow #username,
#password>input {
    width: 300px;
}

.userShow .p-dialog-content>span {
    margin-bottom: 20px;
    display: inline-block;
}
</style>