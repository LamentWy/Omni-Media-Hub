<template>
  <div id="omh-video-index">
    <h1>This is Video Page</h1>
    <div class="omh-video-carousel">
      <a-carousel arrows>
        <template #prevArrow>
          <div class="custom-slick-arrow" style="left: 10px; z-index: 1">
            <left-circle-outlined />
          </div>
        </template>
        <template #nextArrow>
          <div class="custom-slick-arrow" style="right: 10px">
            <right-circle-outlined />
          </div>
        </template>
        <div><h3>后续更换为图片/海报</h3></div>
        <div><h3>2</h3></div>
        <div><h3>3</h3></div>
        <div><h3>4</h3></div>
      </a-carousel>
    </div>

    <div class="default-rc-list">
      <a-row :wrap="true" >
      <template v-for="rc in rcs" :key="rc.id">
        <a-col :span="6">
          <a-card class="card"  :bordered="true" :loading="loading">
            <a-card-meta :title=rc.name  @Click="handleClick(rc.id)"></a-card-meta>
            <p> {{ rc.desc }} </p>
          </a-card>
        </a-col>
      </template>
      </a-row>
    </div>


  </div>
</template>

<script lang="ts" setup>

import { type Ref, ref } from 'vue'
import {
  type CollectionDataItem,
  getVisibleResCollections,
  transToCollectionDataItem
} from '@/requests/admin/res.ts'
import { useRouter } from 'vue-router'

const router = useRouter();
const rcs:Ref<CollectionDataItem[]> = ref([]);
const loading = ref<boolean>(true);

getVisibleResCollections().then((res)=>{
  res.data.forEach((i:any)=>{
    rcs.value.push(transToCollectionDataItem(i));
  })
  loading.value=false;
})


// 打开合集
const handleClick = (rcId:number)=>{
  // 跳转合集详情/播放页
  router.push({name:'rcDetailPage', params: {rcId}})
}

</script>

<style scoped>

.card {
  width: 240px;
}

.omh-video-carousel{
  margin-bottom: 20px;
}

:deep(.slick-slide) {
  text-align: center;
  height: 160px;
  line-height: 160px;
  background: #364d79;
  overflow: hidden;
}

:deep(.slick-arrow.custom-slick-arrow) {
  width: 25px;
  height: 25px;
  font-size: 25px;
  color: #fff;
  background-color: rgba(31, 45, 61, 0.11);
  transition: ease all 0.3s;
  opacity: 0.3;
  z-index: 1;
}
:deep(.slick-arrow.custom-slick-arrow:before) {
  display: none;
}
:deep(.slick-arrow.custom-slick-arrow:hover) {
  color: #fff;
  opacity: 0.5;
}

:deep(.slick-slide h3) {
  color: #fff;
}
</style>
