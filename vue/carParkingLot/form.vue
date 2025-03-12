<template>
    <el-dialog :title="form.lotId ? '编辑' : '新增'" v-model="visible"
      :close-on-click-modal="false" draggable>
      <el-form ref="dataFormRef" :model="form" :rules="dataRules" formDialogRef label-width="90px" v-loading="loading">
       <el-row :gutter="24">
    <el-col :span="24" class="mb20">
      <el-form-item label="路段名称" prop="lotName">
        <el-input v-model="form.lotName" placeholder="请输入路段名称"/>
      </el-form-item>
      </el-col>

    <el-col :span="24" class="mb20">
      <el-form-item label="路段x位置" prop="x">
        <el-input v-model="form.x" placeholder="请输入路段x位置"/>
      </el-form-item>
      </el-col>

    <el-col :span="24" class="mb20">
      <el-form-item label="路段Y位置" prop="y">
        <el-input v-model="form.y" placeholder="请输入路段Y位置"/>
      </el-form-item>
      </el-col>

    <el-col :span="24" class="mb20">
      <el-form-item label="可用车辆" prop="capacity">
        <el-input v-model="form.capacity" placeholder="请输入可用车辆数量"/>
      </el-form-item>
      </el-col>

    <el-col :span="24" class="mb20">
      <el-form-item label="状态" prop="state">
        <!-- <el-input v-model="form.state" placeholder="请输入状态（0正常 1停用）"/> -->
        <el-radio-group v-model="form.state">
          <el-radio :label="0">正常</el-radio>
          <el-radio :label="1">停用</el-radio>
        </el-radio-group>
      </el-form-item>
      </el-col>

			</el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="visible = false">取消</el-button>
          <el-button type="primary" @click="onSubmit" :disabled="loading">确认</el-button>
        </span>
      </template>
    </el-dialog>
</template>

<script>
import request from "@/utils/request";

export default {
  name: 'from',
  data() {
    return {
      form: {
        lotId: '',
        lotName: '',
        x: '',
        y: '',
        capacity: '',
        state: '',
      },
      dataRules: {
        lotName: [{ required: true, message: '路段名称不能为空', trigger: 'blur' }],
        x: [{ required: true, message: '路段x位置不能为空', trigger: 'blur' }],
        y: [{ required: true, message: '路段Y位置不能为空', trigger: 'blur' }],
        capacity: [{ required: true, message: '路段容量不能为空', trigger: 'blur' }],
        state: [{ required: true, message: '状态不能为空', trigger: 'blur' }]
      },
      visible: false,
      loading: false,
      dataFormRef: null,
    };
  },
  methods: {
    openDialog(id) {
      this.visible = true;
      this.form.lotId = '';

      this.$nextTick(() => {
        this.$refs.dataFormRef.resetFields();
      });

      if (id) {
        this.form.lotId = id;
        this.getcarParkingLotData(id);
      }
    },
    getcarParkingLotData(id) {
      this.loading = true;
      request.get('/carLot/' + id)
          .then(res => {
            Object.assign(this.form, res.data);
          })
          .finally(() => {
            this.loading = false;
          });
    },
    onSubmit() {
      this.$refs.dataFormRef.validate().then(() => {
        this.loading = true;
        const apiCall = this.form.lotId ? request.put('/carLot',this.form) : request.post('/carLot', this.form);
        apiCall
            .then(() => {
              this.$message.success(this.form.lotId ? '修改成功' : '添加成功');
              this.visible = false;
              this.$emit('refresh');
            })
            .catch(err => {
              this.$message.error(err.msg);
            })
            .finally(() => {
              this.loading = false;
            });
      });
    }
  }
};

</script>