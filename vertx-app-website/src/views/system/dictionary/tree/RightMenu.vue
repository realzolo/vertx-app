<template>
  <a-menu class="right-menu">
    <a-menu-item
      v-permission="['system:dictionary:update']"
      :disabled="data.builtin && data.type === 1"
      title="修改"
      @click="onClick('update')"
    >
      <span>修改</span>
    </a-menu-item>
    <a-menu-item
      v-permission="['system:dictionary:delete']"
      class="danger"
      :disabled="data.builtin"
      :title="data.builtin ? '系统内置数据不能删除' : '删除'"
      @click="onClick('delete')"
    >
      <span>删除</span>
    </a-menu-item>
  </a-menu>
</template>

<script setup lang="ts">
import type { DictResp } from '@/apis/system'

interface Props {
  data: DictResp
}

const props = withDefaults(defineProps<Props>(), {})

const emit = defineEmits<{
  (e: 'on-menu-item-click', mode: string, data: DictResp): void
}>()

// 点击菜单项
const onClick = (mode: string) => {
  emit('on-menu-item-click', mode, props.data)
}
</script>

<style scoped lang="scss">
:deep(.arco-menu-inner) {
  padding: 4px;

  .arco-menu-item {
    height: 34px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .danger {
    color: rgb(var(--danger-6));
  }

  .danger.arco-menu-disabled {
    color: var(--color-danger-light-3);
  }
}

.right-menu {
  width: 120px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  border: 1px solid var(--color-border-2);
  box-sizing: border-box;

  .arrow-icon {
    margin-right: 0;
  }
}
</style>
