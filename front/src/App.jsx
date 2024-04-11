import {Navigate, Route, Routes} from "react-router-dom";
import {Auth, Dashboard} from "@/layouts";
import SignIn from "@/pages/auth/sign-in.jsx";
import SignUp from "@/pages/auth/sign-up.jsx";

function App() {
  return (
      <Routes>
        <Route path="/dashboard/*" element={<Dashboard/>}/>
        <Route path="/auth" element={<Auth/>}>
          <Route path="sign-in" element={<SignIn/>}/>
          <Route path="sign-up" element={<SignUp/>}/>
        </Route>
        <Route path="*" element={<Navigate to="/dashboard/home" replace/>}/>
      </Routes>
  );
}

export default App;
