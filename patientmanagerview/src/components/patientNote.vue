<template>
    <DataTable :value="patientData" dataKey="id" :editMode="editMode" v-model:expanded-rows="expandedRows">
        <Column field="id" header="ID" bodyStyle="text-align:center" :pt="{ headerContent: 'justify-content-center' }">
        </Column>
        <Column field="name" header="Name" bodyStyle="text-align:center" sortable></Column>
        <Column field="lastname" header="Lastname" bodyStyle="text-align:center" sortables></Column>
        <template #expansion="slotProps">
            <Editor v-model="slotProps.data.note" />
        </template>
    </DataTable>
</template>

<script setup>
import { ref } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Editor from 'primevue/editor';
import patientData from '../assets/testPatient.json';

</script>