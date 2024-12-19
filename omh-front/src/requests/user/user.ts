import { useUserStore } from '@/stores/UserStore'
import omniAxios from '@/requests/global-request-config'
export const getCurrentUserInfo = async () => {
  return omniAxios.get('/z/cur').then(function (response) {
    const userStore = useUserStore()
    userStore.setUserInfo(response.data)
    // todo 实现获取用户信息失败的处理逻辑 : 清空token 等等，其实就是 log out 的逻辑。
  })
}
