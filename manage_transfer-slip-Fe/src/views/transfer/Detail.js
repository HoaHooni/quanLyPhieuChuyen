import React, { useState, useEffect } from 'react';
import axios from 'axios';
import moment from 'moment'
import {
    CButton,
    CCard,
    CCardBody,
    CCardHeader,
    CCol,
    CFormGroup,
    CTextarea,
    CLabel,
    CRow,
    CDataTable,
    CBadge
} from '@coreui/react'
import swal from 'sweetalert';
import { CIcon } from '@coreui/icons-react';

const getBadge = status => {
    switch (status) {
        case 'Chờ chuyển': return 'primary'
        case 'Đang chuyển': return 'warning'
        case 'Đã nhận': return 'success'
        case 'Đã hủy': return 'danger'
        default: return 'primary'
    }
}

const fields = [{ key: 'code', label: "Mã sản phẩm" }, { key: 'productName', label: "Tên sản phẩm" },
{ key: 'total', label: "Số lượng" }]

function Detail(props) {

    const [id] = useState(props.match.params.id);
    const [products_input, setProductInput] = useState([]);
    //const [inputTransferDetailDTOList, setTransferDetail] = useState([]);
   // const [listTransfer, setListTransfer] = useState([]);
    const [transfer, setTransfer] = useState({
        note: "",
        code: "",
        status: "",
        user_id: "",
        inventoryInputId:"",
        inventoryInputName: "",
        inventoryOutputId: "",
        inventoryOutputName: "",
        inputTransferDetailDTOList: []
    });

    const [reLoad, setReLoad] = useState(false)

    const getUser = JSON.parse(sessionStorage.user)

    // const [checkConfirmExport, setCheckConfirmExport] = useState({ display: "none" })
    // const [checkConfirmImport, setCheckConfirmImport] = useState({ display: "inline" })

    const roles = getUser.roles;
    const [roleDisplay, setRoleDisplay] = useState({ display: "none" })


    useEffect(() => {
        if (roles) {
            roles.map((role) => {
                 if (role === "ROLE_COORDINATOR") {
                    setRoleDisplay({ display: "inline" })
                }
            })
        }
    }, [])

    useEffect(() => {

        axios.get(`http://localhost:8080/transfers/${id}`, {
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(res => {
                console.log(res.data)
                setTransfer(res.data)
                setProductInput(res.data.transferDetailDTOList)
                console.log("transfer", transfer)



            });


    }, [id,reLoad]);


    const cancle = () => {
        // props.history.push('/transfers/list');
        props.history.goBack();
    }
    const deleteTransfer = () => {
        // if (window.confirm("Bạn có chắc chắn muốn hủy phiếu này")) {
        //     axios.delete(`http://localhost:8080/transfers/${id}`
        //     ).then(res => {
        //         window.confirm("Hủy phiếu thành công")
        //     })


        // }
        swal({
            title: "Bạn chắc chắn muốn hủy phiếu này?",
            text: "Phiếu đã hủy sẽ không thể khôi phục lại",
            icon: "warning",
            buttons: ["Hủy", "Xoá"],
            dangerMode: true,
        })
            .then((willDelete) => {
                if (willDelete) {
                    axios.delete(`http://localhost:8080/transfers/${getUser.id}/${id}`, {
                        headers: {
                            'authorization': `Bearer ${getUser.token}`,
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        }
                    }
                    ).then(
                        (res) => {
                            console.log(res)
                            if (res.data.mess === "Hàng đã chuyển đi không thể hủy phiếu") {
                                swal({
                                    title: "Hàng đã chuyển đi hoặc phiếu đã hủy",

                                    icon: "error",
                                    buttons: ["Hủy", "Oke"],

                                })
                            }
                            else {
                                swal("Thành công!", "", "success")
                                props.history.push("/transfers/list")
                            }



                        }
                    ).catch(
                        error => {


                        }
                    )
                }
            });

    }

    const ConfirmExport = (e) => {
        var axios = require('axios');
        var data = '';
        var config = {
            method: 'put',
            url: `http://localhost:8080/transfers/shipping/${getUser.id}/${id}`,
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: data
        };

        axios(config)
            .then(function (response) {
                if (response.data.mess === "Hàng đã chuyển đi không thể chuyển tiếp") {
                    swal("Hàng đã chuyển đi không thể chuyển tiếp")
                } else if (response.data.mess === "Đã nhận hàng không thể chuyển tiếp") {
                    swal("Đã nhận hàng không thể chuyển tiếp")
                } else if (response.data.mess === "Đã hủy phiếu") {
                    swal("Đã hủy phiếu")
                } else if (response.data.mess === "TOTAL PRODUCT INVALID") {
                    swal("Lượng hàng trong kho không còn đủ để chuyển đi")
                } else {
                    swal("Thành công!")
                    setReLoad(!reLoad)
                }
            })
            .catch(function (error) {
                console.log(error);
            });


    }

    const ConfirmImport = (e) => {
        var axios = require('axios');
        var data = '';

        var config = {
            method: 'put',
            url: `http://localhost:8080/transfers/receive/${getUser.id}/${id}`,
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: data
        };

        axios(config)
            .then(function (response) {
                console.log("1111111111111 :", JSON.stringify(response.data.mess));
                if (response.data.mess === "Đã nhận hàng rồi") {
                    swal("Đã nhận hàng rồi")
                } else if (response.data.mess === "Chưa chuyển hàng đi") {
                    swal("Chưa chuyển hàng đi")
                } else if (response.data.mess === "Đã hủy phiếu") {
                    swal("Đã hủy phiếu")
                } else {
                    swal("Thành công!")
                    setReLoad(!reLoad);
                }
            })
            .catch(function (error) {
                console.log(error);
            });


    }

    useEffect(() => {
        confirmImport()
    }, [transfer])

    useEffect(() => {
        confirmExport()
    }, [transfer])

    const confirmExport = () => {
        if (transfer.status === "Đang chuyển" && roles.indexOf("ROLE_INVENTORIER") > -1) {
            return (
                <CCard>
                    <CCardBody className="pt-1 pb-1">
                        <CRow>
                          <CIcon name={'cilCheckCircle' } size="xl"></CIcon> <p style={{color: 'black'}} className="pl-3 m-0"> Tất cả sản phẩm đã chuyển đi</p>
                        </CRow>


                    </CCardBody>
                </CCard>

            );
        } else if (transfer.status === "Chờ chuyển" && roles.indexOf("ROLE_INVENTORIER") > -1 && transfer.inventoryOutputId === getUser.inventoryId) {
            return (
                <CCard>
                    <CCardBody className="pt-1 pb-1">
                        <CRow>
                            <CCol xs="12" sm="2">
                            <i class="cis-truck"></i><p className="pl-3 m-0" style={{color: 'black'}}>Chuyển hàng</p>

                            </CCol>
                            <CCol xs="12" sm="8">

                            </CCol>
                            <CCol xs="12" sm="2">
                                <CButton size="sm" active block color="info" aria-pressed="true" onClick={ConfirmExport} >Chuyển hàng</CButton>
                            </CCol>

                        </CRow>


                    </CCardBody>
                </CCard>

            );
        }
        else if (transfer.status === "Đã nhận" && roles.indexOf("ROLE_INVENTORIER") > -1) {
            return (
                <CCard>
                    <CCardBody className="pt-1 pb-1">
                        <CRow>
                        <CIcon name={'cilCheckCircle' } size="xl"></CIcon> <p className="pl-3 m-0" style={{color: 'black'}}> Tất cả sản phẩm đã chuyển đi</p>
                        </CRow>


                    </CCardBody>
                </CCard>

            )
        }
        else if (transfer.status ==="Đã hủy" && roles.indexOf("ROLE_INVENTORIER") > -1) {


        }
    }
    function formatDate(e) {
        if (e == null) {
            return ""
        }
        return moment(e).format('DD/MM/YYYY HH:mm')
    }

    const confirmImport = () => {
        if (transfer.status === "Chờ chuyển" && roles.indexOf("ROLE_INVENTORIER") > -1 ) {

        }
        else if (transfer.status === "Đang chuyển" && roles.indexOf("ROLE_INVENTORIER") > -1 && transfer.inventoryInputId === getUser.inventoryId) {
            return (
                <CCard>
                    <CCardBody className="pt-1 pb-1">
                        <CRow>
                            <CCol xs="12" sm="2">
                                <p  style={{color: 'black'}} className="pl-4 m-0">Nhận  hàng</p>

                            </CCol>
                            <CCol xs="12" sm="8">

                            </CCol>
                            <CCol xs="12" sm="2">
                                <CButton size="sm" active block color="success" aria-pressed="true" onClick={ConfirmImport} >Nhận  hàng</CButton>
                            </CCol>

                        </CRow>


                    </CCardBody>
                </CCard>


            )
        }
        else if (transfer.status === "Đã nhận" && roles.indexOf("ROLE_INVENTORIER") > -1) {
            return (
                <CCard>
                    <CCardBody className="pt-1 pb-1">
                        <CRow>
                        <CIcon name={'cilCheckCircle' } size="xl"></CIcon> <p style={{color: 'black'}} className="pl-3 m-0"> Tất cả sản phẩm đã được nhận</p>
                        </CRow>
                    </CCardBody>
                </CCard>

            )
        }
    }

    return (
        <>
            <CRow>
                <CCol xs="12" sm="12">
                    <div className="mb-3" style={{ cursor: "pointer" }} onClick={cancle}> {`< Quay lại`} </div>
                    <div className="mb-6" style={{ color: "black" }} > <h2><b>Chi tiết phiếu {transfer.code}</b></h2>  </div>
                </CCol>
            </CRow>
            <CRow className="mt-2"  >
                <CCol xs="12" sm="4" >
                    <CCard style={{height:"450px"}}>
                        <CCardHeader>
                            <div style={{ color: "black" }} ><b>Thông tin phiếu chuyển </b><CBadge className="ml-5" color={getBadge(transfer.status)}>
                                {transfer.status}
                            </CBadge></div>
                        </CCardHeader>
                        <CCardBody>
                            <CFormGroup>
                                <CRow>
                                    <CCol xs="5">
                                        Chi nhánh chuyển
                                    </CCol>
                                    <CCol xs="7">
                                        : {transfer.inventoryOutputName}
                                    </CCol>
                                </CRow>

                            </CFormGroup>
                            <CFormGroup>
                                <CRow>
                                    <CCol xs="5">
                                        Chi nhánh nhận
                                    </CCol>
                                    <CCol xs="7">
                                        : {transfer.inventoryInputName}
                                    </CCol>
                                </CRow>
                            </CFormGroup>
                            <CFormGroup>
                                <CRow>
                                    <CCol xs="5">
                                        Ngày tạo
                                    </CCol>
                                    <CCol xs="7">
                                        : {formatDate(transfer.createAt)}
                                    </CCol>
                                </CRow>
                            </CFormGroup>
                            <CFormGroup>
                                <CRow>
                                    <CCol xs="5">
                                        Ngày chuyển
                                    </CCol>
                                    <CCol xs="7">
                                        : {formatDate(transfer.movingAt)}
                                    </CCol>
                                </CRow>

                            </CFormGroup>
                            <CFormGroup>
                                <CRow>
                                    <CCol xs="5">
                                        Ngày nhận
                                    </CCol>
                                    <CCol xs="7">
                                        : {formatDate(transfer.finishAt)}
                                    </CCol>
                                </CRow>
                            </CFormGroup>
                            <CFormGroup>
                                <CRow>
                                    <CCol xs="5">
                                        Nhân viên tạo
                                    </CCol>
                                    <CCol xs="7">
                                        : {transfer.username}
                                    </CCol>
                                </CRow>
                            </CFormGroup>
                            <CFormGroup>
                            <CRow>
                            <CCol xs="5">
                                        Ghi chú
                                    </CCol>
                                    <CCol xs="7">
                                        : {transfer.note}
                                    </CCol>
                                    </CRow>
                            </CFormGroup>
                        </CCardBody>
                    </CCard>
                </CCol>

                <CCol xs="12" sm="8">
                    <CCard>
                        <CCardHeader>
                            <div style={{ color: "black" }} ><b>Thông tin sản phẩm</b></div>
                        </CCardHeader>
                        <CCardBody>

                            <CDataTable
                                items={products_input}
                                fields={fields}
                                hover
                                striped
                                onRowClick={(item) => props.history.push(`/transfers/${item.id}`)}
                                bordered
                                itemsPerPage={5}
                                pagination
                                size="md"
                            />
                        </CCardBody>
                    </CCard>

                    {confirmExport()}

                
                    {confirmImport()}


                        

                </CCol>


            </CRow>
            <CRow className="mb-2">
                <CCol xs="12" sm="1">
                    <CButton size="sm" style={roleDisplay} active block color="danger" aria-pressed="true" onClick={deleteTransfer} >Hủy phiếu</CButton>
                </CCol>
                <CCol xs="12" sm="10">

                </CCol>
                <CCol xs="12" sm="1">
                    <CButton size="sm" active block color="dark" aria-pressed="true" onClick={cancle}>Quay lại</CButton>
                </CCol>
            </CRow>
        </>
    );
}

export default Detail;
