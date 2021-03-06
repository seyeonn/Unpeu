import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
// import FontAwesomeIcon from "@fortawesome/vue-fontawesome";
import { library as faLibrary } from "@fortawesome/fontawesome-svg-core";
import { faHome, faSearch, faPlus } from "@fortawesome/free-solid-svg-icons";
import VueSweetalert2 from "vue-sweetalert2";
import "sweetalert2/dist/sweetalert2.min.css";
import VueGtag from 'vue-gtag'

faLibrary.add(faHome, faSearch, faPlus);
// Vue.component("font-awesome-icon", FontAwesomeIcon);

const options = {
  confirmButtonColor: "#4CAF50",
  cancelButtonColor: "#F78780",
};

Vue.use(VueSweetalert2, options);
Vue.use(VueGtag, {
  config: { 
    id: 'G-GVRMS51PYC',
  },
}, router);
Vue.config.productionTip = false;

new Vue({
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount("#app");
