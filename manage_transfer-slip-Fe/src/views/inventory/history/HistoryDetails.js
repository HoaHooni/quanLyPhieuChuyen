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
    CSelect,
    CPagination,
} from '@coreui/react'
import React, { useState,useEffect} from 'react'
import { useHistory } from 'react-router'
import moment from 'moment'
import DateTimePicker from 'react-datetime-picker'
import axios from 'axios'



export default function HistoryDetails(props) {
    const [data, setData] = useState([])
    const history = useHistory();
    const [fromDate, setFromDate] = useState("")
    const [toDate, setToDate] = useState("")
    const id = props.location.state.id
    const [size, setSize] = useState(0)
    const [search, setSearch] = useState("")
    const user = JSON.parse(sessionStorage.user)
    const now = toStringDate(new Date)
    const URL_HISTORY = "http://localhost:8080/admin/history?id="
    const URL_IMPORT = "http://localhost:8080/admin/history/import"
    const URL_EXPORT = "http://localhost:8080/admin/history/export"

    const URL_SIZE = "http://localhost:8080/admin/history/size?id="

    const URL_FINDING = "http://localhost:8080/admin/history/finding"
    const URL_FILTER = "http://localhost:8080/admin/history/filter"
    const URL_FILTER_IMPORT = "http://localhost:8080/admin/history/import/filter"
    const URL_FILTER_EXPORT = "http://localhost:8080/admin/history/export/filter"
   
    const URL_IMPORT_FINDING = "http://localhost:8080/admin/history/import/finding" 
    const URL_EXPORT_FINDING = "http://localhost:8080/admin/history/export/finding" 
    
    const [page, setPage] = useState({
        number: 1,
        limit: 10
    })
    const [selectFilter, setSelectFilter] = useState("all")
   
    useEffect(() => {
        loadSize()
        loadData()
        
    }, [search,page,fromDate,toDate,selectFilter])

   const fields = () => {
       if ( (selectFilter == "all") || (selectFilter == "import") ) {
         
           return [
            { key: "inventoriesDTOImport.code", label: "Mã kho nhập" },
            { key: "inventoriesDTOImport.name", label: "Tên kho nhập" },
            { key: "inventoriesDTOExport.code", label: "Mã kho xuất" },
            { key: "inventoriesDTOExport.name", label: "Tên kho xuất" },
            {key: "usersDTO.username", label: "Người xác nhận"},
            {key: "transferDTO.code",label:"Mã phiếu chuyển"},
            {key: "action",label: "Trạng thái"},
            {key: "date",label: "Ngày chuyển trạng thái"}
          ];
       }else{
        return [ 
            { key: "inventoriesDTOExport.code", label: "Mã kho xuất" },
            { key: "inventoriesDTOExport.name", label: "Tên kho xuất" },
            { key: "inventoriesDTOImport.code", label: "Mã kho nhập" },
            { key: "inventoriesDTOImport.name", label: "Tên kho nhập" },
            {key: "usersDTO.username", label: "Người xác nhận"},
            {key: "transferDTO.code",label:"Mã phiếu chuyển"},
            {key: "action",label: "Trạng thái"},
            {key: "date",label: "Ngày chuyển trạng thái"}
        ];
       }
   }

    function checkLoadData() {
        if ((search == "") && (toDate == "") && (fromDate == "")) {
            return 1;
        }
        else if ((search != "") && (toDate == "") && (fromDate == "")) {
            return 2;
        }
        else if ((search == "") && (toDate != "") && (fromDate == "")) {
            return 3;
        }
        else if ((search == "") && (toDate == "") && (fromDate != "")) {
            return 4;
        }
        else if ((search != "") && (toDate != "") && (fromDate == "")) {
            return 5;
        }
        else if ((search != "") && (toDate == "") && (fromDate != "")) {
            return 6;
        }
        else if ((search != "") && (toDate != "") && (fromDate != "")) {
            return 7;
        }
        else if ((search == "") && (toDate != "") && (fromDate != "")){
            return 8;
        }

    }

    function checkFilter() {
        if (selectFilter == "all") {
            return 1;
        }
        else if (selectFilter == "import") {
            return 2;
        }else if (selectFilter == "export") {
            return 3;
        }
    }

    function loadSize() {
        var url
        const check = checkLoadData()
        switch (check) {
            case 1:
                    switch (checkFilter()) {
                        case 1:
                            url = URL_SIZE  + id;
                            break;
                        case 2:
                            url = URL_IMPORT + "/size?id=" + id
                            break;
                        case 3:
                            url = URL_EXPORT + "/size?id=" + id
                            break;
                    }     
                break;
            case 2:
                switch (checkFilter()) {
                    case 1:
                        url = URL_FINDING + "/size?id=" + id + "&key=" + search
                        break;
                    case 2:
                        url = URL_IMPORT + "/size?id=" + id + "&key=" + search
                        break;
                    case 3:
                        url = URL_EXPORT + "/size?id=" + id + "&key=" + search
                        break;
                }  
                break;
            case 3:
                switch (checkFilter()) {
                    case 1:
                        url = URL_FILTER + "/size?id=" + id + "&key=" + search + "&from=" + toDate + "&to=" +  convertTo(toDate)
                        break;
                    case 2:
                        url = URL_FILTER_IMPORT + "/size?id=" + id + "&key=" + search + "&from=" + toDate + "&to=" +  convertTo(toDate)
                        break;
                    case 3:
                        url = URL_FILTER_EXPORT + "/size?id=" + id + "&key=" + search + "&from=" + toDate + "&to=" +  convertTo(toDate)
                        break;
                } 
                break;
            case 4:
                switch (checkFilter()) {
                    case 1:
                        url = URL_FILTER + "/size?id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" + toStringDate(new Date())
                        break;
                    case 2:
                        url = URL_FILTER_IMPORT + "/size?id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" + toStringDate(new Date())
                        break;
                    case 3:
                        url = URL_FILTER_EXPORT + "/size?id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" + toStringDate(new Date())
                        break;
                } 
                break;
            case 5:
                switch (checkFilter()) {
                    case 1:
                        url = URL_FILTER + "/size?id=" + id + "&key=" + search + "&from=" + toDate + "&to=" + toStringDate(new Date())
                        break;
                    case 2:
                        url = URL_FILTER_IMPORT + "/size?id=" + id + "&key=" + search + "&from=" + toDate + "&to=" + toStringDate(new Date())
                        break;
                    case 3:
                        url = URL_FILTER_EXPORT + "/size?id=" + id + "&key=" + search + "&from="  + toDate + "&to=" + toStringDate(new Date())
                        break;
                } 
                break;
            case 6:
                switch (checkFilter()) {
                    case 1:
                        url = URL_FILTER + "/size?id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" + toStringDate(new Date())
                        break;
                    case 2:
                        url = URL_FILTER_IMPORT + "/size?id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" + toStringDate(new Date())
                        break;
                    case 3:
                        url = URL_FILTER_EXPORT + "/size?id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" + toStringDate(new Date())
                        break;
                } 
                break;
            case 7:
                switch (checkFilter()) {
                    case 1:
                        url = URL_FILTER + "/size?id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" +  convertTo(toDate)
                        break;
                    case 2:
                        url = URL_FILTER_IMPORT + "/size?id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" +  convertTo(toDate)
                        break;
                    case 3:
                        url = URL_FILTER_EXPORT + "/size?id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" +  convertTo(toDate)
                        break;
                } 
                break;
                case 8:
                    switch (checkFilter()) {
                        case 1:
                            url = URL_FILTER + "/size?id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" +  convertTo(toDate)
                            break;
                        case 2:
                            url = URL_FILTER_IMPORT + "/size?id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" +  convertTo(toDate)
                            break;
                        case 3:
                            url = URL_FILTER_EXPORT + "/size?id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" +  convertTo(toDate)
                            break;
                    } 
                    break;
             
            default:
                break;
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
      }
const getBadge = status => {
    switch (status) {
        case 'Chờ chuyển': return 'text-primary'
        case 'Đang chuyển': return 'text-warning'
        case 'Đã nhận': return 'text-success'
        case 'Đã hủy': return 'text-danger'
        default: return 'text-primary'
    }

}
    function loadData() {
        var url
        const check = checkLoadData()

        switch (check) {
            case 1:
                switch (checkFilter()) {
                    case 1:
                        url = URL_HISTORY +id + "&page=" + page.number + "&limit=" + page.limit
                        break;
                    case 2:
                        url = URL_IMPORT + "?id=" + id +  "&page=" + page.number + "&limit=" + page.limit
                        break;
                    case 3:
                        url = URL_EXPORT + "?id=" + id +  "&page=" + page.number + "&limit=" + page.limit
                        break;
                }     
                break;
            case 2:
                switch (checkFilter()) {
                    case 1:
                        url = URL_FINDING + "?id=" + id + "&key=" + search + "&page=" + page.number + "&limit=" + page.limit
                        break;
                    case 2:
                        url = URL_IMPORT_FINDING + "?id=" + id + "&key=" + search + "&page=" + page.number + "&limit=" + page.limit
                        break;
                    case 3:
                        url = URL_EXPORT_FINDING + "?id=" + id + "&key=" + search + "&page=" + page.number + "&limit=" + page.limit
                        break;
                }     
                break;
            case 3:
                switch (checkFilter()) {
                    case 1:
                        url = URL_FILTER + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + toDate + "&to=" + convertTo(toDate)
                        break;
                    case 2:
                        url = URL_FILTER_IMPORT + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + toDate + "&to=" + convertTo(toDate)
                        break;
                    case 3:
                        url = URL_FILTER_EXPORT + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + toDate + "&to=" +  convertTo(toDate)
                        break;
                }     
                break;
            case 4:
                switch (checkFilter()) {
                    case 1:
                        url = URL_FILTER + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" + toStringDate(new Date())
                        break;
                    case 2:
                        url = URL_FILTER_IMPORT + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" + toStringDate(new Date())
                        break;
                    case 3:
                        url = URL_FILTER_EXPORT + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" + toStringDate(new Date())
                        break;
                }     
                break;
            case 5:
                switch (checkFilter()) {
                    case 1:
                        url = URL_FILTER + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + toDate + "&to=" +  convertTo(toDate)
                        break;
                    case 2:
                        url = URL_FILTER_IMPORT + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + toDate + "&to=" +  convertTo(toDate)
                        break;
                    case 3:
                        url = URL_FILTER_EXPORT + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + toDate + "&to=" +  convertTo(toDate)
                        break;
                }     
                break;
             case 6:
                switch (checkFilter()) {
                    case 1:
                        url = URL_FILTER + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" + toStringDate(new Date())
                        break;
                    case 2:
                        url = URL_FILTER_IMPORT + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" + toStringDate(new Date())
                        break;
                    case 3:
                        url = URL_FILTER_EXPORT + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" + toStringDate(new Date())
                        break;
                }     
                break;
            case 7:
                switch (checkFilter()) {
                    case 1:
                        url = URL_FILTER + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" +  convertTo(toDate)
                        break;
                    case 2:
                        url = URL_FILTER_IMPORT + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" +  convertTo(toDate)
                        break;
                    case 3:
                        url = URL_FILTER_EXPORT + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" +  convertTo(toDate)
                        break;
                }     
                break;
                case 8:
                    switch (checkFilter()) {
                        case 1:
                            url = URL_FILTER + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" +  convertTo(toDate)
                            break;
                        case 2:
                            url = URL_FILTER_IMPORT + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" +  convertTo(toDate)
                            break;
                        case 3:
                            url = URL_FILTER_EXPORT + "?page=" + page.number + "&limit=" + page.limit + "&id=" + id + "&key=" + search + "&from=" + fromDate + "&to=" +  convertTo(toDate)
                            break;
                    }     
                    break;
            default:
                break;
        }
        console.log("url: ",url)
        axios.get(
            url, {
                headers:{
                    "Authorization": user.type + " " + user.token
                }
            }
        ).then(
            (res) => {
                if (res.status == 200) {
                   if (res.data == []) {
                        setData([])
                   }else{
                    setData(res.data)
                   }
                   
                }
            }
        ).catch(
            console.log
        )
    }

    function convertTo(e) {
      
        var toTime = new Date(e);
        console.log(toTime)
        toTime.setDate(toTime.getDate() + 1)
      if (toTime.getMonth < 10) {
            return toTime.getFullYear() + "-0" + (toTime.getMonth()+1) + "-" + toTime.getDate()
      }
        return toTime.getFullYear() + "-" + (toTime.getMonth()+1) + "-" +  toTime.getDate()
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
   
    function reset(e) {
        e.preventDefault()
        setFromDate("")
        setToDate("")
      }
    function toStringDate(e) {
        if( (e.getMonth() + 1) < 10)
        {
            return e.getFullYear() + "-0" +  (e.getMonth() + 1)+ "-" + e.getDate()  
        }
        return e.getFullYear() + "-" +  (e.getMonth() + 1)+ "-" + e.getDate()  
      }
   

      function onFilterChange(e) {
          setSelectFilter(e.target.value)
      }
   
  return (
    <>
           <CRow>
                <CCol xs="12" sm="12">
                    <div className="mb-3" style={{ cursor: "pointer" }} onClick={() =>{
                        props.history.goBack();
                    } }>  {`< Quay lại`} </div>
                    <div className="mb-6" style={{ color: "#686868" }} > <h2><b>Lịch sử kho {id}</b></h2> </div>
                </CCol>
            </CRow>
            <CRow>
                <CCol xs="12" sm="12">
                </CCol>
            </CRow>
            <CRow className="mt-2">

                <CCol xs="12" sm="12">
                    <CCard>
                        <CCardHeader>
                            <CRow>
                                <CCol md='4' className="text-left">
                                    <span>Lịch sử nhập xuất kho</span>
                                </CCol>
                            </CRow>
                        </CCardHeader>
                        <CCardBody>
                            <CCardBody className="">
                                <CRow className="">
                                    <CCol md='4'>
                                        <CFormGroup className="d-flex justify-content-between">
                                            <CLabel htmlFor="search" className="w-25 pt-2">Tìm kiếm:</CLabel>

                                            <CInput id="search" name="search" placeholder="Nhập từ khóa..." onChange={(e) => {
                                                if (e == "") {
                                                    setSearch("")
                                                }else{
                                                    setSearch(e.target.value)
                                                }
                                            }} maxLength="200" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol md='1'>
                                    <CSelect custom style={{width: "130%"}} onChange={onFilterChange}>
                                        <option value="all">Tất cả</option>
                                        <option value="import">Nhập kho</option>
                                        <option value="export">Xuất kho</option>
                                 
                                    </CSelect>
                                    </CCol>
                                    <CCol md="6">
                                    <CFormGroup row>
                                <CCol xs="1" md="1" className="mt-1">
                                    <CLabel htmlFor="date-start">Từ </CLabel>
                                </CCol>
                                <CCol xs="5" md="5">
                                    <CInput type="date"  max={toStringDate(new Date)} value={fromDate} id="date-min" name="dateMin" on className="form-control-warning" onChange={(e) => {
                                        console.log("from: ",e.target.value)
                                        setFromDate(e.target.value)
                                      
                                    }} />
                                  
                                </CCol>

                                <CCol xs="1" md="1" className="mt-1">
                                    <CLabel htmlFor="date-end">  Đến</CLabel>
                                </CCol>
                                <CCol xs="5" md="5">
                                    <CInput type="date" id="date-max" value={toDate}  name="dateMax" max={toStringDate(new Date)} className="form-control-warning" onChange={(e) =>{
                                        setToDate(e.target.value)
                                    }} />
                                  
                                </CCol>

                            </CFormGroup>
                                    </CCol>
                                    <CCol md="1">
                                        <CButton  color="info" onClick={reset}>
                                            Đặt lại
                                        </CButton>
                                    </CCol>
                                </CRow>

                                <CDataTable
                                    items={data}
                                    fields={fields()}
                                    onRowClick={
                                        (e) => {
                                            const url = "/transfers/"+ e.transferDTO.id
                                            history.push(url)
                                        }
                                    }
                                    hover
                                    border
                                    scopedSlots={
                                        {
                                            'inventoriesDTOImport.code': (item) => (
                                                <td>
                                                    {item.inventoriesDTOImport.code}
                                                </td>
                                            ),
                                            'inventoriesDTOImport.name': (item) => (
                                                <td>
                                                    {item.inventoriesDTOImport.name}
                                                </td>
                                            ),
                                            'inventoriesDTOExport.code': (item) => (
                                                <td>{item.inventoriesDTOExport.code}</td>
                                            ),
                                            'inventoriesDTOExport.name': (item) => (
                                                <td>{item.inventoriesDTOExport.name}</td>
                                            ),
                                            'usersDTO.username': (item) => (
                                                <td>{item.usersDTO.username}</td>
                                            ),
                                            'transferDTO.code': (item) => (
                                                <td>{item.transferDTO.code}</td>
                                            ),
                                            'action': (item) => (
                                                <td className={getBadge(item.action)}>{item.action}</td>
                                            )
                                            ,
                                            'date': (item) => (
                                                <td>{moment(item.date).format('DD/MM/YYYY HH:mm')}</td>
                                            )
                                        }
                                    }
                                />
                                <CRow className="d-flex justify-content-between">
                                    <CCol md='5'>
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
                                        <CSelect custom name="rows" id="limit" style={{ width: '100px' }} onChange={
                                            (e) =>{
                                                setPage(
                                                    {
                                                        number: page.number,
                                                        limit: e.target.value
                                                    }
                                                )
                                            }
                                        }>
                                            <option value="10">10</option>
                                            <option value="20">20</option>
                                            <option value="30">30</option>
                                            <option value="50">50</option>
                                        </CSelect>
                                    </CCol>

                                </CRow>
                            </CCardBody>

                        </CCardBody>
                    </CCard>
                </CCol>

            </CRow>
            <CRow className="mb-2">


                <CCol xs="12" sm="2">
                    <CButton
                        color="primary"
                        aria-pressed="true"
                        onClick={
                            () => {
                                history.push("/statistic/transfer")
                            }
                        }
                        className="btn btn-primary px-4"
                    >
                    Báo Cáo
                    </CButton>
                </CCol>
                <CCol xs="12" sm="8"></CCol>
                <CCol xs="12" sm="1">
                    <CButton

                        color="dark"
                        aria-pressed="true"
                        onClick={
                            () => {
                                props.history.goBack()
                            }
                        }
                    >
                        Quay lại
                    </CButton>

                </CCol>

               
               
            </CRow>

    </>
  )
}


