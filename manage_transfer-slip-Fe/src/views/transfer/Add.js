import React, { useState, useEffect,useRef } from 'react';
import axios from 'axios';
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
    CDataTable,
} from '@coreui/react'
import Select from 'react-select'
import Swal from 'sweetalert2';
import { CIcon } from '@coreui/icons-react';

const fields = [{ key: 'code', label: "Mã sản phẩm", _style: { width: '10%' } }, { key: 'name', label: "Tên sản phẩm", _style: { width: '20%', textAlign: 'center' } }, { key: 'numberPro', label: "Tồn kho", _style: { width: '7%' } },
{ key: 'number', label: "Số lượng", _style: { width: '10%', textAlign: 'center' } }, { key: 'delete', label: "", _style: { width: '5%' } }]

function Add(props) {

    const [products_input, setProductInput] = useState([]);
    const [products, setProduct] = useState([]);
    const [inventories, setInventory] = useState([]);
    //const [inventoryIdInput, setInventoryIdInput] = useState();
    const [inventoryIdOutput, setInventoryIdOutput] = useState();
    const [filterInventoryOutput, setFilerInventoryOutput] = useState([]);
    const [filterInventoryInput, setFilerInventoryInput] = useState([]);
    const [filterProduct, setFilerProduct] = useState([]);
    const [inputTransferDetailDTOList, setTransferDetail] = useState([]);
    const [transfer, setTransfer] = useState({
        note: "",
        code: "",
        user_id: "",
        inventoryInputId: "",
        inventoryOutputId: "",
        inputTransferDetailDTOList: inputTransferDetailDTOList
    });

    const [checkValidateRequireInventoryInput, setCheckValidateRequireInventoryInput] = useState({ display: "none" })
    const [checkValidateRequireInventoryOutput, setCheckValidateRequireInventoryOutput] = useState({ display: "none" })
    const [checkValidateRequireInventory, setCheckValidateRequireInventory] = useState({ display: "none" })
    const [checkValidateCodeSpace, setCheckValidateCodeSpace] = useState({ display: "none" })

    const getUser = JSON.parse(sessionStorage.user);

    const outputRef = useRef(null);
    const codeRef = useRef();
    const inputRef = useRef();
    const productRef = useRef();


    const formatOptionLabel = ({ value, label, customAbbreviation }) => (
        <div className="d-flex justify-content-between">
           
          <div>{value.name}<br></br>
          {value.code}
            </div>
          <div style={{   width: "7rem" }}>
            Tồn kho: <b style={{   color: "black" }}>{value.numberPro}</b>
          </div>
        </div>
      );


    useEffect(() => {
        axios.get(`http://localhost:8080/admin/inventory`,{
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(res => {
                setInventory(res.data)
            })

    }, []);

    useEffect(() => {
        inventories.map(item => {
            setFilerInventoryOutput(inventories => [...inventories, { value: item, label: item.name }])
        })


    }, [inventories]);

    useEffect(() => {
        inventories.map(item => {
            setFilerInventoryInput(inventories => [...inventories, { value: item, label: item.name }])
        })


    }, [inventories]);

    

    

    useEffect(() => {
        products.map(item => {
            setFilerProduct(products => [...products, { value: item, label: item.name + item.code  }])
        })


    }, [products]);
    useEffect(() => {
        setTransferDetail([])
        products_input.map(item => {
            setTransferDetail(products_input => [...products_input, { productId: item.productId, total: item.total == null ? "1" : item.total }])
        })
        // setTransfer({ ...transfer, inputTransferDetailDTOList: inputTransferDetailDTOList })
        console.log("lll", inputTransferDetailDTOList);

    }, [products_input]);

    useEffect(() => {

        setTransfer({ ...transfer, inputTransferDetailDTOList: inputTransferDetailDTOList })
        console.log("ll44444l", inputTransferDetailDTOList);

    }, [inputTransferDetailDTOList]);

    useEffect(() => {
        console.log("bbbbbbbbbb")
        axios.get(`http://localhost:8080/admin/products/list/${inventoryIdOutput}`,{
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(res => {
                setProduct(res.data)
                console.log(res.data)
            })

    }, [inventoryIdOutput]);

    const handleMultiChange1 = (option) => {
        console.log("inven",inventories)
        // inventories.map(item => {
        //     setFilerInventoryInput(inventories => [inventories, { value: item, label: item.name }])
        // })
        setInventoryIdOutput(option.value.id)
        setTransfer({ ...transfer, inventoryOutputId: option.value.id })
        setFilerProduct([]);
       // setFilerInventoryInput(filterInventoryInput.filter(filterInventoryInput => filterInventoryInput.value.id !== option.value.id))
    }
    const handleMultiChange2 = (option) => {
        // setInventoryIdInput(option.value.id)
        setTransfer({ ...transfer, inventoryInputId: option.value.id })
    }
    const selectProduct = (option) => {
        setProductInput([...products_input, option.value]);
        setFilerProduct(filterProduct.filter(filterProduct => filterProduct.value.id !== option.value.id))
        setTransfer({ ...transfer, user_id: getUser.id })
        
    }

    const setTotal = (e, item) => {
        let { value, min, max } = e.target;
        value = Math.max(Number(min), Math.min(Number(max), Number(value)));

        console.log("e", e)
        console.log("item", item)
        products_input.map(i => {
            if (i.id === item.id) { i.total = e.target.value }
            console.log(i)
        })

        console.log("iii", products_input);
        setTransferDetail([])
        products_input.map(item => {
            setTransferDetail(products_input => [...products_input, { productId: item.productId, total: item.total == null ? "1" : item.total }])
        })
    }

    const changeInput = (e) => {
        setTransfer({ ...transfer, [e.target.name]: e.target.value })

    }

    const deleteProduct = (e, item) => {

        setProductInput(products_input.filter(products_input => products_input.id !== item.id))
        setFilerProduct(products => [...products, { value: item, label: item.name }])

        setTransferDetail([])
        products_input.map(item => {
            setTransferDetail(products_input => [...products_input, { productId: item.productId, total: item.total == null ? "1" : item.total }])
        })

    }

    const handleSubmit = (e) => {
        
        if(transfer.inventoryOutputId === ""){
            Swal.fire({
                width: 550,
                position: 'top',
                text: 'Vui lòng chọn chi nhánh chuyển',
                    timer: 2500,
                showConfirmButton: false,
                
            }).then((value)=>{
                if(value){
                    outputRef.current.focus();
                }
            })           
    }
    else if(transfer.inventoryInputId === ""){
        Swal.fire({
            width: 550,
            position: 'top',
            text: 'Vui lòng chọn chi nhánh nhận',
                timer: 2500,
            showConfirmButton: false,
    }).then((value)=>{
        if(value){
            inputRef.current.focus();
        }
    })   
    }
    else if(transfer.inventoryInputId === transfer.inventoryOutputId){
        Swal.fire({
            width: 550,
            position: 'top',
            text: 'Chi nhánh chuyển và Chi nhánh nhận không thể trùng nhau',
                timer: 2500,
            showConfirmButton: false,
    }).then((value)=>{
        if(value){
            outputRef.current.focus();
        }
    })   
    }
    else if(transfer.inputTransferDetailDTOList === ""){
        Swal.fire({
            width: 550,
            position: 'top',
            text: 'Vui lòng chọn sản phẩm chuyển',
                timer: 2500,
            showConfirmButton: false,
    }).then((value)=>{
        if(value){
            productRef.current.focus();
        }
    })   
    }
    else {
        console.log("asd", transfer)

        axios.post(`http://localhost:8080/transfers`, transfer,{
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(res => {
                console.log(res)
                Swal.fire({
                    title: "Thành công",
                  
                    icon: "success",
                    timer: 1500
                 
                  }).then((value)=>{
                      if(value){
                          props.history.push(`/transfers/${res.data.id}`)
                      }
                  })
            }).catch(error => {
                
                if (error.response.data.mess === "Error: Code da ton tai") {
                    Swal.fire({
                        width: 300,
            position: 'top',
            text: 'Mã phiếu đã tồn tại',
                timer: 2500,
            showConfirmButton: false,
                     
                      })
                      
                }
                else if (error.response.data.mess === "TOTAL NEGATIVE") {
                    Swal.fire({
                        
                        width: 500,
            position: 'top',
            text: 'Số lượng chuyển không thể âm',
                timer: 2500,
            showConfirmButton: false,
                        
                     
                      })
                }
                else if (error.response.data.mess === "TOTAL PRODUCT INVALID") {
                    Swal.fire({
                        
                        width: 500,
            position: 'top',
            text: 'Số lượng chuyển không hợp lệ',
                timer: 2500,
            showConfirmButton: false,
                        
                     
                      })
                }
                else{

                }
            }
            );

    }
    

        

    }
    const cancle = () => {
        props.history.push('/transfers/list');



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
        validateSpace(e.target.value, setCheckValidateCodeSpace)
    }
    const validateBlurInventoryInput = (e) => {
        
        if (transfer.inventoryInputId === "") {
            setCheckValidateRequireInventoryInput({ display: "inline", color: "red" })
        } else {
            setCheckValidateRequireInventoryInput({ display: "none" })
        }
    }
    const validateBlurInventoryOutput = (e) => {
       
        if (transfer.inventoryOutputId === "") {
            setCheckValidateRequireInventoryOutput({ display: "inline", color: "red" })
        } else {
            setCheckValidateRequireInventoryOutput({ display: "none" })
        }
    }

    const validateBlurInventory = () => {

        if (transfer.inventoryInputId === transfer.inventoryOutputId) {
            setCheckValidateRequireInventory({ display: "inline", color: "red" })
        } else {
            setCheckValidateRequireInventory({ display: "none" })
        }
    }



    return (
        <>
            <CRow>
                <CCol xs="12" sm="12">
                    <div className="mb-3" style={{ cursor: "pointer" }} onClick={cancle}> {`< Quay lại`} </div>
                    <div className="mb-6" style={{ color: "black" }} > <h2><b>Thêm mới phiếu chuyển</b></h2> </div>

                </CCol>
            </CRow>
            <CRow className="mt-2">
                <CCol xs="12" sm="4">
                    <CCard>
                        <CCardHeader>
                            <div style={{ color: "black" }} ><b>Thông tin phiếu chuyển</b></div>
                        </CCardHeader>
                        <CCardBody>

                        <CFormGroup>
                                <CLabel htmlFor="code">Mã phiếu:</CLabel>
                                <CInput id="code" name="code" onChange={changeInput} placeholder="Nhập vào mã " onBlur={validateBlurCode} ref={codeRef}/>
                                <div className="col-12" style={checkValidateCodeSpace}><small>Mã phiếu không chứa dấu cách</small></div>


                            </CFormGroup>
                            <CFormGroup>
                                <CLabel htmlFor="inventory_input">Chi nhánh chuyển<span style={{ color: 'red' }}>*</span>:</CLabel>
                                <Select
                                ref={outputRef}
                                    id="inventoryoutput"
                                    name="inventoryoutput"
                                    placeholder="Chọn chi nhánh chuyển"
                                    options={filterInventoryOutput}
                                    onChange={handleMultiChange1}
                                    onBlur={validateBlurInventoryOutput}
                                />
                                {/* <CSelect
                                    className="form-control-warning"
                                    id="inventoryoutput"
                                    name="inventoryoutput"
                                    onChange={handleMultiChange1}
                                    onBlur={validateBlurInventoryOutput}
                                >
                                    <option value="0">Chọn kho</option>
                                    {filterInventoryOutput.map((item) => (
                                        <option value={item.value}>
                                            {item.label}
                                        </option>
                                    ))}
                                </CSelect> */}
                                <div className="col-12" style={checkValidateRequireInventoryOutput}><small>Thông tin bắt buộc</small></div>
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel htmlFor="inventory_output" >Chi nhánh nhận<span style={{ color: 'red' }}>*</span>:</CLabel>
                                <Select
                                ref={inputRef}
                                    id="inventoryinput"
                                    name="inventoryinput"
                                    placeholder="Chọn chi nhánh nhận"
                                    options={filterInventoryInput}
                                    onChange={handleMultiChange2}
                                    onBlur={validateBlurInventoryInput}
                                />
                                {/* <CSelect
                                    id="inventoryinput"
                                    name="inventoryinput"
                                    onChange={handleMultiChange2}
                                    onBlur={validateBlurInventoryInput}
                                    onBlur={validateBlurInventory}
                                >
                                    <option value="0">Chọn kho</option>
                                    {filterInventoryInput.map((item) => (
                                        <option value={item.value}>
                                            {item.label}
                                        </option>
                                    ))}
                                </CSelect> */}
                                <div className="col-12" style={checkValidateRequireInventoryInput}><small>Thông tin bắt buộc</small></div>
                                <div className="col-12" style={checkValidateRequireInventory}><small>Kho nhận không được trùng với kho chuyển</small></div>



                            </CFormGroup>
                            
                            <CFormGroup>
                                <CLabel htmlFor="note">Ghi chú</CLabel>
                                <CTextarea
                                    name="note"
                                    id="note"
                                    rows="5"
                                    onChange={changeInput}
                                    placeholder="Nội dung..."
                                    required
                                />
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
                            <CFormGroup>
                                <CLabel htmlFor="company">Tìm kiếm sản phẩm</CLabel>
                                <Select
                                ref={productRef}
                                    id="product"
                                    name="product"
                                    noOptionsMessage={() => 'Vui lòng chọn chi nhánh chuyển trước'}
                                    placeholder="Tìm kiếm sản phẩm theo tên, mã sản phẩm"
                                    options={filterProduct}
                                    value=""
                                    formatOptionLabel={formatOptionLabel}
                                    onChange={selectProduct}
                                />
                            </CFormGroup>

                            <CDataTable
                                items={products_input}
                                fields={fields}
                                noItemsView={

                                    { noItems: 'Phiếu chuyển của bạn chưa có sản phẩm nào' }}
                                hover
                                itemsPerPage={5}
                                pagination
                                striped
                                bordered
                                size="sm"
                                scopedSlots={{
                                    'code':
                                        (item) => (
                                            <td className=" pt-3">
                                                {item.code}
                                            </td>
                                        ),
                                    'name':
                                        (item) => (
                                            <td className="text-center pt-3">
                                                {item.name}
                                            </td>
                                        ),
                                    'numberPro':
                                        (item) => (
                                            <td className="text-center pt-3">
                                                {item.numberPro}
                                            </td>
                                        ),
                                    'number':
                                        (item) => {
                                            return (
                                                <td className="pt-2">
                                                    <CFormGroup>
                                                        <CInput  type="number"    min="1" max={item.numberPro} value={item.total == null ? "1" : item.total} name="number" onChange={(event) => setTotal(event, item)} />
                                                    </CFormGroup>
                                                </td>
                                            )
                                        },
                                    'delete':
                                        (item) => {
                                            return (
                                                <td>
                                                    <CFormGroup>
                                                        <CButton><CIcon onClick={(event) => deleteProduct(event, item)} name={'cilTrash'}></CIcon></CButton>
                                                        
                                                    </CFormGroup>
                                                </td>
                                            )
                                        }


                                }}
                            />
                        </CCardBody>
                    </CCard>
                </CCol>

            </CRow>
            <CRow className="mb-2">

                <CCol xs="12" sm="10">

                </CCol>
                <CCol xs="12" sm="1">
                    <CButton size="sm" active block color="dark" aria-pressed="true" onClick={cancle} >Hủy</CButton>
                </CCol>
                <CCol xs="12" sm="1">
                    <CButton size="sm" active block color="primary" aria-pressed="true" onClick={handleSubmit} >Lưu</CButton>
                </CCol>

            </CRow>
        </>
    );
}

export default Add;
