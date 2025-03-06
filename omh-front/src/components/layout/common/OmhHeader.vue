<template>
  <div id="omh-navbar" class="omh-header">
    <a-row class="navbar-row" :wrap="false">
      <!--  logo-col    -->
      <a-col flex="20px"  >
        <!-- logo -->
        <div class="logo">
          <img class="logo-img" src="@/assets/logo.svg"  alt="logo"/>
        </div>
      </a-col>

      <a-col flex="5 1 auto">
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
      <a-col flex="1 20px">
        <div class="omh-navbar-tools">
          <a-switch>改变主题:未实现</a-switch>
        </div>
      </a-col>
      <a-col flex="120px">
        <div class="omh-navbar-auth">
          <div class="auth-switch" v-if="userStore.authenticated">
            <UserDropDown />
          </div>
          <div class="auth-switch" v-else>
            <a-button type="primary" shape="round" @click="loginSwitch">登录/注册</a-button>
            <LoginModel v-model="switchState"/>
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

const loginState = ref<boolean>(false)
loginState.value = userStore.authenticated;


const router = useRouter()
router.afterEach((to) => {
  selectedKeys.value = [to.path]
})
const handleSelect = ({ key }: { key: string }) => {
  console.log(key)
  router.push(key)
}

const switchState = ref(false)

const loginSwitch = () => {
  switchState.value = !switchState.value;
}
</script>

<style>


#omh-navbar .menu {
  text-align: center;
  background-color: #b2dfdb;
  height: 48px;
}
#omh-navbar .logo-img {
  float: left;
  width: 20px;
  margin-top: 24px;
  background: rgba(255, 255, 255, 0.3);
}
</style>
