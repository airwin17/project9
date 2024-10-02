import 'primeicons/primeicons.css';
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import PrimeVue from "primevue/config";
import Aura from '@primevue/themes/aura';
import ToastService from 'primevue/toastservice';

const app = createApp(App)

app.use(router)

app.use(PrimeVue,{theme: {
    preset: Aura,
    options: {
        prefix: 'p',
        darkModeSelector: '.my-app-dark',
        cssLayer: false
    }
}})
app.use(ToastService)
app.mount('#app')
