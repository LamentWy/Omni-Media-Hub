<template>
  <div id="omh-video-player">
    <a-row>
      <a-col :span="20">
        <video ref="videoPlayer" class="video-js vjs-big-play-centered">
          <source :src="resPath" :type="resType" />
        </video>
      </a-col>
      <a-col :span="4">
        <div class="play-list-of-rc">
          <a-menu
            class="play-list-menu"
            v-model:selected-keys="selectedKeys"
            v-model:open-keys="openKeys"
            :items ="items"
            mode="inline"
            theme="dark"
            @click="handlePLClick"
          >

          </a-menu>
        </div>
      </a-col>
    </a-row>

  </div>

</template>

<script lang="ts" setup>
import videojs from 'video.js'
import type { VideoJsPlayer, VideoJsPlayerOptions, } from 'video.js'
import { onBeforeUnmount, onMounted, ref } from 'vue'
import 'video.js/dist/video-js.min.css'
import zh from 'video.js/dist/lang/zh-CN.json'
import toWebVTT from 'srt-webvtt'
import type { MenuProps } from 'ant-design-vue'
import type { ResItem } from '@/requests/admin/res.ts'
type TextTrackOptions = videojs.TextTrackOptions


// play-list
const selectedKeys = defineModel('rId')
const openKeys = defineModel('cId') //cid
const items = defineModel('playList')
const playListDetails = defineModel('playListDetails',{type:Array<ResItem>,default: []})

const findPathById = (id:number) => {
  let fp:string = ''
  playListDetails.value.forEach((i:ResItem)=>{
    if (i.id == id){
    console.log('filePath:',i.filePath)
      fp = i.filePath
    }
  })
  return fp;
}

const handlePLClick: MenuProps['onClick'] = e => {
  console.log('click', e);
  console.log('plDetails:',playListDetails)

  let fp: string;
  if (typeof e.key === 'string') {
     fp = findPathById(parseInt(e.key))
  }else {
    fp = findPathById(e.key)
  }
  resPath.value = '/@fs' + fp
  selectedKeys.value = [e.key]

  const source = {
    src: resPath.value,
    type: resType.value
  }

  player.pause()
  console.log('source',source)
  player.src(source)
  player.load()
};


// CustomButton for add subtitle file
const Button = videojs.getComponent('Button');
class CustomButton extends Button {
  constructor(player: VideoJsPlayer, options:VideoJsPlayerOptions) {
    super(player, options);
    this.controlText('添加字幕');
  }
  buildCSSClass() {
    return `omh-vp-sb ${super.buildCSSClass()}`;
  }
  async handleClick() {
    // 自定义按钮点击事件处理
    // try {
    //   const subFile = null;
    //   const fileName:string = '字幕文件的文件名'
    //   let subVtt;
    //   if (fileName.endsWith('.srt')){
    //     subVtt = await toWebVTT(subFile)
    //   }else {
    //     const content = await subFile.text();
    //     subVtt = URL.createObjectURL(new Blob([content], { type: 'text/vtt' }));
    //   }
    //   const textOptions:TextTrackOptions = {
    //     kind: 'subtitles',
    //     srclang: 'zh-CN',
    //     label: fileName,
    //     src: subVtt,
    //   }
    //
    //   player.addRemoteTextTrack(textOptions, true);
    //
    // } catch (e) {
    //   console.log('error:', e)
    // }
  }
}

videojs.registerComponent('CustomButton', CustomButton);
// models
const resPath = defineModel('src',{type:String});
const resType = defineModel('type', {type:String});


// player options
const options : VideoJsPlayerOptions = {
  preload: 'none',
  controls: true,
  autoplay: false,
  responsive: true,
  fluid: true,
  language: 'zh-CN',
  languages: {
    'zh-CN': zh
  },
  controlBar: {
    subsCapsButton: true
  },
  html5: {
    nativeTextTracks: false
  }
}

const pickerOpts = {
  types: [
    {
      description: "subtitles",
      accept: {
        "text/vtt": [".vtt"],
        "text/plain": [".srt"],
        "application/x-subrip": [".srt"],
        "application/octet-stream": [".srt"],
      },
    },
  ],
  excludeAcceptAllOption: true,
  multiple: false,
};




let player : VideoJsPlayer;
const videoPlayer = ref<string>('vPlayer')

onMounted(()=> {
   player = videojs(videoPlayer.value, options, () => {
    console.log('player: ',player)
    console.log('player src: ', player.src())

    player.controlBar.addChild('CustomButton')
  });

})

onBeforeUnmount(() => {
  if (player) {
    player.dispose();
  }
})
</script>

<style >
/* 修改 color 可以看到一个白框，不过 vjs 本来也没几个图标，懒得查了 暂且透明隐藏掉 */
.omh-vp-sb .vjs-icon-placeholder::before{
  content: '\f07c'; /* */
  font-size: 24px;
  color: transparent;
}

/* 手动下载了 icon ，用按钮背景的方式塞进去了，勉强能看，前端真是好烦 */
.video-js .omh-vp-sb {
  background-image: url("@/assets/player/folder-open-regular.svg") !important;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
  width: 18px; /* SVG的宽度 */
  height: 18px; /* SVG的高度 */
  margin: auto 10px;
}

.play-list-menu{
  min-width: 120px;
  margin-left: 20px;
}

</style>
