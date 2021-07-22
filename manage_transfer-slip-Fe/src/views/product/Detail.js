import React, { useEffect, useState } from "react";
import {
    CButton,
    CCard,
    CCardBody,
    CCardHeader,
    CCol,
    CFormGroup,
    CTextarea,
    CInput,
    CLabel,
    CRow,
    CSelect,
    CImg,
} from '@coreui/react'
// import ImageA from '../../assets/prictures'
import { useHistory } from "react-router-dom";
import swal from 'sweetalert';


const fields = [{ key: 'code', label: "Mã sản phẩm" }, { key: 'productName', label: "Tên sản phẩm" },
{ key: 'total', label: "Số lượng" }]


function Detail(props) {
    console.log("Props Product :", props.location.state.product)
    console.log("Props: :", props)

    const [checkValidateRequireCode, setCheckValidateRequireCode] = useState({ display: "none" })
    const [checkValidateExistCode, setCheckValidateExistCode] = useState({ display: "none" })
    const [checkValidateRequireName, setCheckValidateRequireName] = useState({ display: "none" })
    const [checkValidatePrice, setCheckValidatePrice] = useState({ display: "none" })
    const [checkValidateRequireCategory, setCheckValidateRequireCategory] = useState({ display: "none" })
    const [checkValidateColor, setCheckValidateColor] = useState({ display: "none" })
    const [checkValidateRequireSize, setCheckValidateRequireSize] = useState({ display: "none" })
    const [checkValidateSizeSpace, setCheckValidateSizeSpace] = useState({ display: "none" })

    const [createDisplay, setCreateDisplay] = useState({ display: "none" })
    const [btnDisplay, setBtnDisplay] = useState({ display: "none" })
    const [btn12Display, setBtn12Display] = useState({ display: "none" })
    const [category, setCategory] = useState([])
    const [nameCategory, setNameCategory] = useState([{ value: 0, label: "Tất cả" }])
    const [product, setProduct] = useState(props.location.state.product)
    const history = useHistory();
    const id = props.match.params.id;
    const idDetail = props.match.params.idDetail;
    console.log("id: ", idDetail)
    console.log(props)
    const getUser = JSON.parse(sessionStorage.user)


    const roles = getUser.roles

    useEffect(() => {
        if (product.check === true) {
            setCreateDisplay({ display: "inline" })
        }
    })
    useEffect(() => {
        if (roles) {
            roles.map((role) => {
                if (role === "ROLE_INVENTORIER") {
                    setBtnDisplay({ display: "inline" })
                    setBtn12Display({ display: "none" })
                } else {
                    setBtn12Display({ display: "inline" })
                }
            })
        }
    }, [])

    useEffect(() => {
        // getCode()
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
        setNameCategory([{ value: 0, label: "Tất cả" }]);
        category.map(item => {
            return setNameCategory(nameCategory => [...nameCategory, { value: item.id, label: item.name }])
        })
    }, [category])

    const DeleteItem = () => {
        swal({
            title: "Bạn chắc chắn muốn xóa?",
            text: "Dữ liệu bị xóa sẽ không thể lấy lại",
            icon: "warning",
            buttons: ["Hủy", "Xoá"],
            dangerMode: true,
            showClass: {
                popup: 'animate__animated animate__fadeInDown'
            },
            hideClass: {
                popup: 'animate__animated animate__fadeOutUp'
            }
        })

            .then((check) => {
                if (check) {
                    var axios = require('axios');
                    var data = '';

                    var config = {
                        method: 'delete',
                        url: `http://localhost:8080/admin/products/${product.id}`,
                        headers: {
                            'authorization': `Bearer ${getUser.token}`,
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        data: data
                    };

                    axios(config)
                        .then(function (response) {
                            history.push("/products/list")
                        })

                }
            })

    }

    const format2 = (price) => {
        console.log("Gia: ", price)
        if (price == null) {
            return 0;
        } else {
            return price.toFixed(0).replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
        }
    }

    const UpdateItem = () => {

        var axios = require('axios');
        var data = JSON.stringify(product);

        var config = {
            method: 'put',
            url: `http://localhost:8080/admin/products/${id}`,
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: data
        };

        axios(config)
            .then(function (response) {

                swal({
                    title: "Thành công",
                    text: "Thông báo sửa sản phẩm",
                    icon: "success",
                    timer: 2000,
                    buttons: false
                })
            })
            .catch(function (error) {
                swal({
                    title: "Không thành công",
                    text: "Xem lại thông tin truyền vào",
                    icon: "error",
                    timer: 2000,
                    buttons: false
                })
            });
    }

    const GetInput = (e) => {
        setProduct({ ...product, [e.target.name]: e.target.value })
    }
    const handlePrice = (e) => {
        console.log(e)
        setProduct({ ...product, [e.target.name]: e.target.value })
    }


    const Goback = () => {
        props.history.goBack();
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
    const validateBlurCode = (e) => {
        validateSpace(e.target.value, setCheckValidateRequireCode)
    }
    const validateBlurName = (e) => {
        validateNullAndBlank(e.target.value, setCheckValidateRequireName)
    }

    const validateBlurPrice = (e) => {
        validateNullAndBlank(e.target.value, setCheckValidatePrice)
    }
    const validateBlurCategory = (e) => {
        if (e.target.value == 0) {
            setCheckValidateRequireCategory({ display: "inline", color: "red" })
        } else {
            setCheckValidateRequireCategory({ display: "none" })
        }
    }
    const validateBlurColor = (e) => {
        validateNullAndBlank(e.target.value, setCheckValidateColor)
    }
    const validateBlurSize = (e) => {
        validateNullAndBlank(e.target.value, setCheckValidateRequireSize)
        validateSpace(e.target.value, setCheckValidateSizeSpace)
    }
    console.log("Dataaaaaaaaaaaa: ", product)

    const formatLinkImage = (link) => {
        if (link == null) {
            return "images/1.jpg";
        } else {
            return link;
        }
    }
    return (
        <>
            <CRow>
                <CCol xs="12" sm="12">
                    <div className="mb-3" style={{ cursor: "pointer" }} onClick={Goback}> {`< Quay lại`} </div>


                </CCol>
                <CCol xs="12" sm="10">
                    <div className="mb-6" style={{ color: "black" }} > <h2><b>Chi tiết sản phẩm </b></h2> </div>
                </CCol>
                <CCol xs="12" sm="2">
                    <CButton className="" style={createDisplay} size="xs" block color="info" aria-pressed="true" >Tạo mới SP</CButton>

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

                                    name="code"
                                    defaultValue={product.code}
                                    onChange={GetInput}
                                    onBlur={validateBlurCode}
                                />
                                <div className="col-12" style={checkValidateRequireCode}><small>Mã code không chứa khoảng trắng</small></div>
                                <div className="col-12" style={checkValidateExistCode}><small>Mã sản phẩm đã tồn tại</small></div>
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel >Tên sản phẩm</CLabel>
                                <CInput
                                    name="name"
                                    defaultValue={product.name}
                                    onChange={GetInput}
                                    onBlur={validateBlurName}
                                />
                                <div className="col-12" style={checkValidateRequireName}><small>Thông tin bắt buộc</small></div>
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel >Giá sản phẩm (VNĐ)</CLabel>
                                <CInput type="number" name="price" defaultValue={product.price} onChange={handlePrice} onBlur={validateBlurPrice} />

                                <div className="col-12" style={checkValidatePrice}><small>Thông tin bắt buộc</small></div>
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel >Ghi chú</CLabel>
                                <CTextarea
                                    id="description"
                                    defaultValue={product.description}
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
                            <div style={{ color: "black" }} ><b>Thông tin loại sản phẩm</b></div>
                        </CCardHeader>
                        <CCardBody>
                            <CFormGroup>
                                <CLabel>Loại sản phẩm</CLabel>
                                <CSelect
                                    name="categoryName"
                                    onChange={GetInput}
                                    onBlur={validateBlurCategory}
                                >
                                    {category.map((product, index) => (
                                        <option key={index} defaultValue={product.name}>
                                            {product.name}
                                        </option>
                                    ))}
                                </CSelect>
                                <div className="col-12" style={checkValidateRequireCategory}><small>Thông tin bắt buộc</small></div>
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel >Màu sắc</CLabel>
                                <CInput
                                    name="color"
                                    defaultValue={product.color}
                                    onChange={GetInput}
                                    onBlur={validateBlurColor}
                                />
                                <div className="col-12" style={checkValidateColor}><small>Thông tin bắt buộc</small></div>

                            </CFormGroup>
                            <CFormGroup>
                                <CLabel >Kích thước</CLabel>
                                <CInput name="size" defaultValue={product.size} onChange={GetInput} onBlur={validateBlurSize} />
                                <div className="col-12" style={checkValidateRequireSize}><small>Thông tin bắt buộc</small></div>
                                <div className="col-12" style={checkValidateSizeSpace}><small>Kích thước không chứa dấu cách</small></div>
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel htmlFor="note">Ảnh sản phẩm</CLabel>
                                <CImg
                                    src={process.env.PUBLIC_URL + "/" + formatLinkImage(product.link)} style={{ width: "100%" }}
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
                <CCol xs="12" sm="2">
                    <CButton  size="sm" active block color="danger" aria-pressed="true" onClick={DeleteItem} style={btnDisplay, {width:'60%'}} >Xóa sản phẩm</CButton>
                </CCol>
                <CCol sm="9"></CCol>
                <CCol xs="12" sm="1" className="d-flex justify-content-end">
                </CCol>
                <CCol xs="12" sm="1">
                    <CButton style={{ width: "20%" }} size="sm" active block color="dark" aria-pressed="true" onClick={UpdateItem} style={btnDisplay}>Lưu</CButton>
                </CCol>
            </CRow>
            <CRow className="mb-2">
                <CCol xs="12" sm="1">
                    {/* <CButton size="sm" active block color="danger" aria-pressed="true">  Xóa </CButton> */}
                </CCol>
                <CCol xs="12" sm="9"></CCol>
                <CCol xs="12" sm="1">
                    {/* <CButton size="sm" active block color="dark" aria-pressed="true" > Quay lại </CButton> */}
                </CCol>
                <CCol xs="12" sm="1">
                    {/* <CButton size="sm" active block color="primary" aria-pressed="true"> Lưu </CButton> */}
                    <CButton size="sm" active block color="dark" aria-pressed="true" style={btn12Display} onClick={Goback}> Quay lại </CButton>

                </CCol>
            </CRow>

        </>
    )
}

export default Detail;
