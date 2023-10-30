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
                if(data.result === 'OK') {
                  console.log(JSON.parse(data.data.groups));
                  setAllGroups(JSON.parse(data.data.groups));
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
                  e.target.text = "Joined";
                }
              })
              .catch(e => console.log(e));
    }

    const inUserGroups = (group) => {
      for (const userGroup of groups) {
        if (userGroup.groupname === group.groupname) return true;
      }
      return false;
    }

    return <>
        <section className='main'>
          <div className='container'>
            {allGroups.map(group => (
              inUserGroups(group)? 
                <article className='group'  key={group[0]}>
                  <h3>{group.groupname}</h3>
                  <p>&lt; {group.description} /&gt;</p>
                  <p>~created by <span className='creator'>@{group.creator}</span></p>
                  <button type='btn' id={group.groupId} className='btn join-btn' disabled>Joined</button>
                </article>
                :
                <article className='group'  key={group.groupId}>
                  <h3>{group.groupname}</h3>
                  <p>&lt; {group.description} /&gt;</p>
                  <p>~created by <span className='creator'>@{group.creator}</span></p>
                  <button type='submit' id={group.groupname} className='btn join-btn' onClick={joinGroup}>Join Room</button>
                </article>
            ))}
          </div>
        </section>
    </>;
}

export default AddGroups;