<template>
    <DataTable 
    class="userTable" 
    :value="usersData" 
    dataKey="userid" resizableColumns 
    selectionMode="single" 
    v-model:selection="selectedUser"
    @update:selection="onSelectionChange()"
    @row-unselect="editUserButtonState = true"
    @row-select="editUserButtonState = false"
    >
        <template #header>
            <div style="" >
                <Button icon="pi pi-plus" label="Add" severity="info" @click="onAdd()"></Button>
                <Button icon="pi pi-trash" label="Delete" severity="danger" :disabled="editUserButtonState" @click="onDelete()"></Button>
                <Button label="Edit" icon="pi pi-pencil" severity="warning" :disabled="editUserButtonState" @click="onedit()"></Button>
            </div>
        </template>
        <Column field="userid" header="ID" bodyStyle="text-align:center"></Column>
        <Column field="username" header="Username" sortable bodyStyle="text-align:center"></Column>
        <Column field="authority" header="Role" bodyStyle="text-align:center"></Column>
    </DataTable>
    <userShow v-model:visibility="visibility" :apiGatewayUser="apiGatewayUser" :title="editmode ? 'Edit User' : 'Add User'" :user="selectedUser" v-model:editmode="editmode" @close="dial = false"></userShow>
</template>
<script setup>
import { ref } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import userShow from '@/components/userShow.vue';
import { watch } from 'vue';
var visibility=ref(false);
let selectedUser=ref();
let editUserButtonState = ref(true);
let editmode = ref(false);
let usersData = ref();
const props=defineProps({
    apiGatewayUser: String
})
loadUserList()
function onAdd() {
    editmode.value = false;
    visibility.value = true;
}
function onedit() {
    editmode.value = true;
    visibility.value = true;
}
function onSelectionChange() {
    console.log(selectedUser.value)
    if(selectedUser.value==null){
        editUserButtonState=true;
    }else{
        editUserButtonState=false;
    }
}
watch(usersData, newVal => {
    console.log(newVal)
})
function onDelete() {
    fetch(`${props.apiGatewayUser}/delete?userid=${selectedUser.value.userid}`, {
        method: 'DELETE',
    })
        .then((response) => {
            if(response.ok){
                location.reload();
            }else{
                response.text().then(text => {
                    alert(text);
                })
            }
    })
}

function loadUserList() {
    console.log("loadUserList()")
    fetch(`${props.apiGatewayUser}/getAll`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then((response) => {
            if(response.ok){
                response.json().then((data) => {
                    console.log(data)
                    usersData.value = data
                });
            }else{
                console.log("error")
                location.href="/login";
            }
             
        })
        
}
</script>
<style>
.userTable .p-datatable-header > div{
    display: flex;
     justify-content: flex-end;
     gap: 10px;
}
</style>