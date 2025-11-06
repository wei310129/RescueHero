import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import {globalLogout} from './utils/logout'

window.logout = globalLogout

createApp(App).use(router).mount('#app')
