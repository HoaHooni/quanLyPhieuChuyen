import React, { useState, useEffect } from 'react';
import "../../assets/prictures/logo-sapo.png"

import {
    CButton,
    CCard,
    CCardBody,
    CCardHeader,
    CCol,
    CInputFile,
    CFormGroup,
    CTextarea,
    CInput,
    CLabel,
    CRow,
    CSelect,
    CImg,
} from '@coreui/react'
import swal from 'sweetalert';

const fields = [{ key: 'code', label: "Mã sản phẩm" }, { key: 'productName', label: "Tên sản phẩm" },
{ key: 'total', label: "Số lượng" }]



function Add(props) {
    const [product, setProduct] = useState([]);
    const getUser = JSON.parse(sessionStorage.user)

    const [category, setCategory] = useState([])

    const [checkValidateRequireCode, setCheckValidateRequireCode] = useState({ display: "none" })
    const [checkValidateRequireName, setCheckValidateRequireName] = useState({ display: "none" })
    const [checkValidateRequireSize, setCheckValidateRequireSize] = useState({ display: "none" })
    const [checkValidateRequireCategory, setCheckValidateRequireCategory] = useState({ display: "none" })
    const [checkValidateExistCode, setCheckValidateExistCode] = useState({ display: "none" })
    const [checkValidatePrice, setCheckValidatePrice] = useState({ display: "none" })
    const [checkValidateNumber, setCheckValidateNumber] = useState({ display: "none" })
    const [checkValidateColor, setCheckValidateColor] = useState({ display: "none" })
    const [checkValidateSizeSpace, setCheckValidateSizeSpace] = useState({ display: "none" })
    const [checkImage, setCheckImage] = useState({ display: "none" })
    const [hover, setHover] = useState({ cursor: "pointer" })

    const [idNew, setIdNew] = useState(0);
    const [idNewDetail, setIdNewDetail] = useState(0);

    const [link, setLink] = useState('')
    const [Image, setImage] = useState('')
    const [nameImage, setNameImage] = useState('')

    useEffect(() => {
        fetch("http://localhost:8080/admin/category", {
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(res => res.json())
            .then(
                (result) => {
                    setCategory(result);
                }
            )
    }, [])



    useEffect(() => {
        fetch(`http://localhost:8080/admin/products/last-product`, {
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })

            .then(res => res.json())
            .then(
                (result) => {
                    console.log("result: ", result)
                    setIdNew(result)
                    setProduct({ ...product, code: `pro${result + 1}` })
                    setProduct({ ...product, check: true })
                }
            )
    }, [])
    // useEffect(() => {
    //     if (!product.code) {
    //         console.log("11111111111111111: ", "pro" + idNew)
    //         setProduct({ ...product, code: `pro${idNew}` })
    //         // setProduct({ ...product, [e.target.name]: linkImage })
    //     }
    // }, [idNew])
    useEffect(() => {
        fetch(`http://localhost:8080/admin/products/last-inventory-detail`, {
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })

            .then(res => res.json())
            .then(
                (result) => {
                    setIdNewDetail(result)
                }
            )
    }, [])
    const CreateProduct = () => {
        console.log("product1: ", product)
        console.log("product2: ", product)
        var axios = require('axios');
        var data = JSON.stringify(product);

        var config = {
            method: 'post',
            url: 'http://localhost:8080/admin/products',
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: data
        };

        axios(config)
            .then(function (response) {
                console.log("http status: ", response.status)
                return response.data;
            })
            .then((status) => {
                console.log("status: ", status)
                if (status.length == 0 || status == 0) {
                    setCheckValidateExistCode({ display: "none" })
                    swal({
                        title: "Tạo mới sản phẩm",
                        text: "Tạo mới sản phẩm thành công",
                        icon: "success",
                        timer: 2000,
                        buttons: false
                    }).then(
                        props.history.push({ pathname: `/products/${getUser.inventoryId}/${idNew}/${idNewDetail}`, state: { product } })
                        // props.history.push(`/products/list`)

                    )
                } else {
                    status.forEach(element => {
                        if (element == 1) {
                            setCheckValidateExistCode({ display: "inline", color: "red" });
                        } else if (element == 2) {
                            setCheckValidateRequireName({ display: "inline", color: "red" })
                        } else if (element == 3) {
                            setCheckValidatePrice({ display: "inline", color: "red" })
                        } else if (element == 4) {
                            setCheckValidateRequireCategory({ display: "inline", color: "red" })
                        } else if (element == 5) {
                            setCheckValidateColor({ display: "inline", color: "red" })
                        } else if (element == 6) {
                            setCheckValidateRequireSize({ display: "inline", color: "red" })
                        }
                        swal({
                            title: "Tạo mới sản phẩm",
                            text: "Tạo mới sản phẩm không thành công",
                            icon: "warning",
                            timer: 2000,
                            buttons: false
                        })
                    });
                }

            })
            .catch(function (error) {
                swal({
                    title: "Tạo mới sản phẩm",
                    text: "Tạo mới sản phẩm không thành công",
                    icon: "warning",
                    timer: 2000,
                    buttons: false
                }).then(
                    setCheckValidateRequireName({ display: "inline", color: "red" }),
                    setCheckValidatePrice({ display: "inline", color: "red" }),
                    setCheckValidateRequireCategory({ display: "inline", color: "red" }),
                    setCheckValidateColor({ display: "inline", color: "red" }),
                    setCheckValidateRequireSize({ display: "inline", color: "red" })

                )
            });
    }



    const GetInput = (e) => {
        setProduct({ ...product, [e.target.name]: e.target.value })
    }

    const GetCategory = (e) => {
        // setProduct([...product, e.value])
        setProduct({ ...product, [e.target.name]: e.target.value })
    }

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
    const validateNumber = (value, setCheck) => {
        if (/^\d+$/.test(value)) {
            setCheck({ display: "none" })
        }
        else if (value == "") {
            setCheck({ display: "none" })
        }
        else {
            setCheck({ display: "inline", color: "red" })
        }
    }

    const validateBlurName = (e) => {
        validateNullAndBlank(e.target.value, setCheckValidateRequireName)
    }

    const validateBlurCode = (e) => {
        validateSpace(e.target.value, setCheckValidateRequireCode)
    }
    const validateBlurPrice = (e) => {
        validateNullAndBlank(e.target.value, setCheckValidatePrice)
        validateNumber(e.target.value, setCheckValidateNumber)
    }
    const validateBlurColor = (e) => {
        validateNullAndBlank(e.target.value, setCheckValidateColor)
    }
    const validateBlurSize = (e) => {
        validateNullAndBlank(e.target.value, setCheckValidateRequireSize)
        validateSpace(e.target.value, setCheckValidateSizeSpace)
    }

    const validateBlurCategory = (e) => {
        if (e.target.value == 0) {
            setCheckValidateRequireCategory({ display: "inline", color: "red" })
        } else {
            setCheckValidateRequireCategory({ display: "none" })
        }
    }
    const Goback = () => {
        props.history.push("/products/list")
    }
    const handleImage = (e) => {
        setNameImage(e.target.files[0].name)
        setImage(e.target.files[0]);

        if (e.target.files[0].name !== "") {
            setCheckImage({ display: "inline" })
        }

        var axios = require('axios');
        var FormData = require('form-data');
        var fs = require('fs');
        var data = new FormData();
        data.append('file', e.target.files[0]);

        var config = {
            method: 'post',
            url: 'http://localhost:8080/uploadFile',
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: data
        };

        axios(config)
            .then(function (response) {
                console.log("222222222222222", JSON.stringify(response.data));
            })
            .catch(function (error) {
                setLink(e.target.files[0].name)
            });


        let linkImage = `images/${e.target.value.split("\\")[2]}`
        setProduct({ ...product, [e.target.name]: linkImage })
        console.log("product ", product)
    }

    const onMouseOver = () => {
        setHover({ color: "blue", cursor: "pointer" })
    }

    const onMouseOut = () => {
        setHover({ cursor: "pointer" })
    }
    return (
        <>

            <CRow>
                <CCol xs="12" sm="12">
                    <div className="mb-3" style={{ cursor: "pointer" }} onClick={Goback}> {`< Quay lại`} </div>
                    <div className="mb-6" style={{ color: "black" }} > <h2><b>Thêm mới sản phẩm </b></h2> </div>
                </CCol>
            </CRow>
            <CRow className="mt-2">
                <CCol xs="12" sm="8">
                    <CCard>
                        <CCardHeader>
                            <div style={{ color: "black" }} ><b>Thông tin sản phẩm</b></div>
                        </CCardHeader>
                        <CCardBody>
                            <CFormGroup>
                                <CLabel>Mã code</CLabel>
                                <CInput
                                    onBlur={validateBlurCode}
                                    name="code"
                                    onChange={GetInput}
                                />
                                <div className="col-12" style={checkValidateRequireCode}><small>Mã code không chứa khoảng trắng</small></div>
                                <div className="col-12" style={checkValidateExistCode}><small>Mã sản phẩm đã tồn tại</small></div>
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel >Tên sản phẩm <span style={{ color: "red" }}> *</span></CLabel>
                                <CInput
                                    name="name"
                                    onBlur={validateBlurName}
                                    onChange={GetInput}
                                />
                                <div className="col-12" style={checkValidateRequireName}><small>Thông tin bắt buộc</small></div>
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel >Giá sản phẩm (VNĐ)<span style={{ color: "red" }}> *</span></CLabel>
                                <CInput type="number" name="price" onChange={GetInput} onBlur={validateBlurPrice} />
                                <div className="col-12" style={checkValidatePrice}><small>Thông tin bắt buộc</small></div>
                                <div className="col-12" style={checkValidateNumber}><small>Nhập sai định dạng</small></div>
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel >Ghi chú</CLabel>
                                <CTextarea
                                    id="description"
                                    rows="5"
                                    onChange={GetInput}
                                />
                            </CFormGroup>
                        </CCardBody>
                    </CCard>
                </CCol>

                <CCol xs="12" sm="4">
                    <CCard>
                        <CCardHeader>
                            <div style={{ color: "black", cursor: "pointer" }} ><b>Thông tin loại sản phẩm</b></div>
                        </CCardHeader>
                        <CCardBody>
                            <CFormGroup>
                                <CLabel>Loại sản phẩm <span style={{ color: "red", cursor: "pointer" }}> *</span></CLabel>
                                <CSelect
                                    name="categoryName"
                                    onChange={GetCategory}
                                    onBlur={validateBlurCategory}
                                >
                                    <option value="0" style={{ cursor: "pointer" }}>Chọn Loại sản phẩm</option>
                                    {category.map((item, index) => (
                                        <option key={index} value={item.name} style={{ cursor: "pointer" }}>
                                            {item.name}
                                        </option>
                                    ))}
                                </CSelect>
                                <div className="col-12" style={checkValidateRequireCategory}><small>Thông tin bắt buộc</small></div>
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel >Màu sắc <span style={{ color: "red" }}> *</span></CLabel>
                                <CInput
                                    name="color"
                                    onBlur={validateBlurColor}
                                    onChange={GetInput}
                                />
                                <div className="col-12" style={checkValidateColor}><small>Thông tin bắt buộc</small></div>
                                {/* <div className="col-12" style={checkValidateColor}><small>Thông tin bắt buộc</small></div> */}
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel >Kích thước <span style={{ color: "red" }}> *</span></CLabel>
                                <CInput name="size" onBlur={validateBlurSize} onChange={GetInput} />
                                <div className="col-12" style={checkValidateRequireSize}><small>Thông tin bắt buộc</small></div>
                                <div className="col-12" style={checkValidateSizeSpace}><small>Kích thước không chứa dấu cách</small></div>
                            </CFormGroup>
                            {/* <hr /> */}
                            <CFormGroup row>
                                <CCol xs="12" md="9">
                                    <CLabel id="hoverLink" for="link" style={hover} onMouseOver={onMouseOver} onMouseOut={onMouseOut}><ins>Chọn ảnh sản phẩm</ins>
                                        <CInputFile id="link" name="link" onChange={handleImage} accept="image/png, image/jpeg" style={{ display: "none" }} />
                                    </CLabel>
                                </CCol>
                            </CFormGroup >
                            <CFormGroup style={checkImage}>
                                <CLabel htmlFor="note">Ảnh sản phẩm</CLabel>
                                <CImg
                                    src={process.env.PUBLIC_URL + `/images/${link}`}
                                    block
                                    className="mb-2"
                                    style={{ width: "100%" }}
                                />
                            </CFormGroup>
                        </CCardBody>
                    </CCard>
                </CCol>
            </CRow>
            <CRow className="mb-2">
                <CCol xs="12" sm="6">
                </CCol>
                <CCol xs="12" sm="6" className="d-flex justify-content-end">
                    <CButton style={{ width: "20%" }} size="sm" active block color="info" aria-pressed="true" onClick={CreateProduct}>Tạo</CButton>
                </CCol>
            </CRow>

        </>
    );
}

export default Add;
