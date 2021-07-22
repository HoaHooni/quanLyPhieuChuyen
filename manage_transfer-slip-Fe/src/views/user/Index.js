import React, { useState, useEffect } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import {
  CCol,
  CRow,
  CButton,
  CCard,
  CCardBody,
  CCardHeader,
  CDataTable,
  CSelect,
  CFormGroup,
  CLabel,
  CInput,
  CPagination
} from "@coreui/react";

const fields = [
  { key: "username", label: "Tên đăng nhập" },
  { key: "email", label: "Email" },
  { key: "dob", label: "Ngày sinh" },
  { key: "phone", label: "Số điện thoại" },
  { key: "address", label: "Địa chỉ" },
  { key: "role", label: "Chức vụ" },
];

const Index = (props) => {
  const [users, setUser] = useState([]);
  const [key, setKey] = useState("");

  const [total, setTotal] = useState(1);
  const [currentPage, setCurrentPage] = useState(1)
  const [currentLimit, setCurrentLimit] = useState(5)

  const getUser = JSON.parse(sessionStorage.user)


  useEffect(() => {

    axios.get(`http://localhost:8080/admin/user?key=${key}&page=${currentPage}&limit=${currentLimit}`,{
      headers: {
          'authorization': `Bearer ${getUser.token}`,
          'Accept': 'application/json',
          'Content-Type': 'application/json'
      }
  })
      .then(res => {
        setUser(res.data)
        console.log(res.data)
      })

  }, [key, currentPage, currentLimit]);

  useEffect(() => {

    axios.get(`http://localhost:8080/admin/user?key=${key}`,{
      headers: {
          'authorization': `Bearer ${getUser.token}`,
          'Accept': 'application/json',
          'Content-Type': 'application/json'
      }
  })
      .then(res => {
        setTotal(Math.ceil(res.data.length / currentLimit))
      })

  }, [key, currentLimit]);

  const addTransfer = () => {
    props.history.push("/users/add");
  };
  const changeLimit = (e) => {
    setCurrentLimit(e.target.value);


  }
  const changeKey = (e) => {
    setKey(e.target.value);
    setCurrentPage(1)

  }

  return (
    <>
      <CCard>
        <CCardHeader>
          <CRow>
            <CCol xs="12" sm="11">
              <h2 className="text-left">Danh sách người dùng</h2>
            </CCol>
            <CCol xs="12" sm="1">
              <CButton
                className=""
                size="xs"
                active
                block
                color="primary"
                aria-pressed="true"
                onClick={addTransfer}
              >
                Tạo
              </CButton>
            </CCol>
          </CRow>
        </CCardHeader>
        <CCardBody>
          <CRow className="d-flex justify-content-between">
            <CCol md='4'>
              <CFormGroup className="d-flex justify-content-between">
                <CLabel htmlFor="search" className="w-25 pt-1">Tìm kiếm:</CLabel>

                <CInput id="search" name="search" onChange={changeKey} placeholder="Tìm kiếm..." />

              </CFormGroup>
            </CCol>

          </CRow>


          <CDataTable
            items={users}
            fields={fields}

            hover
            striped
            // onRowClick={(item) => props.history.push(`/users/${item.id}`)}
            bordered
            size="sx"
            scopedSlots={{
              'username':
                (item) => (
                  <td>
                    <Link to={{
                      pathname: `/users/${item.id}`
                       }}>{item.username}</Link>
                  </td>
                )

            }}

          />
          <CRow className="d-flex justify-content-between">
          <CCol md='5'>
            <CPagination
              activePage={currentPage}
              pages={total}
              onActivePageChange={setCurrentPage}
            />
            </CCol>

            <CCol md='2' className="text-right">
              <span>Số bản ghi: </span>
              <CSelect custom name="rows" id="rows" style={{ width: '53%' }} onChange={changeLimit}>
                <option value="5">5</option>
                <option value="10">10</option>
                <option value="15">15</option>
                <option value="20">20</option>
              </CSelect>
            </CCol>
          </CRow>
        </CCardBody>
      </CCard>
    </>
  );
};

export default Index;
