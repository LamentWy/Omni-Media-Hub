import { useUserStore } from '@/stores/UserStore'
import omniAxios from '@/requests/global-request-config'
export const getCurrentUserInfo = async () => {
  return omniAxios.get('/z/cur').then(function (response) {
    const userStore = useUserStore()
    userStore.setUserInfo(response.data)
  })
}
