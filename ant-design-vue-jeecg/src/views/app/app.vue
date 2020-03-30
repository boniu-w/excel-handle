<template>
  <div>
    <a-card>
      <p>测试：</p>
      <div>
        <!-- :headers="headers" -->
        <a-upload
          name="file"
          action="/jeecg-boot/app/appController/getInfo"
          :headers="headers"
          @change="handleUploadChange"
          :multiple="true"
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
        <a-row type="flex" justify="end" style="margin-bottom:10px;">
          <a-col :span="2">
            <a-button @click="handleAdd">
              <a-icon style="font-size:15px; margin-right:5px;" type="plus-circle" />新增条件
            </a-button>
          </a-col>
        </a-row>
        <a-form :form="form">
          <a-row type="flex" justify="space-around" v-for="item of myArr" :key="item">
            <a-col span="5" :sm="24" :md="12" :lg="4">
              <a-form-item label="进出标志" :labelCol="{ span: 7 }" :wrapperCol="{ span: 15 }">
                <a-select
                  style="width:100%;"
                  v-decorator="[
                    `recoveryMark[${item}]`,
                    {
                      validateTrigger: ['change', 'blur'],
                      rules: [
                        {
                          required: true,
                          whitespace: true,
                          message: '请选择进出标志'
                        }
                      ]
                    }
                  ]"
                >
                  <a-select-option value="进">进</a-select-option>
                  <a-select-option value="出">出</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col span="5" :sm="24" :md="12" :lg="4">
              <a-form-item label="交易卡号" :labelCol="{ span: 7 }" :wrapperCol="{ span: 15 }">
                <a-input
                  v-decorator="[
                    `cardEntity[${item}]`,
                    {
                      validateTrigger: ['change', 'blur'],
                      rules: [
                        {
                          required: true,
                          whitespace: true,
                          message: '交易卡号不能为空'
                        }
                      ]
                    }
                  ]"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col span="5" :sm="24" :md="12" :lg="6">
              <a-form-item label="交易日期" :labelCol="{ span: 7 }" :wrapperCol="{ span: 15 }">
                <a-range-picker
                  v-decorator="[
                    `transactionDate[${item}]`,
                    {
                      rules: [
                        {
                          required: true,
                          message: '交易日期不能为空'
                        }
                      ]
                    }
                  ]"
                />
              </a-form-item>
            </a-col>
            <a-col span="5" :sm="24" :md="12" :lg="4">
              <a-row>
                <a-col span="13">
                  <a-form-item label="交易金额" :labelCol="{ span: 14 }" :wrapperCol="{ span: 10 }">
                    <a-input
                      v-decorator="[
                        `startMoney[${item}]`,
                        {
                          validateTrigger: ['change', 'blur'],
                          rules: [
                            {
                              required: true,
                              whitespace: true,
                              message: '初始金额不能为空'
                            }
                          ]
                        }
                      ]"
                      :style="{ width: '50px' }"
                    ></a-input>
                  </a-form-item>
                </a-col>
                <a-col span="3">
                  <div style="border:1px solid #000;width:23px;margin-top:17px;display:inline-block;"></div>
                </a-col>
                <a-col span="5">
                  <a-form-item>
                    <a-input
                      v-decorator="[
                        `endMoney[${item}]`,
                        {
                          validateTrigger: ['change', 'blur'],
                          rules: [
                            {
                              required: true,
                              whitespace: true,
                              message: '结束金额不能为空'
                            }
                          ]
                        }
                      ]"
                      :style="{ width: '50px' }"
                    ></a-input>
                  </a-form-item>
                </a-col>
              </a-row>
            </a-col>
            <a-col span="5" :sm="24" :md="12" :lg="4">
              <a-form-item label="对手户名" :labelCol="{ span: 7 }" :wrapperCol="{ span: 15 }">
                <a-input v-decorator="[`counterParty[${item}]`]"></a-input>
              </a-form-item>
            </a-col>
            <a-col span="2" :md="12" :lg="2">
              <a-icon style="margin:7px 0 0 15px;font-size:25px" type="minus-circle" @click="handleRemove(item)" />
            </a-col>
          </a-row>
          <a-row type="flex" justify="end">
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
          <a-button @click="handleExportXls('导出文件')">导出</a-button>
        </a-col>
      </a-row>
      <a-table
        rowKey="id"
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
import { postAction, getAction } from '@/api/manage.js'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import moment from 'moment'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
let id = 1
export default {
  mixins: [JeecgListMixin],
  data() {
    return {
      headers: { 'X-Access-Token': Vue.ls.get(ACCESS_TOKEN) },
      form: this.$form.createForm(this),
      myArr: [0],
      file: null,
      myDataSource: [],
      fileList: [],
      fileListName: [],
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
      params: {},
      url: {
        getFileInfoUploaded: '/app/appController/getFileInfoUploaded',
        exportXlsUrl: '/app/appController/exportXls'
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
        console.log(res, '初始数据')
        if (res) {
          this.fileList = res

          if (this.fileList.length > 12) {
            this.showButton = true
          }
          this.fileListName = this.fileList.slice(0, 12)
        }
      })
    },
    beforeUpload(file, fileList) {
      return true
    },
    handleAdd() {
      if (this.myArr.length === 0) {
        this.myArr.push(0)
      } else {
        this.myArr = this.myArr.concat(id++)
      }
    },
    handleRemove(key) {
      if (this.myArr.length == 1) {
        this.$message.warning('至少保留一条索引')
        return
      }
      this.myArr = this.myArr.filter(item => {
        return item !== key
      })
    },
    handleUploadChange(info) {
      if (info.file.status !== 'uploading') {
        window.console.log(info.file, info.fileList, 222)
      }
      if (info.file.status === 'done') {
        console.log(info, 'info')
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
      const that = this
      this.loading = true
      this.form.validateFields((err, values) => {
        if (!err) {
          const { cardEntity, recoveryMark, transactionDate, startMoney, endMoney, counterParty } = values
          console.log(this.myArr, 'myArr')
          let data = this.myArr.map(item => {
            return {
              cardEntity: cardEntity[item],
              recoveryMark: recoveryMark[item],
              startTime: moment(transactionDate[item][0]).format('YYYY-MM-DD'),
              endTime: moment(transactionDate[item][1]).format('YYYY-MM-DD'),
              startMoney: startMoney[item],
              endMoney: endMoney[item],
              counterParty: counterParty[item] == '' ? null : counterParty[item]
            }
          })
          let formData = new FormData()
          formData.append('data', JSON.stringify(data))
          formData.append('fileName', that.params.fileName)
          postAction('app/appController/resolve', formData).then(res => {
            let titleMap = res.titleMap
            console.log(titleMap, 'titleMap')
            let titleArr = Object.keys(titleMap).map((item, i) => {
              if (item) {
                return {
                  title: titleMap[item],
                  align: 'center',
                  dataIndex: item
                }
              }
            })
            this.columns = [this.columns[0], ...titleArr]

            this.myDataSource = res.bankFlowList
            this.loading = false
            console.log(res, this.columns)
          })
          window.console.log(this.myDataSource)
        }
      })
    },
    onRadioChange(e) {
      window.console.log(e.target.value)
      this.params.fileName = e.target.value
    },
    handleShow() {
      this.showButton = false
      this.fileListName = this.fileList
    }
  }
}
</script>
