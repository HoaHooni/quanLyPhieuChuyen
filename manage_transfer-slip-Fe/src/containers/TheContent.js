import React, { Suspense, useEffect, useState } from 'react'
import {
  Redirect,
  Route,
  Switch
} from 'react-router-dom'
import { CContainer, CFade } from '@coreui/react'

// routes config
import routes from '../routes'
  
const loading = (
  <div className="pt-3 text-center">
    <div className="sk-spinner sk-spinner-pulse"></div>
  </div>
)

const TheContent = () => {

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
        a1 = true
      }
    })
    return a1;
  }

  const getShow = () => {
    setRoute([]);
    routes.map(item => {
      if (exists(item.role, getRole(roles))) {
        setRoute(route => [...route, item])

      }
    })
  };

  useEffect(() => {
    getShow();
  }, [routes])

  return (
    <main className="c-main">
      <CContainer fluid>
        <Suspense fallback={loading}>
          <Switch>
            {route.map((route, idx) => {
              return route.component && (
                <Route
                  key={idx}
                  path={route.path}
                  exact={route.exact}
                  name={route.name}
                  render={props => (
                    <CFade>
                      <route.component {...props} />
                    </CFade>
                  )} />
              )
            })}
            {/* <Redirect to="/dashboard" /> */}
          </Switch>
        </Suspense>
      </CContainer>
    </main>
  )
}

export default React.memo(TheContent)
