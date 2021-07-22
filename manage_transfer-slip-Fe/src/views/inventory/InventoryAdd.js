import { CButton, CCard, CCardBody, CCardHeader, CCol, CDataTable, CDropdown, CDropdownDivider, CDropdownItem, CDropdownMenu, CDropdownToggle, CForm, CFormGroup, CInput, CInvalidFeedback, CLabel, CPagination, CRow, CSelect, CTextarea, CToast, CToastBody, CToaster, CToastHeader, CValidFeedback } from '@coreui/react'
import axios from 'axios'
import React, { useEffect, useState } from 'react'
import moment from 'moment'
import { useHistory } from 'react-router'
import swal from 'sweetalert'


export default function InventoryAdd(props) {
    const URL_INVENTORY = "http://localhost:8080/admin/inventory"
    const user = JSON.parse( sessionStorage.getItem("user"))
    const history = useHistory()
    const [inventory, setInventory] = useState({
        code: "",
        name: "",
        address: "",
        phone: "",
        mail: ""
    })
    const [message, setMessage] = useState({
        code: '',
        name: '',
        address: '',
        phone: '',
        mail: ''
    })
    const [invalid, setInvalid] = useState({
        code: false,
        name: false,
        address: false,
        phone: false,
        mail: false,
    })   

 function onChange(e) {
    setInventory(
        {
            ...inventory,
            [e.target.id]: e.target.value
        }
    )
}

function checkCode(e) {
    if(e.trim() != "")
    {
        var check = new RegExp("[0-9a-zA-Z]{5,45}")
        if(check.test(e) == true){
            setMessage(
              {
                ...message,
                code: ""
              }
            )
            setInvalid({
                ...invalid,
                code: false
            })
        }
        else{
            setMessage(
                {
                    ...message,
                    code: "Mã code phải ít nhất 5 ký tự gồm có: a-z, 0-9 hoặc A-Z"
                }
            )
            setInvalid({
                ...invalid,
                code: true
            })
        }
    }
}

function checkName(e) {

    if(e.trim() != "")
    {
        var check = new RegExp("[^!-\/:-@[`{-~]{5,50}")
        if(check.test(e) == false){
            
            setMessage(
                {
                    ...message,
                    name: "Tên phải ít nhất 5 ký tự bao gồm: a-z, 0-9 hoặc A-Z, khoảng trắng"
                }
            )
            setInvalid({
                ...invalid,
                name: true
            })
        
        
        }else{
            setMessage(
                {
                    ...message,
                    name: ""
                }
            )
            setInvalid({
                ...invalid,
                name: false
            })
        }

    }
}

function checkAdress(e) {
    if(e.trim() != "")
    {
        var check = new RegExp("[^!-\/:-@[`{~]{5,50}")
        if(check.test(e) == true){
            setMessage(
              {
                ...message,
                address: ""
              }
            )
            setInvalid({
                ...invalid,
                address: false
            })
        }
        else{
            setMessage(
                {
                    ...message,
                    address: "Địa chỉ phải ít nhất 5 ký tự bao gồm: a-z, 0-9 hoặc A-Z, khoảng trắng"
                }
            )
            setInvalid({
                ...invalid,
                address: true
            })
        }
    }
}
function checkPhone(e) {
    if(e.trim() != "")
    {
        var check = new RegExp("(09|03|07|08|05)+[0-9]{8,11}")
        if(check.test(e) == true){
            setMessage(
              {
                ...message,
                phone: ""
              }
            )
            setInvalid({
                ...invalid,
                phone: false
            })
        }
        else{
            setMessage(
                {
                    ...message,
                    phone: "Số điện thoại bắt đầu từ 09 hoặc 03 hoặc 07 hoặc 08 hoặc 05 dài từ 8 đến 11 ký tự"
                }
            )
            setInvalid({
                ...invalid,
                phone: true
            })
        }
    }
}

async function checkMail(e) {
    console.log("here")
    if(e.trim() != "")
    {
        var check = new RegExp("[a-zA-Z][a-z0-9A-Z_\.]{5,32}@[a-zA-Z0-9]{2,}(\.[a-z0-9]{2,4}){1,2}")
        console.log(check.test(e))
        if(check.test(e) == true){
            console.log("here")
            setMessage(
              {
                ...message,
                mail: ""
              }
            )
            setInvalid({
                ...invalid,
                mail: false
            })
        }
        else{

           await setMessage(
                {
                  ...message,
                    mail: "Sai Định dạng email. Ví dụ: thisilaso1@gmail.com. Viết liền không dấu"
                }
              )
            await setInvalid({
                ...invalid,
                 mail: true
            })
        }
    }
}

function handleBlurCode(e) {
 checkCode(e.target.value)
}
function handleBlurName(e) {
   checkName(e.target.value)
}

function handleBlurAddress(e) {
  checkAdress(e.target.value)
}

function handleBlurPhone(e) {
   checkPhone(e.target.value)
}
function handleBlurMail(e) {
    checkMail(e.target.value)
}

function add(e) {
    e.preventDefault()
    
    console.log("inventory",inventory)
    if((message.code == "") && (message.name == "") && (message.address == "") && (message.phone == "") && (message.mail == ""))
    {
        if((inventory.name != "")&& (inventory.address != "")&&(inventory.phone != "")&&(inventory.mail != "") )
        {
            axios.post(URL_INVENTORY,inventory,{
                headers:{
                    "Authorization": user.type + " " + user.token
                }
            }).then(
                (res) => {
                    if(res.status == 200)
                    {
                        console.log("here 200")
                        swal({
                            title: "Thành công ",
                            text:"bạn có muốn về lại trang danh sách",
                            icon: "success",
                            buttons: ["Không","Có"],
                         
                          }).then((value)=>{
                              if(value){
                                  history.push("/inventories")
                              }
                          })
                    }else{
                        if(res.status == 201)
                        {
                            swal("Mã đã tồn tại","","warning")
                            setMessage({
                                ...message,
                                code: "Mã đã tồn tại"
                            })
                        }else{
                            swal("Không thể thêm!","","error")
                            }
                    }
                }
            ).catch(() =>{
                swal("Tên kho đã tồn tại","","error")
                setMessage({
                    ...message,
                    name: "Tên đã tồn tại"
                })
            } )
        }else{
            if(inventory.name == "")
            {
                setMessage(
                    {
                        ...message,
                        name: "Tên còn trống"
                    }
                )
                setInvalid({
                    ...invalid,
                name: true
                })
            }
            else{
                if(inventory.address == "")
                {
                    setMessage(
                        {
                            ...message,
                            address: "Địa chỉ không thể trống"
                        }
                    )
                    setInvalid({
                        ...invalid,
                        address: true
                    })
                }
                else{
                    if(inventory.phone == "")
                    {
                        setMessage(
                            {
                                ...message,
                                phone: "Số điện thoạt là bắt buộc"
                            }
                        )
                        setInvalid({
                            ...invalid,
                            phone: true
                        })
                    }   
                }
            }
        }
    }
}
    return(
        <>
          <CRow>
                <CCol xs="12" sm="12">
                <div className="mb-3" style={{ cursor: "pointer" }} onClick={() =>{
                    history.push("/inventories")
                }}> {`< Quay lại`} </div>
                <div className="mb-6" style={{ color: "#686868" }} > <h2><b>Thêm mới chi nhánh</b></h2> </div>

                </CCol>
      </CRow>
          <CRow>
                <CCol xs="12" sm="12">
                <CCard>
                <CCardHeader><h4>Thêm Chi Nhánh</h4></CCardHeader>
                <CCardBody>
                    <CRow>
                        <CCol md="3"></CCol>
                        <CCol md="6">
                            <CForm
                             className="form-horizontal"
                             >
                            <CFormGroup row>
                                <CCol md="3">
                                     <CLabel htmlFor="code">Mã kho</CLabel>
                                </CCol>
                               <CCol md="9">
                                    <CInput
                                            id="code"
                                            placeholder="Mã kho"
                                            onChange={onChange}
                                            invalid={invalid.code}
                                            defaultValue={inventory.code}
                                            onBlur={handleBlurCode}
                                            maxLength="45"
                                        />  
                                        <CInvalidFeedback>{message.code}</CInvalidFeedback>
                               </CCol>
                               
                            </CFormGroup>
                            <CFormGroup row>
                              <CCol md="3">
                                 <CLabel htmlFor="name">Tên kho</CLabel>
                              </CCol>
                               <CCol md="9">
                                <CInput
                                        id="name"
                                        placeholder="Tên kho"
                                        onChange={onChange}
                                        defaultValue={inventory.name}
                                        invalid={invalid.name}
                                        onBlur={handleBlurName}
                                        maxLength="255"
                                        required="true"
                                    />
                                <CInvalidFeedback>{message.name}</CInvalidFeedback>
                               </CCol>
                            </CFormGroup>
                            <CFormGroup row>
                               <CCol md="3">
                                <CLabel htmlFor="address">Địa chỉ</CLabel>
                               </CCol>
                                <CCol md="9">
                                <CInput id="address"  placeholder="Địa chỉ " 
                                    defaultValue={inventory.address}
                                    onChange={onChange}
                                    invalid={invalid.address}
                                    onBlur={handleBlurAddress}
                                    required="true"
                                    maxLength="255"
                                   />
                                     <CInvalidFeedback>{message.address}</CInvalidFeedback>
                                </CCol>
                            </CFormGroup>
                            <CFormGroup row>
                               <CCol md="3">
                                     <CLabel htmlFor="phone">Số điện thoại</CLabel>
                               </CCol>
                                <CCol md="6">
                                    <CInput
                                        id="phone"
                                        rows="5"
                                        placeholder="Số điện thoại"
                                        defaultValue={inventory.phone}
                                        onChange={onChange}
                                        invalid={invalid.phone}
                                        maxLength="11"
                                        type="number"
                                        onBlur={handleBlurPhone}
                                        required="true"
                                    /> 
                                    <CInvalidFeedback>{message.phone}</CInvalidFeedback>
                                </CCol>
                              
                            </CFormGroup>
                            <CFormGroup row>
                                <CCol md="3">
                                    <CLabel htmlFor="mail">Email</CLabel>
                                </CCol>
                                <CCol md="9">
                                <CInput
                                    id="mail"
                                    rows="5"
                                    onBlur={handleBlurMail}
                                    placeholder="Email"
                                    defaultValue={inventory.mail}
                                    onChange={onChange}
                                    invalid={invalid.mail}
                                    required="true"
                                />
                                <CInvalidFeedback>{message.mail}</CInvalidFeedback>
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
          <CRow className="mb-2">
              <CCol xs="12" sm="10"></CCol>
                <CCol xs="12" sm="1">
                  <CButton
                    size="sm"
                    active
                    block
                    color="dark"
                    aria-pressed="true"
                    onClick={()=> {
                        history.push("/inventories")
                    }}
                  >
                    Quay lại
                  </CButton>
                </CCol>
                
                <CCol xs="12" sm="1">
                  <CButton
                    size="sm"
                    active
                    block
                    color="info"
                    aria-pressed="true"
                    onClick={add}
                  >
                    Lưu
                  </CButton>
                </CCol>
              </CRow>
                                
        </>
    )
}
