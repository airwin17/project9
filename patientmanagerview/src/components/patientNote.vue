<template>
    
    <DataTable :value="notes" dataKey="noteid" v-model:expandedRows="expandedRows" id="Patientnote">
        <template #header style="display: flex;">
            <p>{{ fullname }}</p>
            <div>
                <p>Health status :</p>
                <Tag :value="health" :severity="healthSeverity"></Tag>
            </div>
            <Button icon="pi pi-plus" severity="success" @click="onClickPlus()"></Button>
        </template>
        <Column expander />
        <Column field="noteid" header="ID" bodyStyle="text-align:center" :pt="{ headerContent: 'justify-content-center' }">
        </Column>
        <Column field="author" header="Author" bodyStyle="text-align:center" sortable></Column>
        <Column field="date" header="Date" bodyStyle="text-align:center" sortables></Column>
        <template #expansion="slotProps">
            <div v-html="slotProps.data.note"></div>
        </template>
    </DataTable>
    <patientNoteShow v-model:visibility="visibility" :patientNoteUrl="props.patientNoteUrl" :note="noteTemplate"></patientNoteShow>
</template>

<script setup>
import { ref } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Tag from 'primevue/tag';
import patientNoteShow from "./PatientNoteShow.vue";
let props = defineProps({
    patientNoteUrl: String
})
let patient;
let notes = ref();
let health = ref();
let expandedRows=ref();
let healthSeverity=ref();
let fullname=ref();
let visibility = ref(false);
let noteTemplate=ref();
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
function onClickPlus() {
    newNote();
    visibility.value = true;
}
function newNote() {
    noteTemplate.value = {
        patientid: patient.patientid,
        author: JSON.parse(JSON.parse(localStorage.user)).username,
        note: "",
        date: new Date().toISOString().slice(0, 10)
    }
    console.log(noteTemplate.value)
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