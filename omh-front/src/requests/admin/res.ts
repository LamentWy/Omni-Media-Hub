import omniAxios from '@/requests/global-request-config.ts'

export interface ResItem {
  "id": number,
  "title": string,
  "fileName": string,
  "filePath": string,
  "description": string,
  "cover": string,
  "cId": number,
  "cName": string,
  "cDesc": string,
}

export interface CollectionDataItem {
  id: number;
  name: string;
  desc: string;
  creator: string;
  visibility: string;
}

export const transToCollectionDataItem = (param:any) => {
  const cItem: CollectionDataItem = {
    id: param.id,
    name: param.name,
    desc: param.description,
    creator: param.createdBy,
    visibility: param.visible === 0 ? '否' : '是',
  }
  return cItem;
}

export interface PathParam {
  rootPath: string
}

interface RCParam {
  id: number;
  name: string;
  description: string;
  visible: number;
}

const defaultRCParam : RCParam = { description: '', id: 0, name: '', visible: 0 }

export const transToRCParam = (param: CollectionDataItem) => {
  defaultRCParam.id = param.id;
  defaultRCParam.name = param.name;
  defaultRCParam.description = param.desc;
  defaultRCParam.visible = param.visibility === '是'? 1 : 0;
  return defaultRCParam;
}

// 资源发现
export const resScan = async (param: PathParam) => {
  // 使用 axios.request 发送 get 请求时， data 不会发送给后端
return omniAxios.request({
  url: '/z/admin/res/scan',
  method: 'post',
  data: param,
})
}

// 获取所有合集信息
export const getResCollections = async ()=>{
  return omniAxios.request({
    url: '/z/admin/res/all',
    method: 'get',
  })
}

// 获取所有可见合集信息
export const getVisibleResCollections = async ()=>{
  return omniAxios.request({
    url: '/z/res/rc',
    method: 'get',
  })
}

// 更新合集信息
export const updateResCollection = async (param: RCParam)=>{
  return omniAxios.request({
    url: '/z/admin/res',
    method: 'post',
    data: param,
  })
}

// 删除合集 (只删合集)
export const deleteResCollection = async (rcId: number)=>{
  return omniAxios.request({
    url: '/z/admin/res/'+rcId,
    method: 'delete',
  })
}

// 根据 ID 获取合集内容
export const getResListByRCID = async (rcId: number) => {
  return omniAxios.request({
    url: '/z/res/rc/'+rcId,
    method: 'get',
  })
}
