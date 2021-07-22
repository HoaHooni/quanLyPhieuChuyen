import React, { lazy } from "react";
import {
  CCard,
  CCardBody,
  CCardHeader,
  CCol,
  CRow,
  CJumbotron,
  CContainer,
  
} from "@coreui/react";
import CIcon from "@coreui/icons-react";
import '../../scss/dashboard/_dashboard.scss'

const WidgetsDropdown = lazy(() => import("../widgets/WidgetsDropdown.js"));

const Dashboard = () => {
  return (
    <>
      <WidgetsDropdown />
      {/* Web form */}
      <CRow>
        <CCol className="col-lg-5" >
        <CCard>
            <CCardHeader>
            <h4>Hướng dẫn sử dụng sản phẩm</h4> 
            </CCardHeader>
            <CCardBody>
              <CJumbotron >
              <CCard className="tutorial">
                  <CRow className="items-row">
                    <CIcon
                          name="cil-user"
                          height="40"
                          className="my-4"
                        />
                        <span>Bước 1: Đăng nhập</span>
                  </CRow>
                  <CRow className="items-row">
                    <CIcon
                          name="cil-options"
                          height="40"
                          className="my-4"
                        />
                        <span>Bước 2: Chọn chức năng tại menu giao diên</span>
                  </CRow>
                  <CRow className="items-row">
                    <CIcon
                          name="cil-chevron-bottom"
                          height="40"
                          className="my-4"
                        />
                        <span>Bước 3: Trải nghiệm</span>
                  </CRow>
                </CCard>
              </CJumbotron>
            </CCardBody>
          </CCard>
        </CCol>

        <CCol className="col-lg-7">
        <CCard>
            <CCardHeader>
              <h4>Các sản phẩm của chúng tôi</h4>
            </CCardHeader>
            <CCardBody>
              <CJumbotron fluid>
                <CContainer fluid>
                <CCard className="tutorial">
                  <CRow className="items-row">
                    <CIcon
                         name="cil-tags"
                          height="40"
                          className="my-4"
                        />
                        <span>Web quản lý chuyên kho</span>
                  </CRow>
                 
                </CCard>
                </CContainer>
              </CJumbotron>
            </CCardBody>
          </CCard>
       
     
          </CCol>
      </CRow>
    
    </>
  );
};

export default Dashboard;
