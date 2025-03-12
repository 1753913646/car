<template>
  <div class="home_div">
    <div id="container" style="height: 100%; width: 100%"></div>
  </div>
</template>

<script>
import AMapLoader from "@amap/amap-jsapi-loader";

export default {
  props: {
    text: {
      type: String,
      default: ''
    }
  },
  data(){
    return{
      map: '',
      marker: {},
    }
  },
  methods: {
    pickLocationStop() {
      this.map.off('click', this.showLocation);
    },
    addMarker(maps, item) {
      let marker = new AMap.Marker({
        icon: 'src/assets/icons/ip.png',
        position: item.position,
        content: `
          <div class="marker">
            <div class="marker-index">
              <div class="title">${item.name}</div>
            </div>
            <div class="marker-content">
              <div class="note">${item.note.replaceAll('|', '<br>')}</div>
            </div>
          </div>`,
      });
      maps.add(marker);
      this.map.setCenter(item.position, false, 1000);
    },
    AmapIndex(row) {
      this.addMarker(this.map, {
        position: [row.x, row.y],
        name: row.lotName,
        note: ''
      });
    },
    emitPosition(positionPicked) {
      this.$emit('position-clicked', positionPicked);
    },
    initMap() {
      AMapLoader.load({
        key: "6c871689593b173d1ae05c269e97e51b",
        version: "2.0",
        plugins: [
          'AMap.ToolBar',
          'AMap.Scale',
          'AMap.Geolocation',
        ],
      })
          .then(maps => {
            this.map = new maps.Map('container', {
              center: [117.129533, 36.685668],
              zoom: 10
            });
            this.map.addControl(new AMap.Scale());
            this.map.addControl(new AMap.Geolocation());

            let geolocation = new AMap.Geolocation({
              enableHighAccuracy: true,
              timeout: 10000,
              buttonOffset: new AMap.Pixel(10, 20),
              zoomToAccuracy: true,
              buttonPosition: 'RB'
            });

            geolocation.getCurrentPosition((status, res) => {
              if (status === 'complete') {
                let center = [res.position.lng, res.position.lat];
                this.addMarker(this.map, {
                  position: center,
                  name: '我',
                  note: ''
                });
              }
            });

            this.map.addControl(geolocation);

            this.map.on('click', res => {
              var positionPicked = {
                lng: res.lnglat.lng,
                lat: res.lnglat.lat
              };

              if(this.text !== ''){
                this.map.remove(this.marker);
                const marker = new AMap.Marker({
                  position: new AMap.LngLat(res.lnglat.lng, res.lnglat.lat), //经纬度对象，也可以是经纬度构成的一维数组[116.39, 39.9]
                  title: this.text,
                });

                this.map.add(marker);
                this.marker = marker;

                this.emitPosition(positionPicked);
              }
            });
          })
          .catch(e => {
            console.log(e);
          });
    },

  },
  mounted() {
    this.initMap();
  },
  beforeUnmount() {
    this.map.destroy();
  }
};
</script>


<style>
.home_div {
  height: 80vh;
  width: 100%;
  padding: 0px;
  margin: 0px;
  position: relative;
}
html, body {
  height: 100%;
  margin: 0;
  padding: 0;
}

</style>

