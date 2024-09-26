import { createRouter, createWebHistory } from 'vue-router'
import loginView from '../views/loginView.vue'
import errorView from '../views/ErrorView.vue'
//import SigninView from '../views/SigninView.vue'
import patientDataView from '../views/patientDataView.vue'
import userTableView from '../views/userTableView.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/login",
      component: loginView,
      props: () => ({ loginRoute: "/api/login" })
    },{
      path: "/error",
      component: errorView,
      props: route=> ({ Response: JSON.parse(route.query.Response) })
    },
    {
      path:"/patientData",
      component:patientDataView,
      props: () => ({ patientManagerUrl: "/api/patient" })
    },{
      path: "/userTable",
      component: userTableView,
      props: () => ({ apiGatewayUser: "/api/user" })
    }
  ]
})

export default router
