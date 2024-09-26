<template>
    <h1>error {{ errorCode }}</h1>
    <p>{{ messageFromServer }}</p>
</template>
<script setup>
    defineProps({
        Response: Object
    })
</script>
<style>
body {
    font-family: sans-serif;
    text-align: center;
}

h1 {
    font-size: 3em;
    color: #333;
}

p {
    font-size: 1.5em;
    color: #666;
}
</style>