import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './messagesContainer.css';
import config from '../../config/Config';


const AddGroups = ({currentGroup, user}) => {
    const navigateTo = useNavigate();
    const [errorOccurred, setErrorOccurred] = useState(false);
    const [formData, setFormData] = useState({
      username: user.username,
      groupname: '',
    });

    const createGroup = (e) => {
      e.preventDefault();

      const options = {...config.options, method: 'POST', body: JSON.stringify(formData)};

      fetch(`${config.base_api_url}/groups/createGroup`, options)
              .then(response => response.json())
              .then(data => {
                console.log(data);
                  if (data.result === 'OK') {
                    navigateTo('/home');
                  }
                  else{ setErrorOccurred(true); }
              })
              .catch(e => console.log(e));
    }

    return <>
        <section className='main'>
          <form>
            <div className="form-control">
                <label htmlFor="groupname" className="form-label">groupname</label>
                <input name="groupname" type="text" id="groupname" className="form-input" required
                    value={formData.groupname} 
                    onChange={(e) => setFormData({...formData, groupname: e.target.value})} />
            </div>
            <input name="createGroup" type="submit" value="Login" className="btn" onClick={createGroup}/>
            <p className='error'>{errorOccurred? 'An Error Occurred' : ''}</p>
          </form>
        </section>
    </>;
}

export default AddGroups;