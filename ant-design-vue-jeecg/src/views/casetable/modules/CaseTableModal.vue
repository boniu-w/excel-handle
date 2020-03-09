<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
      
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="案件名称">
          <a-input placeholder="请输入案件名称" v-decorator="['caseName', validatorRules.caseName ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="案件类型">
          <!-- <a-input placeholder="请输入案件类型" v-decorator="['caseTypeId', validatorRules.caseTypeId ]" /> -->
          <a-select placeholder="请输入案件名称" style="width: 100%;" @change="handleChange" v-decorator="['caseTypeId', validatorRules.caseTypeId ]">
                <a-select-option value="非吸" >非吸</a-select-option>
                <a-select-option value="赌博" >赌博</a-select-option>
                <a-select-option value="传销" >传销</a-select-option>
                <a-select-option value="涉税" >涉税</a-select-option>
                <a-select-option value="电信诈骗" >电信诈骗</a-select-option>
                <a-select-option value="金融犯罪" >金融犯罪</a-select-option>
                <a-select-option value="地下钱庄" >地下钱庄</a-select-option>
          </a-select>
        </a-form-item>
       <!-- <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="创建人id">
          <a-input placeholder="请输入创建人id" v-decorator="['introductionId', validatorRules.introductionId ]" />
        </a-form-item> -->
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="地址">
          <a-input placeholder="请输入地址" v-decorator="['path', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="备注">
          <a-input placeholder="请输入备注" v-decorator="['remarks', {}]" />
        </a-form-item>
        
		
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "CaseTableModal",
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
        caseName:{rules: [{ required: true, message: '请输入案件名称!' }]},
        caseTypeId:{rules: [{ required: true, message: '请输入案件类型!' }]},
        introductionId:{rules: [{ required: true, message: '请输入创建人id!' }]},
        },
        url: {
          add: "/casetable/caseTable/add",
          edit: "/casetable/caseTable/edit",
        },
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'caseName','caseTypeId','introductionId','path','remarks','timeStamp','updateCount','deleteIdentifier','reserve1','reserve2','reserve3'))
		  //时间格式化
        });

      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        alert(1)
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            //时间格式化
            
            console.log(formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })



          }
        })
      },
      handleCancel () {
        this.close()
      },


    }
  }
</script>

<style lang="less" scoped>

</style>