<template>
  <div id="omh-login-form">
    <a-divider style="height: 2px; background-color: #009688" />
    <h2>注册登录</h2>
    <a-form
      :model="loginFormState"
      name="login-a-form"
      class="login-form"
      layout="vertical"
      style="margin: 40px auto"
      @finish="submitLoginForm"
    >
      <a-form-item
        label="输入账号 :"
        name="username"
        :rules="[{ required: true, message: '请输入登录账号' }]"
      >
        <a-input v-model:value="loginFormState.username" />
      </a-form-item>

      <a-form-item
        label="输入密码 :"
        name="password"
        :rules="[
          { required: true, message: '请输入密码' },
          { min: 5, message: '密码最少输入 5 位' },
        ]"
      >
        <a-input-password maxLength="20" v-model:value="loginFormState.password" />
      </a-form-item>

      <a-form-item name="remember">
        <a-checkbox class="remember" v-model:checked="loginFormState.remember">记住账号</a-checkbox>
      </a-form-item>

      <a-form-item>
        <a-button
          :disabled="disabled"
          type="primary"
          shape="round"
          html-type="submit"
          class="login-form-button"
        >
          登录
        </a-button>
      </a-form-item>
      <a-form-item>
        <!--   重置密码     -->

        <ResetPasswordModal class="login-form-reset" />

        <!--   注册账号     -->
        <RegisterModal />
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import RegisterModal from '@/components/auth/RegisterModal.vue'
import { transToLoginParams, userLogin } from '@/requests/auth/auth'
import { getCurrentUserInfo } from '@/requests/user/user'
import { reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import ResetPasswordModal from '@/components/auth/ResetPasswordModal.vue'
export interface LoginFormState {
  username: string
  password: string
  remember: boolean
}
const loginFormState = reactive<LoginFormState>({
  username: '',
  password: '',
  remember: true,
})

const router = useRouter()
const open = defineModel()

const submitLoginForm = (loginFormState: LoginFormState) => {
  const loginParams = transToLoginParams(loginFormState)
  // 要扁平，不要嵌套 https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Guide/Using_promises
  userLogin(loginParams)
    .then(getCurrentUserInfo)
    .then(() => {
      open.value = false
      // login success -> goto home page.
      message.success({
        content: () => '登录成功！ 正在跳转首页...',
        duration: 2,
        class: ' login-success-msg',
        style: {
          marginTop: '20vh',
        },
      })
      router.push('/')
    })
    .catch((error) => {
      message.error({
        content: () => '登录失败，请检查账号密码.  code: '+error.response.status,
        duration: 2,
        class: 'login-failed-msg',
        style: { marginTop: '15vh', }
      })
    })

}

// 计算登录按钮状态
const disabled = computed(() => {
  return !(loginFormState.username && loginFormState.password)
})
</script>
<style scoped>
#omh-login-form h2 {
  text-align: center;
}
#omh-login-form .login-form {
  max-width: 320px;
}
#omh-login-form .remember {
  float: right;
}
#omh-login-form  .login-form-reset {
  float: right;
}
#omh-login-form .login-form-button {
  width: 100%;
}
</style>
