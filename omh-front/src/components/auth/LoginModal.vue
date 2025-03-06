<template>
  <div id="omh-login-modal">

    <a-modal
      class="login-modal"
      v-model:open="open"
      :footer="null"
      title="欢迎登录"
      :destroyOnClose="true"
      :confirm-loading="confirmLoading"
    >
      <LoginView v-model="open" />
    </a-modal>
  </div>
</template>
<script lang="ts" setup>
import LoginView from '@/views/auth/LoginView.vue'
import { ref } from 'vue'

// const open = ref<boolean>(false)
const open = defineModel()
const confirmLoading = ref<boolean>(false)

const show = () => {
  open.value = true;
}

</script>
