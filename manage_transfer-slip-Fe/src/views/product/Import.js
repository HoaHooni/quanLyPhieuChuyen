import { CButton, CCard, CCardBody, CCardHeader, CCol, CDataTable, CFormGroup, CInput, CLabel, CPagination, CRow, CSelect, CBadge } from '@coreui/react'
import axios from 'axios'
import React, { useEffect, useState } from 'react'
import moment from 'moment'
import { useHistory } from 'react-router'
import { Link } from 'react-router-dom'
import DateTimePicker from 'react-datetime-picker';
import swal from 'sweetalert'

export default function InventoryDetails() {
    // const URL_IMPORT = "http://localhost:8080/transfers/export?id="
    const URL_IMPORT = "http://localhost:8080/transfers/import?id="
    const URL_IMPORT_SIZE = "http://localhost:8080/transfers/import/size?id="
    const URL_INVENTORY = "http://localhost:8080/admin/inventory"
    const URL_FILTER_TIME = "http://localhost:8080/transfers/history/finding?from="
    const URL_HISTORY_FINDING_SIZE = "http://localhost:8080/transfers/history/finding/size?from="
    const URL_SEARCH = "http://localhost:8080/transfers/history/search-by-key?page="
    const URL_SEARCH_SIZE = "http://localhost:8080/transfers/history/search-by-key/size?key="
    const history = useHistory()
    const [filterData, setFilterData] = useState("All")
    const [search, setSearch] = useState("")
    const getUser = JSON.parse(sessionStorage.user)
    const fields = [
        { key: "code", label: "Mã Phiếu" },
        { key: "inventoryInputName", label: "Chi nhánh nhận" },
        { key: "inventoryOutputName", label: "Chi nhánh chuyển" },
        { key: "username", label: "Người phụ trách" },
        { key: "createAt", label: "Ngày tạo" },
        { key: "status", label: "Trạng thái" },
        { key: "confirm", label: "Hành động" },
    ];


    const getBadge = status => {
        switch (status) {
            case 'Chờ chuyển': return 'text-primary'
            case 'Đang chuyển': return 'text-warning'
            case 'Đã nhận': return 'text-success'
            case 'Đã hủy': return 'text-danger'
            default: return 'text-primary'
        }

    }

    const [fromDate, setFromDate] = useState(null)
    const [toDate, setToDate] = useState(null)

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
    const [data, setData] = useState([])
    const [size, setSize] = useState(0)

    useEffect(() => {
        getIventory()
        loadData()
        loadSize()
    }, [page, reLoad, filterData, fromDate, toDate, search])

    function loadSize() {
        var url
        if (search == "") {
            if ((fromDate === null) && (toDate === null)) {
                url = URL_IMPORT_SIZE + getUser.inventoryId
            }
            else {
                if (fromDate != null) {
                    if (toDate == null) {
                        url = URL_HISTORY_FINDING_SIZE + toStringDate(fromDate) + "&to=" + toStringDate(new Date) + "&id=" + getUser.inventoryId

                    }
                    else {
                        url = URL_HISTORY_FINDING_SIZE + toStringDate(fromDate) + "&to=" + toStringDate(toDate) + "&id=" + getUser.inventoryId
                    }
                } else {
                    url = URL_HISTORY_FINDING_SIZE + toStringDate(fromDate) + "&to=" + toStringDate(toDate) + "&id=" + getUser.inventoryId
                }
            }
        } else {
            url = URL_SEARCH_SIZE + search + "&id=" + getUser.inventoryId
        }
        axios.get(url, {
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(
            (res) => {
                setSize(res.data)
            }
        ).catch(console.log)
    }
    function loadData() {

        var url
        if (search == "") {
            if ((fromDate === null) && (toDate === null)) {
                url = URL_IMPORT + getUser.inventoryId + "&page=" + page.number + "&limit=" + page.limit
            }

            else {
                if (fromDate != null) {

                    if (toDate == null) {
                        url = URL_FILTER_TIME + toStringDate(fromDate) + "&to=" + toStringDate(new Date) + "&page=" + page.number + "&limit=" + page.limit + "&id=" + getUser.inventoryId

                    } else {
                        url = URL_FILTER_TIME + toStringDate(fromDate) + "&to=" + toStringDate(toDate) + "&page=" + page.number + "&limit=" + page.limit + "&id=" + getUser.inventoryId
                    }
                }
                else {
                    url = URL_FILTER_TIME + toStringDate(new Date) + "&to=" + toStringDate(toDate) + "&page=" + page.number + "&limit=" + page.limit + "&id=" + getUser.inventoryId
                }
            }
        }
        else {
            url = URL_SEARCH + page.number + "&limit=" + page.limit + "&key=" + search + "&id=" + getUser.inventoryId
        }
        axios.get(url, {
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(
            (res) => {
                if (res.status == 200) {
                    setData(res.data)
                }
            }
        ).catch(console.log)
    }
    function getIventory() {
        const url = URL_INVENTORY + "/" + getUser.inventoryId
        axios.get(url, {
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(
            (res) => {
                console.log("inventory", res.data)
                setInventory(res.data)
            }
        ).catch(
            console.log
        )
        setReLoad(false)
    }

    function setNumberPage(e) {
        if (e == 0) {
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
        if (e.trim() != "") {
            var check = new RegExp("[0-9a-zA-Z]{5,45}")
            if (check.test(e) == true) {
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
            else {
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

        if (e.trim() != "") {
            var check = new RegExp("[^!-\/:-@[`{-~]{5,50}")
            if (check.test(e) == false) {

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


            }

        }
    }

    function checkAdress(e) {
        if (e.trim() != "") {
            var check = new RegExp("[^!-\/:-@[`{~]{5,50}")
            if (check.test(e) == true) {
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
            else {
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
        if (e.trim() != "") {
            var check = new RegExp("(09|03|07|08|05)+[0-9]{8,11}")
            if (check.test(e) == true) {
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
            else {
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
        if (e.trim() != "") {
            var check = new RegExp("[a-zA-Z][a-z0-9A-Z_\.]{5,32}@[a-zA-Z0-9]{2,}(\.[a-z0-9]{2,4}){1,2}")
            console.log(check.test(e))
            if (check.test(e) == true) {
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
            else {

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
    function deleted(e) {
        e.preventDefault()
        swal({
            title: "Bạn chắc chắn muốn xóa?",
            text: "Dữ liệu bị xóa sẽ không thể lấy lại",
            icon: "warning",
            buttons: ["Hủy", "Xoá"],
            dangerMode: true,
        })
            .then((willDelete) => {
                if (willDelete) {
                    axios.delete(URL_INVENTORY + "/" + getUser.inventoryId, {
                        headers: {
                            'authorization': `Bearer ${getUser.token}`,
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        }
                    }).then(
                        (res) => {
                            if (res.status == 200) {
                                swal("Thành công!", "", "success")
                                history.push("/inventories")
                            } else {
                                if (res.status == 404) {
                                    swal("Không tìm thấy chi nhánh!", "", "error")
                                }
                            }
                        }
                    ).catch(
                        () => {
                            swal({
                                title: "Kho còn hàng?",
                                text: "Bạn cần chuyển hàng để tiếp tục!",
                                icon: "warning",
                                buttons: ["Hủy", "Tiếp tục"]

                            })
                                .then((value) => {
                                    if (value) {
                                        history.push("/transfers/add")
                                    }
                                });
                        }
                    )
                }
            });
    }
    function update(e) {
        e.preventDefault()
        if ((message.code == "") && (message.name == "") && (message.address == "") && (message.phone == "") && (message.mail == "")) {
            if ((inventory.name != "") && (inventory.address != "") && (inventory.phone != "") && (inventory.mail != "")) {
                axios.put(URL_INVENTORY, inventory, {
                    headers: {
                        'authorization': `Bearer ${getUser.token}`,
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    }
                }).then(
                    (res) => {
                        if (res.status == 200) {
                            swal("Thành công!", "", "success")
                            setReLoad(true)
                        } else {
                            if (res.status == 201) {
                                swal("Mã đã tồn tại!", "", "warning")
                            }
                        }
                    }
                ).catch(
                    () => {
                        console.log(inventory)
                        swal("Hãy xem lại thông tin!", "", "error")
                        checkMail(inventory.mail)
                        checkAdress(inventory.address)
                        checkName(inventory.name)
                        checkCode(inventory.code)
                        checkPhone(inventory.phone)
                    }
                )
            } else {
                if (inventory.name == "") {
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
                else {
                    if (inventory.address == "") {
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
                    else {
                        if (inventory.phone == "") {
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
    function onchangeFrom(e) {
        setFromDate(e)
    }
    function onchangeTo(e) {
        setToDate(e)

    }
    function onchangeSearch(e) {
        if (e.target.value == "") {
            setSearch("")
        } else {
            setSearch(e.target.value)
        }
    }
    function reset(e) {
        e.preventDefault()
        setFromDate(null)
        setToDate(null)
    }
    function toStringDate(e) {
        return e.getFullYear() + "-" + e.getDate() + "-" + (e.getMonth() + 1)
    }
    function filter(e) {
        setFilterData(e.target.value)
    }

    const ConfirmImport = (e) => {
        var axios = require('axios');
        var data = '';

        var config = {
            method: 'put',
            url: `http://localhost:8080/transfers/receive/${getUser.id}/${e.id}`,
            headers: {
                'authorization': `Bearer ${getUser.token}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: data
        };

        axios(config)
            .then(function (response) {
                console.log("1111111111111 :", JSON.stringify(response.data.mess));
                if (response.data.mess == "Đã nhận hàng rồi") {
                    swal("Đã nhận hàng rồi")
                } else if (response.data.mess == "Chưa chuyển hàng đi") {
                    swal("Chưa chuyển hàng đi")
                } else if (response.data.mess == "Đã hủy phiếu") {
                    swal("Đã hủy phiếu")
                } else {
                    swal("Thành công!")
                    setReLoad(!reLoad);
                }
            })
            .catch(function (error) {
                console.log(error);
            });


    }

    function gototranferDetails(e) {

        console.log(e.id)
        const url = "/transfers/" + e.id
        history.push(url)
    }
    return (
        <>
            <CRow>
                <CCol xs="12" sm="12">
                    <div className="mb-3" style={{ cursor: "pointer" }} onClick={() => {
                        history.push("/inventories")
                    }}> {`< Quay lại`} </div>
                    <div className="mb-6" style={{ color: "#686868" }} > <h2><b>Thông tin nhận hàng kho {getUser.inventoryId}</b></h2> </div>
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

                                            <CInput id="search" name="search" placeholder="Nhập từ khóa..." onChange={onchangeSearch} maxLength="200" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol md='3' className="d-flex justify-content-end">
                                        <CFormGroup>
                                            <CLabel htmlFor="from" className="w-20 pt-2 mr-2" >Từ:</CLabel>
                                            <DateTimePicker
                                                id="from"
                                                selected={fromDate}
                                                value={fromDate}
                                                onChange={onchangeFrom}
                                                format='d-MM-y'
                                                dateFormat='true'
                                                maxDate={new Date()}
                                                disableClock
                                                yearPlaceholder="yyyy"
                                                dayPlaceholder="dd"
                                                monthPlaceholder="mm"
                                            />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol md='3' className="text-right">
                                        <CFormGroup>
                                            <CLabel htmlFor="toDate" className="w-20 pt-2 mr-2">Đến: </CLabel>

                                            <DateTimePicker id="toDate"
                                                selected={toDate}
                                                value={toDate}
                                                onChange={onchangeTo}
                                                maxDate={new Date()}
                                                format='d-MM-y'
                                                disableClock
                                                yearPlaceholder="yyyy"
                                                dayPlaceholder="dd"
                                                monthPlaceholder="mm"
                                            />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol md="2">
                                        <CButton className="btn-secondary" onClick={reset}>
                                            Đặt lại
                                        </CButton>
                                    </CCol>
                                </CRow>

                                <CDataTable
                                    items={data}
                                    fields={fields}
                                    hover
                                    bordered
                                    striped
                                    scopedSlots={
                                        {
                                            'code':
                                                (item) => (
                                                    <td>
                                                        <Link to={{
                                                            pathname: `/transfers/${item.id}`
                                                        }}>{item.code}</Link>
                                                    </td>
                                                ),
                                            'createAt':
                                                (item) => (
                                                    <td>
                                                        {moment(item.createAt).format("DD/MM/YYYY HH:mm")}
                                                    </td>

                                                ),
                                            'status':
                                                (item) => (
                                                    <td>
                                                        <p className={getBadge(item.status)}>
                                                            {item.status}
                                                        </p>
                                                    </td>
                                                ),
                                            'confirm':
                                                // ()
                                                (item) => (
                                                    <td>
                                                        <span className=" text-primary" style={{ width: "50 %", fontSize: "13px", cursor: "pointer" }} onClick={() => { ConfirmImport(item) }}>Nhận hàng
                                                        </span>
                                                    </td>
                                                ),
                                        }
                                    }
                                    size="sx"

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
                                        {/* <span>Số bản ghi: </span> */}
                                        <CSelect custom name="rows" id="limit" style={{ width: '70%' }} onChange={onChangeLimit}>
                                            <option value="10">10 phiếu chuyển</option>
                                            <option value="20">20 phiếu chuyển</option>
                                            <option value="30">30 phiếu chuyển</option>
                                            <option value="50">50 phiếu chuyển</option>
                                        </CSelect>
                                    </CCol>

                                </CRow>
                            </CCardBody>

                        </CCardBody>
                    </CCard>
                </CCol>

            </CRow>


        </>
    )
}
