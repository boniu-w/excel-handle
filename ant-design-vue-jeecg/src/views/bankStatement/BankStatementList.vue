<template>
  <a-card :title="this.caseName">
    <a-button
      type="primary"
      style="background-color: #E78C45;border-color: #E78C45; position: absolute; right: 50px;top: 10px;"
      @click="FanHui()"
      icon="enter"
      >返回</a-button
    >

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <!-- <a-col :md="6" :sm="8">
            <a-form-item label="案件id">
              <a-input placeholder="请输入案件id" v-model="queryParam.caseId"></a-input>
            </a-form-item>
          </a-col> -->
          <a-col :md="6" :sm="8">
            <a-form-item label="交易日期">
              <a-range-picker @change="onChange" v-model="queryParam.reserve1" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="交易时间">
                <a-time-picker
                  :open.sync="open2"
                  @change="onChange1"
                  format="HH:mm:ss"
                  v-model="queryParam.transactionTime"
                >
                  <a-button slot="addon" size="small" type="primary" @click="handleClose">Ok</a-button>
                </a-time-picker>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="查询卡号">
                <a-input placeholder="请输入查询卡号" v-model="queryParam.queryCardNumber"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="姓名">
                <a-input placeholder="请输入姓名" v-model="queryParam.fullName"></a-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="loadData1(1, 3)" icon="search">查询</a-button>
              <a-button type="primary" @click="loadData1(1, 2)" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('流水表')">导出</a-button>
      <a-upload
        name="file"
        :beforeUpload="beforeUpload"
        :showUploadList="false"
        :multiple="false"
        :data="this.upload"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
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
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
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
    <bankStatement-modal ref="modalForm" @ok="modalFormOk"></bankStatement-modal>
  </a-card>
</template>

<script>
import BankStatementModal from './modules/BankStatementModal'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import Utils from '@/views/MaximumBalance/util.js'

export default {
  name: 'BankStatementList',
  mixins: [JeecgListMixin],
  components: {
    BankStatementModal,
    Utils
  },
  data() {
    return {
      description: '流水表管理页面',
      caseName: this.$route.query.id.caseName,
      id: this.$route.query.id.id,
      open2: false,
      datetime: '',
      type: '2',
      time: null,
      upload: null,
      // 表头
      columns: [
        // {
        //   title: '#',
        //   dataIndex: '',
        //   key:'rowIndex',
        //   width:60,
        //   align:"center",
        //   customRender:function (t,r,index) {
        //     return parseInt(index)+1;
        //   }
        //  },
        {
          title: '交易日期',
          align: 'center',
          format: 'yyyy-MM-dd',
          dataIndex: 'transactionDate'
        },
        {
          title: '交易时间',
          align: 'center',
          format: 'HH:mm:ss',
          dataIndex: 'transactionTime'
        },
        {
          title: '查询卡号',
          align: 'center',
          dataIndex: 'queryCardNumber'
        },
        {
          title: '姓名',
          align: 'center',
          dataIndex: 'fullName'
        },
        {
          title: '账号余额',
          align: 'center',
          dataIndex: 'accountBalance'
        },
        {
          title: '交易金额',
          align: 'center',
          dataIndex: 'transactionAmount'
        },
        {
          title: '借贷标识',
          align: 'center',
          dataIndex: 'loanIdentification'
        },
        {
          title: '对手账号',
          align: 'center',
          dataIndex: 'opponentAccountNumber'
        },
        {
          title: '户名',
          align: 'center',
          dataIndex: 'accountName'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/bankstatement/bankStatement/list',
        delete: '/bankstatement/bankStatement/delete',
        deleteBatch: '/bankstatement/bankStatement/deleteBatch',
        exportXlsUrl: 'bankstatement/bankStatement/exportXls',
        importExcelUrl: 'bankstatement/bankStatement/importExcel'
      }
    }
  },
  computed: {
    importExcelUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    }
  },

  activated: function() {
    if (this.caseName == null) {
      var key = '/bankStatement/BankStatementList?id=%5Bobject%20Object%5D'
      Utils.$emit('demo', key)
    }
    this.update()
    this.loadData1(1, 1)
  },
  methods: {
    onChange(date, dateString) {
      console.log(date, dateString)
      this.datetime = dateString
      return dateString
    },
    onChange1(time, timeString) {
      // alert(timeString)
      this.time = timeString
    },
    FanHui() {
      // alert(encodeURIComponent(this.$route.query.id));
      if(this.$route.query.datetime == undefined){
        var key = '/bankStatement/BankStatementList?id=' + encodeURIComponent(this.$route.query.id)
        Utils.$emit('demo', key)
      }else{
        var key = "/bankStatement/BankStatementList?id=" + encodeURIComponent(this.$route.query.id)+'&datetime='+this.$route.query.datetime;
        Utils.$emit('demo', key)
      }
    },
    update() {
      if (this.caseName != this.$route.query.id.caseName + '"         案件') {
        this.caseName = this.$route.query.id.caseName + '"         案件'
        this.queryParam.caseId = this.$route.query.id.id
      }
    },
    handleClose() {
      this.open2 = false
    },

    beforeUpload() {
      this.upload = { upload: this.id }
    }
  }
}
</script>

