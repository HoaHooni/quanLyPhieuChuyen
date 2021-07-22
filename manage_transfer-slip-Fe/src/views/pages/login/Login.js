import React, { useState, useContext } from 'react'
import axios from 'axios'
import mycontext from '../../../context/Context'
import {
  CButton,
  CCard,
  CCardBody,
  CCardGroup,
  CCol,
  CContainer,
  CForm,
  CInput,
  CInputGroup,
  CInputGroupPrepend,
  CInputGroupText,
  CRow
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import Logo from '../../../assets/prictures/logo-sapo.png'
import Background from '../../../assets/prictures/background-bottom-pos-app.svg'
import { useHistory } from 'react-router-dom'
import swal from 'sweetalert'

const Login = (props) => {

  const [login, setLogin] = useState({})
  const history = useHistory()
  async function getLogin() {
    var config = {
      method: 'post',
      url: 'http://localhost:8080/api/auth/signin',
      headers: {
        'Content-Type': 'application/json'
      },
      data: JSON.stringify(login)
    };


    await axios(config)
      .then(function (response) {
     
        const user = JSON.stringify(response.data)
        sessionStorage.setItem("user",user)
          history.push("/")
      })
      .catch(
      (e) =>{
        sessionStorage.removeItem("user")
        swal({
          title: "Sai tài khoản hoặc mật khẩu",
          text: "",
          icon: "warning",
          timer: 2000
        })
       }
      );
  }

  const handleChange = (event) => {
    const { name, value } = event.target;
    setLogin({
      ...login,
      [name]: value
    });
  }
  const handleSumit = (e) => {
    e.preventDefault();
    console.log("login: ", login)
    getLogin();
  }

  return (
    <div className="c-app c-default-layout flex-row align-items-center" style={{ background: `url(${Background})`, backgroundRepeat: 'no-repeat', backgroundPosition: 'bottom', }}>
      <CContainer>
        <CRow className="justify-content-center">
          <CCol md="5">
            <CCardGroup>
              <CCard className="p-4">
                <CCardBody>
                  <CForm  onSubmit={handleSumit}>
                    <div className="d-flex justify-content-center mb-3">
                      <CIcon
                        className="c-sidebar-brand-full"
                        name="logo-negative"
                        src={Logo}
                        // width="300px"
                        height="60px"
                        width="160px"
                      />
                    </div>
                    <p className="text-muted">Sign In to your account</p>
                    <CInputGroup className="mb-3">
                      <CInputGroupPrepend>
                        <CInputGroupText>
                          <CIcon name="cil-user" />
                        </CInputGroupText>
                      </CInputGroupPrepend>
                      <CInput type="text" placeholder="Username" name="username" autoComplete="username" onChange={handleChange} />
                    </CInputGroup>
                    <CInputGroup className="mb-4">
                      <CInputGroupPrepend>
                        <CInputGroupText>
                          <CIcon name="cil-lock-locked" />
                        </CInputGroupText>
                      </CInputGroupPrepend>
                      <CInput type="password" placeholder="Password" name="password" autoComplete="current-password" onChange={handleChange} />
                    </CInputGroup>
                    <CRow>
                      <CCol xs="12" className="d-flex justify-content-center">
                        <CButton type="submit" style={{ background: '#00DD97', color: "white", fontWeight: 'bold' }} className="px-4">Login</CButton>
                      </CCol>
                    </CRow>
                  </CForm>
                </CCardBody>
              </CCard>

            </CCardGroup>
          </CCol>
        </CRow>
      </CContainer>
    </div>
  )
}

export default Login
