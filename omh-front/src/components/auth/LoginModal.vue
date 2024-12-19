<template>
  <div id="omh-login-modal">
    <a-button type="primary" shape="round" @click="showModal">登录/注册</a-button>
    <a-modal
      class="login-modal"
      v-model:open="open"
      :footer="null"
      title="欢迎登录"
      :destroyOnClose="true"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
    >
      <LoginView v-model="open" />
    </a-modal>
  </div>
</template>
<script lang="ts" setup>
import LoginView from '@/views/auth/LoginView.vue'
import { ref } from 'vue'

const open = ref<boolean>(false)
const confirmLoading = ref<boolean>(false)

const showModal = () => {
  open.value = true
}

const handleOk = () => {
  confirmLoading.value = true
  setTimeout(() => {
    open.value = false
    confirmLoading.value = false
  }, 2000)
}
</script>
