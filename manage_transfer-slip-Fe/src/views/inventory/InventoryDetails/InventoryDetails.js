import { CBadge, CButton, CCard, CCardBody, CCardHeader, CCol, CDataTable, CDropdown, CDropdownDivider, CDropdownItem, CDropdownMenu, CDropdownToggle, CFormGroup, CInput, CInvalidFeedback, CLabel, CPagination, CRow, CSelect, CTextarea, CToast, CToastBody, CToaster, CToastHeader, CValidFeedback } from '@coreui/react'
import axios from 'axios'
import React, { useEffect, useState } from 'react'
import moment from 'moment'
import { useHistory } from 'react-router'
import DateTimePicker from 'react-datetime-picker';
import { cibWindows } from '@coreui/icons'
import swal from 'sweetalert'

export default function InventoryDetails(props) {
    const history = useHistory();
    const id = props.history.location.state.id
    const user = JSON.parse( sessionStorage.getItem("user"))
    // user.roles.map((item) => {
    //     if(item != "ROLE_INVENTORIER"){
    //         history.push("/inventories")
    //     }
    // })
  
    const URL_SIZE_PRODUCT = "http://localhost:8080/admin/inventory-details/page-products/size?id="

    const URL_INVENTORY = "http://localhost:8080/admin/inventory"

    const URL_INVENTORY_PRODUCT   = "http://localhost:8080/admin/inventory-details/page?id="
    

    const URL_FIND = "http://localhost:8080/admin/inventory-details/find-by-key?id=";
  
    const URL_SEARCH_SIZE = "http://localhost:8080/admin/inventory-details/find-by-key-size?key="

    const URL_SUM = "http://localhost:8080/admin/inventory-details/sum/"

    const URL_COUNT = "http://localhost:8080/admin/inventory-details/count/"
    const [search, setSearch] = useState("")
    const fields = [
        { key: "productsDTO.link", label: "Ảnh" ,_style:{width:"10%"}},
        { key: "productsDTO.code", label: "Mã SP" ,_style:{width:"10%"}},
        { key: "productsDTO.name", label: "Tên sản phẩm" ,_style:{width:"50%"}},
        { key: "numberProduct", label: "SL", _style: {
            textAlign: "right",
            width:"10%"
        }},
        { key: "productsDTO.price", label: "Giá bán" ,_style: {
            textAlign: "right",width:"20%"
        }},

      ];
      
      const [fromDate, setFromDate] = useState(null)
      const [toDate, setToDate] = useState(null)
      const [count, setCount] = useState(0)
     const [inventory, setInventory] = useState({})
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
      const [reLoad, setReLoad] = useState(false)
    const [page, setPage] = useState({
        number: 1,
        limit: 10
    })
    const [data, setData] = useState({
        inventories: {},
        customInventoriesDetails: []

    })
    const [size, setSize] = useState(0)
    const [sum,setSum] = useState(0)
    useEffect(() =>{
        loadCount()
        loadSum()
    },[])
     useEffect(() => {
        getIventory()
        loadData()
        loadSize()
    }, [page,reLoad,search])

    function loadCount(){
        const url = URL_COUNT + id
        axios.get(url,{
            headers:{
                "Authorization": user.type + " " + user.token
            }
        }).then(
            (res) => {
                setCount(res.data)
            }
        ).catch(console.log)
    }
    function loadSum() {
        const url = URL_SUM + id
        axios.get(url,{
            headers:{
                "Authorization": user.type + " " + user.token
            }
        }).then(
            (res) => {
                setSum(res.data)
            }
        ).catch(console.log)
    }
    function loadSize() {
        var  url
        if (search == "") {
            url = URL_SIZE_PRODUCT + id
        }else{
           url = URL_SEARCH_SIZE + search + "&id=" + id
        }
        axios.get(url,{
            headers:{
                "Authorization": user.type + " " + user.token
            }
        }).then(
            (res) => {
                setSize(res.data)
            }
        ).catch(console.log)
        console.log(url)
    }


    function loadData() {
        var url
       if (search == "") {
        url = URL_INVENTORY_PRODUCT + id + "&page=" + page.number + "&limit=" + page.limit

       }
       else{
        url = URL_FIND + id + "&key=" + search + "&page=" + page.number + "&limit=" + page.limit

       }
        axios.get(url,{
            headers:{
                "Authorization": user.type + " " + user.token
            }
        }).then(  
         
            (res) => {
               
               if (res.data == []) {
                   setData([])
               }else{
                    setData({
                    inventories: res.data.inventoriesDTO,
                    customInventoriesDetails: res.data.customInventoriesDetails
             
                })
               }
            }
        ).catch(setData([]))
    }
    const getBadge = status => {
        switch (status) {
            case 'Chờ chuyển': return 'success'
            case 'Đang chuyển': return 'secondary'
            case 'Đã nhận': return 'warning'
            case 'Đã hủy': return 'danger'
            default: return 'primary'
        }
    }
  function getIventory() {
        const url = URL_INVENTORY + "/" + id
       axios.get(url,{
        headers:{
            "Authorization": user.type + " " + user.token
        }
    }).then(
            (res) =>{
                setInventory(res.data)
            }
        ).catch(
            console.log
        )
        setReLoad(false)
    }

 async function onChange(e) {
     console.log("e", e.target.value)
        await setInventory(
         {
             ...inventory,
             [e.target.id]: e.target.value
         }
     )
 }
function setNumberPage(e) {
   if(e == 0){
       e = 1
   }
   setPage(
    {
        number: e,
        limit: page.limit
    }
)
}

function onChangeLimit(e) {
    setPage({
        number: page.number,
        limit: e.target.value
    })
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

function handleBlurCode(e) {
    checkCode(e.target.value)
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
    }else{
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
}

function handleBlurName(e) {
   checkName(e.target.value)
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
function handleBlurAddress(e) {
    console.log(e.target.id)
  checkAdress(e.target.value)
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
function handleBlurPhone(e) {
    console.log(e.target.id)
   checkPhone(e.target.value)
}
async function checkMail(e) {
    if(e.trim() != "")
    {
        var check = new RegExp("[a-zA-Z][a-z0-9A-Z_\.]{5,32}@[a-zA-Z0-9]{2,}(\.[a-z0-9]{2,4}){1,2}")
        if(check.test(e) == true){
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

function handleBlurMail(e) {
   checkMail(e.target.value)
}

function deleted(e) {
    e.preventDefault()
    swal({
        title: "Bạn chắc chắn muốn xóa?",
        text: "Dữ liệu bị xóa sẽ không thể lấy lại",
        icon: "warning",
        buttons: ["Hủy","Xoá"],
        dangerMode: true,
      })
   
      .then((willDelete) => {
        if (willDelete) {
            axios.delete(URL_INVENTORY + "/" + id,{
                headers:{
                    "Authorization": user.type + " " + user.token
                }
            }).then(
                (res) => {
                    if(res.status == 200){
                        swal({
                            title: "Thành công",
                            text:"Đã xóa kho",
                            icon: "success",
                            timer: 2000,
                            buttons: false

                        });
                        history.push("/inventories")
                    }else{
                        if(res.status == 404){
                        swal("Không tìm thấy chi nhánh!","","error")
                        }
                    }
                }
            ).catch(
                () => {
                    swal({
                        title: "Kho còn hàng?",
                        text: "Bạn cần chuyển hàng để tiếp tục!",
                        icon: "warning",
                        buttons:["Hủy","Tiếp tục"]

                      })
                      .then((value) => {
                       if(value) {
                            history.push("/transfers/add")
                       }
                      });
                }
            )
        }
      });
}
function update(e) {
    checkMail(inventory.mail)
    checkAdress(inventory.address)
    checkName(inventory.name)
    checkCode(inventory.code)
    checkPhone(inventory.phone)
    e.preventDefault()
    if((message.code == "") && (message.name == "") && (message.address == "") && (message.phone == "") && (message.mail == ""))
    {
        if((inventory.name != "")&& (inventory.address != "")&&(inventory.phone != "")&&(inventory.mail != "") )
        {
            axios.put(URL_INVENTORY,inventory,{
                headers:{
                    "Authorization": user.type + " " + user.token
                }
            }).then(
                (res) => {
                    console.log(res.status)
                    if(res.status == 200)
                    {
                        console.log( "in",inventory)
                        swal({
                            title: "Thành công",
                            text:"Sửa đổi hoàn tất",
                            icon: "success",
                            timer: 2000,
                            buttons: false

                        });
                        sessionStorage.removeItem("id")
                       setReLoad(true)
                    }
                }
            ).catch(
              () => {
                swal("Hãy xem lại thông tin!","","error")
              
                checkMail(inventory.mail)
                checkAdress(inventory.address)
                checkName(inventory.name)
                checkCode(inventory.code)
                checkPhone(inventory.phone)
                swal({
                    title: "Tên chi nhánh đã tồn tại",
                    text: "Tên chi nhánh đã tồn tại vui lòng chọn mã khác hoặc sử dụng mã cũ",
                    icon: "error",
                    timer: 1500
                })
                setMessage({
                    ...message,
                    name: "Tên chi nhánh đã tồn tại"
                })
                setInvalid({
                    ...invalid,
                    name: true
                })
               }
            )
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

function onchangeSearch(e){
    console.log(e.target.value)
    if(e.target.value == ""){
        setSearch("")
    }else{
        setSearch(e.target.value)
    }
}
const formatLinkImage = (link) => {
    if (link == null) {
        return "images/1.jpg";
    } else {
        return `${link}`;
    }
}
function gotoProductsDetails(e) {
    // /products/1/1/1
    const url = "/products/"+ data.inventories.id + "/" + e.productsDTO.id + "/" + e.idDetails
    history.push(url,{ product: e.productsDTO })
    
}
    return(
        <>
       <CRow>
                <CCol xs="12" sm="12">
                <div className="mb-3" style={{ cursor: "pointer" }} onClick={() =>{
                    sessionStorage.removeItem("id")
                    history.push("/inventories")
                }}> {`< Quay lại`} </div>
                <div className="mb-6" style={{ color: "#686868" }} > <h2><b>Chi tiết chi nhánh</b></h2> </div>
                </CCol>
      </CRow>
            <CRow>
            <CCol xs="12" sm="12">
            </CCol>
            </CRow>
            <CRow className="mt-2">
                <CCol xs="12" sm="4">
                    <CCard>
                        <CCardHeader>
                        <div><b>Thông tin chi nhánh</b></div>
                        </CCardHeader>
                        <CCardBody>
                            <CFormGroup>
                                <CLabel htmlFor="code">Mã kho</CLabel>
                                <CInput
                                    selected={fromDate}
                                    id="code"
                                    placeholder="Mã kho"
                                    onChange={onChange}
                                    invalid={invalid.code}
                                    defaultValue={inventory.code}
                                    onBlur={handleBlurCode}
                                    maxLength="45"
                                    disabled

                                />
                                <CInvalidFeedback>{message.code}</CInvalidFeedback>
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel htmlFor="name">Tên kho</CLabel>
                                <CInput
                                    selected={toDate}
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
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel htmlFor="address">Địa chỉ</CLabel>
                                <CInput id="address"  placeholder="Địa chỉ "
                                    defaultValue={inventory.address}
                                    onChange={onChange}
                                    invalid={invalid.address}
                                    onBlur={handleBlurAddress}
                                    maxLength="255"
                                    required="true"
                                   />
                                <CInvalidFeedback>{message.address}</CInvalidFeedback>
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel htmlFor="phone">Số điện thoại</CLabel>
                                <CInput
                                    id="phone"
                                    rows="5"
                                    placeholder="Số điện thoại"
                                    defaultValue={inventory.phone}
                                    onChange={onChange}
                                    invalid={invalid.phone}
                                    maxLength="11"
                                    type="tel"
                                    onBlur={handleBlurPhone}
                                    required="true"
                                />
                               <CInvalidFeedback>{message.phone}</CInvalidFeedback>
                            </CFormGroup>
                            <CFormGroup>
                                <CLabel htmlFor="mail">Email</CLabel>
                                <CInput
                                    id="mail"
                                    rows="5"
                                    onBlur={handleBlurMail}
                                    placeholder="Email"
                                    defaultValue={inventory.mail}
                                    onChange={onChange}
                                    invalid={invalid.mail}
                                    maxLength="255"
                                    required="true"
                                />
                                <CInvalidFeedback>{message.mail}</CInvalidFeedback>
                            </CFormGroup>

                        </CCardBody>
                    </CCard>
                </CCol>

                <CCol xs="12" sm="8">
                    <CCard>
                        <CCardHeader>
                            <CRow>

                                <CCol md='4' className="text-left">
                                <div><b>Danh sách sản phẩm trong kho</b></div>
                                </CCol>
                                <CCol md="6">

                                </CCol>
                                <CCol md="2">
                                <CButton className="" size="xs" active block color="info" aria-pressed="true" onClick={(e) =>{
                                e.preventDefault()
                                history.push("/history/details",{id:inventory.id})
                                }} >Lịch sử Kho</CButton>
                            </CCol>
                            </CRow>
                        </CCardHeader>            
                            <CCardBody className="">
                         <CRow className="d-flex ">
                <CCol md='5'>
                    <CFormGroup row>
                        <CCol md="8">
                            <CLabel htmlFor="from" className="w-10 pt-2" ><b>Số sản phẩm: <span>{count}</span></b></CLabel>
                            <CLabel htmlFor="from" className="w-10 pt-2" ><b>Số lượng sản phẩm: <span>{sum}</span></b></CLabel>
                        </CCol>
                    </CFormGroup>
                </CCol>
                <CCol md="6">

                </CCol>
              </CRow>
            
              <CRow className="d-flex justify-content-between">
                <CCol md='7'>
                  <CFormGroup className="d-flex justify-content-between">
                    <CLabel htmlFor="search" className="w-25 pt-2">Tìm kiếm:</CLabel>
                    <CInput id="search" name="search" placeholder="Nhập từ khóa..." onChange={onchangeSearch} maxLength="200"/>
                    
                  </CFormGroup>
                </CCol>
              </CRow>
             
              <CDataTable
                items={data.customInventoriesDetails}
                fields={fields}
                onRowClick={gotoProductsDetails}
                hover
                border
                // sorter
                scopedSlots={{
                    'productsDTO.link':(item) => (
                        <td>
                           <img src={process.env.PUBLIC_URL + "/" + formatLinkImage(item.productsDTO.link)} style={{ width: "100%" }} />


                        </td>
                    ),
                    'productsDTO.code': (item) => (
                        <td>
                            {item.productsDTO.code}
                        </td>
                    ),
                    'productsDTO.name': (item) => (
                        <td style={{overflow: 'hidden'}}>
                            {item.productsDTO.name}
                        </td>
                    ),
                    'productsDTO.color': (item) => (
                        <td>
                            {item.productsDTO.color}
                        </td>
                    ),
                    'productsDTO.size': (item) => (
                        <td>
                            {item.productsDTO.size}
                        </td>
                    ),
                    'productsDTO.price': (item) => (
                        <td className="text-right">
                            {item.productsDTO.price.toFixed(0).replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,')} vnđ 
                        </td>
                    ),
                    'numberProduct': (item) => (
                        <td className="text-right">
                            {item.numberProduct}
                        </td>
                    ),
                    
                }}
                size="sx"
              />
              <CRow className="d-flex justify-content-between">
              <CCol  md='5'>
                    <CPagination
                    activePage={page.number}
                    pages={Math.ceil(size / page.limit)}
                    onActivePageChange={setNumberPage}
              />
                </CCol>
                <CCol md="4">

                </CCol>
                <CCol md='3' className="text-right">
                  <span>Số bản ghi: </span>
                  <CSelect custom name="rows" id="limit" style={{ width: '53%' }} onChange={onChangeLimit}>
                    <option value="10">10</option>
                    <option value="20">30</option>
                    <option value="50">50</option>
                    <option value="80">80</option>
                  </CSelect>
                </CCol>

              </CRow>
            </CCardBody>

                </CCard>
                </CCol>

            </CRow>
            <CRow className="mb-2">


            <CCol xs="12" sm="1">
          <CButton


            color="primary"
            aria-pressed="true"
            onClick={update}
            className="btn btn-primary px-4"
          >
           Lưu
          </CButton>
        </CCol>

        <CCol xs="12" sm="1">
          <CButton

            color="dark"
            aria-pressed="true"
            onClick={()=>{
                sessionStorage.removeItem("id")
                history.push("/inventories")
            }}
          >
            Quay lại
          </CButton>

        </CCol>

        <CCol xs="12" sm="8"></CCol>
        <CCol xs="12" sm="2">
          <CButton

            color="danger"
            aria-pressed="true"
            onClick={deleted}

          >
            Ngưng hoạt động
          </CButton>
        </CCol>
      </CRow>


        </>
    )
}
