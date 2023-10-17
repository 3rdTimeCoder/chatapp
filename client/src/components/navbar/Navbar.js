import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaPlus } from "react-icons/fa";
import { FaPowerOff } from "react-icons/fa";
import './navbar.css';


const Navbar = () => {
    const navigateTo = useNavigate();

    const logout = () => {
      localStorage.removeItem('username');
      localStorage.removeItem('email');
      navigateTo('/');
    }

    const addNewGroup = () => {}

    return <>
      <nav>
        <h2>DevZone</h2>
        <input type='text' placeholder='search...' className='search-bar'/>
        {/* <button type='button' className='logout-btn' onClick={logout}>logout</button> */}
        {/* <button type='button' className='logout-btn' onClick={logout}>
            <FaPowerOff className='nav-icon' onClick={logout}/>
        </button> */}
        <div className='nav-icons'>
            <FaPlus className='nav-icon' onClick={addNewGroup} title='add group'/>
            <FaPowerOff className='nav-icon' onClick={logout} title='logout'/>
        </div>
      </nav>
    </>;
}

export default Navbar;