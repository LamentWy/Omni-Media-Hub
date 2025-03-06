<template>
<h1>合集展示页面，左边播放器，右边资源列表</h1>
  <div class="omh-res-page">
  <a-row >
    <a-col :span="18">
      <template v-if="resPath != ''">
        <VideoJsPlayer :src="resPath" :type="resType" :play-list="items" :play-list-details="resList" :r-id="defaultRID"></VideoJsPlayer>
      </template>

    </a-col>

  </a-row>
  </div>
</template>

<script setup lang="ts">
import VideoJsPlayer from '@/components/player/VideoJsPlayer.vue'
import { ref, type VueElement } from 'vue'
import { getResListByRCID, type ResItem } from '@/requests/admin/res.ts'
import { useRoute } from 'vue-router'
import type { ItemType } from 'ant-design-vue'

const resList = ref<ResItem[]>([]);
const resPath = ref<string>('')

const defaultCID = ref<string[]>([''])
const defaultRID = ref<string[]>([''])

const resType = ref<string>('video/mp4')
const route = useRoute();
const rcId:number = parseInt(<string>route.params.rcId);

function getItem(
  label: VueElement | string,
  key: string,
  icon?: any,
  children?: ItemType[],
  type?: 'group',
): ItemType {
  return {
    key,
    icon,
    children,
    label,
    type,
  } as ItemType;
}

const items = ref<ItemType[]>([]);

const createItems = () => {

  const subItems: ItemType[] = [];

   resList.value.forEach((i)=>{
    subItems.push(getItem(i.title,i.id.toString()))
  })
  console.log('subItems:',subItems)
  return [getItem('合集：'+resList.value[0].cName,resList.value[0].cId.toString(),null,subItems,'group')]
}



getResListByRCID(rcId)
  .then((res)=>{
    console.log("res.data",res.data)
    resList.value = res.data;
    console.log("resList",resList)
    items.value = createItems();
    resPath.value = '/@fs'+resList.value[0].filePath
    defaultCID.value[0] = resList.value[0].cId.toString()
    defaultRID.value[0] = resList.value[0].id.toString()
  })

</script>


<style scoped>

</style>
