import React, { useState, useEffect } from 'react';
import './home.css';
import config from '../../config/Config';


const Home = ({user}) => {
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
    </>;
}

export default Home;