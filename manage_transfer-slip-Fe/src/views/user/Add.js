import React, { useState, useEffect } from "react";
import axios from "axios";
import swal from "sweetalert";
import {
  CButton,
  CCard,
  CCardBody,
  CCardHeader,
  CCol,
  CForm,
  CFormGroup,
  CInput,
  CLabel,
  CRow,
} from "@coreui/react";
import Select from "react-select";


const Add = (props) => {
  const [user, setUser] = useState({
    code: "",
    role: []
  });


  const filterOptions = [
    { value: "ROLE_MANAGER", label: "Thủ kho" },
    { value: "ROLE_INVENTORIER", label: "Nhân viên kho" },
    { value: "ROLE_COORDINATOR", label: "Nhân viên điều phối" },
  ];

  //const [checkValidateRequireCode, setCheckValidateRequireCode] = useState({ display: "none" })
  const [checkValidateRequireUsername, setCheckValidateRequireUsername] = useState({ display: "none" })
  const [checkValidateRequireEmail, setCheckValidateRequireEmail] = useState({ display: "none" })
  const [checkValidateRequirePassword, setCheckValidateRequirePassword] = useState({ display: "none" })
  const [checkValidateRequireRole, setCheckValidateRequireRole] = useState({ display: "none" })
  //const [checkValidateCodeSpace, setCheckValidateCodeSpace] = useState({ display: "none" })
  const [checkValidateNameSpace, setCheckValidateNameSpace] = useState({ display: "none" })
  const [checkValidatePasswordSpace, setCheckValidatePasswordSpace] = useState({ display: "none" })
  const [checkValidateFromEmail, setCheckValidateFromEmail] = useState({ display: "none" })
  const [checkValidateFromPhone, setCheckValidateFromPhone] = useState({ display: "none" })

  const getUser = JSON.parse(sessionStorage.user)
  const [inventories, setInventory] = useState([]);

  const [filterInventory, setFilerInventory] = useState([]);


  useEffect(() => {
    axios.get(`http://localhost:8080/admin/inventory`, {
      headers: {
        'authorization': `Bearer ${getUser.token}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    })
      .then(res => {
        console.log("inven", res.data)
        setInventory(res.data)
      })

  }, []);

  useEffect(() => {
    inventories.map(item => {
      setFilerInventory(inventories => [...inventories, { value: item, label: item.name }])
    })


  }, [inventories]);

  // useEffect(() => {
  //   setUser({...user, role: roles });
  // }, [roles]);



  const changeInput = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };
  const handleSubmit = (e) => {
    console.log("asd", user);

    axios.post(`http://localhost:8080/api/auth/signup`, user, {
      headers: {
        'authorization': `Bearer ${getUser.token}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    })
      .then(res => {

        swal({
          title: "Thành công bạn có muốn về lại trang danh sách",

          icon: "success",
          buttons: ["Không", "Có"],

        }).then((value) => {
          if (value) {
            props.history.push("/users/list")
          }
        })
      }).catch(error => {

        if (error.response.data.mess === "Error: Username da ton tai") {
          swal({
            title: "Tên đăng nhập đã tồn tại",

            icon: "error",
            buttons: ["Hủy", "Oke"],

          })
        }


      }
      );
  };
  const cancle = () => {
    props.history.push("/users/list");
  };

  const validateNullAndBlank = (value, setCheck) => {
    if (/((\r\n|\n|\r)$)|(^(\r\n|\n|\r))|^\s*$/.test(value)) {
      setCheck({ display: "inline", color: "red" })
    } else {
      setCheck({ display: "none" })
    }
  }
  const validateSpace = (value, setCheck) => {
    if (/\s/.test(value)) {
      setCheck({ display: "inline", color: "red" })
    } else {
      setCheck({ display: "none" })

    }
  }

  const validateEmail = (value, setCheck) => {
    if (/^[a-zA-Z0-9]+@+[a-zA-Z0-9]+.+[A-z]/.test(value)) {
      setCheck({ display: "none" })

    } else {
      setCheck({ display: "inline", color: "red" })

    }
  }
  const validatePhone = (value, setCheck) => {
    if (/^[0-9\-\+]{9,15}$/.test(value)) {
      setCheck({ display: "none" })

    } else {
      setCheck({ display: "inline", color: "red" })

    }
  }




  // const validateBlurCode = (e) => {
  //     validateSpace(e.target.value, setCheckValidateCodeSpace)
  // }
  const validateBlurUsername = (e) => {
    validateSpace(e.target.value, setCheckValidateNameSpace)
    validateNullAndBlank(e.target.value, setCheckValidateRequireUsername)
  }
  const validateBlurEmail = (e) => {
    validateEmail(e.target.value, setCheckValidateFromEmail)
    validateNullAndBlank(e.target.value, setCheckValidateRequireEmail)
  }

  const validateBlurPhone = (e) => {
    validatePhone(e.target.value, setCheckValidateFromPhone)
  }
  const validateBlurPassword = (e) => {
    validateSpace(e.target.value, setCheckValidatePasswordSpace)
    validateNullAndBlank(e.target.value, setCheckValidateRequirePassword)
  }

  const validateBlurRole = (e) => {
    console.log(e.target.value)
    if (e.target.value === "0") {
      setCheckValidateRequireRole({ display: "inline", color: "red" })
    } else {
      setCheckValidateRequireRole({ display: "none" })
    }
  }

  useEffect(() => {
    inventoryId()
  }, [user])
  const inventoryId = () => {

    if (user.role.indexOf("ROLE_INVENTORIER") > -1) {
      return (
        <CFormGroup row >
          <CCol md="3">
            <CLabel htmlFor="company">
              Chi nhánh phụ trách<span style={{ color: "red" }}>*</span>:
            </CLabel>
          </CCol>
          <CCol xs="12" md="9">
            <Select
              id="inventoryId"
              name="inventoryId"
              placeholder="Chi nhánh"
              options={filterInventory}
              onChange={(event) => { setUser({ ...user, 'inventoryId': event.value.id }); }}
            //onBlur={validateBlurRole}
            />
            <div className="col-12" style={checkValidateRequireRole}><small>Thông tin bắt buộc</small></div>
          </CCol>
        </CFormGroup>
      )
    }

  }

  return (
    <div>
      <CRow>
        <CCol xs="12" sm="12">
          <div className="mb-3" style={{ cursor: "pointer" }} onClick={cancle}> {`< Quay lại`} </div>
          <div className="mb-6" style={{ color: "black" }} > <h2><b>Thêm mới người dùng</b></h2> </div>

        </CCol>
      </CRow>

      <CRow className="mt-2">
        <CCol xs="12" sm="12">
          <CCard>
            <CCardHeader>
              <div style={{ color: "black" }} ><b>Thông tin người dùng</b></div>
            </CCardHeader>
            <CCardBody>
              <CRow>
                <CCol md="3"></CCol>
                <CCol md="6">
                  <CForm
                    action=""
                    method="post"
                    encType="multipart/form-data"
                    className="form-horizontal"
                  >
                    <CFormGroup row>
                      <CCol md="3">
                        <CLabel htmlFor="text-input">
                          Tên đăng nhập<span style={{ color: "red" }}>*</span>
                        </CLabel>
                      </CCol>
                      <CCol xs="12" md="9">
                        <CInput
                          id="username"
                          name="username"
                          placeholder="Nhập tên đăng nhập"
                          onChange={changeInput}
                          onBlur={validateBlurUsername}
                        />
                        <div className="col-12" style={checkValidateRequireUsername}><small>Thông tin bắt buộc</small></div>
                        <div className="col-12" style={checkValidateNameSpace}><small>Tên đăng nhập không chứa dấu cách</small></div>

                      </CCol>
                    </CFormGroup>

                    <CFormGroup row>
                      <CCol md="3">
                        <CLabel htmlFor="email-input">
                          Email<span style={{ color: "red" }}>*</span>
                        </CLabel>
                      </CCol>
                      <CCol xs="12" md="9">
                        <CInput
                          type="email"
                          id="email"
                          name="email"
                          placeholder="Nhập email"
                          autoComplete="email"
                          onChange={changeInput}
                          onBlur={validateBlurEmail}
                        />
                        <div className="col-12" style={checkValidateFromEmail}><small>Email không hợp lệ</small></div>
                        <div className="col-12" style={checkValidateRequireEmail}><small>Thông tin bắt buộc</small></div>


                      </CCol>
                    </CFormGroup>
                    <CFormGroup row>
                      <CCol md="3">
                        <CLabel htmlFor="password-input">
                          Mật khẩu<span style={{ color: "red" }}>*</span>
                        </CLabel>
                      </CCol>
                      <CCol xs="12" md="9">
                        <CInput
                          type="password"
                          id="password"
                          name="password"
                          placeholder="Nhập password"
                          autoComplete="new-password"
                          onChange={changeInput}
                          onBlur={validateBlurPassword}
                        />
                        <div className="col-12" style={checkValidateRequirePassword}><small>Thông tin bắt buộc</small></div>
                        <div className="col-12" style={checkValidatePasswordSpace}><small>Mật khẩu không chứa dấu cách</small></div>


                      </CCol>
                    </CFormGroup>
                    <CFormGroup row>
                      <CCol md="3">
                        <CLabel htmlFor="date-input">Ngày sinh</CLabel>
                      </CCol>
                      <CCol xs="12" md="9">
                        <CInput
                          type="date"
                          id="dob"
                          name="dob"
                          placeholder="date"
                          onChange={changeInput}
                        />
                      </CCol>
                    </CFormGroup>

                    <CFormGroup row>
                      <CCol md="3">
                        <CLabel htmlFor="phone-input">Số điện thoại</CLabel>
                      </CCol>
                      <CCol xs="12" md="9">
                        <CInput
                          type="phone"
                          id="phone"
                          name="phone"
                          placeholder="Nhập số điện thoại"
                          autoComplete="phone"
                          onChange={changeInput}
                          onBlur={validateBlurPhone}
                        />
                        <div className="col-12" style={checkValidateFromPhone}><small>Số điện thoại không hợp lệ</small></div>
                      </CCol>
                    </CFormGroup>
                    <CFormGroup row>
                      <CCol md="3">
                        <CLabel htmlFor="address-input">Địa chỉ</CLabel>
                      </CCol>
                      <CCol xs="12" md="9">
                        <CInput
                          type="address"
                          id="address"
                          name="address"
                          placeholder="Nhập địa chỉ"
                          autoComplete="address"
                          onChange={changeInput}
                        />
                      </CCol>
                    </CFormGroup>
                    <CFormGroup row>
                      <CCol md="3">
                        <CLabel htmlFor="company">
                          Chức vụ<span style={{ color: "red" }}>*</span>
                        </CLabel>
                      </CCol>
                      <CCol xs="12" md="9">
                        <Select
                          id="role"
                          name="role"
                          placeholder="Chức vụ"
                          options={filterOptions}
                          //onChange={handleChange}
                          onChange={(event) => {
                            setUser({ ...user, 'role': event.map(item => item.value) });

                          }}
                          onBlur={validateBlurRole}
                          isMulti
                        />
                        {/* <option value="0">Chọn chức vụ</option>
                                    {filterOptions.map((item) => (
                                        <option value={item.value}>
                                            {item.label}
                                        </option>
                                    ))}
                        </Select> */}
                        <div className="col-12" style={checkValidateRequireRole}><small>Thông tin bắt buộc</small></div>
                      </CCol>
                    </CFormGroup>
                    {inventoryId()}


                  </CForm>
                </CCol>
                <CCol md="3"></CCol>
              </CRow>
            </CCardBody>
          </CCard>
        </CCol>
      </CRow>
      <CRow className="mb-2">
        <CCol xs="12" sm="10"></CCol>
        <CCol xs="12" sm="1">
          <CButton
            size="sm"
            active
            block
            color="dark"
            aria-pressed="true"
            onClick={cancle}
          >
            Quay lại
          </CButton>
        </CCol>

        <CCol xs="12" sm="1">
          <CButton
            size="sm"
            active
            block
            color="info"
            aria-pressed="true"
            onClick={handleSubmit}
          >
            Lưu
          </CButton>
        </CCol>
      </CRow>
    </div>
  );
};

export default Add;
