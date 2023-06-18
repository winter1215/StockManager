import request from '@/utils/request'

// 查询库存列表
export function listLog(query) {
  return request({
    url: '/stock/log/list',
    method: 'get',
    params: query
  })
}
// 查询 children 列表
export function getChildren(childPage) {
  return request({
    url: '/stock/log/list',
    method: 'get',
    params: childPage
  })
}

// 查询库存详细
export function getLog(id) {
  return request({
    url: '/stock/log/' + id,
    method: 'get'
  })
}

// 新增库存
export function addLog(data) {
  return request({
    url: '/stock/log',
    method: 'post',
    data: data
  })
}

// 修改库存
export function updateLog(data) {
  return request({
    url: '/stock/log',
    method: 'put',
    data: data
  })
}

// 删除库存
export function delLog(id) {
  return request({
    url: '/stock/log/' + id,
    method: 'delete'
  })
}

// 通过日志类型查询日志
export function listByLogType(logType) {
  const params = { logType: logType }
  return request({
    url: '/stock/log/list',
    method: 'get',
    params: params
  })

}
// 查询待处理的日志
export function listDraft() {
  const params = { state: 1 }
  return request({
    url: '/stock/log/list',
    method: 'get',
    params: params
  })
}
//  抛弃
export function removeDraft(id) {
  return request({
    url: `/stock/log/removeDraft/${id}`,
    method: 'delete',
  })
}

// 确认
export function useDraft(id) {
  return request({
    url: `/stock/log/useDraft/${id}`,
    method: 'post',
  })
}


