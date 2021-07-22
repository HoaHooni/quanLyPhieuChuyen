import { exact } from "prop-types";
import React, { lazy } from "react";
import { TheLayout } from "./containers";



const Dashboard = React.lazy(() => import("./views/dashboard/Dashboard"));

//Quản lý phiếu chuyển
const Transfers = React.lazy(() => import("./views/transfer/Index"));
const TransferAdd = React.lazy(() => import("./views/transfer/Add"));
const TransferDetail = React.lazy(() => import("./views/transfer/Detail"));
// const DemoAdd = React.lazy(() => import("./views/form/Add"));

//Quản lý tài khoản

const Users = React.lazy(() => import("./views/user/Index"));
const UserAdd = React.lazy(() => import("./views/user/Add"));
const UserUpdate = React.lazy(() => import("./views/user/Update"));
//Quản lý chi nhánh
const HistoryDetails = React.lazy(() => import("./views/inventory/history/HistoryDetails"));
const Inventories = React.lazy(() => import("./views/inventory/Index"));
const InventoriesDetail = React.lazy(() => import("./views/inventory/InventoryDetails/InventoryDetails"))
const InventoriesAdd = React.lazy(() => import("./views/inventory/InventoryAdd"));
//Quản lý danh mục
const Product = React.lazy(() => import("./views/product/Index"));
const ProductAdd = React.lazy(() => import("./views/product/Add"));
const ProductExport = React.lazy(() => import("./views/product/Export"));
const ProductImport = React.lazy(() => import("./views/product/Import"));
const ProductDetail = React.lazy(() => import("./views/product/Detail"));
//Thống kê

const StatisticImport = React.lazy(() => import("./views/statistic/Import"));
const StatisticProduct = React.lazy(() => import("./views/statistic/Product"));
const StatisticInventory = React.lazy(() => import("./views/statistic/Inventory"));


//Đổi mật khẩu
const ChangePass = React.lazy(() => import("./views/user/ChangePass"));

const Profile = React.lazy(() => import("./views/user/Profile"));




const routes = [


  //login
  // {path: "/login",name: "Login",component: Login},

  { path: "/", exact: true, name: "Trang chủ", component: Dashboard, role: [1, 2, 3] },
  { path: "/dashboard", name: "", component: Dashboard, role: [1, 2, 3] },
  { path: "/products/list", name: "Quản lý sản phẩm", component: Product, role: [1, 2, 3] },

  { path: "/products/add", name: "Thêm sản phẩm", component: ProductAdd, role: [3] },
  { path: "/products/:inventory/:id/:idDetail", name: "Xem chi tiết sản phẩm", component: ProductDetail, role: [1, 2, 3] },



  { path: "/transfers/list", name: "Danh sách phiếu chuyển", component: Transfers, role: [1, 2] },
  { path: "/transfers/add", name: "Tạo phiếu chuyển", component: TransferAdd, role: [1, 2] },
  { path: "/transfers/:id", name: "Chi tiết phiếu chuyển", component: TransferDetail, role: [1, 2, 3] },
  { path: "/transfer/import", name: "Nhận sản phẩm", component: ProductImport, role: [3] },
  { path: "/transfer/export", name: "Xuất sản phẩm", component: ProductExport, role: [3] },

  // { path: "/demo/add", name: "add", component: DemoAdd },



  { path: "/inventories", exact: true, name: "Danh sách chi nhánh", component: Inventories, role: [1, 2] },
  { path: "/inventories/details", name: "Chi tiết chi nhánh", component: InventoriesDetail, role: [1, 2] },
  { path: "/inventories/add-inventory", name: "Thêm Chi Nhánh", component: InventoriesAdd, role: [1] },

  { path: "/history/details", name: "Lịch sử kho", component: HistoryDetails, role: [1, 2] },



  { path: "/users/list", name: "Quản lý tài khoản", component: Users, role: [1] },
  { path: "/users/add", name: "Thêm tài khoản", component: UserAdd, role: [1] },
  { path: "/users/:id", name: "Chi tiết tài khoản", component: UserUpdate, role: [1, 2, 3] },
  { path: "/change-password", name: "Đổi mật khẩu", component: ChangePass, role: [1, 2, 3] },
  { path: "/profile/:id", name: "Thông tin cá nhân", component: Profile, role: [1, 2, 3] },


  { path: "/statistic", exact: true, name: "Báo cáo", component: StatisticImport, role: [1] },
  { path: "/statistic/transfer", exact: true, name: "Phiếu chuyển", component: StatisticImport, role: [1] },
  { path: "/statistic/product", exact: true, name: "Sản phẩm", component: StatisticProduct, role: [1] },
  { path: "/statistic/inventory", exact: true, name: "Chi tiết", component: StatisticInventory, role: [1] },

];

export default routes;
