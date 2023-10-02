import React from 'react';
import { Link } from 'react-router-dom';
import './landing.css';

const LandingPage = () => {
  return (
    <>
        <div class="home-container">
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
                        <input name="register" type="submit" value="Register" class="btn"/>
                        <input name="login" type="submit" value="Login" class="btn"/>
                    </div>
                </form>
            </div>
        </div>
    </>
  );
}

export default LandingPage;