<template>
  <DataTable 
    :value="patientData" 
    dataKey="patientid" 
    :editMode="editMode" 
    v-model:editingRows="editingRows"
    @row-edit-save="onRowEditComplete($event)" 
    @update:selection="onSelectionChange()"
    @row-unselect="deletePossible = true"
    @row-select="deletePossible = false"
    size="normal" 
    resizableColumns 
    v-model:expandedRows="expandedRows"
    selectionMode="single" 
    v-model:selection="selectedPatient">
    <template #header>
      <div style="display: flex; justify-content: flex-end ; gap: 10px;">
        <Button icon="pi pi-plus" severity="success" @click="onAdd($event)"></Button>
        <Button icon="pi pi-trash" severity="danger" class="deleteButton" 
        :disabled="deletePossible" 
        @click="onDelete()" ></Button>
        <Button severity="info" icon="pi pi-paperclip" :disabled="deletePossible" @click="onSeeMore()"></Button>
      </div>
    </template>
    <Column field="patientid" header="ID" bodyStyle="text-align:center" :pt="{ headerContent: 'justify-content-center' }">
    </Column>
    <Column field="firstname" header="Firstname" bodyStyle="text-align:center" sortable>
      <template #editor="{ data, field }">
        <InputText type="text" v-model="data[field]" />
      </template>
    </Column>
    <Column field="lastname" header="Lastname" bodyStyle="text-align:center" sortables>
      <template #editor="{ data, field }">
        <InputText type="text" v-model="data[field]" />
      </template>
    </Column>
    <Column field="birthdate" header="Birthdate" bodyStyle="text-align:center" class="optionalColumn">
      <template #editor="{ data, field }">
        <DatePicker type="text" v-model="data[field]" dateFormat="dd/mm/yy" />

      </template>
    </Column>
    <Column field="gender" header="Gender" bodyStyle="text-align:center" class="optionalColumn">
      <template #editor="{ data, field }">
        <Select v-model="data[field]" :options="genderOptions" />
      </template>
    </Column>
    <Column field="zipcode" header="Zipcode" bodyStyle="text-align:center" class="optionalColumn">
      <template #editor="{ data, field }">
        <InputText type="text" v-model="data[field]" />
      </template>
    </Column>
    <Column field="phone" header="Phone" bodyStyle="text-align:center" class="optionalColumn">
      <template #editor="{ data, field }">
        <InputText type="text" v-model="data[field]" />
      </template>
    </Column>
    <Column :row-editor="true" bodyStyle="text-align:center"></Column>
    <template #expansion="slotProps">
      <patientNote :patient="slotProps.data" />
    </template>
  </DataTable>
</template>
<script setup>
import { ref } from 'vue';
import Select from 'primevue/select';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import DatePicker from 'primevue/datepicker';
import patientNote from '@/components/patientNote.vue'; 
let patientData = ref();
let genderOptions = ref(["MALE", "FEMALE"]);
let expandedRows = ref([]);
let editingRows = ref();
let selectedPatient = ref({});
let editMode = ref("row");
let deletePossible = ref(true);
const props = defineProps({
  patientManagerUrl: String
})
loadData();
function onRowEditComplete(e) {
  savePatient(e.newData)
}
function onSelectionChange() {
  if (selectedPatient.value == null) {
    deletePossible.value = true;
  } else {
    deletePossible.value = false;
  }
}
function onDelete() {
  let id = selectedPatient.value.patientid
  deletePatient(id)
}
function onAdd() {
  patientData.value.push({
    id: '',
    firstname: '',
    lastname: '',
    birthdate: '',
    gender: 'MALE',
    address: '',
    phone: ''
  })
}
function savePatient(data) {
  data.birthdate = data.birthdate.getDate() + "/" + (data.birthdate.getMonth() + 1) + "/" + data.birthdate.getFullYear();
  
  fetch(props.patientManagerUrl+"/save", {
    method: "POST",
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  }).then(response => {
    if(!response.ok) {
      response.json().then(result => {
        alert(
    `firstname: ${result.firstname} 
    lastname: ${result.lastname} 
    birthdate: ${result.birthdate}`
  )
      })
  }else {
    loadData();
  }
})
  
}
function deletePatient(id) {
  fetch(props.patientManagerUrl+"/delete?patientid=" + id, {
    method: "DELETE"
  }).then(response => {
    if (!response.ok) {
      alert("Error deleting patient");
    } else {
      loadData();
    }
  })
  }
function loadData() {
  fetch(props.patientManagerUrl+"/getAll", {
    method: "GET"}).then(response => {
      if(response.ok) {
      response.json().then(result => {
        patientData.value= result; 
      })
    }else{
      location.href="/login"
    }
    })
    
  
}
function onSeeMore() {
  localStorage.setItem("patient",JSON.stringify(selectedPatient.value));
  console.log(localStorage.getItem("patient"))
  location.href = "/patientnote"
}
</script>
<style>
.p-datatable-column-header-content {
  justify-content: center;
}

@media (max-width: 920px) {
  .reponsiveColumn {
    display: table-cell;
  }

  .optionalColumn {
    display: none;
  }
}
</style>