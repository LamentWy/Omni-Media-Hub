<template>
  <div id="omh-video-index">
    <h1>视频组件，嵌入其他组件 or 页面</h1>
    <!--<VidStackPlayer v-model:src="src"  v-model:type="type"/>-->
    <VideoJsPlayer v-model:src="src" v-model:type="type" />
  </div>
</template>

<script setup lang="ts">
import VideoJsPlayer from '@/components/player/VideoJsPlayer.vue'
import { ref } from 'vue'

const src = ref<string>("@fs/Users/lament/Downloads/正念谋杀/out2.mp4");
const type = ref<string>("video/mp4")


// const src = ref<string>("@fs/Users/lament/Downloads/正念谋杀/out2.mp4");
// const type = ref<string>("video/mp4")

</script>


<style scoped>

</style>
