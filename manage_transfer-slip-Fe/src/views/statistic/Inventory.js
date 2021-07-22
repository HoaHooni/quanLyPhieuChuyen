import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import moment from 'moment'
import {
    CCol,
    CRow,
    CCard,
    CCardBody,
    CCardHeader,
    CDataTable,
    CInput,
    CSelect,
    CFormGroup,
    CLabel,
    CPagination,
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
const Transfer = (props) => {
    const date = props.location.state.date;
    const inventory = props.location.state.inventory;
    const [transfers, setTransfer] = useState([]);
    const [key, setKey] = useState("");
    const [data, setData] = useState([])


    //Trang hiển thị
    const [total, setTotal] = useState(1);
    const [currentPage, setCurrentPage] = useState(1)
    const [currentLimit, setCurrentLimit] = useState(5)


    const getUser = JSON.parse(sessionStorage.user)

    useEffect(() => {

        axios.get(`http://localhost:8080/transfers/statistic/inventory-detail?key=${key}&date=${date}&inventory=${inventory}&page=${currentPage}&limit=${currentLimit}`, {
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(res => {
                setData(res.data)
            })
        console.log("111")
    }, [key, currentPage, currentLimit]);

    useEffect(() => {
        setTransfer(data[0])
        setTotal(Math.ceil(data[1] / currentLimit))
    }, [data])

    const changeLimit = (e) => {
        setCurrentLimit(e.target.value);
    }
    const changeKey = (e) => {
        setKey(e.target.value);
        setCurrentPage(1)
    }

    function formatDate(e) {
        if (e == null) {
            return ""
        }
        return moment(e).format('DD/MM/YYYY HH:mm')
    }

    const Goback = () => {
        props.history.push("/statistic/transfer")
    }


    return (
        <>
            <CRow>
                <CCol xs="12" sm="12">
                    <div className="mb-3" style={{ cursor: "pointer" }} onClick={Goback}> {`< Quay lại`} </div>
                </CCol>
            </CRow>
            <CRow>
                <CCol xs="12" sm="12">
                    <CCard>
                        <CCardHeader>
                            <CRow>
                                <CCol xs="12" sm="11">
                                    <h2 className="text-left">Báo cáo chi tiết kho</h2>
                                </CCol>
                            </CRow>

                        </CCardHeader>
                        <CCardBody>
                            <CRow className="d-flex justify-content-between">
                                <CCol md='4'>
                                    <CFormGroup className="d-flex justify-content-between">
                                        <CLabel htmlFor="search" className="w-25 pt-1">Tìm kiếm:</CLabel>

                                        <CInput id="search" name="search" value={key} onChange={changeKey} placeholder="Tìm kiếm... " />

                                    </CFormGroup>
                                </CCol>

                            </CRow>

                            <CDataTable
                                items={transfers}
                                fields={fields}
                                hover
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

                                    />

                                </CCol>


                                <CCol md='2' className="text-right">
                                    <span>Số bản ghi: </span>
                                    <CSelect custom name="rows" id="rows" style={{ width: '53%' }} onChange={changeLimit}>
                                        <option value="1">5</option>
                                        <option value="2">10</option>
                                        <option value="3">15</option>
                                        <option value="4">20</option>
                                    </CSelect>
                                </CCol>
                            </CRow>
                        </CCardBody>
                    </CCard>
                </CCol>
            </CRow>


        </>

    );
};

export default Transfer;