import axios from "axios";
import React, { useState, useEffect } from "react";
import { Link, useHistory } from "react-router-dom";
import moment from "moment";
import {
  CCard,
  CCardBody,
  CCardHeader,
  CCol,
  CFormGroup,
  CInput,
  CLabel,
  CRow,
  CButton,
  CDataTable,

  CPagination,
  CSelect,

} from "@coreui/react";

export default function InventoryComponent(props) {
  const URL_INVENTORY = "http://localhost:8080/admin/inventory/page";
  const URL_SIZE_INVENTORY = "http://localhost:8080/admin/inventory/size";
  const URL_FIND_INVENTORY = "http://localhost:8080/admin/inventory/find-by-key?key=";
  const URL_FIND_EXACT_INVENTORY = "http://localhost:8080/admin/inventory/find-key?key=";
  const URL_FIND_SIZE = "http://localhost:8080/admin/inventory/size-finding?key="
  const user = JSON.parse(sessionStorage.getItem("user"))
  const history = useHistory();
  const [pageHistory, setPageHistory] = useState(1)
  const [page, setPage] = useState({
    number: 1,
    limit: 5,
  });
  const [search, setSearch] = useState("")
  const [data, setData] = useState([]);
  const [size, setSize] = useState(0);

  const fields = [
    { key: "code", label: "Mã Kho" },
    { key: "name", label: "Tên kho" },
    { key: "address", label: "Địa chỉ" },
    { key: "phone", label: "Số điện thoại" },
    { key: "mail", label: "Email" },
    { key: "createAt", label: "Ngày tạo" },
    {
      key: "updateAt",
      label: "Ngày sửa đổi"

    },
  ];

  useEffect(() => {
    loadData();
    loadSize();
  }, [page, search]);


  async function loadData() {
    var url
    if (search == "") {
      url = URL_INVENTORY + "?page=" + page.number + "&limit=" + page.limit;

    } else {
      url = URL_FIND_INVENTORY + search + "&page=" + page.number + "&limit=" + page.limit
    }
    console.log("url", url)
    await axios
      .get(url,{
        headers:{
            "Authorization": user.type + " " + user.token
        }
    })
      .then((res) => {
        if (res.status == 200) {
          console.log("data:", res.data)
          setData(res.data);
        }
      })
      .catch(console.log);
  }



  async function loadSize() {
    if (search == "") {
      await axios
        .get(URL_SIZE_INVENTORY,{
          headers:{
              "Authorization": user.type + " " + user.token
          }
      })
        .then((res) => setSize(res.data))
        .catch(console.log);
    } else {
      await axios
        .get(URL_FIND_SIZE + search,{
          headers:{
              "Authorization": user.type + " " + user.token
          }
      })
        .then((res) => setSize(res.data))
        .catch(console.log);
    }
  }



  //api
  function setNumberPage(e) {
    console.log(e)
    if (e == 0) {
      e = 1
    }
    setPage(
      {
        number: e,
        limit: page.limit
      }
    )
  }

  function selectRow(e) {
    console.log(e.target.value)
  }

  function onChange(e) {
    console.log("size", size)
    console.log("e: ", e.target.value)

    setSearch(e.target.value)
    if (e.target.value == "") {
      setPage({
        number: pageHistory,
        limit: page.limit
      })
      setPageHistory(1)

    }
    else {
      if (pageHistory == 1) {
        setPageHistory(page.number)
      }

    }
  }
  function limitOnChange(e) {
    console.log(e.target.value)
    setPage(
      {
        number: page.number,
        limit: e.target.value
      }
    )
  }


  function onClick(e) {
    e.preventDefault()
    axios.get(URL_FIND_EXACT_INVENTORY + search)
      .then(
        (res) => {
          setData(res.data)
        }
      ).catch(console.log)
  }

  function onGoToDetails(e) {
    history.push("/inventories/details",{id: e.id})
    console.log(e)
  }

  function convertDate(e) {
    if (e == null) {
      return ""
    }
    return moment(e).format("DD/MM/YYYY HH:mm")
  }

  function convertInfor(e) {
    console.log(e)
       if (e == null) {
      return ""
    }
    return e   
  }

  return (
    <>
          <CCard className="mt-12" style={{
            
          }}>
            <CCardHeader className="d-flex justify-content-between">
            <CCol xs="12" sm="11">
                    <h2 className="text-left">Danh sách chi nhánh</h2>
                </CCol>
                <CCol xs="12" sm="1">
                    <CButton className="" size="xs" active block color="primary" aria-pressed="true" onClick={(e) =>{
                      e.preventDefault()
                      history.push("/inventories/add-inventory")
                    }} >Tạo</CButton>
                </CCol>
            </CCardHeader>
            <CCardBody className="">
              <CRow className="d-flex justify-content-between">
                <CCol md='4'>
                  <CFormGroup className="d-flex justify-content-between">
                    <CLabel htmlFor="search" className="w-25 pt-2">Tìm kiếm:</CLabel>

                    <CInput id="search" name="search" placeholder="Nhập từ khóa..." onChange={onChange} />
                    {/* <CButton block color="primary" style={{
                      width: '150px',
                      height: '35px'
                    }}
                      onClick={onClick}
                    >Tìm kiếm</CButton> */}
                  </CFormGroup>
                </CCol>

              
              </CRow>
              <CDataTable
                items={data}
                fields={fields}
                onRowClick={
                  onGoToDetails
                }
                hover
                border
                
                scopedSlots={
                  {
                    'createAt':
                      (item) => (
                        <td>
                          {convertDate(item.createAt)}
                        </td>

                      ),
                    'updateAt':
                      (item) => (
                        <td>
                          {convertDate(item.updateAt)}
                        </td>
                      ),
                      'mail':
                      (item) => (
                        <td>
                        {convertInfor(item.mail)}
                        </td>
                      )
                      ,
                      'address': (item) => (
                        <td>
                          {convertInfor(item.address)}
                        </td>
                      )
                      
                  }
                }
                size="sx"

              />
              <CRow classNam="d-flex justify-content-between">
              <CCol  md='5'>
                    <CPagination
                    activePage={page.number}
                    pages={Math.ceil(size / page.limit)}
                    onActivePageChange={setNumberPage}

              />
                </CCol>
                <CCol md="5">

                </CCol>
                <CCol md='2' className="text-right">
                  <span>Số bản ghi: </span>
                  <CSelect custom name="rows" id="limit" style={{ width: '53%' }} onChange={limitOnChange} >
                    <option value="5">5 hàng</option>
                    <option value="10">10 hàng</option>
                    <option value="15">15 hàng</option>
                    <option value="50">50 hàng</option>
                  </CSelect>
                </CCol>
         
              </CRow>
            </CCardBody>
          
          </CCard>
      
    </>
  );
}
