import React from 'react';
import './home.css';

const Home = () => {
  return <>
    <nav>
      <h2>DevZone</h2>
      <input type='text' placeholder='search...' className='search-bar'/>
      <button type='button' className='logout-btn'>logout</button>
    </nav>
    <div className='home-container'>
      <section className='rooms'>
        <ul>
          <li className='room active'>
            <h4 className='room-name'>The 13th Floor</h4>
            <p className='last-sent'> 
              <span className='last-sender'>~admin213: </span> 
              <span className='last-sent-message'>I see this will be a very productive room...</span> 
            </p>
          </li>
          <li className='room active'>
            <h4 className='room-name'>Juniorz</h4>
            <p className='last-sent'> 
              <span className='last-sender'>~johnnybarvo: </span> 
              <span className='last-sent-message'>senior work, junior salary lol</span> 
            </p>
          </li>
          <li className='room active'>
            <h4 className='room-name'>Doomsday Debuggers</h4>
            <p className='last-sent'> 
              <span className='last-sender'>~janedoe: </span> 
              <span className='last-sent-message'>This isn't even a bug anymore it's a straight up rat</span> 
            </p>
          </li>
          <li className='room active'>
            <h4 className='room-name'>Analitica</h4>
            <p className='last-sent'> 
              <span className='last-sender'>~james: </span> 
              <span className='last-sent-message'>I just be drawing charts man...</span> 
            </p>
          </li>
          <li className='room active'>
            <h4 className='room-name'>Code Confessions</h4>
            <p className='last-sent'> 
              <span className='last-sender'>~yoli: </span> 
              <span className='last-sent-message'>I dont really like java, I just like the idea of being someone who likes java</span> 
            </p>
          </li>
        </ul>
      </section>

      <section className='main'>
        <div className='info'>
          <h3>The 13th Floor</h3>
          <p><span className='number'>234</span> members</p>
        </div>
        <div className='messages'>
          <article className="message user">
            <div className='message-header'>
              <h4>JonnyBravo</h4>
              <p>4 hours ago</p>
            </div>
            <p className='message-body'>HEya!!! Welcome to The 13th Floor, Here are some basic house rulezz...</p>
          </article>
          <article className="message">
            <div className='message-header'>
              <h4>JaneDoe</h4>
              <p>4 hours ago</p>
            </div>
            <p className='message-body'>Hello loserz, let's do this!!!!!!</p>
          </article>
          <article className='message user'>
            <div className='message-header'>
              <h4>JonnyBravo</h4>
              <p>4 few hours ago</p>
            </div>
            <p className='message-body'>Dude!!! Did I not just say we need to be respectfull in the house rulez?!?!!!</p>
          </article>
          <article className="message">
            <div className='message-header'>
              <h4>JaneDoe</h4>
              <p>3 hours ago</p>
            </div>
            <p className='message-body'>Chillout grandma</p>
          </article>
          <article className="message">
            <div className='message-header'>
              <h4>admin213</h4>
              <p>2 hours ago</p>
            </div>
            <p className='message-body'>I see this will be a very productive room...</p>
          </article>

          <article className="message">
            <div className='message-header'>
              <h4>admin213</h4>
              <p>2 hours ago</p>
            </div>
            <p className='message-body'>I see this will be a very productive room...</p>
          </article>
          <article className="message">
            <div className='message-header'>
              <h4>admin213</h4>
              <p>2 hours ago</p>
            </div>
            <p className='message-body'>I see this will be a very productive room...</p>
          </article>
          <article className="message">
            <div className='message-header'>
              <h4>admin213</h4>
              <p>2 hours ago</p>
            </div>
            <p className='message-body'>I see this will be a very productive room...</p>
          </article>
          <article className="message">
            <div className='message-header'>
              <h4>admin213</h4>
              <p>2 hours ago</p>
            </div>
            <p className='message-body'>I see this will be a very productive room...</p>
          </article>
          <article className="message">
            <div className='message-header'>
              <h4>admin213</h4>
              <p>2 hours ago</p>
            </div>
            <p className='message-body'>I see this will be a very productive room...</p>
          </article>
          <article className="message">
            <div className='message-header'>
              <h4>admin213</h4>
              <p>2 hours ago</p>
            </div>
            <p className='message-body'>I see this will be a very productive room...</p>
          </article>
          <article className="message">
            <div className='message-header'>
              <h4>admin213</h4>
              <p>2 hours ago</p>
            </div>
            <p className='message-body'>I see this will be a very productive room...</p>
          </article>
        </div>


        <div className='text-box-wrapper'>
          <div className='text-box'>
            <form>
              <textarea className='input-box box-shadow' placeholder='type your message...'></textarea>
              <input type='submit' value='&gt;' className='send-btn' />
            </form>
          </div>
        </div>

      </section>
    </div>
  </>;
}

export default Home;