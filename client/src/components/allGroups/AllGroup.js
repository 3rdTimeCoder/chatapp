import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './allGroup.css';
import config from '../../config/Config';


const AddGroups = ({user}) => {
    const navigateTo = useNavigate();
    const [errorMessage, setErrorMessage] = useState('');
    const [allGroups, setAllGroups] = useState([]);
    const [formData, setFormData] = useState({
      username: user.username,
      groupname: '',
    });

    useEffect(() => {
      fetchAllGroups();
    }, [])

    const fetchAllGroups = () => {
      fetch(`${config.base_api_url}/groups`, config.options)
              .then(response => response.json())
              .then(data => {
                console.log('test', data.data.groups);
                if(data.result === 'OK') {
                  setAllGroups(data.data.groups);
                  console.log('allGroups after setting them: ', allGroups);
                }
              })
              .catch(e => console.log(e));
    }

    const joinGroup = (e) => {
      e.preventDefault();
      console.log('join group:', e.target.id);
    }

    return <>
        <section className='main'>
          <div className='container'>
            {allGroups.map(group => (
              <article className='group'  key={group[0]}>
                <h3>{group[1]}</h3>
                {/* <p>created by: {group[2]}</p>
                <p>created on: {group[3]}</p> */}
                <button type='submit' id={group[1]} className='btn join-btn' onClick={joinGroup}>Join Room</button>
              </article>
            ))}
          </div>
        </section>
    </>;
}

export default AddGroups;