<template>
  <GiPageLayout>
    <template #left>
      <DictTree @node-click="handleSelectDict" />
    </template>
    <a-row align="stretch" :gutter="14" class="h-full page_content">
      <a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" :xxl="24" flex="1" class="h-full overflow-hidden">
        <GiTable
          row-key="id"
          :data="dataList"
          :columns="columns"
          :loading="loading"
          :scroll="{ x: '100%', y: '100%', minWidth: 600 }"
          :pagination="pagination"
          :disabled-tools="['size']"
          :disabled-column-keys="['label']"
          @refresh="search"
        >
          <template #toolbar-left>
            <a-input-search v-model="queryForm.remark" placeholder="搜索标签/描述" allow-clear @search="search" />
            <a-button @click="reset">
              <template #icon><icon-refresh /></template>
              <template #default>重置</template>
            </a-button>
          </template>
          <template #toolbar-right>
            <a-button v-permission="['system:dictionary:create']" :disabled="builtin" type="primary" @click="onAdd">
              <template #icon><icon-plus /></template>
              <template #default>新增</template>
            </a-button>
            <a-button v-permission="['system:dictionary:clear-cache']" :disabled="builtin" type="outline" status="warning" @click="onClearCache">
              <template #icon><icon-delete /></template>
              <template #default>清除缓存</template>
            </a-button>
          </template>
          <template #name="{ record }">
            <a-tag :color="record.color">{{ record.name }}</a-tag>
          </template>
          <template #status="{ record }">
            <GiCellStatus :status="record.status" />
          </template>
          <template #action="{ record }">
            <a-space>
              <a-link v-permission="['system:dictionary:update']" :disabled="record.builtin" title="修改" @click="onUpdate(record)">修改</a-link>
              <a-link
                v-permission="['system:dictionary:delete']"
                :disabled="record.builtin"
                status="danger"
                title="删除"
                @click="onDelete(record)"
              >
                删除
              </a-link>
            </a-space>
          </template>
        </GiTable>
      </a-col>
    </a-row>

    <DictItemAddModal ref="DictItemAddModalRef" @save-success="search" />
  </GiPageLayout>
</template>

<script setup lang="ts">
import type { TableInstance } from '@arco-design/web-vue'
import { Message, Modal } from '@arco-design/web-vue'
import DictTree from './tree/index.vue'
import DictItemAddModal from './DictItemAddModal.vue'
import { type DictItemQuery, type DictItemResp, clearDictCache, deleteDictItem, listDictItem } from '@/apis/system/dict'
import { useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: 'SystemDict' })

const queryForm = reactive<DictItemQuery>({
  groupId: 0,
  sort: ['createTime,desc'],
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete,
} = useTable((page) => listDictItem({ ...queryForm, ...page }), { immediate: false })
const columns: TableInstance['columns'] = [
  {
    title: '序号',
    width: 66,
    align: 'center',
    render: ({ rowIndex }) => h('span', {}, rowIndex + 1 + (pagination.current - 1) * pagination.pageSize),
  },
  { title: '字典名称', dataIndex: 'name', slotName: 'name', minWidth: 100, align: 'center' },
  { title: '字典值', dataIndex: 'value', minWidth: 100, align: 'center', ellipsis: true, tooltip: true },
  { title: '状态', dataIndex: 'status', slotName: 'status', align: 'center' },
  {
    title: '排序',
    dataIndex: 'sort',
    align: 'center',
    sortable: {
      sortDirections: ['ascend', 'descend'],
    },
  },
  { title: '描述', dataIndex: 'remark', minWidth: 130, ellipsis: true, tooltip: true },
  // { title: '创建人', dataIndex: 'createUserString', width: 140, ellipsis: true, tooltip: true, show: false },
  { title: '创建时间', dataIndex: 'createTime', width: 180 },
  // { title: '修改人', dataIndex: 'updateUserString', width: 140, ellipsis: true, tooltip: true, show: false },
  { title: '修改时间', dataIndex: 'updateTime', width: 180, show: false },
  {
    title: '操作',
    dataIndex: 'action',
    slotName: 'action',
    width: 130,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['system:dictionary:update', 'system:dictionary:delete']),
  },
]

// 重置
const reset = () => {
  queryForm.remark = undefined
  queryForm.status = undefined
  search()
}

// 删除
const onDelete = (record: DictItemResp) => {
  return handleDelete(() => deleteDictItem(record.id), {
    content: `是否确定删除字典项「${record.name}」？`,
    showModal: true,
  })
}

const dictName = ref()
const dictValue = ref()
const builtin = ref(false)
// 清除缓存
const onClearCache = () => {
  if (!dictValue.value) {
    return Message.warning('请先选择字典')
  }
  Modal.warning({
    title: '提示',
    content: `是否确定清除字典「${dictName.value}(${dictValue.value})」缓存？`,
    hideCancel: false,
    maskClosable: false,
    onOk: async () => {
      await clearDictCache(dictValue.value)
      Message.success('清除成功')
    },
  })
}

// 根据选中字典查询
const handleSelectDict = (dict: { dictId: number, dictName: string, dictValue: string, builtin: boolean }) => {
  queryForm.groupId = dict.dictId
  dictName.value = dict.dictName
  dictValue.value = dict.dictValue
  builtin.value = dict.builtin
  search()
}

const DictItemAddModalRef = ref<InstanceType<typeof DictItemAddModal>>()
// 新增
const onAdd = () => {
  DictItemAddModalRef.value?.onAdd(queryForm.groupId)
}

// 修改
const onUpdate = (record: DictItemResp) => {
  DictItemAddModalRef.value?.onUpdate(record.id)
}
</script>

<style scoped lang="scss">
.page_header {
  flex: 0 0 auto;
}

.page_content {
  flex: 1;
  overflow: auto;
}
</style>
