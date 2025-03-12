import { createRouter, createWebHistory } from 'vue-router'
import Layout from "../layout/Layout";

const routes = [
  {
    path: '/',
    name: 'Layout',
    redirect:"user",
    component: Layout,
    children:[
      {
        path:'user',
        name:'user',
        component:() => import("@/views/User")
      },
      {
        path: 'car',
        name: 'car',
        component: () => import("@/views/Car")
      },
      {
        path: 'person',
        name: 'Person',
        component: () => import("@/views/Person")
      },
      {
        path: 'password',
        name: 'Password',
        component: () => import("@/views/Password")
      },
      {
        path: 'lendrecord',
        name: 'LendRecord',
        component: () => import("@/views/LendRecord")
      },
      {
        path:'dashboard',
        name:'Dashboard',
        component:() => import("@/views/Dashboard")
      },
      {
        path: 'carwithuser',
        name: 'CarWithUser',
        component: () => import("@/views/CarWithUser")
      },
      {
        path: 'car-lot',
        name: 'car-lot',
        component: () => import("@/views/carParkingLot/index.vue")
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import("@/views/Login")
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import("@/views/Register")
  },


]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
