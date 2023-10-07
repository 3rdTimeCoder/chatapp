import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './landing.css';
import config from '../../config/Config';

function LandingPage() {

    const navigateTo = useNavigate();
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
    });
    
    const getOptions = () => {
        return {
            method: 'POST', 
            headers: {
              'Content-Type': 'application/json',
              'mode': 'cors',
               
            },
            body: JSON.stringify(formData), 
          };
    }

    const handleSubmit = (e) => {
        e.preventDefault();

        const action = e.target.value.toLowerCase();
        const options = getOptions();
        const formMessage = document.getElementById('form-message');
        formMessage.innerHTML = `<p>Processing...</em>...</p>`;

        if (formData.password !== formData.confirmPassword && action === "register") {
            formMessage.innerHTML = `<p><em>Passwords do not match</em>...</p>`;
        }
        else {
            fetch(`${config.base_api_url}/${action}`, options)
              .then(response => response.json())
              .then(data => {
                  if (data.result === "OK" && action === "register") 
                    formMessage.innerHTML = `<p><em>Registration Successful! Login</em>...</p>`;
                  else if (data.result === "OK" && action === "login") {
                    localStorage.setItem('username', formData.username);
                    localStorage.setItem('email', formData.email);
                    navigateTo('/home');
                  } 
                  else formMessage.innerHTML = data.data.message;
              })
              .catch(e => formMessage.innerHTML = "An Error Occured");
        }
    }

    return (
        <>
            <div className="container">
                <div className="text">
                    <h1 className="heading">&lt; DevZone /&gt;</h1>
                </div>

                <div className="form-container">
                    <form id="form">
                        <div className="form-control">
                            <label htmlFor="username" className="form-label">username</label>
                            <input name="username" type="text" id="username" className="form-input" required
                                value={formData.username} 
                                onChange={(e) => setFormData({...formData, username: e.target.value})} />
                        </div>
            
                        <div className="form-control">
                            <label htmlFor="email" className="form-label">email</label>
                            <input name="email" type="email" id="email" className="form-input" required
                                value={formData.email} 
                                onChange={(e) => setFormData({...formData, email: e.target.value})}/>
                            </div>   
            
                        <div className="form-control">
                            <label htmlFor="password" className="form-label">password</label>
                            <input name="password" type="password" id="password" minLength="8" className="form-input" required
                                value={formData.password} 
                                onChange={(e) => setFormData({...formData, password: e.target.value})}/>
                            </div>
            
                        <div className="form-control">
                            <label htmlFor="confirm-password" className="form-label">confirm password</label>
                            <input name="confirm-password" type="password" id="confirm-password" minLength="8" className="form-input" required
                                value={formData.confirmPassword} 
                                onChange={(e) => setFormData({...formData, confirmPassword: e.target.value})}/>
                            </div>

                        <div id="form-message" className="form-message"></div>
            
                        <div className="form-btns">
                            <input name="register" type="submit" value="Register" className="btn" onClick={handleSubmit}/>
                            <input name="login" type="submit" value="Login" className="btn" onClick={handleSubmit}/>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );
}

export default LandingPage;