

<template>
  <div id="omh-admin-res-scan" >
    <h1>资源扫描页</h1>
    <a-divider/>
      <h2> 1. 文件扫描 </h2>
      <p>
        将本地资源库的根目录复制粘贴到输入框中，然后点击“扫描本地资源”按钮即可自动扫描该目录下的所有资源。<br>
        不填则默认扫描“用户/下载”目录，以 MacOS 为例就是: <code>/Users/{userName}/Downloads</code>。
      </p>
      <a-input v-model:value="path" style="width: 380px" placeholder="输入资源根目录的绝对地址" ></a-input>
      <a-button type="primary" :loading="isLoading" @click="doScan">
        扫描本地资源
      </a-button>

    <a-divider/>

    <h2> 2. 默认合集一键可见 </h2>
    <p>
      系统会在扫描后为资源自动创建的“默认合集”，所有“默认合集”均默认为前台不可见，以避免“默认合集”所包含的的私密资源公开。<br>
      如果资源库中的所有资源全部老少皆宜，所有家庭成员均可访问，则可以使用该功能一键公开。否则请在“资源管理”中手动设置哪些“合集”或“资源”可见。<br>
      当然你也可以先全部可见，然后手动设置部分为不可见。<br>
    </p>
    <a-button type="primary" > 资源全部可见 </a-button>
    <a-divider/>

  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { message } from 'ant-design-vue'
import { type PathParam, resScan } from '@/requests/admin/res.ts'



const isLoading = ref<boolean>(false)
const path = ref<string>("")


const doScan = () => {
  isLoading.value = true;
  message.info({
    content: () => '异步扫描中...',
    class: 'scan-info-msg',
    duration: 1,
    style: {
      marginTop: '20vh',
    },
  })
  // 发起请求 直接 input 传递用户指定目录
  const param: PathParam = {
    rootPath: path.value
  }
  resScan(param)
    .then((response) => {
      isLoading.value = false;

      const successContent: string =
        `扫描完成:
        ${ (response.data.collectionCount > 0) ? `自动生成 ${response.data.collectionCount} 个默认合集.\n`:''}
        ${ (response.data.videoCount > 0) ? `发现 ${response.data.videoCount} 个视频资源.\n`:'' }
        ${ (response.data.audioCount > 0) ? `发现 ${response.data.audioCount} 个音频资源\n`:'' }
        ${ (response.data.bookCount > 0) ? `发现 ${response.data.bookCount} 个音频资源\n`:'' }
        ${ (response.data.picturesCount > 0) ? `发现 ${response.data.picturesCount} 个图像资源\n`:'' }
        点击关闭该消息。
        `;

      message.success({
        content: () =>  successContent,
        class: 'scan-success-msg',
        duration: 0,
        style: {
          marginTop: '20vh',
        },
        onClick: () => {
          message.destroy()
        }
      })
    })
    .catch((error)=>{
      isLoading.value = false;
      console.log("error"+error)
      message.error({
        content: () => '扫描失败，请检查资源根目录是否为绝对路径，是否为目录.  根目录: '+path.value + ' | code:' + error.response.status,
        duration: 2,
        class: 'login-failed-msg',
        style: { marginTop: '15vh', }
      })
    })

}
</script>

<style scoped>

code{
  background-color: lightgray;
}


</style>
