import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import './home.css';
import config from '../../config/Config';

const getOptions = () => {
  return {
    method: 'GET', 
    headers: {
      'Content-Type': 'application/json',
      'mode': 'cors',
       
    },
  };
}


const Home = () => {
    const [user, setUser] = useState({
      username: localStorage.getItem('username'),
      email: localStorage.getItem('email')
    });
    const [groups, setGroups] = useState([]);
    const [currentGroup, setCurrentGroup] = useState('');
    const [currentGroupMessages, setCurrentGroupMessages] = useState([]);
    const [textToSend, setTextToSend] = useState('');
    const messagesContainerRef = useRef(null);
    const navigateTo = useNavigate();

    useEffect(()=>{
      fetch(`${config.base_api_url}/groups/${user.username}`, getOptions())
              .then(response => response.json())
              .then(data => {
                  setGroups(data.data.groups);
                  setCurrentGroup(data.data.groups[0]);
              })
              .catch(e => console.log(e));
    }, []);

    useEffect(()=>{
      const fetchMessages = () => {
        fetch(`${config.base_api_url}/groups/getMessages/${currentGroup}`, getOptions())
          .then(response => response.json())
          .then(data => {
            if (data.result === 'OK') {
              const messages = JSON.parse(data.data.messages);
              setCurrentGroupMessages(messages);
            }
          })
          .catch(e => console.log(e));
      };

      // scrollDown();
  
      // Fetch messages initially when the component mounts
      fetchMessages();

      scrollDown();
  
      const milliSeconds = 2000;
      const intervalId = setInterval(fetchMessages, milliSeconds);
  
      // Clean up the interval when the component unmounts
      return () => {
        clearInterval(intervalId);
      };
    }, [currentGroup]);

    const scrollDown = () => {
      if (messagesContainerRef.current) {
        messagesContainerRef.current.scrollTop = messagesContainerRef.current.scrollHeight;
      }
    }


    const sendMessage = (e) => {
      e.preventDefault();

      const formData = {
        username: user.username,
        groupname: currentGroup,
        message: textToSend
      }
      const options = {...getOptions(), method: 'POST', body: JSON.stringify(formData)};

      fetch(`${config.base_api_url}/groups/sendMessage/${currentGroup}`, options)
              .then(response => response.json())
              .then(data => {
                  if (data.result === 'OK') {
                    setTextToSend('');
                  }
              })
              .catch(e => console.log(e));
    }


    const logout = (e) => {
      localStorage.removeItem('username');
      localStorage.removeItem('email');
      setUser({});
      navigateTo('/');
    }


    return <>
      <nav>
        <h2>DevZone</h2>
        <input type='text' placeholder='search...' className='search-bar'/>
        <button type='button' className='logout-btn' onClick={logout}>logout</button>
      </nav>
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

        <section className='main'>
          <div className='info'>
            <h3>{currentGroup}</h3>
            <p><span className='number'>234</span> members</p>
          </div>
          <div className='messages' ref={messagesContainerRef}>
            {currentGroupMessages.map((message, index) => (
              <article className={message.sender_name === user.username? "message user" : "message"} key={message.message_id}>
                <div className='message-header'>
                  <h4>{message.sender_name}</h4>
                  <p>{message.date_sent}</p>
                </div>
                <p className='message-body'>{message.message}</p>
              </article>
            ))}
          </div>

          <div className='text-box-wrapper'>
            <div className='text-box'>
              <form>
                <textarea className='input-box box-shadow' placeholder='type your message...' 
                  value={textToSend} onChange={e => setTextToSend(e.target.value)}></textarea>
                <input type='submit' value='&gt;' className='send-btn' onClick={sendMessage} />
              </form>
            </div>
          </div>

        </section>
      </div>
    </>;
}

export default Home;