import React, { useEffect, useState } from 'react'
import {
  CRow,
  CCol,
  CCard,
  CCardBody,
} from '@coreui/react'
import '../../scss/dashboard/_dashboard.scss'
import { useHistory } from 'react-router-dom'
import axios from 'axios'

export default function WidgetsDropdown(props) {
  // history
  const history = useHistory()
  const getUser = JSON.parse(sessionStorage.user)

  //URL of api
  const URL_NUMBER_INVENTORY = "http://localhost:8080/admin/inventory/size"
  const NUMBER_PRODUCT = `http://localhost:8080/admin/products/total-product-in-inventory/${getUser.inventoryId}`;
  const NUMBER_TRANSFER = `http://localhost:8080/transfers/countAll`;
  const NUMBER_USER = `http://localhost:8080/admin/user/countAll`;

  //useState Number
  const [inventoriesSize, setinventoriesSize] = useState("0")
  const [numberProduct, setNumberProduct] = useState("0")
  const [numberTransfer , setNumberTransfer] = useState("0")
  const [numberUser , setNumberUser] = useState("0")

  const user = JSON.parse(sessionStorage.getItem("user"))

  //setup card
  const setupWibget = [
    {
      title: 'Tổng số Chi nhánh',
      content: 'quản lý các chi nhánh cửa hàng',
      backgroundColor: 'indigo',
      number: inventoriesSize,
      color: '#ffffff',
      backgroundColorCircle: '#868686',
      colorCircle: '#ffffff',
      to: '/inventories'
    },
    {
      title: 'Tổng số Sản phẩm',
      content: 'quản lý các chi nhánh cửa hàng',
      backgroundColor: 'indigo',
      number: numberProduct,
      color: '#ffffff',
      backgroundColorCircle: '#868686',
      colorCircle: '#ffffff',
      to: 'products/list'
    },
    {
      title: 'Tổng số Phiếu chuyển',
      content: 'quản lý các phiếu chuyển của cửa hàng',
      backgroundColor: 'indigo',
      number: numberTransfer,
      color: '#ffffff',
      backgroundColorCircle: '#868686',
      colorCircle: '#ffffff',
      to: 'transfers/list'
    },
    {
      title: 'Tổng số Người dùng',
      content: 'quản lý người dùng',
      backgroundColor: 'indigo',
      number: numberUser,
      color: '#ffffff',
      backgroundColorCircle: '#868686',
      colorCircle: '#ffffff',
      to: 'users/list'
    },

  ]

  //setUup get number
  useEffect(() => {
    axios.get(URL_NUMBER_INVENTORY,{
      headers:{
        "Authorization": user.type + " " + user.token
    }
    })
      .then(
        (res) => {
          if (res.status === 200) {
            setinventoriesSize(res.data)
          }
        }
      ).catch(

      )
  }, [])
  useEffect(() => {
    axios.get(NUMBER_PRODUCT, {
      headers: {
        'authorization': `Bearer ${getUser.token}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    })
      .then(
        (res) => {
          console.log("number product: ", res.status)

          setNumberProduct(res.data)

        }
      ).catch(
        console.log
      )
  }, [])
  useEffect(() => {
    axios.get(NUMBER_TRANSFER,{
      headers:{
        "Authorization": user.type + " " + user.token
    }
    })
      .then(
        (res) => {
          if (res.status === 200) {
            setNumberTransfer(res.data)
          }
        }
      ).catch(

      )
  }, [])
  useEffect(() => {
    axios.get(NUMBER_USER,{
      headers:{
        "Authorization": user.type + " " + user.token
    }
    })
      .then(
        (res) => {
          if (res.status === 200) {
            setNumberUser(res.data)
          }
        }
      ).catch(

      )
  }, [])

  console.log("number product: ", numberProduct)
  function numberInventory() {
    //code here
  }


  function showWidget() {

    const displayWibget = setupWibget.map(
      (item, index) => {
        return (
          <CCol sm="6" lg="3" style={{
            margin: '0 auto'
          }} key={index}>
            <CCard style={{
              backgroundColor: item.backgroundColor
            }} className="text-white text-center" onClick={() => {
              history.push(item.to)
            }}>
              <CCardBody className="card-wrapper">
                <div className="circle-icon" style={{
                  backgroundColor: item.backgroundColorCircle
                }}>
                  <span style={{ color: item.colorCircle }}>{item.number}</span>
                </div>
                <div className="note-card">
                  <span style={{ color: item.color }}>{item.title}</span>
                </div>
              </CCardBody>
            </CCard>
          </CCol>
        )
      }
    )
    return displayWibget
  }

  return (
    <CRow>
      {showWidget()}
    </CRow>
  )
}


