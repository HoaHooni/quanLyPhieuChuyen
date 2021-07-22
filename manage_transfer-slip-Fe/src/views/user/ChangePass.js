import React, { useState } from 'react'
import axios from 'axios'
import {
    CButton,
    CCard,
    CCardBody,
    CCol,
    CForm,
    CInput,
    CRow,
    CCardHeader,
    CFormGroup,
    CLabel,
    CInputCheckbox
} from '@coreui/react'
import swal from 'sweetalert';

const ChangePass = (props) => {

    const id = JSON.parse(sessionStorage.user).id
    const [changePass, setChangePass] = useState({ id: id })
    const [showPass, setShowPass] = useState(false)
    const [validate, setValidate] = useState({ oldPass: false, newpass: false, confirmPass: false })

    const [checkNullOldPass, setCheckNullOldPass] = useState({ display: "none" })
    const [checkOldPass, setCheckOldPass] = useState({ display: "none" })
    const [checkNullNewPass, setCheckNullNewPass] = useState({ display: "none" })
    const [checkNewPass, setCheckNewPass] = useState({ display: "none" })
    const [checkNullConfirmPass, setCheckNullConfirmPass] = useState({ display: "none" })
    const [checkConfirmPass, setCheckConfirmPass] = useState({ display: "none" })

    const handleChange = (e) => {
        setChangePass({ ...changePass, [e.target.name]: e.target.value })
    }

    const cancle = () => {
        props.history.push("/dashboard");
    };

    async function getSubmit() {

        var config = {
            method: 'post',
            url: 'http://localhost:8080/admin/user/changePassword',
            headers: {
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(changePass)
        };

        await axios(config)
            .then(function (response) {
                // console.log(response);
                if (response.data.mess === "SUCCESS") {
                    swal("Đổi mật khẩu thành công!")
                    props.history.push("/dashboard");
                } else {
                    setCheckOldPass({ display: "inline", color: "red" })
                    swal("Đổi mật khẩu không thành công!")
                }

            })
            .catch(function (error) {
                console.log(error);
            });
    }

    //validate check null
    const validateNullAndBlank = (value) => {
        if (/((\r\n|\n|\r)$)|(^(\r\n|\n|\r))|^\s*$/.test(value)) {
            return true;
        } else {
            return false;
        }
    }

    const validateMin = (value) => {
        if (!(/^(?=.*\d).{8,}$/.test(value))) {
            return true;
        } else {
            return false;
        }
    }

    const validateOldPass = (e) => {
        if (validateNullAndBlank(e.target.value)) {
            setCheckNullOldPass({ display: "inline", color: "red" })
            setValidate({ oldPass: false, newpass: validate.newpass, confirmPass: validate.confirmPass })
        } else {
            setCheckNullOldPass({ display: "none", color: "red" })
            setValidate({ oldPass: true, newpass: validate.newpass, confirmPass: validate.confirmPass })
        }
    }

    const validateNewPass = (e) => {

        if (validateNullAndBlank(e.target.value)) {
            setCheckNullNewPass({ display: "inline", color: "red" })
            setValidate({ oldPass: validate.oldPass, newpass: false, confirmPass: validate.confirmPass })
        } else {
            setCheckNullNewPass({ display: "none", color: "red" })
            if (validateMin(e.target.value)) {
                setCheckNewPass({ display: "inline", color: "red" })
                setValidate({ oldPass: validate.oldPass, newpass: false, confirmPass: validate.confirmPass })
            } else {
                setCheckNewPass({ display: "none", color: "red" })
                setValidate({ oldPass: validate.oldPass, newpass: true, confirmPass: validate.confirmPass })
            }

        }
    }

    const validateConfirmPass = (e) => {

        if (validateNullAndBlank(e.target.value)) {
            setCheckNullConfirmPass({ display: "inline", color: "red" })
            setValidate({ oldPass: validate.oldPass, newpass: validate.newpass, confirmPass: false })
        } else {
            setCheckNullConfirmPass({ display: "none", color: "red" })
            if (changePass.newPassword !== changePass.confirmNewPassword) {
                setCheckConfirmPass({ display: "inline", color: "red" })
            } else {
                setCheckConfirmPass({ display: "none" })
                setValidate({ oldPass: validate.oldPass, newpass: validate.newpass, confirmPass: true })
            }
        }
    }


    const submit = () => {
        if (validate.oldPass && validate.newpass && validate.confirmPass) {

            getSubmit();
        } else {
            swal("Người dùng nhập không hợp lệ")
        }

    }

    return (
        <div>
            <CRow className="mt-2">
                <CCol xs="12" sm="12">
                    <CCard>
                        <CCardHeader>
                            <div style={{ color: "black" }} ><b>Đổi mật khẩu</b></div>
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
                                                <CLabel htmlFor="oldPassword">
                                                    Mật khẩu cũ<span style={{ color: "red" }}>*</span>:
                                                </CLabel>
                                            </CCol>
                                            <CCol xs="12" md="9">
                                                <CInput
                                                    type={showPass ? "text" : "password"}
                                                    id="oldPassword"
                                                    name="oldPassword"
                                                    placeholder="Mật khẩu cũ"
                                                    onChange={handleChange}
                                                    onBlur={validateOldPass}
                                                />
                                                <div className="col-12" style={checkNullOldPass}><small>Không được để trống</small></div>
                                                <div className="col-12" style={checkOldPass}><small>Sai mật khẩu</small></div>
                                            </CCol>
                                        </CFormGroup>

                                        <CFormGroup row>
                                            <CCol md="3">
                                                <CLabel htmlFor="newPassword">
                                                    Mật khẩu mới<span style={{ color: "red" }}>*</span>:
                                                </CLabel>
                                            </CCol>
                                            <CCol xs="12" md="9">
                                                <CInput
                                                    type={showPass ? "text" : "password"}
                                                    id="newPassword"
                                                    name="newPassword"
                                                    placeholder="Mật khẩu mới"
                                                    onChange={handleChange}
                                                    onBlur={validateNewPass}
                                                />
                                                <div className="col-12" style={checkNullNewPass}><small>Không được để trống</small></div>
                                                <div className="col-12" style={checkNewPass}><small>Mật khẩu tối thiểu 8 ký tự</small></div>
                                            </CCol>
                                        </CFormGroup>
                                        <CFormGroup row>
                                            <CCol md="3">
                                                <CLabel htmlFor="confirmNewPasswordt">
                                                    Nhập lại mật khẩu mới<span style={{ color: "red" }}>*</span>:
                                                </CLabel>
                                            </CCol>
                                            <CCol xs="12" md="9">
                                                <CInput
                                                    type={showPass ? "text" : "password"}
                                                    id="confirmNewPassword"
                                                    name="confirmNewPassword"
                                                    placeholder="Nhập lại password"
                                                    onChange={handleChange}
                                                    onBlur={validateConfirmPass}
                                                />
                                                <div className="col-12" style={checkNullConfirmPass}><small>Không được để trống</small></div>
                                                <div className="col-12" style={checkConfirmPass}><small>Mật khẩu không khớp</small></div>
                                            </CCol>
                                        </CFormGroup>
                                        <CFormGroup row>
                                            <CCol xs="12" sm="6">
                                                <CFormGroup variant="custom-checkbox" inline>
                                                    <CInputCheckbox
                                                        custom
                                                        id="show"
                                                        name="showPass"
                                                        value="show"
                                                        // checked={showPass}
                                                        onChange={e => { setShowPass(e.target.checked) }}
                                                    />
                                                    <CLabel variant="custom-checkbox" htmlFor="show">Hiện mật khẩu</CLabel>
                                                </CFormGroup>
                                            </CCol>
                                            <CCol xs="12" sm="3">
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

                                            <CCol xs="12" sm="3">
                                                <CButton
                                                    size="sm"
                                                    active
                                                    block
                                                    color="info"
                                                    aria-pressed="true"
                                                    onClick={submit}
                                                >
                                                    Đổi mật khẩu
                                                </CButton>
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

        </div>
    );
};

export default ChangePass;