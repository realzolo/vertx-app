import type { Directive, DirectiveBinding } from 'vue'
import { useUserStore } from '@/stores'

/**
 * @desc v-hasRole 角色权限处理
 * @desc 使用 v-hasRole="['admin', 'user]"
 */
function checkRole(el: HTMLElement, binding: DirectiveBinding) {
  const userStore = useUserStore()
  const { value } = binding
  const superAdmin = 'super'
  if (value && Array.isArray(value) && value.length) {
    const roleValues: string[] = value
    const hasRole = userStore.roles.some((role) => {
      return superAdmin === role.value || roleValues.includes(role.value)
    })
    if (!hasRole) {
      el.parentNode && el.parentNode.removeChild(el)
    }
  } else {
    throw new Error(`need role! Like v-hasRole="['admin','user']"`)
  }
}

const directive: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    checkRole(el, binding)
  },
  updated(el: HTMLElement, binding: DirectiveBinding) {
    checkRole(el, binding)
  },
}

export default directive
