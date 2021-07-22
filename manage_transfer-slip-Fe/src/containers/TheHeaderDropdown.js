  import React from 'react'
import {
  CBadge,
  CDropdown,
  CDropdownItem,
  CDropdownMenu,
  CDropdownToggle,
  CImg
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import { useHistory } from 'react-router-dom'

const TheHeaderDropdown = () => {

  const getUser = JSON.parse(sessionStorage.user)

  const history = useHistory();

  return (
    <CDropdown
      inNav
      className="c-header-nav-items mx-2"
      direction="down"
    >
      <CDropdownToggle className="c-header-nav-link" caret={false}>
        <div className="c-avatar">
          <p className="mr-2 mt-3">{getUser.username}</p>
          <CImg 
            src={'avatars/7.jpg'}
            className="c-avatar-img"
            alt={getUser.username}
          />
        </div>
      </CDropdownToggle>
      <CDropdownMenu className="pt-0" placement="bottom-end">

        <CDropdownItem
          header
          tag="div"
          color="light"
          className="text-center"
        >
          <strong>Tài khoản</strong>
        </CDropdownItem>
        <CDropdownItem onClick={()=>{
            history.push(`/profile/${getUser.id}`)
          }}>
          <CIcon name="cil-user" className="mfe-2" />Thông tin cá nhân
        </CDropdownItem>
        <CDropdownItem  onClick={()=>{
            history.push("/change-password")
          }}>
          <CIcon name="cil-settings" className="mfe-2" />
          Đổi mật khẩu
        </CDropdownItem>

        <CDropdownItem divider />
        <CDropdownItem onClick={(e) => {
            console.log("here")
            e.preventDefault();
            sessionStorage.removeItem("user");
            history.replace("/")
          }}>
          <CIcon name="cil-lock-locked" className="mfe-2" />
          Đăng xuất
        </CDropdownItem>
      </CDropdownMenu>
    </CDropdown>
  )
}

export default TheHeaderDropdown
