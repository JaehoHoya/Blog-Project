import React from 'react'
import Footer from '../Footer'
import Header from '../Header'
import { Outlet, useLocation } from 'react-router-dom'
import { AUTH_PATH } from '../../Constants';


// component: 컨테이너 레이아웃 
export default function Container() {
// state: 현재 페이지 path name 상태 

const {pathname}=useLocation();

// render: 컨테이너 레이아웃 렌더링 
  return (
    < >
      <Header />
      <Outlet/>
      {pathname !==AUTH_PATH()&& <Footer/>}
  
    </>
  )
}
