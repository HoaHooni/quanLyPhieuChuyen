import React, { useState, useEffect } from 'react';
import {
    CCol,
    CFormGroup,
    CRow,
    CDataTable,
    CSelect,
    CPagination,
} from '@coreui/react'

import axios from 'axios';
import { useHistory } from 'react-router-dom';

const fields = [{ key: 'inventory', label: 'Kho', _style: { width: '20%', textAlign: "left" } }, { key: 'transferImport', label: "Phiếu nhập", _style: { width: '10%', textAlign: "right" } },
    { key: "transferExport", label: "Phiếu xuất", _style: { width: '10%', textAlign: "right" } }, { key: "productImport", label: "Sản phẩm nhập", _style: { width: '10%', textAlign: "right" } }
    , { key: "productExport", label: "Sản phẩm xuất", _style: { width: '10%', textAlign: "right" } }, { key: "proTotalImport", label: "Số lượng nhập", _style: { width: '10%', textAlign: "right" } }
    ,  { key: "proTotalExport", label: "Số lượng xuất", _style: { width: '10%', textAlign: "right" } }]


const DateDetail = (props) => {
    const history = useHistory();
    const jwt = JSON.parse(sessionStorage.user).token;
    const [dataTable, setDataTable] = useState([]);
    const [pageMini, setPageMini] = useState({ limit: 3, currentPage: 1 })
    const [countMini, setCountMini] = useState(1)
    const [data, setData] = useState([], {})
    const [sorterMini, setSorterMini] = useState({})
    const [searchFormMini, setSearchFormMini] = useState({
        date: props.date,
        inventory: !props.inventory ? null : props.inventory,
        user: !props.user ? null : props.user
    })
    console.log("prop", props)

    async function getData() {
        var config = {
            method: 'post',
            url: 'http://localhost:8080/admin/statistic/inventory',
            headers: {
                'Content-Type': 'application/json',
                'authorization': `Bearer ${jwt}`
            },
            data: searchFormMini
        };

        await axios(config)
            .then(function (response) {
                setData(response.data);
                
            })
            .catch(function (error) {
                console.log(error);
            });
    }
    useEffect(() => {
        getData();
    }, [])

    useEffect(() => {
        getData()
    }, [searchFormMini])

    useEffect(() => {
        setSearchFormMini({
            date: props.date,
            inventory: !props.inventory ? null : props.inventory,
            user: !props.user ? null : props.user
        })
    }, [props])

    useEffect(() => {
        setDataTable(data[0])
        if (data[1] != null) {
            setCountMini(data[1]);
        }
    }, [data])

    useEffect(() => {
        setSearchFormMini({ ...searchFormMini, page: pageMini })
    }, [pageMini])

    useEffect(() => {
        setSearchFormMini({ ...searchFormMini, sort: sorterMini })
    }, [sorterMini])

    
    return (
        <div style={{ background: '#eff3f7' }}>
            <CDataTable
                items={dataTable}
                fields={fields}
                hover
                bordered
                sorter
                onSorterValueChange={(e) => { setSorterMini(e) }}
                onRowClick={(item) => { history.push("/statistic/inventory",{date:  props.date, inventory: item.inventoryId} ) }}
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
                    'proTotalImport':
                        (item) => (
                            <td className={"text-right"}>
                                {item.proTotalImport}
                            </td>
                        ),
                    'proTotalExport':
                        (item) => (
                            <td className={"text-right"}>
                                {item.proTotalExport}
                            </td>
                        )
                }}
            />

            <CRow className="d-flex justify-content-between mx-0">
                <CCol md='5'>
                    <CPagination
                        style={{ background: '#eff3f7' }}
                        activePage={pageMini.currentPage}
                        pages={Math.ceil(countMini / pageMini.limit)}
                        onActivePageChange={(e) => { setPageMini({ ...pageMini, currentPage: e }) }}
                    />
                </CCol>
                <CCol md='2' className="text-right">
                    <CFormGroup>
                        <CSelect custom name="rows" id="rows" style={{ width: '53%' }} onChange={(e) => { setPageMini({ ...pageMini, limit: e.target.value }) }} >
                            <option value='3'>3 hàng</option>
                            <option value='5'>5 hàng</option>
                            <option value='7'>7 hàng</option>
                            <option value='9'>9 hàng</option>
                        </CSelect>
                    </CFormGroup>
                </CCol>
            </CRow>
        </div>
    );
};

export default DateDetail;