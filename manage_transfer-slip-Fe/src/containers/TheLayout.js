import React, { useContext, useState } from 'react'
import mycontext from '../context/Context'
import { Redirect } from 'react-router-dom'
import {
  TheContent,
  TheSidebar,
  TheFooter,
  TheHeader
} from './index'

const TheLayout = () => {
  function display() {
    if (sessionStorage.getItem("user") != null) {
      return (
        <div className="c-app c-default-layout">
          <TheSidebar />
          <div className="c-wrapper">
            <TheHeader />
            <div className="c-body">
              <TheContent />
            </div>
            <TheFooter />
          </div>
        </div>
      )
    } else {
      return <Redirect from="/" to="/login" />
    }
  }

  return (
    <>
      {display()}
    </>
  )
}

export default TheLayout
