<template>
  <div id="omh-admin-collection">
    <h1>合集管理</h1>
    <p>可在此处修改合集名称、描述、可见性。</p>
    <a-divider/>
  <a-table bordered :data-source="dataSource" :columns="columns">
    <template #bodyCell="{ column, text, record }">

      <template v-if="column.dataIndex === 'name'">
        <div class="editable-cell">
          <div v-if="editableData[record.id]" class="editable-cell-input-wrapper">
            <a-input v-model:value="editableData[record.id].name" @pressEnter="save(record.id)" />
            <check-outlined class="editable-cell-icon-check" @click="save(record.id)" />
          </div>
          <div v-else class="editable-cell-text-wrapper">
            {{ text || ' ' }}
            <edit-outlined class="editable-cell-icon" @click="edit(record.id)" />
          </div>
        </div>
      </template>

      <template v-if="column.dataIndex === 'desc'">
        <div class="editable-cell">
          <div v-if="editableData[record.id]" class="editable-cell-input-wrapper">
            <a-input v-model:value="editableData[record.id].desc" @pressEnter="save(record.id)" />
            <check-outlined class="editable-cell-icon-check" @click="save(record.id)" />
          </div>
          <div v-else class="editable-cell-text-wrapper">
            {{ text || ' ' }}
            <edit-outlined class="editable-cell-icon" @click="edit(record.id)" />
          </div>
        </div>
      </template>

      <template v-else-if="column.dataIndex === 'visibility'">
        <div class="editable-cell">
          <div v-if="editableData[record.id]" class="editable-cell-input-wrapper">
            <a-input v-model:value="editableData[record.id].visibility" @pressEnter="save(record.id)" />
            <check-outlined class="editable-cell-icon-check" @click="save(record.id)" />
          </div>
          <div v-else class="editable-cell-text-wrapper">
            {{ text || ' ' }}
            <edit-outlined class="editable-cell-icon" @click="edit(record.id)" />
          </div>
        </div>
      </template>

      <template v-else-if="column.dataIndex === 'operation'">
        <a-popconfirm
          v-if="dataSource.length"
          title="确定删除此合集?"
          ok-text="确定"
          cancel-text="取消"
          @confirm="onDelete(record.id)"
        >
          <a>删除</a>
        </a-popconfirm>
      </template>

    </template>
  </a-table>
  </div>
</template>
<script lang="ts" setup>
import { reactive, ref } from 'vue';
import type { Ref, UnwrapRef } from 'vue';
import { CheckOutlined, EditOutlined } from '@ant-design/icons-vue';
import { cloneDeep } from 'lodash';
import {
  type CollectionDataItem,
  deleteResCollection,
  getResCollections,
  transToCollectionDataItem,
  transToRCParam,
  updateResCollection
} from '@/requests/admin/res.ts'
import { message } from 'ant-design-vue'


const columns = [
  {
    title: '合集 ID',
    dataIndex: 'id',
    width: '10%',
  },
  {
    title: '合集名称',
    dataIndex: 'name',
    width: '20%',
  },
  {
    title: '合集描述',
    dataIndex: 'desc',
  },
  {
    title: '创建者',
    dataIndex: 'creator',
    width: "10%"
  },
  {
    title: '是否可见',
    dataIndex: 'visibility',
    width: "10%"
  },
  {
    title: '操作',
    dataIndex: 'operation',
    width: "20%"
  },
];

const dataSource: Ref<CollectionDataItem[]> = ref([]);

getResCollections().then((res)=>{
  res.data.forEach((i:never)=>{dataSource.value.push(transToCollectionDataItem(i))})
})


const editableData: UnwrapRef<Record<number, CollectionDataItem>> = reactive({});


const edit = (id: number) => {
  editableData[id] = cloneDeep(dataSource.value.filter(item => id === item.id)[0]);
};

const save = (id: number) => {
  Object.assign(dataSource.value.filter(item => id === item.id)[0], editableData[id]);
  // doSave
  updateResCollection(transToRCParam(editableData[id]))
    .then(()=>{
      message.success({
        content: () => '更新成功!',
        duration: 2,
        style: {
          marginTop: '20vh',
        },
      })
    }).catch((error)=>{
    message.error({
      content: () => '更新失败! code:' + error.response.status,
      duration: 2,
      style: {
        marginTop: '20vh',
      },
    })
  })

  delete editableData[id];


};

const onDelete = (id: number) => {
  dataSource.value = dataSource.value.filter(item => item.id !== id);

  deleteResCollection(id)
    .then(()=>{
      message.success({
        content: () => '删除成功!',
        duration: 2,
        style: {
          marginTop: '20vh',
        },
      })
    }).catch((error)=>{
    message.error({
      content: () => '审查失败! code:' + error.response.status,
      duration: 2,
      style: {
        marginTop: '20vh',
      },
    })
  })

};

</script>

<style scoped>
.editable-cell {
  position: relative;
  .editable-cell-input-wrapper,
  .editable-cell-text-wrapper {
    padding-right: 24px;
  }

  .editable-cell-text-wrapper {
    padding: 5px 24px 5px 5px;
  }

  .editable-cell-icon,
  .editable-cell-icon-check {
    position: absolute;
    right: 0;
    width: 20px;
    cursor: pointer;
  }

  .editable-cell-icon {
    margin-top: 4px;
    display: none;
  }

  .editable-cell-icon-check {
    line-height: 28px;
  }

  .editable-cell-icon:hover,
  .editable-cell-icon-check:hover {
    color: #108ee9;
  }

  /* button style */
  .editable-add-btn {
    margin-bottom: 8px;
  }
}
.editable-cell:hover .editable-cell-icon {
  display: inline-block;
}
</style>
