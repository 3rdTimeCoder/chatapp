// import {register} from './helpers';

const base_url = 'http://localhost:5000/v1';


const register = (router) => {

  const form = document.getElementById("form");
  form.addEventListener("submit", (event) => {
      event.preventDefault();

      const data = new FormData(event.target);
      const body = {
          username: data.get("username"),
          email: data.get("email"),
          password: data.get("password")
      }

      const options = {
          method: 'POST', 
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(body), 
        };

      const formMessage = document.getElementById('form-message');
      formMessage.innerHTML = `<p>Processing...</em>...</p>`;

      fetch(`${base_url}/register`, options)
          .then(response => response.json())
          .then(data => {
              console.log(data)
              if (data.result == "OK") router.navigateTo('/dictionary') 
              else formMessage.innerHTML = data.data.message 
          })
          .catch(e => formMessage.innerHTML = "An Error Occured");
  });
}


function lookupWord(searchType) {
  const form = document.getElementById("form");
  form.addEventListener("submit", (event) => {
      event.preventDefault();

      const data = new FormData(event.target);
      const word = data.get("word");

      const options = {
          method: 'GET',
      };

      document.getElementById('results').innerHTML = `<p>Searching for <em>'${word}'</em>...</p>`;
      

      fetch(`https://api.dictionaryapi.dev/api/v2/entries/en/${word}`, options)
          .then(response => response.json())
          .then(data => {
              console.log(data[0]);
              if (searchType == "dictionary") {
                data = dictionaryData(data);
              }
              else if (searchType == "antonymns") {
                data = antonymnsData(data);
              }else {
                data = synonymsData(data);
                console.log("data:", data);
              }
              const template = document.getElementById('results-template').innerText;
              const compiledFunction = Handlebars.compile(template);
              document.getElementById('results').innerHTML = compiledFunction(data);
          });
  });;
} 

const dictionaryData = (resData) => {
  return {
    word: resData[0].word,
    phonetic: resData[0].phonetic,
    audio: resData[0].phonetics[0]?.audio,
    partsOfSpeech: resData[0].meanings,
    definitions: resData[0].meanings[0].definitions
  };
}

const antonymnsData = (resData) => {
  antonyms1 = resData[0].meanings[1].antonyms
  return {
    word: resData[0].word,
    phonetic: resData[0].phonetic,
    antonyms: antonyms1.length == 0? ["No antonyms found."] : antonyms1
  };
}

const synonymsData = (resData) => {
  synonyms1 = resData[0].meanings[0].synonyms
  return {
    word: resData[0].word,
    phonetic: resData[0].phonetic,
    synonyms: synonyms1.length == 0? ["No synonyms found."] : synonyms1
  };
}

// tag::router[]
window.addEventListener('load', () => {
  const app = $('#app');

  const defaultTemplate = Handlebars.compile($('#default-template').html());
  const dictionaryTemplate = Handlebars.compile($('#dictionary-template').html());
  const landingTemplate = Handlebars.compile($('#landing-template').html());
  const loginTemplate = Handlebars.compile($('#login-template').html());
  const antonymnsTemplate = Handlebars.compile($('#antonymns-template').html());
  const synonymsTemplate = Handlebars.compile($('#synonyms-template').html());
  const thesaurusTemplate = Handlebars.compile($('#thesaurus-template').html());

  const router = new Router({
    mode:'hash',
    root:'index.html',
    page404: (path) => {
      const html = landingTemplate();
      app.html(html);
    },
    defaultRoute: '/',
  });

  router.add('/dictionary', async () => {
    const html = dictionaryTemplate();
    app.html(html);
    lookupWord("dictionary");
  });

  router.add('/landing', async () => {
    const html = landingTemplate();
    app.html(html);
    register(router);
  });


  router.add('/antonymns', async () => {
    const html = antonymnsTemplate();
    app.html(html);
    lookupWord("antonymns");
  });

  router.add('/synonyms', async () => {
    const html = synonymsTemplate();
    app.html(html);
    lookupWord("synonyms");
  });


  router.add('/thesaurus', async () => {
    const html = thesaurusTemplate();
    app.html(html);
  });

  router.addUriListener();

  $('a').on('click', (event) => {
    event.preventDefault();
    const target = $(event.target);
    const href = target.attr('href');
    const path = href.substring(href.lastIndexOf('/'));
    router.navigateTo(path);
  });
  

  router.navigateTo('/landing');
});
// end::router[]