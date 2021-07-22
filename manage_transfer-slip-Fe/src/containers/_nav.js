import React from "react";
import CIcon from "@coreui/icons-react";

const _nav = [
  {
    _tag: "CSidebarNavItem",
    name: "Trang chủ",
    to: "/dashboard",
    icon: 'cil-home',
    role: [1, 2, 3]
  },
  {
    _tag: "CSidebarNavDropdown",
    name: "Quản lý sản phẩm",
    route: "/products",
    icon: "cil-bookmark",
    role: [1, 2, 3],
    _children: [
      {
        _tag: "CSidebarNavItem",
        name: "Danh sách sản phẩm",
        to: "/products/list",
        role: [1, 2, 3],
      },
      {
        _tag: "CSidebarNavItem",
        name: "Thêm sản phẩm",
        to: "/products/add",
        role: [3],
      },
    ],
  },
  {
    _tag: "CSidebarNavDropdown",
    name: "Quản lý phiếu chuyển",
    to: "/transfers",
    icon: "cil-envelope-open",
    role: [1, 2, 3],
    _children: [
      {
        _tag: "CSidebarNavItem",
        name: "Danh sách phiếu chuyển",
        to: "/transfers/list",
        role: [1, 2],
      },
      {
        _tag: "CSidebarNavItem",
        name: "Tạo phiếu chuyển",
        to: "/transfers/add",
        role: [2],
      },
      {
        _tag: "CSidebarNavItem",
        name: "Phiếu nhập",
        to: "/transfer/import",
        role: [3],
      },
      {
        _tag: "CSidebarNavItem",
        name: "Phiếu xuất",
        to: "/transfer/export",
        role: [3],
      },
    ],
  },
  {
    _tag: "CSidebarNavDropdown",
    name: "Quản lý tài khoản",
    to: "/users",
    icon: "cil-user",
    role: [1],
    _children: [
      {
        _tag: "CSidebarNavItem",
        name: "Danh sách tài khoản",
        to: "/users/list",
        role: [1],
      },
      {
        _tag: "CSidebarNavItem",
        name: "Thêm tài khoản",
        to: "/users/add",
        role: [1],
      },
    ],
  },
  {
    _tag: "CSidebarNavDropdown",
    name: "Quản lý chi nhánh",
    to: "/inventories",
    icon: 'cilFactory',
    role: [1],
    _children: [
      {
        _tag: "CSidebarNavItem",
        name: "Danh sách chi nhánh",
        to: "/inventories",
        role: [1],
      },
      {
        _tag: "CSidebarNavItem",
        name: "Thêm Chi Nhánh",
        to: "/inventories/add-inventory",
        role: [1],
      },
    ],
  },
  {
    _tag: "CSidebarNavDropdown",
    name: "Báo cáo/ Thống kê",
    to: "/statistic",
    icon: "cilBarChart",
    role: [1],
    _children: [
      {
        _tag: "CSidebarNavItem",
        name: "Phiếu chuyển",
        to: "/statistic/transfer",
        role: [1]
      },
      {
        _tag: "CSidebarNavItem",
        name: "Sản phẩm",
        to: "/statistic/product",
        role: [1]
      },
    ]
  },

];

export default _nav;
