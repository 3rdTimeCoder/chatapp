const register = () => {
    const form = document.getElementById("form");
    form.addEventListener("submit", (event) => {
        event.preventDefault();

        const data = new FormData(event.target);
        const body = {
            username: data.get("username"),
            email: data.get("email"),
            password: data.get("email")
        }

        const options = {
            method: 'POST', 
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(body), 
          };

        document.getElementById('form-message').innerHTML = `<p>Processing...</em>...</p>`;

        fetch(`https://api.dictionaryapi.dev/api/v2/entries/en/${word}`, options)
            .then(response => response.json())
            .then(data => {
                console.log(data);
                // if (searchType == "dictionary") {
                //     data = dictionaryData(data);
                // }
                // else if (searchType == "antonymns") {
                //     data = antonymnsData(data);
                // }else {
                //     data = synonymsData(data);
                //     console.log("data:", data);
                // }
                // const template = document.getElementById('results-template').innerText;
                // const compiledFunction = Handlebars.compile(template);
                // document.getElementById('results').innerHTML = compiledFunction(data);
            });
    });;
}

module.exports = { register };