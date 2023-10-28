export const isAuthenticated = () => {
    return localStorage.getItem('username') != null && 
    localStorage.getItem('email') != null;
}