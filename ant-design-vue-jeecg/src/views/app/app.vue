<template>
  <div>
    <a-card>
      <p>测试：</p>
      <div>
        <!-- :headers="headers" -->
        <a-upload
          name="file"
          action="/jeecg-boot/app/appController/getFileInfo"
          :headers="headers"
          @change="handleUploadChange"
        >
          <a-button>上传</a-button>
        </a-upload>
      </div>
      <p>筛选条件：</p>
      <div>
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
                    `mark[${item}]`,
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
          <a-button>导出</a-button>
        </a-col>
      </a-row>
      <a-table
        rowKey="id"
        size="small"
        bordered
        :columns="columns"
        :dataSource="myDataSource"
        :scroll="{ x: 2000 }"
        style="font-size:5px;"
        :showHeader="myDataSource.length > 0"
      ></a-table>
    </a-card>
  </div>
</template>
<script>
import { postAction, getAction } from '@/api/manage.js'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import moment from 'moment'
let id = 1
export default {
  data() {
    return {
      headers: { 'X-Access-Token': Vue.ls.get(ACCESS_TOKEN) },
      form: this.$form.createForm(this),
      myArr: [0],
      file: null,
      myDataSource: [],
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
        },
        {
          title: '交易主体',
          align: 'center',
          dataIndex: 'transaction_date'
        },
        {
          title: '交易主体账号',
          align: 'center',
          dataIndex: 'counter_party'
        },
        {
          title: '交易主体卡号',
          align: 'center',
          dataIndex: 'card_entity'
        }
      ]
    }
  },
  methods: {
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
      } else if (info.file.status === 'error') {
        this.$message.error(`${info.file.name} file upload failed.`)
      }
    },
    handleAnalysis() {
      this.form.validateFields((err, values) => {
        if (!err) {
          const { cardEntity, mark, transactionDate, startMoney, endMoney, counterParty } = values
          console.log(this.myArr, 'myArr')
          let data = this.myArr.map(item => {
            return {
              cardEntity: cardEntity[item],
              mark: mark[item],
              startTime: moment(transactionDate[item][0]).format('YYYY-MM-DD'),
              endTime: moment(transactionDate[item][1]).format('YYYY-MM-DD'),
              startMoney: startMoney[item],
              endMoney: endMoney[item],
              counterParty: counterParty[item] == '' ? null : counterParty[item]
            }
          })

          let formData = new FormData()
          formData.append('data', JSON.stringify(data))

          postAction('app/appController/getTableInfo', formData).then(res => {
            console.log(res)
          })
          let title = {
            transaction_date: '入账日期',
            counter_party: '对手户名'
          }

          this.columns.forEach((item, colIndex) => {
            Object.keys(title).forEach(titleItem => {
              if (item.dataIndex === titleItem) {
                this.columns[colIndex].title = title[titleItem]
              }
            })
          })

          let res = [
            {
              account_subject: 6228480028048672670,
              card_entity: 20180102,
              counter_party: 184526037,
              日志号: 184526037,
              传票号: 'EP010000',
              客户代码: 1612506680220158,
              客户名称: '张立芹',
              交易金额: -101.9,
              交易后余额: 236.19,
              交易渠道: 'EPAY',
              摘要: '财付通',
              商户名称: '',
              交易地点: '',
              APSH地点线索: '029999',
              交易对手账号: 41007801941001021,
              交易对手客户代码: '',
              交易对手户名: '财付通支付科技有限公司客户备付金',
              对手交易后余额: '0',
              交易行号: '  029999',
              交易行名称: '天津市分行资金清算中心'
            }
          ] // let columns = Object.keys(res[0]).map(item => { //   return { //     title: item, //     align: "center", //     dataIndex: item //   }; // }); // this.columns = [this.columns[0], ...columns];

          this.myDataSource = res

          window.console.log(this.myDataSource)
        }
      })
    }
  }
}
</script>
