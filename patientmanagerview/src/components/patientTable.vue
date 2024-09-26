<template>
  <DataTable 
    :value="patientData" 
    dataKey="id" 
    :editMode="editMode" 
    v-model:editingRows="editingRows"
    @row-edit-save="onRowEditComplete($event)" 
    size="normal" 
    resizableColumns 
    v-model:expandedRows="expandedRows"
    selectionMode="single" 
    v-model:selection="selectedPatient">
    <template #header>
      <div style="display: flex; justify-content: flex-end ; gap: 10px;">
        <Button icon="pi pi-plus" severity="info" @click="onAdd($event)"></Button>
        <Button icon="pi pi-trash" severity="danger" class="deleteButton" @click="onDelete(index)" />
      </div>
    </template>

    <Column expander class="reponsiveColumn">
    </Column>
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
        <InputText type="text" v-model="data[field]" />
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
  </DataTable>
</template>
<script setup>
import { ref } from 'vue';
import Select from 'primevue/select';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
let patientData = ref();
let genderOptions = ref(["Male", "Female"])
let expandedRows = ref([]);
let editingRows = ref();
let selectedPatient = ref({});
let editMode = ref("row");

const props = defineProps({
  patientManagerUrl: String
})
loadData();
function onRowEditComplete(e) {
  patientData.value[e.index] = e.newData
}
function onDelete(e) {
  
  patientData.value.splice(e, 1)
}
function onAdd() {
  patientData.value.push({
    id: patientData.value.length + 1,
    name: '',
    lastname: '',
    birthdate: '',
    gender: '',
    address: '',
    phone: ''
  })
}
async function loadData() {
  let response=await fetch(props.patientManagerUrl+"/getAll", {
    method: "GET"})
  patientData.value = await response.json()
}
</script>
<style>
.p-datatable-column-header-content {
  justify-content: center;
}

.reponsiveColumn {
  display: none;
}

@media (max-width: 920px) {
  .reponsiveColumn {
    display: table-cell;
  }

  .optionalColumn {
    display: none;
  }
}

@media (max-width: 460px) {
  .deleteColumn {
    display: none;
  }
}
</style>