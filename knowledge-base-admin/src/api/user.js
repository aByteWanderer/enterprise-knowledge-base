import request from '@/utils/request'

export function getUserList(params) {
  return request({
    url: '/users',
    method: 'get',
    params
  })
}

export function getUser(id) {
  return request({
    url: `/users/${id}`,
    method: 'get'
  })
}

export function createUser(data) {
  return request({
    url: '/users',
    method: 'post',
    data
  })
}

export function updateUser(id, data) {
  return request({
    url: `/users/${id}`,
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/users/${id}`,
    method: 'delete'
  })
}

export function resetPassword(id, newPassword) {
  return request({
    url: `/users/${id}/password`,
    method: 'put',
    params: { newPassword }
  })
}

export function assignRoles(id, roleIds) {
  return request({
    url: `/users/${id}/roles`,
    method: 'put',
    data: roleIds
  })
}

export function updateUserStatus(id, status) {
  return request({
    url: `/users/${id}/status`,
    method: 'put',
    params: { status }
  })
}
