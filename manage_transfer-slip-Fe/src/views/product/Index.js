import React, { useEffect, useState } from 'react';

import {
    CCard,
    CCardBody,
    CCardHeader,
    CCol,
    CFormGroup,
    CInput,
    CLabel,
    CRow,
    CButton,
    CDataTable,
    CPagination,
    CSelect,
} from '@coreui/react'
import Select from 'react-select'

//data
import { useHistory, Link } from 'react-router-dom';

const fields = [ { key: 'link', label: 'Ảnh', _style: { width: '7%' } },{ key: 'code', label: "Mã SP", _style: { width: '15%' } }, { key: 'name', label: 'Tên sản phẩm', _style: { width: '20%' } },  { key: "categoryName", label: "Loại sản phẩm", _style: { width: '20%' } },{ key: 'price', label: "Giá bán", _style: { width: '15%', textAlign: "right" } }, { key: 'numberPro', label: ' Số lượng', _style: { width: '10%', textAlign: "right" } }]

const Index = () => {
    const getUser = JSON.parse(sessionStorage.user)

    const [items, setItems] = useState([]);
    const history = useHistory();
    const [sizePage, setSizePage] = useState(10);
    const [currPage, setCurrPage] = useState(1);
    const [keySearch, setKeySearch] = useState('');
    const [currInventory, setCurrInventory] = useState(getUser.inventoryId)
    const [total, setTotal] = useState(1);
    const [totalProduct, setTotalProduct] = useState(1);
    const [inventory, setInventory] = useState([])
    const [nameInventory, setNameInventory] = useState([])
    const [nameInventoryDisplay, setNameInventoryDisplay] = useState({ display: "none" });
    const [roleDisplay, setRoleDisplay] = useState({ display: "none" })

    const roles = getUser.roles

    useEffect(() => {
        if (roles) {
            roles.map((role) => {
                if (role === "ROLE_INVENTORIER") {
                    setRoleDisplay({ display: "inline" })
                } else if (role === "ROLE_MANAGER") {
                    setNameInventoryDisplay({ display: "inline" })
                } else if (role === "ROLE_COORDINATOR") {
                    setNameInventoryDisplay({ display: "inline" })
                }
            })
        }
    }, [])

    useEffect(() => {
        fetch(`http://localhost:8080/admin/products/${currInventory}?key-search=${keySearch}&page=${currPage}&size=${sizePage}`, {
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(res => res.json())
            .then(
                (result) => {
                    setItems(result[0])
                    setTotal(Math.ceil(result[1] / sizePage));
                    setTotalProduct(result[1])
                }
            )
    }, [currInventory, currPage, sizePage, keySearch])



    useEffect(() => {
        fetch(`http://localhost:8080/admin/inventory`, {
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })

            .then(res => res.json())
            .then(
                (result) => {
                    setInventory(result)
                }
            )
    }, [])

    useEffect(() => {
        setNameInventory([])
        if (inventory.length > 1) {
            inventory.map(item => {
                return setNameInventory(nameInventory => [...nameInventory, { value: item.id, label: item.name }])
            })
        }

    }, [inventory])

    const handleSearchChange = (e) => {
        setKeySearch(e.target.value)
        setCurrPage(1)
    }

    const getSizePage = (e) => {
        setSizePage(e.target.value)
    }

    const formatLinkImage = (link) => {
        if (link == null) {
            return "images/1.jpg";
        } else {
            return link;
        }
    }

    const CreateProduct = (e) => {
        history.push(`/products/add`)
    }
    const GetInventory = (e) => {
        setCurrInventory(e.value)
    }

    const format2 = (price, currency) => {
        if (price == null) {
            return 0 + " " + currency;
        } else {
            return price.toFixed(0).replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,') + " " + currency;
        }
    }

    return (

        <CCard>
            <CCardHeader className="d-flex justify-content-between">
                <CCol xs="12" sm="11">
                    <h2 className="text-left">Danh sách sản phẩm tồn kho {currInventory}</h2>
                </CCol>
                <CCol xs="12" sm="1">
                    <CButton className=""  size="xs" style={roleDisplay} active block color="primary" aria-pressed="true" onClick={CreateProduct}>Tạo</CButton>

                </CCol>
            </CCardHeader>
            <CCardBody className="">
                <CRow className="d-flex justify-content-between">
                    <CCol md='4'>
                        <CFormGroup className="d-flex justify-content-between">
                            <CLabel htmlFor="search" className="w-25 pt-1">Tìm kiếm:</CLabel>
                            <CInput id="search" name="search" placeholder="Tìm kiếm..." onChange={handleSearchChange} />
                        </CFormGroup>
                    </CCol>
                    <CCol md='0'>

                    </CCol>
                    <CCol md='2' className="" style={nameInventoryDisplay}>
                        <Select
                            style={{ color: "red" }}
                            placeholder="Chi nhánh"
                            name="nameInventory"
                            defaultMenuIsOpen={inventory.nameInventory}
                            options={nameInventory}
                            onChange={GetInventory}
                        />
                    </CCol>
                </CRow>
                <CRow className="mb-2">
                    <CCol>
                        Tổng số lượng sản phẩm <ins>{totalProduct}</ins>
                    </CCol>
                </CRow>

                <CDataTable
                    items={items}
                    fields={fields}
                    hover
                    bordered
                    striped
                    // onRowClick={ShowDetail}
                    size="sx"
                    scopedSlots={{
                        'code':
                            (product) => (
                                <td>
                                    <Link to={{
                                        pathname: `/products/${product.inventoryId}/${product.productId}/${product.id}`, state: { product }
                                    }}>{product.code}</Link>
                                </td>
                            ),
                        'price':
                            (item) => (
                                <td className="text-right">
                                    {format2(item.price, "vnđ")}
                                </td>
                            ),
                        'link':
                            (item) => (
                                <td className="text-right">
                                    <img src={process.env.PUBLIC_URL + "/" + formatLinkImage(item.link)} style={{ width: "100%" }} />
                                </td>
                            ),
                        'numberPro':
                            (item) => (
                                <td className="text-right">
                                    {item.numberPro}
                                </td>
                            ),
                        'numberInput':
                            (item) => (
                                <td className="text-right"> {item.numberInput} </td>
                            )
                    }}
                />
                <CRow className="d-flex justify-content-between">
                    <CCol md='6'>
                        <CPagination
                            activePage={currPage}
                            pages={total}
                            onActivePageChange={setCurrPage}
                        />
                    </CCol>

                    <CCol md='2'>
                        {currPage}/{total}
                    </CCol>

                    <CCol md="2" className="text-right " >
                        <CSelect custom name="rows" id="rows" style={{ width: '100%' }} onChange={getSizePage}>
                            <option value="10">10 sản phẩm</option>
                            <option value="20">20 sản phẩm</option>
                            <option value="30">30 sản phẩm</option>
                            <option value="50">50 sản phẩm</option>
                        </CSelect>
                    </CCol>
                </CRow>

            </CCardBody>
        </CCard>
    );
};

export default Index;
