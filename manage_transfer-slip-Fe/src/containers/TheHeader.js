import React, { useState, useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import {
  CHeader,
  CToggler,
  CHeaderBrand,
  CHeaderNav,
  CBreadcrumbRouter
} from '@coreui/react'
import CIcon from '@coreui/icons-react'

// routes config
import routes from '../routes'

import {
  TheHeaderDropdown,
} from './index'

const TheHeader = () => {
  const dispatch = useDispatch()
  const sidebarShow = useSelector(state => state.sidebarShow)
  const [route, setRoute] = useState([])

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
    return list;
  }


  function exists(list1, list2) {
    var a1 = false;
    list1.map(el1 => {
      if (list2.indexOf(el1) > -1) {
        a1 = true;
      }
    })
    return a1
  }

  const getShow = () => {
    setRoute([]);
    routes.map(item => {
      if (exists(item.role, getRole(roles))) {
        setRoute(route => [...route, item]);
      }
    })
  };

  useEffect(() => {
    getShow();
  }, [routes])


  const toggleSidebar = () => {
    const val = [true, 'responsive'].includes(sidebarShow) ? false : 'responsive'
    dispatch({ type: 'set', sidebarShow: val })
  }

  const toggleSidebarMobile = () => {
    const val = [false, 'responsive'].includes(sidebarShow) ? true : 'responsive'
    dispatch({ type: 'set', sidebarShow: val })
  }
  return (
    <CHeader withSubheader style={{
      height: '65px'
    }}>
      <CToggler
        inHeader
        className="ml-md-3 d-lg-none"
        onClick={toggleSidebarMobile}
      />
      <CToggler
        inHeader
        className="ml-3 d-md-down-none"
        onClick={toggleSidebar}
      />
      <CHeaderBrand className="mx-auto d-lg-none" to="/">
        <CIcon name="logo" height="48" alt="Logo" />
      </CHeaderBrand>

      <CHeaderNav className="d-md-down-none mr-auto">

        <CBreadcrumbRouter
          className="border-0 c-subheader-nav m-0 px-0 px-md-3"
          routes={route}
        />
      </CHeaderNav>

      <CHeaderNav className="px-3">

        <TheHeaderDropdown />

      </CHeaderNav>


    </CHeader>
  )
}

export default TheHeader
