<template>
  <div id ="omh-reset-pwd-modal">
    <a-typography-link type="primary" @click="showModal" >重置密码</a-typography-link>
    <a-modal v-model:open="open"
             title="重置密码"
             :confirm-loading="confirmLoading"
             @ok="handleOk"
             ok-text="确认"
             cancel-text="取消"
             width="450px"
             style="top: 20vh"
    >
      <a-divider style="height: 2px; background-color: #009688" />
      <p>{{ resetPwdDesc }}</p>
      <a-input class="reset-pwd-input" v-model:value="userName" placeholder="在这里输入需要重置密码的账号...">
        <template #prefix>
          <user-outlined  />
        </template>
      </a-input>
    </a-modal>
  </div>
</template>


<script lang="ts" setup>
import { ref } from 'vue';
import { resetPassword, transToResetPwdParams } from '@/requests/auth/auth.ts'
import { message } from 'ant-design-vue'

const resetPwdDesc = ref<string>(`输入账号并点击“确认”即可，该账号的密码将被重置为: 12345 `);
const open = ref<boolean>(false);
const confirmLoading = ref<boolean>(false);
const userName = ref<string>('');

const showModal = () => {
  open.value = true;
};

const handleOk = () => {
  resetPwdDesc.value = '正在重置，请稍候～';
  confirmLoading.value = true;
  const resetParam = transToResetPwdParams(userName.value)
  resetPassword(resetParam)
    .then( () => {
      open.value = false;
      confirmLoading.value = false;
      message.success({
        content: () => '重置成功！',
        duration: 1,
        style: { marginTop: '15vh', }
      })
    })
    .catch( (error) => {
      console.log('reset error:' + error.response.data);
      open.value = true;
      resetPwdDesc.value = '请检查帐号是否正确';
      confirmLoading.value = false;
      message.error({
        // todo 根据响应码分别提醒
        content: () => '重置失败，请检查账号是否正确后重试。 code: '+error.response.status,
        duration: 3,
        style: { marginTop: '15vh', }
      })
    })
};
</script>

<style scoped>


#omh-reset-pwd-modal .reset-pwd-input{
  left: 60px;
  max-width: 280px;
  margin: 20px auto;
}
</style>
