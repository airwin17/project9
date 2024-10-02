<template>
    
    <Dialog v-model:visible="visible" modal position="top">
        <Editor v-model="note" editorStyle="height: 300px" />
        <template #header>
            <h3>Enter new note</h3>
        </template>
        <template #footer>
            <Button label="Save" icon="pi pi-check" severity="success" @click="onSave()" />
            <Button label="Cancel" icon="pi pi-times" severity="secondary" @click="onCancel()" />
        </template>
    </Dialog>
</template>
<script setup>
import { ref } from 'vue';
import Dialog from 'primevue/dialog';
import Editor from 'primevue/editor';
import Button from 'primevue/button';
import { defineModel } from 'vue';


let props = defineProps({
    patientNoteUrl: String,
    note: Object
})

let visible = defineModel("visibility", { type: Boolean, default: false });
let note = ref();
function onSave() {
    let noteObject=props.note;
    noteObject.note=note.value;
    if (note.value !== null && note.value.length > 20) {    
    fetch(props.patientNoteUrl+"/add", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(noteObject)
    }).then(res => {
        if (res.ok) {
            visible.value = false ;
            alert("Note saved");
            location.reload();
        }else{
            res.json().then(data => {
                alert(data);
            })
        }
    });
    }else{
       alert("Note must be at least 20 characters long");
    }
}
function onCancel() {
    visible.value = false;
    note.value="";
}
</script>