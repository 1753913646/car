<template>
  <div class="layout-padding" style="height: 100%; width: 100%;">
    <div class="layout-padding-auto layout-padding-view" style="display: flex; flex-direction: row; height: 100%; width: 100%;">
      <!-- 左半区域：表格 -->
      <div class="table-container" style="flex: 1; margin-right: 5px;margin-left: 5px;margin-top: 10px;width: 60%;">
        <el-row v-show="showSearch">
          <el-form ref="queryRef" :inline="true" :model="state.queryForm" @keyup.enter="getDataList">
            <el-form-item prop="lotName">
              <el-input v-model="state.queryForm.lotName" clearable/>
            </el-form-item>
            <el-form-item>
              <el-button icon="Search" type="primary" @click="getDataList">{{}}</el-button>
              <el-button icon="Refresh" @click="resetQuery">{{  }}</el-button>
            </el-form-item>
          </el-form>
        </el-row>
        <el-row>
          <div class="mb8" style="width: 100%">
            <el-button icon="folder-add" type="primary" class="ml10" @click="this.$refs.formDialogRef.openDialog()" v-auth="'carParkingLot_carParkingLot_add'">
              新 增
            </el-button>
            <el-button plain :disabled="multiple" icon="Delete" type="primary" v-auth="'carParkingLot_carParkingLot_del'" @click="handleDelete(selectObjs)">
              删除
            </el-button>
            <right-toolbar v-model:showSearch="showSearch" :export="'carParkingLot_carParkingLot_export'" @exportExcel="exportExcel" class="ml10 mr20" style="float: right;" @queryTable="getDataList"></right-toolbar>
          </div>
        </el-row>
        <div class="table-container" style="height: 80%;width: 100%">
          <el-table
              :data="state.dataList"
              v-loading="state.loading"
              border
              :cell-style="tableStyle.cellStyle"
              :header-cell-style="tableStyle.headerCellStyle"
              @selection-change="selectionChangHandle"
              @row-click="handleRowClick"
              height="450px"
          >
            <el-table-column type="selection" width="40" align="center" />
            <el-table-column type="index" label="#" width="40" align="center" />
            <el-table-column prop="lotName" label="路段名称" show-overflow-tooltip align="center" />
            <el-table-column prop="x" label="路段x位置" show-overflow-tooltip align="center" />
            <el-table-column prop="y" label="路段Y位置" show-overflow-tooltip align="center" />
            <el-table-column prop="capacity" label="剩余" show-overflow-tooltip width="50" align="center" />
            <el-table-column label="操作" prop="selectPoint" width="180" align="center">
              <template #default="scope">
                <el-button
                    size="small"
                    icon="edit"
                    text
                    type="primary"
                    v-auth="'carParkingLot_carParkingLot_edit'"
                    @click="handleEdit(scope.row.lotId)"
                >
                  {{ text === '' ? '编辑' : '保存' }}
                </el-button>
                <el-button
                    size="small"
                    icon="delete"
                    text
                    type="primary"
                    v-auth="'carParkingLot_carParkingLot_del'"
                    @click="handleDelete([scope.row.lotId])"
                >
                  {{ text === '' ? '删除' : '取消' }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column label="选点" prop="selectPoint" width="100" align="center">
              <template #default="scope">
                <el-button
                    size="small"
                    icon="map-location"
                    text
                    type="warning"
                    @click="myAddress(scope.row)"
                >
                  选点
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <el-pagination
            v-model:current-page="state.pagination.current"
            v-model:page-size="state.pagination.size"
            :page-sizes="[5, 10, 20, 30]"
            :size="state.pagination.size"
            background="blue"
            layout="sizes, prev, pager, next"
            :total="state.pagination.total"
            @size-change="sizeChangeHandle"
            @current-change="currentChangeHandle"
        />
      </div>
      <!-- 右半区域：地图组件 -->
      <div class="map-container" style="height: 100%; width: 100%;margin-top: 10px">
        <!-- <el-input style="width: 200px" placeholder="输入较完整的地址" v-model="address"></el-input> -->
        <el-form inline  size="mini">
          <el-form-item class="mb-0" label="搜索地点:">
            <el-row>
              <div style="margin-right: 5px;">
                <el-input style="width: 200px" placeholder="输入较完整的地址" v-model="address.text"></el-input>
              </div>
              <div>
                <el-button  type="primary" @click="search"  icon="search">搜索</el-button>
              </div>
            </el-row>
          </el-form-item>
        </el-form>
        <div v-if="address.resultText !== ''" style="color: blue;">
          {{address.resultText}}
        </div>
        <Amap style="margin-top: 4px;" ref="amapRef" :text="text" @position-clicked="handlePositionClicked" />
      </div>
    </div>
    <!-- 编辑、新增表单 -->
    <From ref="formDialogRef" @refresh="getDataList(false)"/>
  </div>
</template>

<script>
import request from "@/utils/request";
import Amap from "@/views/carParkingLot/amap.vue";
import From from "./form.vue";

export default {
  components: {
    Amap,
    From
  },
  data() {
    return {
      state: {
        queryForm: {
          lotName: ''
        },
        dataList: [],
        loading: false,
        pagination: {
          current: 1,
          size: 10,
          total: 0
        }
      },
      tableStyle: {},
      selectObjs: [],
      multiple: true,
      showSearch: true,
      address: {
        resultText: '',
        text: ''
      },
      addressData: {},
      amapRef: null,
      text: '',
      lng: '',
      lat: ''
    };
  },
  created() {
    this.getDataList();
  },
  methods: {
    getDataList() {
      const query = {
        ...this.state.pagination,
        lotName: this.state.queryForm.lotName
      };

      request.get('/carLot/page', { params: query }).then(res => {
        this.state.dataList = res.data.list;
        this.state.pagination.total = res.data.total;
      }).catch(err => {
        console.error(err);
      });
    },
    resetQuery() {
      this.$refs.queryRef.resetFields();
      this.state.queryForm.lotName = '';
      this.getDataList();
    },
    search() {
      const url = 'https://restapi.amap.com/v5/place/text';
      request.get(url, {
        params: {
          key: "d3460720729a83b4a1ffaf4587983b70",
          keywords: this.address.text,
        }
      }).then(response => {
        const geoLocation = response.pois[0].location;
        const [lng, lat] = geoLocation.split(',').map(Number);
        const row = {
          x: lng,
          y: lat,
          lotName: this.address.text
        };
        this.$refs.amapRef.AmapIndex(row);
        this.address.resultText = `搜索结果: ${response.pois[0].name}`;
      }).catch(error => {
        console.error(error);
      });
    },
    exportExcel() {
      this.downBlobFile('/carParkingLot/carParkingLot/export', Object.assign(this.state.queryForm, { ids: this.selectObjs }), 'carParkingLot.xlsx');
    },
    selectionChangHandle(objs) {
      this.selectObjs = objs.map(({ lotId }) => lotId);
      this.multiple = !objs.length;
    },
    handleRowClick(row, column) {
      if (column.property === 'selectPoint') return;
      if (this.$refs.amapRef) this.$refs.amapRef.AmapIndex(row);
    },
    handleEdit(lotId){
      if(this.text !== ''){
        this.onSubmit();
        this.text = '';
        return;
      }

      this.$refs.formDialogRef.openDialog(lotId)
    },
    handleDelete(ids) {

      if(this.text !== ''){
        this.addressData.x = this.lng;
        this.addressData.y = this.lat;
        this.text = '';
        return;
      }

      this.$confirm('此操作将永久删除所选内容，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return request.post('/carLot/delByIds', ids);
      }).then(() => {
        this.$message.success('删除成功');
        this.getDataList();
      }).catch(err => {
        if (err) this.$message.error('删除失败');
      });
    },
    myAddress(row) {
        this.$message.success('请点击地图选点');
        this.addressData = row;
        this.text = row.lotName;
        this.lat = this.addressData.y;
        this.lng = this.addressData.x;
    },
    handlePositionClicked(data) {
      if (this.addressData) {
        this.addressData.x = data.lng;
        this.addressData.y = data.lat;
      }
    },
    currentChangeHandle(page) {
      this.state.pagination.current = page;
      this.getDataList();
    },
    sizeChangeHandle(size) {
      this.state.pagination.size = size;
      this.getDataList();
    },
    downBlobFile(url, data, filename) {
      // Implement file download logic here
    },
    onSubmit() {
      request.put('/carLot',this.addressData).then(() => {
        this.$message.success('选点成功');
        this.getDataList();
        this.addressData = ''
      }).catch(err => {
        console.error(err);
      });
    },
  }
};
</script>

<style scoped>
.layout-padding-auto {
  margin: 0 auto;
}


</style>
