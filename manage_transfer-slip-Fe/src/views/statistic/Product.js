import React, { useEffect, useState } from 'react';
import axios from 'axios';

import {
    CCard,
    CCardBody,
    CCardHeader,
    CCol,
    CForm,
    CFormGroup,
    CInput,
    CLabel,
    CRow,
    CButton,
    CDataTable,
    CInvalidFeedback,
    CSelect,
    CPagination,
    CCollapse
} from '@coreui/react'
import Select from 'react-select'

const fields = [{ key: 'code', label: 'Mã sản phẩm', _style: { width: '20%', textAlign: "left" } }, { key: 'name', label: "Tên sản phẩm", _style: { width: '20%', textAlign: "left" } },
{ key: "transferImport", label: "Nhập", _style: { width: '10%', textAlign: "right" } }, { key: "transferExport", label: "Xuất", _style: { width: '10%', textAlign: "right" } }
    , { key: "productImport", label: "Số lượng nhập", _style: { width: '10%', textAlign: "right" } }, { key: "productExport", label: "Số lượng xuất", _style: { width: '10%', textAlign: "right" } }]

const Product = () => {

    const jwt = JSON.parse(sessionStorage.user).token;
    const [searchForm, setSearchForm] = useState({})
    const [validate, setValidate] = useState(null)
    const [InventoryData, setInventoryData] = useState([])
    const [filterOptions, setFilterOptions] = useState([{}])
    const [dataTable, setDataTable] = useState([])
    const [page, setPage] = useState({ limit: 5, currentPage: 1 })
    const [count, setCount] = useState(5)
    const [data, setdata] = useState([], {})
    const [sorter, setSorter] = useState({})
    const [details, setDetails] = useState([])

    async function getInventoryData() {
        await axios.get(`http://localhost:8080/admin/inventory`, {
            headers: {
                'authorization': `Bearer ${jwt}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(res => {
                setInventoryData(res.data)
            });

    }


    async function getDataTable() {
        var config = {
            method: 'post',
            url: 'http://localhost:8080/admin/statistic/product',
            headers: {
                'Authorization': `Bearer ${jwt}`,
                'Content-Type': 'application/json'
            },
            data: searchForm
        };

        await axios(config)
            .then(function (response) {
                setdata(response.data);
            })
            .catch(function (error) {
                console.log(error);
            });
    };

    useEffect(() => {
        getInventoryData();
        getDataTable();

    }, []);
    useEffect(() => {
        getDataTable();
    }, [searchForm]);

    useEffect(() => {
        setSearchForm({ ...searchForm, page: page })
    }, [page])
    useEffect(() => {
        setSearchForm({ ...searchForm, sort: sorter })
    }, [sorter])

    useEffect(() => {
        setFilterOptions([])
        InventoryData.map(item => {
            return setFilterOptions(filterOptions => [...filterOptions, { value: item.id, label: item.name }])
        })
    }, [InventoryData])

    useEffect(() => {
        setDataTable(data[0])
        setCount(data[1])
    }, [data])

    //validate
    useEffect(() => {
        if (searchForm.dateStart == null || searchForm.dateEnd == null) {
            setValidate(null)
        }
        else if (searchForm.dateStart > searchForm.dateEnd > 0) {
            setValidate(true);
        }
        else {
            setValidate(false)
        }
    }, [searchForm])

    const toggleDetails = (index) => {

        const positions = details.indexOf(index)
        let newDetails = details.slice()
        if (positions !== -1) {
            newDetails.splice(positions, 1)
        } else {
            newDetails = [...details, index]
        }
        setDetails(newDetails)
    }

    const handleChange = (e) => {
        const { name, value } = e.target;
        setSearchForm({
            ...searchForm,
            [name]: value
        });
    }

    return (
        <>
            <CCard>
                <CCardHeader>
                    <h3>Thống kê theo sản phẩm</h3>
                </CCardHeader>
                <CCardBody>
                    <CForm>
                        <CRow className="d-flex justify-content-between">
                            <CCol md='5' >
                                <CFormGroup row>
                                    <CCol xs="1" md="1" className="mt-1">
                                        <CLabel htmlFor="date-start">Từ </CLabel>
                                    </CCol>
                                    <CCol xs="5" md="5">
                                        <CInput type="date" invalid={validate} valid={(validate == null ? validate : !validate)} id="date-start" name="dateStart" on className="form-control-warning" onChange={handleChange} />
                                        <CInvalidFeedback className="help-block">
                                            Thời gian chọn không hợp lệ
                                        </CInvalidFeedback>
                                    </CCol>

                                    <CCol xs="1" md="1" className="mt-1">
                                        <CLabel htmlFor="date-end">  Đến</CLabel>
                                    </CCol>
                                    <CCol xs="5" md="5">
                                        <CInput type="date" id="date-end" invalid={validate} valid={(validate == null ? validate : !validate)} name="dateEnd" className="form-control-warning" onChange={handleChange} />
                                        <CInvalidFeedback className="help-block">
                                            Thời gian chọn không hợp lệ
                                        </CInvalidFeedback>
                                    </CCol>
                                </CFormGroup>
                            </CCol>
                            <CCol md='3'>
                                <CFormGroup className="d-flex justify-content-start">
                                    <CLabel className="mt-1" htmlFor="inventory">Chi nhánh</CLabel>
                                    <div style={{width:"70%"}}>
                                        <Select
                                            className="ml-3"
                                            id="inventory"
                                            name="inventory"
                                            placeholder="Chi  nhánh"
                                            options={filterOptions}
                                            onChange={(event) => {
                                                setSearchForm({ ...searchForm, 'inventory': event.map(item => item.value) });
                                            }}
                                            isMulti
                                        />
                                    </div>
                                </CFormGroup>
                            </CCol>

                            <CCol md='' className="text-left">
                                <CButton className="w-25" active block color="info" aria-pressed="true" onClick={() => { setSearchForm({}) }}>Đặt lại</CButton>

                            </CCol>
                        </CRow>
                    </CForm>
                    <CDataTable
                        items={dataTable}
                        fields={fields}
                        hover
                        noItemsView={{
                            noItems: 'Phiếu chuyển của bạn chưa có sản phẩm nào'
                        }}
                        bordered
                        sorter={{ external: true }}
                        onSorterValueChange={(e) => { setSorter(e); console.log("sort", sorter) }}
                        size="sx"
                        scopedSlots={{
                            'transferImport':
                                (item) => (
                                    <td className={"text-right"}>
                                        {item.transferImport}
                                    </td>
                                ),
                            'transferExport':
                                (item) => (
                                    <td className={"text-right"}>
                                        {item.transferExport}
                                    </td>
                                ),
                            'productImport':
                                (item) => (
                                    <td className={"text-right"}>
                                        {item.productImport}
                                    </td>
                                ),
                            'productExport':
                                (item) => (
                                    <td className={"text-right"}>
                                        {item.productExport}
                                    </td>
                                ),

                          
                        }}
                    />

                    <CRow className="d-flex justify-content-between">
                        <CCol md='5'>
                            <CPagination
                                activePage={page.currentPage}
                                pages={Math.ceil(count / page.limit)}
                                onActivePageChange={(e) => { setPage({ ...page, currentPage: e }) }}
                            />
                        </CCol>
                        <CCol md='2' className="text-right">
                            <CFormGroup>
                                <CSelect custom name="rows" id="rows" style={{ width: '53%' }} onChange={(e) => { setPage({ ...page, limit: e.target.value }) }} >
                                    <option value='5'>5 hàng</option>
                                    <option value='10'>10 hàng</option>
                                    <option value='15'>15 hàng</option>
                                    <option value='20'>20 hàng</option>
                                </CSelect>
                            </CFormGroup>
                        </CCol>
                    </CRow>
                </CCardBody>
            </CCard>
        </>

    );
};

export default Product;