import { createRouter, createWebHistory } from 'vue-router'
import AboutView from '@/views/AboutView.vue'
import VideoView from '@/views/VideoView.vue'
import HomeView from '@/views/HomeView.vue'
import LoginView from '@/views/auth/LoginView.vue'
import RegisterView from '@/views/auth/RegisterView.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/video',
      name: 'videoPage',
      component: VideoView,
    },
    {
      path: '/about',
      name: 'aboutPage',
      component: AboutView,
      // lazy-load
      // component: () => import('../views/AboutView.vue'),
    },
    {
      path: '/login',
      name: 'LoginPage',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'RegisterPage',
      component: RegisterView,
    },
  ],
})

export default router
