<template>
  <div class="home" style="padding: 10px">
    <!-- 搜索-->
    <div style="margin: 10px 0;">

      <el-form inline="true" size="small">
        <el-form-item label="车辆编号">
          <el-input v-model="search1" placeholder="请输入车辆编号" clearable>
            <template #prefix><el-icon class="el-input__icon">
                <search />
              </el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item label="用户" v-if="user.role == 1">
          <el-input v-model="search2" placeholder="请输入用户昵称" clearable>
            <template #prefix><el-icon class="el-input__icon">
                <search />
              </el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="margin-left: 1%" @click="load" size="mini">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="danger" @click="clear">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 按钮-->
    <div style="margin: 10px 0;">
      <el-popconfirm title="确认删除?" @confirm="deleteBatch" v-if="user.role == 1">
        <template #reference>
          <el-button type="danger" size="mini">批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>
    <!-- 数据字段-->
    <el-table :data="tableData" stripe border="true" @selection-change="handleSelectionChange">
      <el-table-column v-if="user.role == 1" type="selection" width="55">
      </el-table-column>
      <el-table-column prop="carId" label="车辆编号" sortable />
      <el-table-column prop="user.nickName" label="用户" />
      <el-table-column prop="rentalStart" label="出租时间" />
      <!--      <el-table-column prop="rentalEnd" label="最迟归还日期" />-->
      <el-table-column prop="extensionCount" label="可续借次数" />
      <el-table-column fixed="right" label="操作">
        <template v-slot="scope">
          <el-button size="mini" @click="handleEdit(scope.row)" v-if="user.role == 1">修改</el-button>
          <el-popconfirm title="确认删除?" @confirm="handleDelete(scope.row)" v-if="user.role == 1">
            <template #reference>
              <el-button type="danger" size="mini">删除</el-button>
            </template>
          </el-popconfirm>
          <el-popconfirm title="确认续借(续借一次延长10天)?" @confirm="handlereProlong(scope.row)" v-if="user.role == 2"
            :disabled="scope.row.prolong == 0">
            <template #reference>
              <el-button type="danger" size="mini" :disabled="scope.row.prolong == 0">续借</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!--    分页-->
    <div style="margin: 10px 0">
      <el-pagination v-model:currentPage="currentPage" :page-sizes="[5, 10, 20]" :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange">
      </el-pagination>

      <el-dialog v-model="dialogVisible2" title="修改租凭信息" width="30%">
        <el-form :model="form" label-width="120px">

          <el-form-item label="车辆编号">
            <el-input style="width: 80%" v-model="form.carId"></el-input>
          </el-form-item>

          <el-form-item label="用户">
            <el-input style="width: 80%" v-model="form.nickName"></el-input>
            <!-- <el-input style="width: 80%" v-model="form.user.nickName"></el-input> -->
          </el-form-item>

          <el-form-item label="出租时间">
            <el-date-picker v-model="form.rentalStart" type="datetime" value-format="YYYY-MM-DD HH:mm:ss">
            </el-date-picker>
          </el-form-item>

          <el-form-item label="最迟归还日期">
            <el-date-picker v-model="form.rentalEnd" type="datetime" value-format="YYYY-MM-DD HH:mm:ss">
            </el-date-picker>
          </el-form-item>

          <el-form-item label="续借次数">
            <el-input style="width: 80%" v-model="form.extensionCount"></el-input>
          </el-form-item>

        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible2 = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </span>
        </template>
      </el-dialog>

    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import request from "../utils/request";
import { ElMessage } from "element-plus";
import moment from "moment";
export default {
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
    this.load()
  },
  name: 'carwithuser',
  methods: {

    handleSelectionChange(val) {
      this.forms = val
    },
    

    // 批量删除
    async deleteBatch() {
      if (!this.forms.length) {
        ElMessage.warning("请选择数据！")
        return
      }
      let res = await request.post("carwithuser/deleteRecords", this.forms)
      if (res.status == 200) {
        ElMessage.success("批量删除成功")
        this.load()
      }
      else {
        ElMessage.error(res.msg)
      }

    },


    load() {
      if (this.user.role == 1) {
        request.get("/carwithuser", {
          params: {
            pageNum: this.currentPage,
            pageSize: this.pageSize,
            carId: this.search1,
            userId: this.search2,
            search3: this.search3,
          }
        }).then(res => {
          console.log(res)
          this.tableData = res.data.list
          this.total = res.data.total
        })
      }
      else {
        request.get("/carwithuser", {
          params: {
            pageNum: this.currentPage,
            pageSize: this.pageSize,
            carId: this.search1,
            userId: this.search2,
            search3: this.user.id,
          }
        }).then(res => {
          console.log(res)
          // 过滤数据，只显示与当前用户 nickName 相同的记录
          // this.tableData = res.data.list.filter(record => record.nickName = record.user.nickName);
          this.tableData = res.data.list
          console.log(this.tableData)
          this.total = res.data.total
        })
      }
    },


    clear() {
      this.search1 = ""
      this.search2 = ""
      this.search3 = ""
      this.load()
    },




    // 自己修改的内容开始
    async handleDelete(row) {
      let form3 = JSON.parse(JSON.stringify(row))
      this.id = form3.id
      try {
        let res = await request.post("/carwithuser/deleteRecord", { id: this.id })
        if (res.status === 200) {
          ElMessage.success("删除成功!")
          this.load()
        } else {
          ElMessage.error(res.message)
        }
      } catch (error) {
        ElMessage.error("删除失败!" + error.message)
      }
    },
    add() {
      this.dialogVisible2 = true
      this.form = {}
    },
    // 自己修改的内容结束


    handlereProlong(row) {
      var nowDate = new Date(row.deadtime);
      nowDate.setDate(nowDate.getDate() + 30);
      // row.deadtime = moment(nowDate).format("yyyy-MM-DD HH:mm:ss");
      if (row.extensionCount === 0) {
        ElMessage({
          message: '次数不足',
          type: 'error',
        })
        return;
      }
      row.extensionCount = row.extensionCount - 1;
      request.post("/carwithuser", row).then(res => {
        console.log(res)
        if (res.status == 200) {
          ElMessage({
            message: '续借成功',
            type: 'success',
          })
        }
        else {
          ElMessage.error(res.msg)
        }
        this.load()
        this.dialogVisible2 = false
      })
    },

    async save() {
      let ret = await request.post("/carwithuser", this.form).then(res => {
        console.log(res)
        if (res.status == 200) {
          ElMessage({
            message: '信息修改成功',
            type: 'success',
          })
        }
        else {
          ElMessage.error(res.msg)
        }
        this.load()
        this.dialogVisible2 = false
      })
    },


    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible2 = true
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },

  },
  data() {
    return {
      form: {},
      form2: {},
      form3: {},
      dialogVisible: false,
      dialogVisible2: false,
      search1: '',
      search2: '',
      search3: '',
      total: 10,
      currentPage: 1,
      pageSize: 10,
      tableData: [],
      user: {},
      forms: [],
    }
  },
}
</script>
