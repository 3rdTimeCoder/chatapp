import React, { useState, useEffect, useRef } from 'react';
import './messagesContainer.css';
import config from '../../config/Config';


const MessagesContainer = ({currentGroup, user}) => {
    const [currentGroupMessages, setCurrentGroupMessages] = useState([]);
    const [textToSend, setTextToSend] = useState('');
    const messagesContainerRef = useRef(null);


    useEffect(()=>{
      const fetchMessages = () => {
        fetch(`${config.base_api_url}/groups/getMessages/${currentGroup}`, config.options)
          .then(response => response.json())
          .then(data => {
            if (data.result === 'OK') {
              const messages = JSON.parse(data.data.messages);
              setCurrentGroupMessages(messages);
            }
          })
          .catch(e => console.log(e));
      };
  
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
      const options = {...config.options, method: 'POST', body: JSON.stringify(formData)};

      fetch(`${config.base_api_url}/groups/sendMessage/${currentGroup}`, options)
              .then(response => response.json())
              .then(data => {
                  if (data.result === 'OK') {
                    setTextToSend('');
                  }
              })
              .catch(e => console.log(e));
    }

    const deletMessage = (e) => {
      console.log('Delete message:', e.target)
    }


    return <>
        <section className='main'>
          <div className='info'>
            <h3>{currentGroup}</h3>
            <p><span className='number'>234</span> members</p>
          </div>
          <div className='messages' ref={messagesContainerRef}>
            {currentGroupMessages.map((message, index) => (
              <article className={message.sender_name === user.username? "message user" : "message"} key={message.message_id}>
                <div className='message-header'>
                  <div className='left'>
                    <h4>{message.sender_name}</h4>
                    <p>{message.date_sent}</p>
                  </div>
                  <div className='right'>
                    {message.sender_name === user.username? <p className='delete-message' onClick={deletMessage}>Delete</p> : ''}
                  </div>
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
    </>;
}

export default MessagesContainer;