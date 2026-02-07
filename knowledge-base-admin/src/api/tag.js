import request from '@/utils/request'

export function getTagList() {
  return request({
    url: '/tags',
    method: 'get'
  })
}

export function getHotTags(limit = 10) {
  return request({
    url: '/tags/hot',
    method: 'get',
    params: { limit }
  })
}

export function getTag(id) {
  return request({
    url: `/tags/${id}`,
    method: 'get'
  })
}

export function createTag(data) {
  return request({
    url: '/tags',
    method: 'post',
    data
  })
}

export function updateTag(id, data) {
  return request({
    url: `/tags/${id}`,
    method: 'put',
    data
  })
}

export function deleteTag(id) {
  return request({
    url: `/tags/${id}`,
    method: 'delete'
  })
}
