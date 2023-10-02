import React, { useState } from 'react';
import { Link } from 'react-router-dom';
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

    const register = (e) => {
        e.preventDefault();
        const form = document.getElementById('form');
        const elements = form.elements;

        const newFormData = {
            username: elements.username.value,
            email: elements.email.value,
            password: elements.password.value,
            confirmPassword: elements['confirm-password'].value,
        };

        setFormData(newFormData);

        const options = {
            method: 'POST', 
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData), 
          };

        const formMessage = document.getElementById('form-message');
        formMessage.innerHTML = `<p>Processing...</em>...</p>`;

        fetch(`${config.base_api_url}/register`, options)
          .then(response => response.json())
          .then(data => {
              console.log(data);
              if (data.result === "OK") navigateTo('/home');
              else formMessage.innerHTML = data.data.message;
          })
          .catch(e => formMessage.innerHTML = "An Error Occured");

    }

    return (
        <>
            <div class="container">
                <div class="text">
                    <h1 class="heading">&lt; DevZone /&gt;</h1>
                </div>

                <div class="form-container">
                    <form id="form">
                        <div class="form-control">
                            <label for="username" class="form-label">username</label>
                            <input name="username" type="text" id="username" class="form-input" required/>
                        </div>
            
                        <div class="form-control">
                            <label for="email" class="form-label">email</label>
                            <input name="email" type="email" id="email" class="form-input" required/>
                        </div>   
            
                        <div class="form-control">
                            <label for="password" class="form-label">password</label>
                            <input name="password" type="password" id="password" minlength="8" class="form-input" required/>
                        </div>
            
                        <div class="form-control">
                            <label for="confirm-password" class="form-label">confirm password</label>
                            <input name="confirm-password" type="password" id="confirm-password" minlength="8" class="form-input" required/>
                        </div>

                        <div id="form-message" class="form-message"></div>
            
                        <div class="form-btns">
                            <input name="register" type="submit" value="Register" class="btn" onClick={register}/>
                            <input name="login" type="submit" value="Login" class="btn"/>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );
}

export default LandingPage;