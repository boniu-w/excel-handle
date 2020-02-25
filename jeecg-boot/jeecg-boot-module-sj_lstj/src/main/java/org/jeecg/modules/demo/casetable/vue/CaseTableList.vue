<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="案件名称">
              <a-input placeholder="请输入案件名称" v-model="queryParam.caseName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="案件类型">
              <a-input placeholder="请输入案件类型" v-model="queryParam.caseTypeId"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="创建人id">
              <a-input placeholder="请输入创建人id" v-model="queryParam.introductionId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="地址">
              <a-input placeholder="请输入地址" v-model="queryParam.path"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="备注">
              <a-input placeholder="请输入备注" v-model="queryParam.remarks"></a-input>
            </a-form-item>
          </a-col>
          </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('案件表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <caseTable-modal ref="modalForm" @ok="modalFormOk"></caseTable-modal>
  </a-card>
</template>

<script>
  import CaseTableModal from './modules/CaseTableModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "CaseTableList",
    mixins:[JeecgListMixin],
    components: {
      CaseTableModal
    },
    data () {
      return {
        description: '案件表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
           },
		   {
            title: '案件名称',
            align:"center",
            dataIndex: 'caseName'
           },
		   {
            title: '案件类型',
            align:"center",
            dataIndex: 'caseTypeId'
           },
		   {
            title: '创建人id',
            align:"center",
            dataIndex: 'introductionId'
           },
		   {
            title: '地址',
            align:"center",
            dataIndex: 'path'
           },
		   {
            title: '备注',
            align:"center",
            dataIndex: 'remarks'
           },
		   {
            title: '时间戳',
            align:"center",
            dataIndex: 'timeStamp'
           },
		   {
            title: '更改次数',
            align:"center",
            dataIndex: 'updateCount'
           },
		   {
            title: '删除标识',
            align:"center",
            dataIndex: 'deleteIdentifier'
           },
		   {
            title: 'Reserve1',
            align:"center",
            dataIndex: 'reserve1'
           },
		   {
            title: 'Reserve2',
            align:"center",
            dataIndex: 'reserve2'
           },
		   {
            title: 'Reserve3',
            align:"center",
            dataIndex: 'reserve3'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/casetable/caseTable/list",
          delete: "/casetable/caseTable/delete",
          deleteBatch: "/casetable/caseTable/deleteBatch",
          exportXlsUrl: "casetable/caseTable/exportXls",
          importExcelUrl: "casetable/caseTable/importExcel",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
     
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>