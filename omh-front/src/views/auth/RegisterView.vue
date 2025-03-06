<template>
  <div id="omh-register-page">
    <a-divider style="height: 2px; background-color: #009688" />
    <h2>注册页面</h2>

    <a-form
      class="register-form"
      ref="formRef"
      name="register-validation"
      layout="vertical"
      :model="registerFormState"
      :rules="rules"
      @finish="submitRegisterForm"
      @validate="handleValidate"
      @finishFailed="handleFinishFailed"
    >
      <a-divider class="register-divider" style="border-color: #009688" dashed />
      <a-form-item label="输入账号 :" name="userName">
        <a-input
          v-model:value="registerFormState.userName"
          placeholder="账号仅用于登录，简单好记即可，本系统不连外网"
        />
      </a-form-item>
      <a-form-item has-feedback label="输入密码 :" name="password">
        <a-input
          v-model:value="registerFormState.password"
          type="password"
          autocomplete="off"
          placeholder="输入密码，简单好记即可，本系统不连外网"
        />
      </a-form-item>
      <a-form-item has-feedback label="确认密码 :" name="checkPass">
        <a-input
          v-model:value="registerFormState.checkPass"
          type="password"
          autocomplete="off"
          placeholder="再次输入密码，确保二者一致。"
        />
      </a-form-item>

      <a-form-item label="输入昵称 :">
        <a-input
          v-model:value="registerFormState.nickName"
          placeholder="起个昵称，比如 '我妈超美'"
        />
      </a-form-item>
      <!-- Todos: 头像啥的 -->
      <a-divider class="register-divider" style="border-color: #009688" dashed />

      <a-form-item :wrapper-col="{ span: 12, offset: 6 }">
        <a-button
          class="register-submit-button"
          :disabled="disabled"
          type="primary"
          html-type="submit"
          >注册</a-button
        >
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import { computed, reactive, ref } from 'vue'
import type { Rule } from 'ant-design-vue/es/form'
import type { FormInstance } from 'ant-design-vue'
import { transToRegisterParams, userRegister } from '@/requests/auth/auth'
import { message } from 'ant-design-vue'

export interface RegisterFormState {
  userName: string
  nickName: string
  password: string
  checkPass: string
}
const formRef = ref<FormInstance>()
const registerFormState = reactive<RegisterFormState>({
  userName: '',
  nickName: '',
  password: '',
  checkPass: '',
})

const disabled = computed(() => {
  return !(registerFormState.userName && registerFormState.password && registerFormState.checkPass)
})

const validatePass = async (_rule: Rule, value: string) => {
  if (value === '') {
    return Promise.reject('请输入密码')
  } else {
    if (registerFormState.checkPass !== '') {
      formRef.value?.validateFields('checkPass')
    }
    return Promise.resolve()
  }
}
const validatePass2 = async (_rule: Rule, value: string) => {
  if (value === '') {
    return Promise.reject('再次输入密码')
  } else if (value !== registerFormState.password) {
    return Promise.reject('两次输入的密码不一致')
  } else {
    return Promise.resolve()
  }
}

const rules: Record<string, Rule[]> = {
  userName: [{ required: true, message: '请输入登录账号' }],
  password: [
    { required: true, validator: validatePass, trigger: 'change' },
    { min: 5, message: '密码最少输入 5 位' },
  ],
  checkPass: [{ required: true, validator: validatePass2, trigger: 'change' }],
}

const registerOpen = defineModel()

const submitRegisterForm = () => {
  const registerParams = transToRegisterParams(registerFormState)

  userRegister(registerParams)
    .then(() => {
      registerOpen.value = false
      message.success({
        content: () => '注册成功！',
        duration: 2,
        class: ' register-success-msg',
        style: {
          marginTop: '20vh',
        },
      })
    })
    .catch((errors: unknown) => {
      // TODO 调整接口错误信息,包括后端
      message.error({
        content: () => '注册失败!',
        style: {
          marginTop: '20vh',
        },
      })
      console.log('Register Errors:' + errors)
    })
}
const handleFinishFailed = (errors: unknown) => {
  console.log(errors)
}

const handleValidate = (...args: unknown[]) => {
  console.log(args)
}
</script>

<style scoped>
#omh-register-page h2 {
  text-align: center;
}

#omh-register-page .register-form {
  max-width: 380px;
  margin: 0 auto;
}

#omh-register-page .register-divider {
  margin: 40px auto;
}

#omh-register-page .register-submit-button {
  width: 100%;
}
</style>
