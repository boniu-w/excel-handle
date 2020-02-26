<template>
  <page-layout>
    <!-- 查询区域 -->
    <a-card  :title="this.pp">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
      
            <a-col :md="6" :sm="8">
              <a-form-item label="日期">
                <a-input placeholder="请输入日期" v-model="queryParam.date"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="卡号">
                <a-input placeholder="请输入卡号" v-model="queryParam.cardId"></a-input>
              </a-form-item>
            </a-col>
          <template v-if="toggleSearchStatus">
          <a-col :md="6" :sm="8">
              <a-form-item label="最大金额">
                <a-input placeholder="请输入最大金额" v-model="queryParam.maxMoney"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="最大余额">
                <a-input placeholder="请输入最大余额" v-model="queryParam.maxBalance"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="案件id">
                <a-input placeholder="请输入案件id" v-model="queryParam.caseId"></a-input>
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
    </a-card>
    <!-- 折线图区域 -->
    <a-card style="margin-top: 15px;">
      <line-chart-multid :fields="visitFields" :dataSource="visitInfo"></line-chart-multid>
    </a-card>
  <a-card :bordered="false" style="margin-top: 15px;" >
    

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('最大余额表')">导出</a-button>
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
    <maximumBalance-modal ref="modalForm" @ok="modalFormOk"></maximumBalance-modal>
  </a-card>
  </page-layout>
</template>

<script>
  import MaximumBalanceModal from './modules/MaximumBalanceModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import LineChartMultid from '@/components/chart/LineChartMultid'

  export default {
    name: "MaximumBalanceList",
    mixins:[JeecgListMixin],
    components: {
      MaximumBalanceModal,
      LineChartMultid
    },
    data () {
      return {
        description: '最大余额表管理页面',
        pp:this.$route.query.id,
        visitFields:['最大余额','最大金额'],
        visitInfo:[{"tian":"2020-02-24","最大余额":2,"最大金额":10,"type":"02-24"},{"tian":"2020-02-25","最大余额":1,"最大金额":10,"type":"02-25"},{"tian":"2020-02-26","最大余额":1,"最大金额":5,"type":"02-26"}],
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
            title: '日期',
            align:"center",
            dataIndex: 'date'
           },
		   {
            title: '卡号',
            align:"center",
            dataIndex: 'cardId'
           },
		   {
            title: '最大金额',
            align:"center",
            dataIndex: 'maxMoney'
           },
		   {
            title: '最大余额',
            align:"center",
            dataIndex: 'maxBalance'
           },
		   {
            title: '案件id',
            align:"center",
            dataIndex: 'caseId'
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
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/maximumbalance/maximumBalance/list",
          delete: "/maximumbalance/maximumBalance/delete",
          deleteBatch: "/maximumbalance/maximumBalance/deleteBatch",
          exportXlsUrl: "maximumbalance/maximumBalance/exportXls",
          importExcelUrl: "maximumbalance/maximumBalance/importExcel",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
      ckfx(){
        // alert(this.$route.params.id)
        // alert(this.$route.query.id)
        this.ckfx = this.$route.query.id;
      }
     
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>