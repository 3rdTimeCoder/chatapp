import React, { useState, useEffect } from 'react';
import './home.css';
import config from '../../config/Config';
import MessagesContainer from '../../components/messages-container/MessagesContainer';
import AllGroups from '../../components/allGroups/AllGroup';
import Navbar from '../../components/navbar/Navbar';
import AddGroups from '../../components/addGroup/AddGroup';
import { FaPlus } from "react-icons/fa";
import { FaPowerOff } from "react-icons/fa";
import { FaHome } from "react-icons/fa";
import { FaSearch } from "react-icons/fa";
import { FaSolarPanel } from "react-icons/fa";
import { useNavigate } from 'react-router-dom';


const Home = () => {
    const [user, setUser] = useState({
      username: localStorage.getItem('username'),
      email: localStorage.getItem('email')
    });
    const [groups, setGroups] = useState([]);
    const [currentGroup, setCurrentGroup] = useState('');
    const [main, setMain] = useState('home');

    useEffect(()=>{
      fetchGroups();
    }, []);

    const navigateTo = useNavigate();
    const logout = () => {
      localStorage.removeItem('username');
      localStorage.removeItem('email');
      navigateTo('/');
    }

    const fetchGroups = () => {
      fetch(`${config.base_api_url}/groups/${user.username}`, config.options)
        .then(response => response.json())
        .then(data => {
            let resp = JSON.parse(data.data.groups);
            console.log(groups);
            setGroups(resp);
            setCurrentGroup(resp[0].groupname);
        })
        .catch(e => console.log(e));
    }

    return <>
      {/* <Navbar/> */}
      <nav>
        <h2>DevZone</h2>
        {/* <input type='text' placeholder='search...' className='search-bar'/> */}
        <div className='nav-icons'>
            <FaHome className='nav-icon' onClick={() => {
              fetchGroups();
              setMain('home');
            }} title='Home'/>
            <FaSolarPanel className='nav-icon' title='Discover Groups' onClick={() => setMain('allGroups')}/>
            <FaPlus className='nav-icon' onClick={() => setMain('addGroup')} title='Add Group'/>
            <FaSearch className='nav-icon' title='Search'/>
            <FaPowerOff className='nav-icon' onClick={logout} title='Logout'/>
        </div>
      </nav>

      <div className='home-container'>
        <section className='rooms'>
          <ul>
          {groups.map((group, index) => (
            <li className='room' key={index} onClick={() => {
              setCurrentGroup(group.groupname);
              setMain('home');
            }}>
              <h4 className='room-name'>{group.groupname}</h4>
              <p className='last-sent'>
                {/* <span className='last-sender'>~admin213: </span> */}
                <span className='last-sent-message'>&lt; {group.description} /&gt;</span>
              </p>
            </li>
          ))}
          </ul>
        </section>

        { main === 'home'? 
            <MessagesContainer {...{currentGroup, user}} /> : 
              main === 'addGroup'? <AddGroups {...{user}} /> : <AllGroups {...{user, groups}} />
        }
      </div>
    </>;
}

export default Home;