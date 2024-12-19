import axios from 'axios'

// global set

const omniAxios = axios.create({
  // backend
  baseURL: 'http://localhost:8080/',
  timeout: 50000,
})

// request 拦截器
omniAxios.interceptors.request.use(
  function (config) {
    // 在发送请求之前做些什么
    // get token & set to header
    const token = localStorage.getItem('OMH-Token') || sessionStorage.getItem('OMH-Token')
    if (token) {
      // `Bearer ${token}` 中的 ` 叫反斜线，作用是创建字符串，from ES6
      if (!config.headers) {
        omniAxios.defaults.headers.common['Authorization'] = `Bearer ${token}`
      } else {
        config.headers.Authorization = `Bearer ${token}`
      }
    }
    return config
  },
  function (error) {
    // 对请求错误做些什么

    return Promise.reject(error)
  },
)
// response 拦截器
omniAxios.interceptors.response.use(
  function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    console.log(response)

    const { data } = response
    console.log(data)
    // Todo 根据自定义响应码制定通用跳转逻辑
    // 比如未登录时访问受限资源跳转登录页面等等

    return response
  },
  function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    const status = error.response.status

    if (error.response) {
      if (status === 403 || status === 401) {
        // 弹出错误信息
        console.log(status)
      }
      if (status >= 500) {
        console.log('Server Error!')
      }
    }

    return Promise.reject(error)
  },
)
// export
export default omniAxios
