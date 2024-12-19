<template>
  <div id="omh-register-modal">
    <a-typography-link type="primary" @click="showModal">注册账号</a-typography-link>
    <a-modal
      class="register-modal"
      width="640px"
      v-model:open="registerOpen"
      :destroyOnClose="true"
      title="欢迎注册"
      :footer="null"
    >
      <RegisterView v-model="registerOpen" />
    </a-modal>
  </div>
</template>
<script lang="ts" setup>
import RegisterView from '@/views/auth/RegisterView.vue'
import { ref } from 'vue'
const registerOpen = ref<boolean>(false)

const showModal = () => {
  registerOpen.value = true
}
</script>

<style scoped></style>
