import './App.css';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/home/Home";
import Landing from "./pages/landing/Landing";
import Error from "./pages/error/Error";
import PrivateRoute from './routes/PrivateRoute';
import { isAuthenticated } from './auth/auth';
 
function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route exact path="/" element={<Landing />} />
          <Route 
            exact path="/home" 
            element={
              <PrivateRoute component={Home} isAuthenticated={isAuthenticated()} />
            } 
          />
          <Route path="*" element={<Error />} />
        </Routes>
      </Router>
    </>
  );
}

export default App;
