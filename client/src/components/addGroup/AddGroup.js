import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './addGroup.css';
import config from '../../config/Config';


const AddGroups = ({user}) => {
    const navigateTo = useNavigate();
    const [errorMessage, setErrorMessage] = useState('');
    const [formData, setFormData] = useState({
      username: user.username,
      groupname: '',
      groupDescription: '',
    });

    const createGroup = (e) => {
      e.preventDefault();

      const options = {...config.options, method: 'POST', body: JSON.stringify(formData)};

      fetch(`${config.base_api_url}/groups/createGroup`, options)
              .then(response => response.json())
              .then(data => {
                console.log(data);
                setErrorMessage(data.data.message);
              })
              .catch(e => console.log(e));
    }

    return <>
        <section className='main'>
          <div className='addGroup__form-container'>
            <form>
              <div className="form-control">
                  <label htmlFor="groupname" className="form-label">Enter Group Name</label>
                  <input name="groupname" type="text" id="groupname" className="form-input" minLength='2' maxLength='30' required
                      value={formData.groupname} 
                      onChange={(e) => setFormData({...formData, groupname: e.target.value})} />

                  <label htmlFor="group-desc" className="form-label">Enter Group Description</label>
                  <input name="group-desc" type="text" id="group-desc" className="form-input" minLength='2' maxLength='30' required
                      value={formData.groupDescription} 
                      onChange={(e) => setFormData({...formData, groupDescription: e.target.value})} />
              </div>
              <input name="createGroup" type="submit" value="Create Group" className="btn" onClick={createGroup}/>
              <p className='addGroup__form-message'>{errorMessage}</p>
            </form>
          </div>
        </section>
    </>;
}

export default AddGroups;