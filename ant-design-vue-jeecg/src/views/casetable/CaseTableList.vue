<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper" style="margin-top: 20px;">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="案件名称">
              <a-input placeholder="请输入案件名称" v-model="queryParam.caseName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="案件类型">
              <!-- <a-input placeholder="请输入案件类型" v-model="queryParam.caseTypeId"></a-input> -->
              <a-select placeholder="请输入案件名称" style="width: 100%;" @change="handleChange">
                <a-select-option value="非吸">非吸</a-select-option>
                <a-select-option value="赌博">赌博</a-select-option>
                <a-select-option value="传销">传销</a-select-option>
                <a-select-option value="涉税">涉税</a-select-option>
                <a-select-option value="电信诈骗">电信诈骗</a-select-option>
                <a-select-option value="金融犯罪">金融犯罪</a-select-option>
                <a-select-option value="地下钱庄">地下钱庄</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="创建人">
              <a-input placeholder="请输入创建人" v-model="queryParam.introductionId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="">
              <!-- <a-input placeholder="" v-model="queryParam.introductionId"></a-input> -->
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="创建时间">
              <a-range-picker @change="onChange" v-model="queryParam.reserve1" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="地址">
                <a-input placeholder="请输入地址" v-model="queryParam.path"></a-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
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
    <div class="table-operator" style="margin-top: 50px;">
      <a-upload
        id="upload"
        name="file"
        :data="this.upload"
        :disabled="disabled"
        :beforeUpload="beforeUpload"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import" style="height: 40px; " @click="importExcel">导入银行流水</a-button>
      </a-upload>

      <a-button type="primary" icon="download" @click="handleExportXls3('银行流水模板')" style="height: 40px; "
        >银行流水模板</a-button
      >
      <a-button
        style="background-color: #EE9900;border-color: #EE9900;margin-left: 40px;width: 15%;height: 40px;"
        @click="handleAdd"
        type="primary"
        icon="plus"
        >添加案件</a-button
      >
      <a-button type="primary" icon="download" @click="handleExportXls('案件表')" style="height: 40px; "
        >导出案件</a-button
      >
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import" style="height: 40px; ">导入案件</a-button>
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

          <a @click="ckfx(record)" style="color: #52C41A;margin-left: 10px;">查控分析</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a style="color: #CC0000;">删除</a>
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
import { getAction, postAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export default {
  name: 'CaseTableList',
  mixins: [JeecgListMixin],
  components: {
    CaseTableModal
  },
  data() {
    return {
      description: '案件表管理页面',
      datetime: null,
      caseName: '1',
      disabled: false,
      id: null,
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
          title: '案件名称',
          align: 'center',
          dataIndex: 'caseName'
        },
        {
          title: '案件类型',
          align: 'center',
          dataIndex: 'caseTypeId'
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime'
        },
        {
          title: '创建人',
          align: 'center',
          dataIndex: 'introductionId'
        },
        {
          title: '地址',
          align: 'center',
          dataIndex: 'path'
        },
        {
          title: '备注',
          align: 'center',
          dataIndex: 'remarks'
        },

        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/casetable/caseTable/list',
        delete: '/casetable/caseTable/delete',
        deleteBatch: '/casetable/caseTable/deleteBatch',
        exportXlsUrl: 'casetable/caseTable/exportXls',
        exportXlsUrl1: 'bankstatement/bankStatement/exportXls1',
        importExcelUrl: 'bankstatement/bankStatement/importExcel'
      }
    }
  },
  computed: {
    importExcelUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    }
  },
  methods: {
    onChange(date, dateString) {
      console.log(date, dateString)
      this.datetime = dateString
      return dateString
    },
    ckfx: function(id) {
      // console.log("---------:"+JSON.stringify(id));
      this.$router.push({ path: '/MaximumBalance/MaximumBalanceList', query: { id: id } })
    },
    handleChange(value) {
      this.queryParam.caseTypeId = `${value}`
    },
    importExcel: function(a) {
      if (this.selectedRowKeys.length == 1) {
        this.disabled = false

        var ids = ''
        for (var a = 0; a < this.selectedRowKeys.length; a++) {
          ids += this.selectedRowKeys[a]
        }
        // this.queryParam.id = ids
        // getAction(this.url.list, this.queryParam).then(res => {
        //   console.log(res)
        //   this.mydata = res.result.records[0]
        // })
      } else {
        this.disabled = true
        this.$message.warning('请选择一个案件!')
      }
    },
    handleImportExcel(info) {
      // alert(JSON.stringify(info))
      if (info.file.status !== ' ') {
        console.log(info.file, info.fileList)
      }
      if (info.file.status === 'done') {
        if (info.file.response.success) {
          // this.$message.success(`${info.file.name} 文件上传成功`);
          if (info.file.response.code === 201) {
            let {
              message,
              result: { msg, fileUrl, fileName }
            } = info.file.response
            let href = window._CONFIG['domianURL'] + fileUrl
            this.$warning({
              title: message,
              content: (
                <div>
                  <span>{msg}</span>
                  <br />
                  <span>
                    具体详情请{' '}
                    <a href={href} target="_blank" download={fileName}>
                      点击下载
                    </a>{' '}
                  </span>
                </div>
              )
            })
          } else {
            this.$message.success(info.file.response.message || `${info.file.name} 文件上传成功`)
          }
          // this.loadData()
        } else {
          this.$message.error(`${info.file.name} ${info.file.response.message}.`)
        }
      } else if (info.file.status === 'error') {
        this.$message.error(`文件上传失败: ${info.file.msg} `)
      }
    },

    beforeUpload() {
      this.upload = { upload: this.selectedRowKeys[0] }
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>
