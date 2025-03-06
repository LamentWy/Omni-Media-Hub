import axios from 'axios'

// global set

const omniAxios = axios.create({
  // backend
  baseURL: 'http://192.168.0.102:8080/',
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
        console.log('this branch : ' + token)
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

    const { data } = response
    console.log('success | code: ' + data)

    return response
  },
  function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    const status = error.response.status

    if (error.response) {
      if (status === 403 || status === 401) {
        //
        console.log('failed | code: ' + status + ' reason: ' + error.response.statusText)
      }
      if (status >= 500) {
        console.log('Server Error!')
        // todo jump to sever-error-page
      }
    }

    return Promise.reject(error)
  },
)
// export
export default omniAxios
