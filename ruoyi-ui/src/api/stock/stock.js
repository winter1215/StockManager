import request from '@/utils/request'

// 查询库存列表
export function listStock(query) {
  return request({
    url: '/stock/stock/list',
    method: 'get',
    params: query
  })
}

// 查询库存详细
export function getStock(id) {
  return request({
    url: '/stock/stock/' + id,
    method: 'get'
  })
}
// 通过 profileCode 查询库存详细
export function getStockByCode(code) {
  return request({
    url: '/stock/stock/getByCode?profileCode=' + code,
    method: 'get'
  })
}

// 新增库存
export function addStock(data) {
  return request({
    url: '/stock/stock',
    method: 'post',
    data: data
  })
}

// 修改库存
export function updateStock(data) {
  return request({
    url: '/stock/stock',
    method: 'put',
    data: data
  })
}

// 删除库存
export function delStock(id) {
  return request({
    url: '/stock/stock/' + id,
    method: 'delete'
  })
}
