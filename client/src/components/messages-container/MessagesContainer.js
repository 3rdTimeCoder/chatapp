import React, { useState, useEffect, useRef } from 'react';
import { FaTrash, FaTrashAlt } from "react-icons/fa";
import config from '../../config/Config';
import InputEmoji from 'react-input-emoji';
import './messagesContainer.css';


const MessagesContainer = ({ currentGroup, user }) => {
    const [currentGroupMessages, setCurrentGroupMessages] = useState([]);
    const [textToSend, setTextToSend] = useState('');
    const [groupMembers, setGroupMembers] = useState([]);
    const [groupMemberCount, setGroupMemberCount] = useState('');
    const messagesContainerRef = useRef(null);

    const fetchMessages = async () => {
      await fetch(`${config.base_api_url}/groups/getMessages/${currentGroup}`, config.options)
        .then(response => response.json())
        .then(data => {
          if (data.result === 'OK') {
            const messages = JSON.parse(data.data.messages);
            setCurrentGroupMessages(messages);
          }
        })
        .catch(e => console.log(e));
    };

    const fetchGroupMembers = async () => {
      await fetch(`${config.base_api_url}/groups/getMembers/${currentGroup}`, config.options)
        .then(response => response.json())
        .then(data => {
          if (data.result === 'OK') {
            setGroupMembers(data.data.members);
            setGroupMemberCount(data.data.member_count);
          }
        })
        .catch(e => console.log(e));
    };

    useEffect(()=>{
      // Fetch messages initially when the component mounts
      fetchGroupMembers();
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

    const deletMessage = (messageId) => {
      const formData = {
        username: user.username,
        messageId: messageId
      }
      const options = {...config.options, method: 'POST', body: JSON.stringify(formData)};

      fetch(`${config.base_api_url}/groups/deleteMessage/${messageId}`, options)
              .then(response => response.json())
              .then(data => {})
              .catch(e => console.log(e));

    }

    const leaveGroup = (e) => {
      e.preventDefault();

      const formData = {
        username: user.username,
        groupname: currentGroup
      }
      const options = {...config.options, method: 'POST', body: JSON.stringify(formData)};

      fetch(`${config.base_api_url}/groups/leaveGroup`, options)
              .then(response => response.json())
              .then(data => {
                console.log(data)
              })
              .catch(e => console.log(e));
    }

    return <>
        <section className='main'>
          <div className='info'>
            <h3>{currentGroup}</h3>
            <p><span className='number'>{groupMemberCount}</span> members</p>
            <form id="leaveGroupForm">
              <input type="submit" id="leaveGroup" name="leaveGroup" value="Leave Group" onClick={(e) => leaveGroup(e)} />
            </form>
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
                    {message.sender_name === user.username? 
                      <p className='delete-message' onClick={() => deletMessage(message.message_id)}><FaTrash/></p> : ''}
                  </div>
                </div>
                <p className='message-body'>{message.message}</p>
              </article>
            ))}
          </div>

          <div className='text-box-wrapper'>
            <div className='text-box'>
              <form>
                <InputEmoji 
                  className='input-box box-shadow' 
                  placeholder='Type a message...' 
                  value={textToSend} 
                  onChange={setTextToSend} 
                  onEnter={sendMessage}
                />
              </form>
            </div>
          </div>
        </section>
    </>;
}

export default MessagesContainer;