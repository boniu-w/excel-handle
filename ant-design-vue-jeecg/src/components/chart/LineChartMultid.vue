<template>
<!-- <div :style="{ padding: '0 0 32px 32px' }" @click="pp" >
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4> -->
    <v-chart :force-fit="true" :height="height" :data="data" :scale="scale">
    <v-tooltip/>
      <v-axis/>
      <v-legend/>
      <v-line position="type*y" color="x" :onClick="this.pp" />
      <!-- <v-point position="type*y" color="x" :size="4" :v-style="style" :shape="'circle'"/> -->
    </v-chart>
  <!-- </div> -->
</template>

<script>
  import { DataSet } from '@antv/data-set'

  export default {
    name: 'LineChartMultid',
    props: {
      title: {
        type: String,
        default: '数据分析图(总和)：'
      },
      dataSource: {
        type: Array,
        default: () => [
          { type: 'Jan', jeecg: 7.0, jeebt: 3.9 },
          { type: 'Feb', jeecg: 6.9, jeebt: 4.2 },
          { type: 'Mar', jeecg: 9.5, jeebt: 5.7 },
          { type: 'Apr', jeecg: 14.5, jeebt: 8.5 },
          { type: 'May', jeecg: 18.4, jeebt: 11.9 },
          { type: 'Jun', jeecg: 21.5, jeebt: 15.2 },
          { type: 'Jul', jeecg: 25.2, jeebt: 17.0 },
          { type: 'Aug', jeecg: 26.5, jeebt: 16.6 },
          { type: 'Sep', jeecg: 23.3, jeebt: 14.2 },
          { type: 'Oct', jeecg: 18.3, jeebt: 10.3 },
          { type: 'Nov', jeecg: 13.9, jeebt: 6.6 },
          { type: 'Dec', jeecg: 9.6, jeebt: 4.8 }
        ]
      },
      fields: {
        type: Array,
        default: () => ['jeecg', 'jeebt']
      },
      // 别名，需要的格式：[{field:'name',alias:'姓名'}, {field:'sex',alias:'性别'}]
      aliases:{
        type: Array,
        default: () => []
      },
      height: {
        type: Number,
        default: 300
      },
    },
    data() {
      return {
        scale: [{
          dataKey: 'x',
          min: 0,
          max: 1
        }],
        style: { stroke: '#fff', lineWidth: 1 }
      }
    },
    computed: {
      data() {
        const dv = new DataSet.View().source(this.dataSource)
         var a = dv.rows.slice(0);  //或者arr.concat()
         a.pop();
         dv.rows = a;
         console.log(dv.rows.length)
        dv.transform({
          type: 'fold',
          fields: this.fields,
          key: 'x',
          value: 'y'
        })
        let rows =  dv.rows
        // 替换别名
        rows.forEach(row => {
          for (let item of this.aliases) {
            if (item.field === row.x) {
              row.x = item.alias
              break
            }
          }
        })
        return rows
      }
    },
    methods:{
      pp(){
        const dv = new DataSet.View().source(this.dataSource)
        var num = dv.rows.length-1
        if(dv.rows[num].type[0] != undefined){
          var datetime = dv.rows[num].type[0]+","+dv.rows[num].type[1];
          this.$router.push({path:'/bankStatement/BankStatementList',query:{id:dv.rows[num].最大余额,datetime:datetime}}); 
        }else {
          
          this.$router.push({path:'/bankStatement/BankStatementList',query:{id:dv.rows[num].最大余额}}); 
        }
      }
    }
  }
</script>

<style scoped>

</style>