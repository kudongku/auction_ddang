const Axios = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {'content-type': 'application/json'}
});