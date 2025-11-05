import {createRouter, createWebHistory} from 'vue-router'
import LoginPage from '../views/LoginPage.vue'
import RegisterPage from '../views/RegisterPage.vue'
import HelloWorld from '../components/HelloWorld.vue'
import RescueTasksPage from '../views/RescueTasksPage.vue'

const routes = [
  { path: '/', component: HelloWorld },
  { path: '/login', component: LoginPage },
  { path: '/register', component: RegisterPage },
  { path: '/tasks', component: RescueTasksPage }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
