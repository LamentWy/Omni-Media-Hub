<template >
  <a-dropdown placement="bottom">
    <a class="ant-dropdown-link" @click.prevent>
      {{ userStore.userInfo.nickName }}
      <CaretDownOutlined />
    </a>
    <template #overlay>
      <a-menu @click="onClick">
        <a-menu-item key="user-info"> 用户信息 </a-menu-item>
        <a-menu-divider />
        <a-menu-item key="admin-page" v-if="userStore.userDto.roles[0] ==='ROLE_ADMIN'"> 后台管理 </a-menu-item>
        <a-menu-divider />
        <a-menu-item key="logout">退出登录</a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>
<script lang="ts" setup>
import { useUserStore } from '@/stores/UserStore'
import { CaretDownOutlined } from '@ant-design/icons-vue'
import type { MenuProps } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'


const userStore = useUserStore()
const router = useRouter()

const logout = async () => {
  localStorage.removeItem('OMH-Token')
  sessionStorage.removeItem('OMH-Token')
  userStore.logout()
  await router.push('/')
}

const onClick: MenuProps['onClick'] = ({ key }) => {
  console.log(`Click on item ${key}`)

  if (key === 'user-info'){
    router.push('/z/user')
  }

  if (key === 'admin-page'){
    router.push('/z/admin')
  }

  if (key === 'logout') {
    logout().then(() => {
      message.success({
        content: () => '退出成功！',
        duration: 2,
        class: 'custom-class',
        style: {
          marginTop: '20vh',
        },
      })
    })
  }
}
</script>
