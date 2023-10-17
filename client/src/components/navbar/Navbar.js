import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './navbar.css';


const Navbar = () => {
    const navigateTo = useNavigate();

    const logout = () => {
      localStorage.removeItem('username');
      localStorage.removeItem('email');
      navigateTo('/');
    }

    return <>
      <nav>
        <h2>DevZone</h2>
        <input type='text' placeholder='search...' className='search-bar'/>
        <button type='button' className='logout-btn' onClick={logout}>logout</button>
      </nav>
    </>;
}

export default Navbar;