<template>
    <DataTable :value="notes" dataKey="patientid" v-model:expandedRows="expandedRows" id="Patientnote">
        <template #header style="display: flex;">
            <p>{{ fullname }}</p>
            <div>
                <p>Health status :</p>
                <Tag :value="health" :severity="healthSeverity"></Tag>
            </div>
            <Button icon="pi pi-plus" severity="success" @click="onAdd($event)"></Button>
        </template>
        <Column expander />
        <Column field="noteid" header="ID" bodyStyle="text-align:center" :pt="{ headerContent: 'justify-content-center' }">
        </Column>
        <Column field="firstname" header="Name" bodyStyle="text-align:center" sortable></Column>
        <Column field="lastname" header="Lastname" bodyStyle="text-align:center" sortables></Column>
        <Column field="health" header="Health state" bodyStyle="text-align:center"></Column>
    </DataTable>
</template>

<script setup>
import { ref } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Editor from 'primevue/editor';
import Tag from 'primevue/tag';
let props = defineProps({
    patientNoteUrl: String
})
let patient;
let notes = ref();
let health = ref();
let expandedRows=ref();
let healthSeverity=ref();
let fullname=ref();
loadData();
function loadData() {
    fetch(props.patientNoteUrl+"/get", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: localStorage.getItem("patient")
    }).then(response => {
        if (!response.ok) {
            alert("Error loading notes");
        }else{
            response.json().then(result => {
                notes.value = result.notes;
                health.value = result.health;
                patient = result.patient;
                fullname.value="Fullname : "+patient.firstname+" "+patient.lastname
            })
            console.log(patient)
        }
    })
}

</script>
<style>
    #Patientnote .p-datatable-header{
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    #Patientnote .p-datatable-header > div{
        display: flex;
        align-items: center;
    }
    #Patientnote .p-datatable-header p{
        margin: 10px;
        font-size: large;
    }
</style>