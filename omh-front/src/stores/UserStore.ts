/* eslint-disable @typescript-eslint/no-explicit-any */
import { defineStore } from 'pinia'

// UserStore 用来存储用户信息，比如 用户 ID，登录名，昵称，用户角色等等。
// jwt token 不应该存储于 Store，应该通过 localStorage/sessionStorage 存储。

export interface UserStatus {
  // signIn: boolean;
  userDto: any
  authenticated: boolean
}

export const defaultUserStatus: UserStatus = {
  userDto: null,
  authenticated: false,
}

// try Option Store style
// Using a Option Object as param for defineStore method which has state、actions and getters.
// useXXX 为组合式函数命名的约定格式
export const useUserStore = defineStore('userStore', {
  state: (): UserStatus => ({ ...defaultUserStatus }),
  getters: {
    userInfo: (state) => state.userDto,
  },
  actions: {
    setUserInfo(userInfoParam: any) {
      this.userDto = userInfoParam
      this.authenticated = true
    },
    logout() {
      this.userDto = null
      this.authenticated = false
    },
  },
  // pinia persist plugin
  // set true, to avoid refresh lost state problem.
  persist: true,
})
