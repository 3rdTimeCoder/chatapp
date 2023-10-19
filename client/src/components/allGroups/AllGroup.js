import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './allGroup.css';
import config from '../../config/Config';


const AddGroups = ({user, groups}) => {
    const navigateTo = useNavigate();
    const [allGroups, setAllGroups] = useState([]);


    useEffect(() => {
      fetchAllGroups();
    }, [])

    const fetchAllGroups = () => {
      fetch(`${config.base_api_url}/groups`, config.options)
              .then(response => response.json())
              .then(data => {
                // console.log('test', data.data.groups);
                if(data.result === 'OK') {
                  setAllGroups(data.data.groups);
                  // console.log('allGroups after setting them: ', allGroups);
                }
              })
              .catch(e => console.log(e));
    }

    const joinGroup = (e) => {
      e.preventDefault();

      const formData = {
        username: user.username,
        groupname: e.target.id
      }
      const options = {...config.options, method: 'POST', body: JSON.stringify(formData)};

      fetch(`${config.base_api_url}/groups/joinGroup`, options)
              .then(response => response.json())
              .then(data => {
                // console.log(data);
                if(data.result === 'OK') {
                  e.target.disabled = true;
                  // make it fetch the groups again.
                }
              })
              .catch(e => console.log(e));
    }

    const inUserGroups = (group) => {
      groups.forEach(userGroup => {
        console.log('userGroup: ', userGroup, 'groupPendingDisplay: ', group[1])
        console.log(userGroup === group[1]);
        if (userGroup === group[1]) return true;
      });
      return false;
    }

    return <>
        <section className='main'>
          <div className='container'>
            {allGroups.map(group => (
              inUserGroups(group)? '' :
                <article className='group'  key={group[0]}>
                  <h3>{group[1]}</h3>
                  <button type='submit' id={group[1]} className='btn join-btn' onClick={joinGroup}>Join Room</button>
                </article>
            ))}
          </div>
        </section>
    </>;
}

export default AddGroups;