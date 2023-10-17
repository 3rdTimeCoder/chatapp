const config = {
    env: process.env.NODE_ENV || 'development',
    base_api_url: process.env.BASE_API_URL || 'http://localhost:5000/v1',
    options: {
                method: 'GET', 
                headers: {
                'Content-Type': 'application/json',
                'mode': 'cors',
                
                },
            },
}

export default config;