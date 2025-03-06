<template>
  <div id="omh-admin-layout">
  <a-layout style="min-height: 100vh" >
    <a-layout-sider v-model:collapsed="collapsed" collapsible theme="dark" >
      <div class="logo">
        <img class="admin-logo-img" src="@/assets/logo.svg"  alt="logo"/>
      </div>
      <a-menu @click="onClick" theme="dark" mode="inline">
        <a-menu-item key="file-scan">
          <FolderOpenOutlined />
          <span>资源扫描</span>
        </a-menu-item>
        <a-menu-item key="admin-user">
          <team-outlined />
          <span>用户管理</span>
        </a-menu-item>

        <a-sub-menu key="res">
          <template #title>
            <span>
              <desktop-outlined />
              <span>资源管理</span>
            </span>
          </template>
          <a-menu-item key="res-collection">合集管理</a-menu-item>
          <a-menu-item key="res-video">视频资源</a-menu-item>
        </a-sub-menu>

        <a-menu-item key="front">
          <HomeOutlined />
          <span>返回前台</span>
        </a-menu-item>

      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="background: #fff; padding: 16px" />
      <a-layout-content style="margin: 0 16px">

       <RouterView/>
      </a-layout-content>
      <a-layout-footer style="text-align: center">
        后台管理 Made by Lament with ❤️
      </a-layout-footer>
    </a-layout>
  </a-layout>
  </div>
</template>
<script lang="ts" setup>

import { ref } from 'vue';
import { useRouter } from 'vue-router'
import type { MenuProps } from 'ant-design-vue'
const collapsed = ref<boolean>(false);
// const selectedKeys = ref<string[]>(['1']);
const router = useRouter()

const onClick: MenuProps['onClick'] = ({ key }) => {
  if (key === 'file-scan'){
    router.push('/z/admin/res/scan')
  }

  if (key === 'admin-user'){
    router.push('/z/admin/users/')
  }

  if (key == 'res-collection'){
    router.push('/z/admin/res/collection')
  }

  if (key == 'front'){
    router.push('/')
  }
}


</script>
<style scoped>

#omh-admin-layout .admin-logo-img {
  height: 50px;
  margin-left: 75px;
}

</style>
