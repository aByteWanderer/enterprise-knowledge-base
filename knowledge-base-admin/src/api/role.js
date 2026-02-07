import request from '@/utils/request'

export function getRoleList() {
  return request({
    url: '/roles',
    method: 'get'
  })
}

export function getRole(id) {
  return request({
    url: `/roles/${id}`,
    method: 'get'
  })
}

export function createRole(data) {
  return request({
    url: '/roles',
    method: 'post',
    data
  })
}

export function updateRole(id, data) {
  return request({
    url: `/roles/${id}`,
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/roles/${id}`,
    method: 'delete'
  })
}

export function assignPermissions(id, permissionIds) {
  return request({
    url: `/roles/${id}/permissions`,
    method: 'put',
    data: permissionIds
  })
}

export function getRolePermissions(id) {
  return request({
    url: `/roles/${id}/permissions`,
    method: 'get'
  })
}

export function updateRoleStatus(id, status) {
  return request({
    url: `/roles/${id}/status`,
    method: 'put',
    params: { status }
  })
}
