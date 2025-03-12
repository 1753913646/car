<template>
  <div style="height: 50px; line-height:50px; border-bottom: 1px solid #ADD8E6; display: flex;background-color: #ADD8E6">
    <div style="width: 200px; padding-left:30px; font-weight: bold; color:dodgerblue;display: flex;">
      <img :src="imgUrl" class="icon" >
      车辆馆管理系统</div>
    <div style="flex: 1; display: flex; align-items: center; justify-content: center;margin-top: 10px">
      <div id="ma" style="width: 100%; height: 80%;box-sizing: border-box;"></div>
    </div>

    <div style="width: 100px">
      <el-dropdown>
      <span class="el-dropdown-link">
        {{user.nickName}} <el-icon class="el-icon--right">
          <arrow-down />
          </el-icon>
      </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="exit">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import {ElMessage} from "element-plus";
import * as echarts from 'echarts'

export default {
  name: "Header",
  props: ['user'],
  data(){
    return{
      user:[],
      imgUrl:require("../assets/icon/Sedan.png")
    }
  },
  created(){
    let userStr = sessionStorage.getItem("user")||"{}"
    this.user = JSON.parse(userStr)
  },
  mounted() {
    // 基于准备好的dom，初始化echarts实例
    var myChart1 = echarts.init(document.getElementById('ma'))
    // 绘制图表
    myChart1.setOption({
      graphic: {
        elements: [
          {
            type: 'text',
            left: 'center',
            top: 'center',
            style: {
              text: 'apache 租车',
              fontSize: 30,
              fontWeight: 'bold',
              lineDash: [0, 100],
              lineDashOffset: 0,
              fill: 'transparent',
              stroke: '#ADD8E6',
              lineWidth: 1
            },
            keyframeAnimation: {
              duration: 8000,
              loop: false,
              keyframes: [
                {
                  percent: 0.5,
                  style: {
                    fill: '#000'
                  }
                }
              ]
            }

          }
        ]
      }
    })
    window.addEventListener('resize', () => {
      myChart1.resize()
    })
  },
  methods:{
    exit(){
      sessionStorage.removeItem("user")
      this.$router.push("/login")
      ElMessage.success("退出系统成功")
    }
  }

}
</script>

<style scoped>
.icon {
  width: 40px;
  height: 40px;
  padding-top: 5px;
  padding-right: 10px;
}

</style>
