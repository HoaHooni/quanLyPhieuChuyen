import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import moment from 'moment'
import {
    CCol,
    CRow,
    CButton,
    CCard,
    CCardBody,
    CCardHeader,
    CDataTable,
    CInput,
    CSelect,
    CFormGroup,
    CLabel,
    CPagination,

    CInvalidFeedback
} from '@coreui/react'


const fields = [{ key: 'code', label: "Mã phiếu" }, { key: 'createAt', label: "Ngày tạo" }, { key: 'inventoryOutputName', label: "Chi nhánh chuyển" }, { key: 'status', label: "Trạng thái" }, { key: 'inventoryInputName', label: "Chi nhánh nhận" },
{ key: 'movingAt', label: "Ngày chuyển" }, { key: 'finishAt', label: "Ngày nhận" }, { key: 'username', label: "Người tạo" }]

const getBadge = status => {
    switch (status) {
        case 'Chờ chuyển': return 'text-primary'
        case 'Đang chuyển': return 'text-warning'
        case 'Đã nhận': return 'text-success'
        case 'Đã hủy': return 'text-danger'
        default: return 'text-primary'
    }

}
const Index = (props) => {


    const [transfers, setTransfer] = useState([]);
    const [key, setKey] = useState("");
    const [dateMin, setDateMin] = useState("");
    const [dateMax, setDateMax] = useState("");


    //Trang hiển thị
    const [total, setTotal] = useState(1);
    const [currentPage, setCurrentPage] = useState(1)
    const [currentLimit, setCurrentLimit] = useState(5)

    const [validate, setValidate] = useState(null)
    const [roleDisplay, setRoleDisplay] = useState({ display: "none" })

    const getUser = JSON.parse(sessionStorage.user)
    const roles = getUser.roles;

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

        axios.get(`http://localhost:8080/transfers?key=${key}&dateMin=${dateMin}&dateMax=${dateMax}&page=${currentPage}&limit=${currentLimit}`, {
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(res => {
                setTransfer(res.data)
                console.log(res.data)
            })

    }, [key, currentPage, currentLimit, dateMin, dateMax]);

    useEffect(() => {

        axios.get(`http://localhost:8080/transfers?key=${key}&dateMin=${dateMin}&dateMax=${dateMax}`, {
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(res => {
                setTotal(Math.ceil(res.data.length / currentLimit))
            })

    }, [key, currentLimit, dateMin, dateMax]);

    const addTransfer = () => {
        props.history.push('/transfers/add');
    }

    const changeLimit = (e) => {
        setCurrentLimit(e.target.value);


    }
    const changeKey = (e) => {
        console.log(e.target.value);
        setKey(e.target.value);
        setCurrentPage(1)



    }
    const handleChange1 = (e) => {
        setDateMin(e.target.value);
        setCurrentPage(1)
    }
    const handleChange2 = (e) => {
        setDateMax(e.target.value);
        setCurrentPage(1)
    }

    // useEffect(() => {
    //     if (dateMin == null || dateMax == null) {
    //         setValidate(null)
    //     }
    //     else if (dateMin > dateMax > 0) {
    //         setValidate(true);
    //     }
    //     else {
    //         setValidate(false)
    //     }
    // }, [dateMin,dateMax])

    const resetDate = () => {
        setDateMin("");
        setDateMax("");
        setKey("");
        setCurrentPage(1)
    }
    function formatDate(e) {
        if (e == null) {
            return ""
        }
        return moment(e).format('DD/MM/YYYY HH:mm')
    }




    return (
        <>

            <CCard>
                <CCardHeader>
                    <CRow>
                        <CCol xs="12" sm="11">
                            <h2 className="text-left">Danh sách phiếu chuyển</h2>
                        </CCol>
                        <CCol xs="12" sm="1">
                            <CButton style={roleDisplay} className="" size="xs" active block color="primary" aria-pressed="true" onClick={addTransfer} >Tạo</CButton>
                        </CCol>


                    </CRow>

                </CCardHeader>
                <CCardBody>
                    <CRow className="d-flex justify-content-between">
                        <CCol md='4'>
                            <CFormGroup className="d-flex justify-content-between">
                                <CLabel htmlFor="search" className="w-25 pt-1">Tìm kiếm:</CLabel>

                                <CInput id="search" name="search" value={key} onChange={changeKey} placeholder="Tìm kiếm theo mã phiếu, chi nhánh, người tạo..." />

                            </CFormGroup>
                        </CCol>
                        <CCol md='6' >
                            <CFormGroup row>
                                <CCol xs="1" md="1" className="mt-1">
                                    <CLabel htmlFor="date-start">Từ </CLabel>
                                </CCol>
                                <CCol xs="5" md="5">
                                    <CInput type="date" invalid={validate} valid={(validate == null ? validate : !validate)} value={dateMin} id="date-min" name="dateMin" on className="form-control-warning" onChange={handleChange1} />
                                    <CInvalidFeedback className="help-block">
                                        Thời gian chọn không hợp lệ
                                    </CInvalidFeedback>
                                </CCol>

                                <CCol xs="1" md="1" className="mt-1">
                                    <CLabel htmlFor="date-end">  Đến</CLabel>
                                </CCol>
                                <CCol xs="5" md="5">
                                    <CInput type="date" id="date-max" value={dateMax} invalid={validate} valid={(validate == null ? validate : !validate)} name="dateMax" className="form-control-warning" onChange={handleChange2} />
                                    <CInvalidFeedback className="help-block">
                                        Thời gian chọn không hợp lệ
                                    </CInvalidFeedback>
                                </CCol>

                            </CFormGroup>
                        </CCol>
                        <CCol md='' className="text-left">
                            <CButton className="w-50" active block color="info" aria-pressed="true" onClick={resetDate}>Đặt lại</CButton>

                        </CCol>
                    </CRow>

                    <CDataTable
                        items={transfers}
                        fields={fields}
                        hover
                        //onRowClick={(item) => props.history.push(`/transfers/${item.id}`)}
                        scopedSlots={{
                            'code':
                                (item) => (
                                    <td>
                                        <Link to={{
                                            pathname: `/transfers/${item.id}`
                                        }}>{item.code}</Link>
                                    </td>
                                ),
                            'createAt': (item) => (
                                <td>{formatDate(item.createAt)}</td>
                            ),
                            'movingAt': (item) => (
                                <td>{formatDate(item.movingAt)}</td>
                            ),
                            'finishAt': (item) => (
                                <td>{formatDate(item.finishAt)}</td>
                            ),
                            'status':
                                (item) => (
                                    <td>
                                        <p className={getBadge(item.status)}>
                                            {item.status}
                                        </p>
                                    </td>
                                )
                        }}
                        bordered
                        size="sx"
                        striped

                    />

                    <CRow className="d-flex justify-content-between">
                        <CCol md='5'>
                            <CPagination
                                activePage={currentPage}
                                pages={total}
                                onActivePageChange={setCurrentPage}
                                onClick={() => {
                                    console.log(1111)
                                }}
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
