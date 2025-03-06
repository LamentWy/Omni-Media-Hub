import { createRouter, createWebHistory } from 'vue-router'
import AboutView from '@/views/AboutView.vue'
import VideoView from '@/views/VideoView.vue'
import HomeView from '@/views/HomeView.vue'
import LoginView from '@/views/auth/LoginView.vue'
import RegisterView from '@/views/auth/RegisterView.vue'
import CommonLayout from '@/components/layout/CommonLayout.vue'
import AdminLayout from '@/components/layout/AdminLayout.vue'
import AdminUserView from '@/views/admin/AdminUserView.vue'
import UserInfoView from '@/views/UserInfoView.vue'
import { useUserStore } from '@/stores/UserStore.ts'
import ResourceScanView from '@/views/admin/res/ResourceScanView.vue'
import AdminCollectionView from '@/views/admin/res/AdminCollectionView.vue'
import ResCollectionView from '@/views/res/ResCollectionView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'commonLayout',
      component: CommonLayout,
      children: [
        {
          path: '/',
          name: 'home',
          component: HomeView,
          alias: '/home',
        },
        {
          path: '/video',
          name: 'videoPage',
          component: VideoView,
        },
        {
          path: '/rc/:rcId',
          name: 'rcDetailPage',
          component: ResCollectionView,

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
          name: 'loginPage',
          component: LoginView,
        },
        {
          path: '/register',
          name: 'registerPage',
          component: RegisterView,
        },
      ]
    },
    {
      path: '/z/user',
      name: 'userInfoPage',
      component: UserInfoView,
      beforeEnter: () => {
        const userStore = useUserStore();
        if (!userStore.authenticated){
          // 如何在守卫中弹出登陆框？
          return '/login'
        }
      },
    },
    {
      path: '/z/admin',
      name: 'adminLayout',
      component: AdminLayout,
      beforeEnter: (to,from) => {
        const userStore = useUserStore();
        if (!userStore.authenticated){
          return '/login'
        }
        if (userStore.userDto.roles[0] != 'ROLE_ADMIN'){

          // same
          return from.path
        }
      },
      children: [
        {
          path: '/z/admin/users/',
          name: 'adminUserPage',
          component: AdminUserView,
        },
        {
          path: '/z/admin/res/collection',
          name: 'adminCollectionPage',
          component: AdminCollectionView,
        },
        {
          path: '/z/admin/res/scan',
          name: 'resScanPage',
          component: ResourceScanView,
        },
      ]
    },
  ],
})

export default router
