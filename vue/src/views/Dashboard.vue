<template>
  <div>
    <!--    <div id="ma" style="margin-left: 5px"></div>-->
    <el-row :gutter="20" style="">
      <el-col :span="6" v-for="item in cards" style="margin-top: 10px" :key="item.title">
        <el-card class="box-card">
          <div slot="header" class="clearfix">{{ item.title }}</div>
          <div class="text item">
            <svg class="icon" aria-hidden="true">
              <use :xlink:href="item.icon" style="width: 100px"></use>
            </svg>
            <span class="text">{{ item.data }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <div id="myTimer" style="margin-left: 15px;font-weight: 550;"></div>
    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="chart-container" ref="chart1"></div>
      </el-col>
      <el-col :span="6">
        <div class="chart-container" ref="chart4"></div>
      </el-col>
      <el-col :span="6">
        <div class="chart-container" ref="chart3"></div>
      </el-col>
      <el-col :span="6">
        <div class="chart-container" ref="chart2"></div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import * as echarts from 'echarts'
import { ElMessage } from "element-plus";
import request from "../utils/request";

export default {

  data() {
    return {
      cards: [
        { title: '已出租', data: 0, icon: '#iconlend-record-pro' },
        { title: '总访问', data: 0, icon: '#iconvisit' },
        { title: '车辆数', data: 0, icon: '#iconbook-pro' },
        { title: '用户数', data: 0, icon: '#iconpopulation' }
      ],
      data: {}
    }
  },
  created() {
  },
  mounted() {
    this.circleTimer()

    request.get("/dashboard").then(res => {
      if (res.status == 200) {
        console.log(res.data)
        this.cards[0].data = res.data.lendRecordCount
        this.cards[1].data = res.data.visitCount
        this.cards[2].data = res.data.carCount
        this.cards[3].data = res.data.userCount

        this.maleCount = res.data.maleCount;
        this.femaleCount = res.data.femaleCount;

        this.weeklyCarRentals = res.data.weeklyCarRentals;

        this.carBrands = res.data.carBrands;

        this.initCharts();
      }
      else {
        ElMessage.error(res.message)
      }


      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(document.getElementById('main'))
      console.log(this.cards[0].data)
      // 绘制图表
      myChart.setOption({

        title: {
          text: '统计'
        },
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.cards.map(item => item.title),
          axisTick: {
            alignWithLabel: true
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            type: 'bar',
            label: { show: true },
            barWidth: '25%',
            data: [
              {
                value: this.cards[0].data,
                itemStyle: { color: '#5470c6' }
              },
              {
                value: this.cards[1].data,
                itemStyle: { color: '#91cc75' }
              },
              {
                value: this.cards[2].data,
                itemStyle: { color: '#fac858' }
              },
              {
                value: this.cards[3].data,
                itemStyle: { color: '#ee6666' }
              }
            ]
          }
        ]
      })
      window.addEventListener('resize', () => {
        myChart.resize()
      })

      // 基于准备好的dom，初始化echarts实例
      var myChartOfGender = echarts.init(document.getElementById('gender'))
      // 绘制图表
      myChartOfGender.setOption({

        title: {
          text: '租车人性别统计'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 40,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 1048, name: 'Search Engine' },
              { value: 735, name: 'Direct' },
              { value: 580, name: 'Email' },
              { value: 484, name: 'Union Ads' },
              { value: 300, name: 'Video Ads' }
            ]
          }
        ]
      })
      window.addEventListener('resize', () => {
        myChartOfGender.resize()
      })
    })

  },
  methods: {
    circleTimer() {
      this.getTimer()
      setInterval(() => {
        this.getTimer()
      }, 1000)
    },
    getTimer() {
      var d = new Date()
      var t = d.toLocaleString()
      document.getElementById('myTimer').innerHTML = t
    },
    initCharts() {
      // 图表1: 饼图
      const chart1 = echarts.init(this.$refs.chart1);
      chart1.setOption({
        title: {
          text: '租车人性别统计'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 40,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: this.maleCount, name: 'Male' },
              { value: this.femaleCount, name: 'Female' },
            ]
          }
        ]
      });

      // 图表2: 柱形图
      const chart2 = echarts.init(this.$refs.chart2);
      // console.log('柱形图', this.cards[0])
      chart2.setOption({
        title: {
          text: '统计'
        },
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.cards.map(item => item.title),
          axisTick: {
            alignWithLabel: true
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            type: 'bar',
            label: { show: true },
            barWidth: '25%',
            data: [
              {
                value: this.cards[0].data,
                itemStyle: { color: '#5470c6' }
              },
              {
                value: this.cards[1].data,
                itemStyle: { color: '#91cc75' }
              },
              {
                value: this.cards[2].data,
                itemStyle: { color: '#fac858' }
              },
              {
                value: this.cards[3].data,
                itemStyle: { color: '#ee6666' }
              }
            ]
          }
        ]
      });

      // 图表3: 折线图
      const chart3 = echarts.init(this.$refs.chart3);
      chart3.setOption({
        title: { text: '一周租赁汽车热度图' },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: this.weeklyCarRentals,
            type: 'line',
          },
        ],
      });

      // 图表4: 雷达图
      const chart4 = echarts.init(this.$refs.chart4);
      chart4.setOption({
        title: { text: '汽车品牌租赁热度图' },
        radar: {
          indicator: [
            { name: this.carBrands[0], max: 7500 },
            { name: this.carBrands[1], max: 15000 },
            { name: this.carBrands[2], max: 29000 },
            { name: this.carBrands[3], max: 39000 },
            { name: this.carBrands[4], max: 59000 },
            { name: this.carBrands[5], max: 22000 },
            { name: this.carBrands[6], max: 19000 },
          ],
        },
        series: [
          {
            name: '预算 vs 开销',
            type: 'radar',
            data: [
              {
                value: [4200, 3000, 20000, 35000, 50000, 18000],
                name: '预算分配',
              },
              {
                value: [5000, 14000, 28000, 26000, 42000, 21000],
                name: '实际开销',
              },
            ],
          },
        ],
      });
    },
  }
}
</script>

<style scoped>
.box-card {
  width: 80%;
  margin-bottom: 25px;
  margin-left: 10px;
}

.clearfix {
  text-align: center;
  font-size: 15px;
}

.text {
  text-align: center;
  font-size: 24px;
  font-weight: 700;
  vertical-align: super;
}

#main {
  width: 20%;
  height: 30%;
  margin-top: 20px;
}

#gender {
  width: 20%;
  height: 30%;
  margin-top: 20px;
}

#ma {
  width: 100%;
  height: 15%;
}

.icon {
  width: 50px;
  height: 50px;
  padding-top: 5px;
  padding-right: 10px;
}

.chart-container {
  width: 100%;
  height: 400px;
}
</style>