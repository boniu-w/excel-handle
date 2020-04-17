<template>
  <div>
    <a-card>
      <a-alert v-if="message.length > 0" message="Warning" :description="message" type="warning" showIcon />
      <a-row type="flex" justify="space-between">
        <a-col :span="2">
          <p>测试：</p>
        </a-col>
        <a-col :span="4">
          <a-popconfirm title="是否清空数据库流水表" @confirm="confirm" @cancel="cancel" okText="是" cancelText="否">
            <a-button type="danger">清空数据库流水表</a-button>
          </a-popconfirm>
        </a-col>
      </a-row>
      <div>
        <!-- :headers="headers" -->
        <a-upload
          name="file"
          action="/jeecg-boot/app/appController/getInfo"
          :headers="headers"
          @change="handleUploadChange"
          :multiple="false"
          :fileList="fileList"
        >
          <a-button>上传</a-button>
        </a-upload>
      </div>
      <p>筛选条件：</p>
      <div>
        <a-row>
          <a-col>
            <a-card v-resize="resize" id="card">
              <a-radio-group style="width:100%;" @change="onRadioChange">
                <a-row type="flex" justify="start" style="width:100%;">
                  <a-col span="7" :sm="24" :md="12" :lg="8" v-for="(item, i) in fileListName" :key="i">
                    <a-radio :value="item.fileName">{{ item.fileName }}</a-radio>
                  </a-col>
                  <a-col v-if="showButton">
                    <a-button @click="handleShow" size="small"> <a-icon type="caret-down" />显示更多 </a-button>
                  </a-col>
                </a-row>
              </a-radio-group>
            </a-card>
          </a-col>
        </a-row>
        <p>上传条件文件：</p>
        <a-upload
          name="file"
          @change="handleConditionsChange"
          :headers="headers"
          :multiple="false"
          :remove="handleRemove"
          :fileList="conditionsFileList"
          :beforeUpload="beforeUpload"
          :openFileDialogOnClick="openFileDialogOnClick"
        >
          <a-button :style="{ margin: '10px 0' }" @click="handleConditionsClick">上传</a-button>
        </a-upload>
        <a-form :form="form">
          <a-row type="flex" justify="start">
            <a-col :span="5">
              <a-row type="flex" justify="start">
                <a-col :span="23">
                  <a-form-item label="交易金额百分比" :labelCol="{ span: 10 }" :wrapperCol="{ span: 14 }">
                    <a-input-number
                      :style="{ width: '100%' }"
                      v-decorator="['percentage', { rules: [{ required: true, message: '请输入交易金额百分比!' }] }]"
                      placeholder="0~100"
                      :min="0"
                      :max="100"
                    ></a-input-number>
                  </a-form-item>
                </a-col>
                <a-col :span="1">
                  <span :style="{ display: 'block', marginTop: '9px', fontWeight: 'blod' }">%</span>
                </a-col>
              </a-row>
            </a-col>
            <a-col :span="7">
              <a-form-item
                label="交易日期范围(之后多少天)"
                :labelCol="{ span: 10 }"
                :wrapperCol="{ span: 14 }"
                :style="{ marginLeft: '20px' }"
              >
                <a-input-number
                  :style="{ width: '70%' }"
                  v-decorator="['dateScope', { rules: [{ required: true, message: '请输入交易日期范围!' }] }]"
                  placeholder="请输入多少天之内"
                  :min="0"
                ></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item label="对手户名" :labelCol="{ span: 7 }" :wrapperCol="{ span: 13 }">
                <a-input v-decorator="['counterParty', {}]" placeholder="请输入对手户名"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="2">
              <a-button @click="handleAnalysis" type="primary">分析</a-button>
            </a-col>
          </a-row>
        </a-form>
      </div>
    </a-card>
    <a-card>
      <a-row type="flex" justify="end">
        <a-col>
          <form :action="'jeecg-boot' + url.exportXlsUrl" method="post">
            <input type="text" name="data" :value="JSON.stringify(myDataSource)" hidden="hidden" />
            <input type="submit" value="导出" />
          </form>

          <!-- <a-button @click="handleExportXls('导出文件')">导出</a-button> -->
        </a-col>
      </a-row>
      <a-table
        rowkey="key"
        size="small"
        bordered
        :columns="columns"
        :dataSource="myDataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :scroll="{ x: 2000 }"
        style="font-size:5px;"
      ></a-table>
    </a-card>
  </div>
</template>
<script>
import { postAction, getAction, downFile } from '@/api/manage.js'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import moment from 'moment'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import axios from 'axios'
let id = 1
export default {
  mixins: [JeecgListMixin],
  data() {
    return {
      headers: { 'X-Access-Token': Vue.ls.get(ACCESS_TOKEN) },
      form: this.$form.createForm(this),
      myArr: [0],
      file: null,
      message: '',
      myDataSource: [],
      fileList: [],
      fileListName: [],
      conditionsFileList: [],
      resData: [],
      titleArr: null,
      columns: [
        {
          title: '#',
          key: 'rowIndex',
          dataIndex: '',
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          },
          fixed: 'left'
        }
      ],
      showButton: false,
      loading: false,
      // myConditionsDataSource: [],
      openFileDialogOnClick: false,
      params: {},
      url: {
        getFileInfoUploaded: '/app/appController/getFileInfoUploaded',
        exportXlsUrl: '/app/appController/export2'
        // exportXlsUrl: '/app/appController/exportExcel'
      }
    }
  },
  created() {
    this.load()
  },
  directives: {
    resize: {
      bind(el, binding) {
        let width = '',
          height = ''
        function isReize() {
          const style = document.defaultView.getComputedStyle(el)
          if (width !== style.width || height !== style.height) {
            binding.value()
          }
          width = style.width
          height = style.height
        }
        el.__vueSetInterval__ = setInterval(isReize, 300)
      },
      unbind(el) {
        clearInterval(el.__vueSetInterval__)
      }
    }
  },
  methods: {
    loadData() {},
    resize() {
      let card = document.getElementById('card')
      window.console.log(card.clientHeight)
      if (card && card.clientHeight > 175) {
        card.style.height = '175px'
        card.style.overflow = 'scroll'
      }
    },
    load() {
      postAction(this.url.getFileInfoUploaded).then(res => {
        console.log(res, 222)
        if (res.length > 0) {
          let fileList = res
          this.resData = res.map(item => {
            return {
              createTime: item.createTime,
              fileLocation: item.fileLocation,
              fileName: item.fileName,
              fileSize: item.fileSize,
              fileTitle: JSON.parse(item.fileTitle),
              id: item.id,
              updateTime: item.updateTime
            }
          })
          let titleArr = this.resData[0].fileTitle
          // 获取数据表头
          this.getTableTitle(titleArr)
          if (fileList.length > 12) {
            this.showButton = true
          }
          this.fileListName = fileList.slice(0, 12)
        }
        console.log(res, '初始数据')
      })
    },
    getTableTitle(titleMap) {
      console.log(titleMap, 111)
      let titleArr = Object.keys(titleMap).map((item, i) => {
        if (item) {
          return {
            title: titleMap[item],
            align: 'center',
            dataIndex: item
          }
        }
      })
      this.titleArr = titleArr
      this.columns = [this.columns[0], ...titleArr]
    },
    beforeUpload(file) {
      this.conditionsFileList = [...this.conditionsFileList, file]
      return false
    },
    handleRemove(file) {
      const index = this.conditionsFileList.indexOf(file)
      const newFileList = this.conditionsFileList.slice()
      newFileList.splice(index, 1)
      this.conditionsFileList = newFileList
    },
    handleAdd() {
      if (this.myArr.length === 0) {
        this.myArr.push(0)
      } else {
        this.myArr = this.myArr.concat(id++)
      }
    },
    handleUploadChange(info) {
      this.fileList = [...info.fileList]
      if (info.file.status !== 'uploading') {
        // window.console.log(info.file, info.fileList, 222)
      }
      if (info.file.status === 'done') {
        let altMessage = info.file.response
        console.log(altMessage, 'info')

        if (altMessage.message && altMessage.message.length > 0) {
          this.message = altMessage.message
          return
        } else if (altMessage.fileMessage && altMessage.fileMessage.length > 0) {
          this.message = altMessage.fileMessage
          return
        } else {
          this.message = false
        }

        this.$message.success(`${info.file.name} file uploaded successfully`)
        this.load()
      } else if (info.file.status === 'error') {
        this.$message.error(`${info.file.name} file upload failed.`)
      }
    },
    handleAnalysis() {
      if (!this.params.fileName) {
        this.$message.warning('至少选择一个表')
        return
      }
      if (!this.conditionsFileList.length) {
        this.$message.warning('请上传一个条件表')
        return
      }
      const that = this
      this.loading = true
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log(values)
          let formData = new FormData()
          formData.append('percentage', values.percentage)
          formData.append('dateScope', values.dateScope)
          formData.append('counterParty', values.counterParty ? '' : '')
          formData.append('fileName', that.params.fileName)
          this.conditionsFileList.forEach(file => {
            formData.append('files[]', file)
          })
          postAction('app/appController/conditionExcelExamine', formData).then(res => {
            this.conditionsFileList = []

            this.myDataSource = res
            this.loading = false
            console.log(res, this.columns)
          })
          window.console.log(this.myDataSource)
        }
      })
    },
    onRadioChange(e) {
      window.console.log(e.target.value)
      this.myDataSource = []
      this.conditionsFileList = []
      this.params.fileName = e.target.value
      if (this.resData) {
        let titleArr = this.resData.filter(item => item.fileName == this.params.fileName)[0].fileTitle
        this.getTableTitle(titleArr)
      }
    },
    handleShow() {
      this.showButton = false
      this.fileListName = this.fileList
    },
    handleConditionsChange(info) {
      if (info.file.status !== 'uploading') {
      }

      if (info.file.status === 'done') {
        window.console.log(info.file, info.fileList)
        this.$message.success(`${info.file.name} file uploaded successfully`)
      } else if (info.file.status === 'error') {
        this.$message.error(`${info.file.name} file upload failed.`)
      }
    },
    handleConditionsClick() {
      window.console.log(1)
      if (!this.params.fileName) {
        this.$message.warning('请选择一个表')
        this.openFileDialogOnClick = false
      } else {
        this.openFileDialogOnClick = true
      }
      return
    },
    async handleExportXls(fileName) {
      if (!fileName || typeof fileName != 'string') {
        fileName = '导出文件'
      }
      console.log(this.myDataSource, this.columns, this.titleArr)
      // let a = qs.stringify({ myDataSource: JSON.stringify(this.myDataSources) })
      // console.log(a)
      // let formData = new FormData()
      // formData.append('titleArr', JSON.stringify(this.titleArr))
      // formData.append('myDataSource', JSON.stringify(this.myDataSource))
      // return

      let myDataSource = this.myDataSource

      // await axios({
      //   url: '/jeecg-boot' + this.url.exportXlsUrl,
      //   headers: { 'X-Access-Token': Vue.ls.get(ACCESS_TOKEN) },
      //   method: 'post',
      //   responseType: 'blob'
      // }).then(res => {
      //   console.log(res)
      // })

      postAction(this.url.exportXlsUrl, myDataSource)
      return
      let param = { ...this.queryParam }
      if (this.selectedRowKeys && this.selectedRowKeys.length > 0) {
        param['selections'] = this.selectedRowKeys.join(',')
      }
      console.log('导出参数', param)
      await axios({
        url: '/jeecg-boot' + this.url.exportXlsUrl,
        data: param,
        headers: { 'X-Access-Token': Vue.ls.get(ACCESS_TOKEN) },
        method: 'post',
        responseType: 'blob'
      }).then(data => {
        console.log(data, 'data export')
        if (!data) {
          this.$message.warning('文件下载失败')
          return
        }
        if (typeof window.navigator.msSaveBlob !== 'undefined') {
          window.navigator.msSaveBlob(new Blob([data]), fileName + '.xls')
        } else {
          let url = window.URL.createObjectURL(new Blob([data]))
          let link = document.createElement('a')
          // link.innerText = '测试测试'
          link.style.display = 'none'
          link.href = url
          console.log('八八八八八八八八八八八八八:' + JSON.stringify(link))
          console.log('------------')
          link.setAttribute('download', fileName + '.xls')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link) //下载完成移除元素
          window.URL.revokeObjectURL(url) //释放掉blob对象
        }
      })
    },
    confirm(e) {
      getAction('/app/appController/clearDatabase').then(res => {
        if (res) {
          this.$message.success(res)
          this.clear()
        }
      })
    },
    cancel(e) {
      this.$message.error('清空异常')
    },
    clear() {
      this.fileListName = []
      this.fileList = []
      this.form.resetFields()
    },
    handleSubmit(e) {
      // e.preventDefault()
      console.log(1111)
      let values = JSON.stringify(this.myDataSource)
      console.log(values + '------------------------')
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log('Received values of form: ', values)
        }
      })
    }
  }
}
</script>
