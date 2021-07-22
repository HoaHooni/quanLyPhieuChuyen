import React, { useState, useEffect } from "react";
import axios from "axios";
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
  CSelect
} from "@coreui/react";
import Select from "react-select";
import swal from "sweetalert";

const Profile = (props) => {
  const [id] = useState(props.match.params.id);
  const [roles, setRole] = useState([]);
  const [user, setUser] = useState({
    code: "",
    username: "",
    email: "",
    password: "",
    phone: "",
    address: "",
    dob: "",
    status: 1,
    roles: [],
    role: "",
    inventoryId:""
  });
  const [userUpdate, setUserUpdate] = useState({
    email: "",
    phone: "",
    address: "",
    dob: "",
    roles: [],
    inventoryId:""
  });

  const filterOptions = [
    { value: "ROLE_MANAGER", label: "Thủ kho" },
    { value: "ROLE_INVENTORIER", label: "Nhân viên kho" },
    { value: "ROLE_COORDINATOR", label: "Nhân viên điều phối" },
  ];
  const [defaultRole, setDefaultRole] = useState([])
  

  const getUser = JSON.parse(sessionStorage.user)



  const [checkValidateRequireEmail, setCheckValidateRequireEmail] = useState({ display: "none" })
  const [checkValidateRequireRole, setCheckValidateRequireRole] = useState({ display: "none" })
  const [checkValidateFromEmail, setCheckValidateFromEmail] = useState({ display: "none" })
  const [checkValidateFromPhone, setCheckValidateFromPhone] = useState({ display: "none" })

  useEffect(() => {
    axios.get(`http://localhost:8080/admin/user/${id}`,{
      headers: {
          'authorization': `Bearer ${getUser.token}`,
          'Accept': 'application/json',
          'Content-Type': 'application/json'
      }
  }).then((res) => {
      console.log(res.data);
      setUser(res.data);
      setUserUpdate({
        ...userUpdate,
        email: res.data.email,
        phone: res.data.phone,
        address: res.data.address,
        dob: res.data.dob,
        roles: res.data.roles
      });

    });

  }, [id]);


  useEffect(()=>{
    setDefaultRole([])
    filterOptions.map(item =>{
      if(user.roles.indexOf(item.value)>-1){
        setDefaultRole(defaultRole =>[...defaultRole, {value: item.value, label: item.label}])
      }
    })
    
  },[user.roles])
  console.log("aaa", user)
  console.log("default", defaultRole)
  console.log("default0", defaultRole)
  console.log("123", filterOptions)
  

  
  


  const handleChange = (option) => {
    console.log(option.target.value)
    setRole(option.target.value);
    setUserUpdate({ ...userUpdate, roles: [option.target.value] });
  };

  const changeInput = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
    setUserUpdate({ ...userUpdate, [e.target.name]: e.target.value });
    console.log(userUpdate);
  };
  const handleSubmit = () => {
    console.log("asd", userUpdate);

    axios
      .put(`http://localhost:8080/admin/user/${id}`, userUpdate,{
        headers: {
            'authorization': `Bearer ${getUser.token}`,
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
      .then((res) => {
        swal({
          title: "Thành công",

          icon: "success",
          timer: 2000

        }).then((value) => {
          if (value) {
            props.history.push(`/profile/${id}`)
          }
        })
      });
  };
  const cancle = () => {
    props.history.push("/users/list");
  };
  const deleteUser = () => {
    // if (window.confirm("Bạn có chắc chắn muốn xóa")) {
    //   axios.delete(`http://localhost:8080/admin/user/${id}`).then((res) => {
    //     window.confirm("Xóa thành công");
    //   });
    // }
    swal({
      title: "Bạn chắc chắn muốn xóa?",
      text: "Dữ liệu bị xóa sẽ không thể lấy lại",
      icon: "warning",
      buttons: ["Hủy", "Xoá"],
      dangerMode: true,
    })
      .then((willDelete) => {
        if (willDelete) {
          axios.delete(`http://localhost:8080/admin/user/${id}`,{
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(
            (res) => {


              swal("Thành công!", "", "success")
              props.history.push("/users/list")




            }
          ).catch(
            error => {


            }
          )
        }
      });
  };
  
  

  const validateNullAndBlank = (value, setCheck) => {
    if (/((\r\n|\n|\r)$)|(^(\r\n|\n|\r))|^\s*$/.test(value)) {
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


  const validateBlurEmail = (e) => {
    validateEmail(e.target.value, setCheckValidateFromEmail)
    validateNullAndBlank(e.target.value, setCheckValidateRequireEmail)
  }

  const validateBlurPhone = (e) => {
    validatePhone(e.target.value, setCheckValidateFromPhone)
  }

  const validateBlurRole = (e) => {
    console.log(e.target.value)
    if (e.target.value == "0") {
      setCheckValidateRequireRole({ display: "inline", color: "red" })
    } else {
      setCheckValidateRequireRole({ display: "none" })
    }
  }

  return (
    <div>
      <CRow>
        <CCol xs="12" sm="12">
          <div className="mb-6" style={{ color: "black" }} > <h2><b>Thông tin cá nhân</b></h2> </div>

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
                        <CLabel htmlFor="text-input">Tên đăng nhập</CLabel>
                      </CCol>
                      <CCol xs="12" md="9">
                        <CInput
                          id="username"
                          name="username"
                          value={user.username}
                          placeholder="Text"
                          disabled
                        />
                      </CCol>
                    </CFormGroup>
                    <CFormGroup row>
                      <CCol md="3">
                        <CLabel htmlFor="email-input">Email</CLabel>
                      </CCol>
                      <CCol xs="12" md="9">
                        <CInput
                          type="email"
                          id="email"
                          name="email"
                          value={user.email}
                          placeholder="Enter Email"
                          onChange={changeInput}
                          onBlur={validateBlurEmail}
                        />
                        <div className="col-12" style={checkValidateFromEmail}><small>Email không hợp lệ</small></div>
                        <div className="col-12" style={checkValidateRequireEmail}><small>Thông tin bắt buộc</small></div>
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
                          value={user.dob}
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
                          value={user.phone}
                          placeholder="Enter phone"
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
                          value={user.address}
                          placeholder="Enter address"
                          autoComplete="address"
                          onChange={changeInput}
                        />
                      </CCol>
                    </CFormGroup>
                     </CForm>
                </CCol>
                <CCol md="3"></CCol>
              </CRow>
            </CCardBody>

          </CCard>
        </CCol>
      </CRow>
      <CRow className="mb-2">
        <CCol xs="12" sm="1">
        </CCol>
        <CCol xs="12" sm="9"></CCol>
        <CCol xs="12" sm="1">
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

export default Profile;
