<template>
  <div id="omh-navbar" class="omh-header">
    <a-row class="navbar-row">
      <a-col :xs="2" :sm="2" :md="2" :lg="2" :xl="2">
        <!-- logo -->
        <div class="logo">
          <img class="logo-img" src="@/assets/logo.svg" />
        </div>
      </a-col>

      <a-col :xs="14" :sm="17" :md="17" :lg="17" :xl="19" push="12">
        <div class="omh-navbar-menu">
          <!-- menu: using @select to route  -->
          <!-- also you can use :items and @click  -->
          <a-menu
            class="menu"
            v-model:selectedKeys="selectedKeys"
            mode="horizontal"
            @select="handleSelect"
          >
            <a-menu-item key="/">首页</a-menu-item>
            <a-menu-item key="/video">视频</a-menu-item>
            <a-menu-item key="/about">关于</a-menu-item>
          </a-menu>
        </div>
      </a-col>
      <a-col :xs="2" :sm="1" :md="1" :lg="1" :xl="1">
        <div class="omh-navbar-tools">
          <a-switch>改变主题:未实现</a-switch>
        </div>
      </a-col>
      <a-col :xs="6" :sm="4" :md="4" :lg="4" :xl="2" push="1">
        <div class="omh-navbar-auth">
          <div class="auth-switch" v-if="userStore.authenticated">
            <UserDropDown :nn="nickname" />
          </div>
          <div class="auth-switch" v-else>
            <LoginModel />
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import LoginModel from '@/components/auth/LoginModal.vue'
import { useUserStore } from '@/stores/UserStore'
import UserDropDown from '@/components/auth/UserDropDown.vue'

const userStore = useUserStore()
const selectedKeys = ref<string[]>(['/'])

const nickname = ref<string>('')
nickname.value = userStore.userInfo?.nickName

const router = useRouter()
router.afterEach((to) => {
  selectedKeys.value = [to.path]
})
const handleSelect = ({ key }: { key: string }) => {
  console.log(key)
  router.push(key)
}
</script>

<style>
#omh-navbar .menu {
  background-color: #b2dfdb;
}
#omh-navbar .logo-img {
  float: left;
  width: 60px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}
</style>
