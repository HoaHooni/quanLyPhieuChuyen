import React, { useState, useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import {
  CCreateElement,
  CSidebar,
  CSidebarBrand,
  CSidebarNav,
  CSidebarNavDivider,
  CSidebarNavTitle,
  CSidebarMinimizer,
  CSidebarNavDropdown,
  CSidebarNavItem,
} from '@coreui/react'

import CIcon from '@coreui/icons-react'
import Logo from '../assets/prictures/logo-sapo.png'
import LogoMini from '../assets/prictures/logo-sapo-mini.png'

// sidebar nav config
import navigation from './_nav'

const TheSidebar = () => {
  const dispatch = useDispatch()
  const show = useSelector(state => state.sidebarShow)

  const [navigations, setNavigation] = useState([])
  const roles = JSON.parse(sessionStorage.getItem("user")).roles;

  function getRole(roles) {
    const list = [];
    roles.map(item => {
      if (item === "ROLE_MANAGER") {
        list.push(1);
      }
      if (item === "ROLE_COORDINATOR") {
        list.push(2);
      }
      if (item === "ROLE_INVENTORIER") {
        list.push(3);
      }
    });
    return list
  }

  function exists(list1, list2) {
    var a1 = false;
    list1.map(el1 => {
      if (list2.indexOf(el1) > -1) {
        a1 = true
      }
    })
    return a1;
  }

  const getShow = () => {
    setNavigation([]);
    navigation.map(item => {
      if (exists(item.role, getRole(roles))) {

        if (item._children == null) {
          setNavigation(navigations => [...navigations, item])
        } else {
          const b = item._children.filter(a => exists(a.role, getRole(roles) ))
          setNavigation(navigations => [...navigations, {...item, _children: b}])

        }
      }
    })
  };


  useEffect(() => {
    getShow();
  }, [navigation])


  return (
    <CSidebar
      show={show}
      onShowChange={(val) => dispatch({ type: 'set', sidebarShow: val })}
    >
      <CSidebarBrand className="d-md-down-none" to="/">
        <div className="image-box" style={{
          width: '150px',
          height: "50px"
        }}>
          <CIcon
            className="c-sidebar-brand-full"
            name="logo-negative"
            src={Logo}
            width='100%'
            height='100%'
           // height='65px'
          />
        </div>
        <CIcon
          className="c-sidebar-brand-minimized"
          name="sygnet"
          src={LogoMini}
          height={35}
        />
      </CSidebarBrand>
      <CSidebarNav>

        <CCreateElement
          items={navigations}
          components={{
            CSidebarNavDivider,
            CSidebarNavDropdown,
            CSidebarNavItem,
            CSidebarNavTitle
          }}
        />
      </CSidebarNav>
      <CSidebarMinimizer className="c-d-md-down-none" />
    </CSidebar>
  )
}

export default React.memo(TheSidebar)
