import React, { useState, useEffect } from 'react';
import './home.css';
import config from '../../config/Config';
import MessagesContainer from '../../components/messages-container/MessagesContainer';
import Navbar from '../../components/navbar/Navbar';


const Home = () => {
    const [user, setUser] = useState({
      username: localStorage.getItem('username'),
      email: localStorage.getItem('email')
    });
    const [groups, setGroups] = useState([]);
    const [currentGroup, setCurrentGroup] = useState('');

    useEffect(()=>{
      fetch(`${config.base_api_url}/groups/${user.username}`, config.options)
              .then(response => response.json())
              .then(data => {
                  console.log(data.data.groups);
                  setGroups(data.data.groups);
                  setCurrentGroup(data.data.groups[0]);
              })
              .catch(e => console.log(e));
    }, []);

    return <>
      <Navbar/>

      <div className='home-container'>
        <section className='rooms'>
          <ul>
          {groups.map((group, index) => (
            <li className='room' key={index} onClick={() => setCurrentGroup(group)}>
              <h4 className='room-name'>{group}</h4>
              <p className='last-sent'>
                <span className='last-sender'>~admin213: </span>
                <span className='last-sent-message'>I see this will be a very productive room...</span>
              </p>
            </li>
          ))}
          </ul>
        </section>

        {groups.length !== 0? 
          <MessagesContainer {...{currentGroup, user}}/> : 
          <section className='main'>No messages to display...</section>
        }
      </div>
    </>;
}

export default Home;