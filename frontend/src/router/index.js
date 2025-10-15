import {createRouter, createWebHistory} from 'vue-router'
import LoginPage from '../views/LoginPage.vue'
import HelloWorld from '../components/HelloWorld.vue'

const routes = [
  { path: '/', component: HelloWorld },
  { path: '/login', component: LoginPage }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

