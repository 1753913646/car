<template>
  <div class="home" style ="padding: 10px">
    <!-- <div class="home" style="padding: 10px; display: flex; flex-direction: column; min-height: 50vh;">-->
       <!-- 搜索-->
    <div style="margin: 10px 0;">
      <el-form inline="true" size="small">
        <el-form-item label="订单编号" >
          <el-input v-model="search1" placeholder="请输入订单编号"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search/></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item label="车辆编号" >
          <el-input v-model="search2" placeholder="请输入车辆编号"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item label="客户编号" >
          <el-input v-model="search3" placeholder="请输入客户编号"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search /></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item>
          <el-button type="primary" style="margin-left: 1%" @click="load" size="mini">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="mini"  type="danger" @click="clear">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

<!--按钮-->
    <div style="margin: 10px 0;" v-if="user.role == 1">
      <el-popconfirm title="确认删除?" @confirm="deleteBatch" >
        <template #reference>
          <el-button type="danger" size="mini" >批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>
    <!-- 数据字段-->

   <el-table :data="tableData" stripe border="true" @selection-change="handleSelectionChange">
     <!-- <el-table :data="tableData" stripe border="true" @selection-change="handleSelectionChange" style="flex-grow: 1;">-->
      <el-table-column v-if="user.role ==1"
                       type="selection"
                       width="55">
      </el-table-column>
      <el-table-column prop="rentalId" label="订单编号" sortable />
      <el-table-column prop="carId" label="车辆编号" />
      <el-table-column prop="userId" label="客户编号" sortable/>
      <el-table-column prop="rentalStart" label="出租时间" sortable/>
      <el-table-column prop="rentalEnd" label="归还时间" sortable/>
      <el-table-column prop="status" label="状态" >
        <template v-slot="scope">
          <el-tag v-if="scope.row.status == 0" type="warning">出租中</el-tag>
          <el-tag v-else type="success">已还车</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="user.role === 1" label="操作" width="150px">
        <template v-slot="scope">
          <el-button  size="mini" @click ="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确认删除?" @confirm="handleDelete(scope.row)">
            <template #reference>
              <el-button type="danger" size="mini" >删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!--    分页-->
    <div style="margin: 10px 0">
      <el-pagination
          v-model:currentPage="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      >
      </el-pagination>


      <el-dialog v-model="dialogVisible" title="修改租凭记录" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="出租时间" >
            <el-date-picker
                v-model="form.rentalStart"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="归还时间" >

            <el-date-picker
                v-model="form.rentalEnd"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
            >
            </el-date-picker>

          </el-form-item>
          <el-form-item label="是否归还" prop="status">
            <el-radio v-model="form.status" label="0">未归还</el-radio>
            <el-radio v-model="form.status" label="1">已归还</el-radio>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save(form.rentalId)">确 定</el-button>
      </span>
        </template>
      </el-dialog>
    </div>
  </div>

</template>

<script >

import request from "../utils/request";
import {ElMessage} from "element-plus";
import { defineComponent, reactive, toRefs } from 'vue'

export default defineComponent({

  created(){
    this.load()
    let userStr = sessionStorage.getItem("user") ||"{}"
    this.user = JSON.parse(userStr)
  },
  name: 'LendRecord',
  methods: {
    handleSelectionChange(val){
      this.forms = val
    },
    async deleteBatch(){
      if(!this.forms.length){
        ElMessage.warning("请选择数据！")
        return
      }
      let res= await request.post("/LendRecord/deleteRecords",this.forms)
        if(res.status == 200){
          ElMessage.success("批量删除成功")
          this.load()
        }
        else {
          ElMessage.error(res.msg)
        }
    },
    load(){
      request.get("/RentalRecord",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          rentalId: this.search1,
          carId: this.search2,
          userId: this.search3
        }
      }).then(res =>{
        console.log(res)
        this.tableData = res.data.list
        this.total = res.data.total
      })
    },

    // save(rentalId){
    //   console.log(this.form.rentalId)
    //   //ES6语法
    //   //地址,但是？IP与端口？+请求参数
    //   // this.form?这是自动保存在form中的，虽然显示时没有使用，但是这个对象中是有它的
    //   if(this.form.rentalId){
    //     request.post("/RentalRecord/" + rentalId, this.form).then(res => {
    //       console.log(res)
    //       if (res.code == 0) {
    //         ElMessage({
    //           message: '新增成功',
    //           type: 'success',
    //         })
    //       } else {
    //         ElMessage.error(res.msg)
    //       }
    //
    //       this.load() //不知道为啥，更新必须要放在这里面
    //       this.dialogVisible = false
    //     })
    //   }
    //   else {
    //     request.put("/LendRecord/" + rentalId, this.form).then(res => {
    //       console.log(res)
    //       if (res.code == 0) {
    //         ElMessage({
    //           message: '更新成功',
    //           type: 'success',
    //         })
    //       } else {
    //         ElMessage.error(res.msg)
    //       }
    //
    //       this.load() //不知道为啥，更新必须要放在这里面
    //       this.dialogVisible2 = false
    //     })
    //   }
    //
    // },

    save(rentalId){
      console.log(this.form.rentalId);

      if(this.form.rentalId){
        request.put("/RentalRecord/" + rentalId, this.form).then(res => {
          console.log(res);
          if (res.status == 200) {
            ElMessage({
              message: '更新成功',
              type: 'success',
            });
          } else {
            ElMessage.error(res.msg);
          }

          this.load();
          this.dialogVisible = false;
        });
      } else {
        request.post("/RentalRecord", this.form).then(res => {
          console.log(res);
          if (res.code == 0) {
            ElMessage({
              message: '新增成功',
              type: 'success',
            });
          } else {
            ElMessage.error(res.msg);
          }

          this.load();
          this.dialogVisible = false;
        });
      }
    },


    clear(){
      this.search1 = ""
      this.search2 = ""
      this.search3 = ""
      this.load()
    },
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      this.pageNum = pageNum
      this.load()
    },
    async handleDelete(row){
     let form3 = JSON.parse(JSON.stringify(row))
      this.id=form3.rentalId
      // console.log(typeof this.id)
      let res=await request.post("/RentalRecord/deleteRecord",this.id)
        if(res.status == 200 ){
          ElMessage.success("删除成功")
          this.load()
        }
        else
          ElMessage.error(res.message)
    },
    // handleDelete(row){
    //   const form3 = JSON.parse(JSON.stringify(row))
    //   this.id=form3.rentalId
    //   console.log(row)
    //   request.post("RentalRecord/deleteRecord",form3).then(res =>{
    //     console.log(res)
    //     if(res.code == 0 ){
    //       ElMessage.success("删除成功")
    //     }
    //     else
    //       ElMessage.error(res.msg)
    //     this.load()
    //   })
    // },
    add(){
      this.dialogVisible2 = true
      this.form ={}
    }
  },

  setup() {
    const state = reactive({
      shortcuts: [
        {
          text: 'Today',
          value: new Date(),
        },
        {
          text: 'Yesterday',
          value: () => {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24)
            return date
          },
        },
        {
          text: 'A week ago',
          value: () => {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
            return date
          },
        },
      ],
      value1: '',
      value2: '',
      value3: '',
      defaultTime: new Date(2000, 1, 1, 12, 0, 0), // '12:00:00'
    })

    return toRefs(state)
  },
  data() {
    return {
      form: {},
      search1:'',
      search2:'',
      search3:'',
      total:10,
      currentPage:1,
      pageSize: 10,
      tableData: [],
      user:{},
      dialogVisible : false,
      dialogVisible2: false

    }
  },

})
</script>
