<template>
  <a-card style="background-color:#fcfcfc; color: #fcfcfc;" width="100%" height="100%">
    <div style="background-color:#fcfcfc; color: #fcfcfc; width: 1000px;height: 760px;">
       <!-- <index-chart v-if="indexStyle==1"></index-chart>
        <index-bdc v-if="indexStyle==2"></index-bdc>
        <index-task v-if="indexStyle==3"></index-task>
        <div style="width: 100%;text-align: right;margin-top: 20px">
          请选择首页样式：
          <a-radio-group v-model="indexStyle">
            <a-radio :value="1">统计图表</a-radio>
            <a-radio :value="2">统计图表2</a-radio>
            <a-radio :value="3">任务表格</a-radio>
          </a-radio-group>
        </div> -->
       <img src="./img/timg.jpg" style="width: 150px; height: 160px;position: absolute; left: 550px;top: 180px;" />
       <a style="position: absolute; left: 750px; font-size: 50px;top: 220px; color: #000000;">资  金  查  控  平  台</a>
       <a-button @click="TZ" style="font-size: 20px;width: 350px; height: 55px;position: absolute; left: 700px;top: 440px; font-size: 35px;background-color: #EE9900;color: #fcfcfc;">
         查 看 案 件
       </a-button>
      </div>
  </a-card>
</template>

<script>
  import IndexChart from './IndexChart'
  import IndexTask from "./IndexTask"
  import IndexBdc from './IndexBdc'

  export default {
    name: "Analysis",
    components: {
      IndexChart,
      IndexTask,
      IndexBdc
    },
    data() {
      return {
        indexStyle:1
      }
    },
    created() {

    },
    methods: {
      TZ(){
        this.$router.push({path:'/casetable/CaseTableList'}); 
      }
    }
  }
</script>