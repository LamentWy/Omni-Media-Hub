import omniAxios from '@/requests/global-request-config'
import type { LoginFormState } from '@/views/auth/LoginView.vue'
import type { RegisterFormState } from '@/views/auth/RegisterView.vue'
// declare params type
interface RegisterParams {
  loginName: string
  password: string
  // optional
  nickName: string
  activated: boolean
}
const defaultRegisterParams: RegisterParams = {
  loginName: '',
  nickName: '',
  password: '',
  activated: true,
}

interface LoginParams {
  userName: string
  password: string
  rememberMe: boolean
}
const defaultLoginParams: LoginParams = {
  userName: '',
  password: '',
  rememberMe: false,
}

// trasfer

export const transToRegisterParams = (params: RegisterFormState) => {
  defaultRegisterParams.loginName = params.userName
  if (params.nickName === '') {
    defaultRegisterParams.nickName = '默认昵称'
  } else {
    defaultRegisterParams.nickName = params.nickName
  }
  defaultRegisterParams.password = params.password

  return defaultRegisterParams
}

export const transToLoginParams = (params: LoginFormState) => {
  defaultLoginParams.userName = params.username
  defaultLoginParams.password = params.password
  defaultLoginParams.rememberMe = params.remember
  return defaultLoginParams
}
// register
export const userRegister = async (params: RegisterParams) => {
  // todo encode password
  // backend should decode the password first

  // 新注册账户默认为激活状态
  params.activated = true
  return omniAxios.request({
    url: '/z/register',
    method: 'post',
    data: params,
  })
}

/**
 * 用户登录:
 *
 * 登录获取 jwtToken，并入 localStorage/sessionStorage
 */
export const userLogin = async (params: LoginParams) => {
  // 指定 content-type: 自定义一个 header 并添加到 request 中即可。
  // headers: { "Content-Type": "application/json" },
  const res = omniAxios

    .request({
      url: '/z/login',
      method: 'post',
      data: params,
    })
    .then(async (response) => {
      const jwt = response.data.token
      if (params.rememberMe) {
        localStorage.setItem('OMH-Token', jwt)
        sessionStorage.removeItem('OMH-Token')
      } else {
        sessionStorage.setItem('OMH-Token', jwt)
        localStorage.removeItem('OMH-Token')
      }
    })

  return res
}
