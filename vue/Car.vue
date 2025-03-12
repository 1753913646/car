<template>
  <div class="home" style="padding: 10px">
    <!-- 搜索-->
    <div style="margin: 10px 0">
      <el-form inline="true" size="small">
        <el-form-item label="车牌号">
          <el-input v-model="query.licensePlate" placeholder="请输入车牌号" clearable>
            <template #prefix
              ><el-icon class="el-input__icon"><search /></el-icon
            ></template>
          </el-input>
        </el-form-item>
        <el-form-item label="车型">
          <el-input v-model="query.model" placeholder="请输入车型" clearable>
            <template #prefix
              ><el-icon class="el-input__icon"><search /></el-icon
            ></template>
          </el-input>
        </el-form-item>
        <el-form-item label="制造商">
          <el-input v-model="query.manufacturer" placeholder="请输入制造商" clearable>
            <template #prefix
              ><el-icon class="el-input__icon"><search /></el-icon
            ></template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            style="margin-left: 1%"
            @click="load"
            size="mini"
          >
            <svg-icon iconClass="search" />查询
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="danger" @click="clear">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right" v-if="numOfOutDataCar != 0">
          <el-popconfirm
            confirm-button-text="查看"
            cancel-button-text="取消"
            :icon="InfoFilled"
            icon-color="red"
            title="您有车辆已逾期，请尽快归还"
            @confirm="toLook"
          >
            <template #reference>
              <el-button type="warning">逾期通知</el-button>
            </template>
          </el-popconfirm>
        </el-form-item>
      </el-form>
    </div>
    <!-- 按钮-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add" v-if="user.role == 1"
        >上架</el-button
      >
      <el-popconfirm
        title="确认删除?"
        @confirm="deleteBatch"
        v-if="user.role == 1"
      >
        <template #reference>
          <el-button type="danger" size="mini">批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>
    <!-- 数据字段-->
    <el-table
      :data="tableData"
      stripe
      :border="true"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        v-if="user.role == 1"
        type="selection"
        width="55"
      ></el-table-column>
      <el-table-column prop="id" label="车辆编号" sortable />
      <el-table-column prop="licensePlate" label="车牌号" />
      <el-table-column prop="model" label="车型" />
      <el-table-column prop="manufacturer" label="制造商" />
      <el-table-column prop="pricePerDay" label="价格" sortable />
      <el-table-column prop="status" label="状态">
        <template v-slot="scope">
          <el-tag v-if="scope.row.status == 0" type="warning">已租借</el-tag>
          <el-tag v-else type="success">未租借</el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作">
        <template v-slot="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.row)"
            v-if="user.role == 1"
            >修改</el-button
          >
          <el-popconfirm
            title="确认删除?"
            @confirm="handleDelete(scope.row.id)"
            v-if="user.role == 1"
          >
            <template #reference>
              <el-button type="danger" size="mini">删除</el-button>
            </template>
          </el-popconfirm>
          <el-button
              type="primary"
            size="mini"
            @click="handleLend(scope.row.id, scope.row.id, scope.row.model)"
            v-if="user.role == 2"
            :disabled="scope.row.status == 0"
            >租借</el-button
          >
          <el-popconfirm
            title="确认还车?"
            @confirm="handleReturn(scope.row.id, scope.row)"
            v-if="user.role == 2"
            :disabled="scope.row.status == 1"
          >
            <template #reference>
              <el-button
                type="danger"
                size="mini"
                :disabled="
                  this.carIdArray.has(scope.row.carId) ||
                  scope.row.status == 1
                "
                >还车</el-button
              >
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- 逾期详情对话框-->
    <el-dialog
      v-model="dialogVisible3"
      v-if="numOfOutDataCar != 0"
      title="逾期详情"
      width="50%"
      :before-close="handleClose"
    >
      <el-table :data="outDateCar" style="width: 100%">
        <el-table-column prop="carId" label="车辆编号" />
        <el-table-column prop="carName" label="车辆名称" />
        <el-table-column prop="lendTime" label="租借日期" />
        <el-table-column prop="deadTime" label="截至日期" />
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="dialogVisible3 = false"
            >确认</el-button
          >
        </span>
      </template>
    </el-dialog>
    <!-- 分页-->
    <div style="margin: 10px 0">
      <el-pagination
        v-model:currentPage="currentPage"
        :page-sizes="[5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      ></el-pagination>
      <!-- 上架车辆对话框-->
      <el-dialog v-model="dialogVisible" title="上架车辆" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="车辆编号">
            <el-input style="width: 80%" v-model="form.id"></el-input>
          </el-form-item>
          <el-form-item label="车牌号">
            <el-input style="width: 80%" v-model="form.licensePlate"></el-input>
          </el-form-item>
          <el-form-item label="车型">
            <el-input style="width: 80%" v-model="form.model"></el-input>
          </el-form-item>
          <el-form-item label="制造商">
            <el-input style="width: 80%" v-model="form.manufacturer"></el-input>
          </el-form-item>
          <el-form-item label="价格">
            <el-input style="width: 80%" v-model="form.pricePerDay"></el-input>
          </el-form-item>
          <el-form-item label="生产年份">
            <el-input
              style="width: 80%"
              v-model="form.productionYear"
            ></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="save1">确定</el-button>
          </span>
        </template>
      </el-dialog>
      <!-- 修改车辆信息对话框-->
      <el-dialog v-model="dialogVisible2" title="修改车辆信息" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="车辆编号">
            <el-input style="width: 80%" v-model="form.id"></el-input>
          </el-form-item>
          <el-form-item label="车牌号">
            <el-input style="width: 80%" v-model="form.licensePlate"></el-input>
          </el-form-item>
          <el-form-item label="车型">
            <el-input style="width: 80%" v-model="form.model"></el-input>
          </el-form-item>
          <el-form-item label="制造商">
            <el-input style="width: 80%" v-model="form.manufacturer"></el-input>
          </el-form-item>
          <el-form-item label="价格">
            <el-input style="width: 80%" v-model="form.pricePerDay"></el-input>
          </el-form-item>
          <el-form-item label="生产年份">
            <el-input
              style="width: 80%"
              v-model="form.productionYear"
            ></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible2 = false">取消</el-button>
            <el-button type="primary" @click="save2">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from "../utils/request";
import { ElMessage } from "element-plus";
import moment from "moment";

export default {
  created() {
    let userStr = sessionStorage.getItem("user") || "{}";
    this.user = JSON.parse(userStr);
    this.user.role = 1
    this.load();
  },
  name: "Car",
  methods: {
    handleSelectionChange(val) {
      this.ids = val.map((v) => v.id);
    },
    async deleteBatch() {
      if (!this.ids.length) {
        ElMessage.warning("请选择数据！");
        return;
      }
      let res= await request.post("/car/deleteBatch", this.ids)
        if (res.status == 200) {
          ElMessage.success("批量删除成功");
          this.load();
        } else {
          ElMessage.error(res.message);
        }
    },
    async load() {
      this.numOfOutDataCar = 0;
      this.outDateCar = [];

      let url = "/cars/number/" + this.currentPage + "/size/" + this.pageSize;
      let res = await request.post(url, this.query);
      this.tableData = res.data.list;
      this.total = res.data.total;

      // request
      //   .get("/car", {
      //     params: {
      //       pageNum: this.currentPage,
      //       pageSize: this.pageSize,
      //       search1: this.search1,
      //       search2: this.search2,
      //       search3: this.search3,
      //     },
      //   })
      //   .then((res) => {
      //     this.tableData = res.data.records;
      //     this.total = res.data.total;
      //   });

      // if (this.user.role == 2) {
      //   request
      //     .get("/carwithuser", {
      //       params: {
      //         pageNum: "1",
      //         pageSize: this.total,
      //         search1: "",
      //         search2: "",
      //         search3: this.user.id,
      //       },
      //     })
      //     .then((res) => {
      //       this.carData = res.data.records;
      //       this.number = this.carData.length;
      //       var nowDate = new Date();
      //       this.carIdArray = new Map();
      //       for (let i = 0; i < this.number; i++) {
      //         this.carIdArray.set(this.carData[i].carId, this.carData[i].userId);
      //         let dDate = new Date(this.carData[i].deadTime);
      //         if (dDate < nowDate) {
      //           this.outDateCar[this.numOfOutDataCar] = {
      //             carId: this.carData[i].carId,
      //             carName: this.carData[i].carName,
      //             deadTime: this.carData[i].deadTime,
      //             lendTime: this.carData[i].lendTime,
      //           };
      //           this.numOfOutDataCar = this.numOfOutDataCar + 1;
      //         }
      //       }
      //     });
      // }
    },
    clear() {
      // this.search1 = "";
      // this.search2 = "";
      // this.search3 = "";
      this.query = {}
      this.load();
    },
    async handleDelete(id) {
      let res = await request.delete("car/" + id)
        if (res.status == 200) {
          ElMessage.success("删除成功");
          this.load();
        } else ElMessage.error(res.message);
    },
    handleReturn(id, row) {
      this.form = {
        ...row
      };
      this.form.status = "1";
      this.form.id = id;
      request.put("/car", this.form).then((res) => {
        if (res.code == 0) {
          ElMessage({
            message: "还车成功",
            type: "success",
          });
        } else {
          ElMessage.error(res.msg);
        }
        let endDate = moment(new Date()).format("yyyy-MM-DD HH:mm:ss");
        this.form3 = {
          rentalEnd: endDate,
        };
        this.form3.userId = this.user.id;
        this.form3.carId = id;
        request.put("/RentalRecord", this.form3).then((res) => {
          request.post("/carwithuser/deleteRecord", this.form3).then((res) => {
            this.load();
          });
        });
      });
    },
    handleLend(id, carId, name) {
      if (this.number == 5) {
        ElMessage.warning("您不能再租借更多的车辆了");
        return;
      }
      if (this.numOfOutDataCar != 0) {
        ElMessage.warning("在您归还逾期车辆前不能再租借车辆");
        return;
      }
      this.form.status = "0";
      this.form.id = id;
      request.put("/car", this.form).then((res) => {
        if (res.code == 0) {
          ElMessage({
            message: "租借成功",
            type: "success",
          });
        } else {
          ElMessage.error(res.msg);
        }
      });

      this.form2.status = "0";
      this.form2.carId = carId;
      this.form2.carName = name;
      this.form2.userId = this.user.id;
      this.form2.status = 0;
      let startDate = moment(new Date()).format("yyyy-MM-DD HH:mm:ss");
      this.form2.rentalStart = startDate;
      request.post("/RentalRecord", this.form2).then((res) => {
        this.load();
      });
      let form3 = {};
      form3.carId = carId;
      form3.carName = name;
      form3.nickName = this.user.username;
      form3.userId = this.user.id;
      let nowDate = new Date(startDate);
      nowDate.setDate(nowDate.getDate() + 30);
      form3.rentalStart = moment(nowDate).format("yyyy-MM-DD HH:mm:ss");
      form3.extensionCount = 1;
      request.post("/carwithuser/insertNew", form3).then((res) => {
        this.load();
      });
    },
    add() {
      this.dialogVisible = true;
      this.form = {};
    },
    async save1() {
      if (this.form.productionYear) {
        this.form.productionYear = moment(this.form.productionYear).format(
          "YYYY"
        );
      }
      if (this.form.id) {
        this.form.status = 1;
        this.form.rental_count = 0;
        let res2= await request.post("/car/add", this.form)
        if (res2.status == 200) {
            ElMessage.success("上架车辆成功");
            this.load();
            this.dialogVisible = false;
          } else {
            ElMessage.error(res2.message);
          }
      }else{
        this.form.status = 1;
        this.form.rental_count = 0;
        let res2= await request.post("/car/add", this.form)
        if (res2.status == 200) {
            ElMessage.success("上架车辆成功");
            this.load();
            this.dialogVisible = false;
          } else {
            ElMessage.error(res2.message);
          }
      }
    },
    async save2() {
      if (this.form.productionYear) {
        this.form.productionYear = moment(this.form.productionYear).format(
          "YYYY"
        );
      }
      if (this.form.id) {
        let res= await request.put("/car", this.form)
          if (res.status == 200) {
            ElMessage.success("修改车辆信息成功");
            this.load();
            this.dialogVisible2 = false;
          } else {
            ElMessage.error(res.message);
          }
      }
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogVisible2 = true;
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
    toLook() {
      this.dialogVisible3 = true;
    },
  },
  data() {
    return {
      form: {},
      form2: {},
      form3: {},
      dialogVisible: false,
      dialogVisible2: false,
      query: {},
      // search1: "",
      // search2: "",
      // search3: "",
      total: 10,
      currentPage: 1,
      pageSize: 10,
      tableData: [],
      user: {},
      number: 0,
      carData: [],
      carIdArray: new Map(),
      outDateCar: [],
      numOfOutDataCar: 0,
      dialogVisible3: true,
    };
  },
};
</script>
