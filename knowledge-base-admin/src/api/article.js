import request from '@/utils/request'

export function getArticleList(params) {
  return request({
    url: '/articles',
    method: 'get',
    params
  })
}

export function getArticle(id) {
  return request({
    url: `/articles/${id}`,
    method: 'get'
  })
}

export function createArticle(data) {
  return request({
    url: '/articles',
    method: 'post',
    data
  })
}

export function updateArticle(id, data) {
  return request({
    url: `/articles/${id}`,
    method: 'put',
    data
  })
}

export function deleteArticle(id) {
  return request({
    url: `/articles/${id}`,
    method: 'delete'
  })
}

export function submitArticle(id) {
  return request({
    url: `/articles/${id}/submit`,
    method: 'post'
  })
}

export function auditArticle(id, status, remark) {
  return request({
    url: `/articles/${id}/audit`,
    method: 'post',
    params: { status, remark }
  })
}

export function getPendingArticles() {
  return request({
    url: '/articles/pending',
    method: 'get'
  })
}

export function uploadImage(data) {
  return request({
    url: '/articles/upload/image',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function searchArticles(params) {
  return request({
    url: '/articles/search',
    method: 'get',
    params
  })
}
