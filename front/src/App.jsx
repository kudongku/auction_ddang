import { Navigate, Route, Routes, useLocation } from 'react-router-dom';
import { Auth, Dashboard } from '@/layouts';
import SignIn from '@/pages/auth/sign-in.jsx';
import SignUp from '@/pages/auth/sign-up.jsx';

function App() {
  return (
    <Routes>
      <Route
        path="/dashboard/*"
        element={
          <RequireAuth>
            <Dashboard />
          </RequireAuth>
        }
      />
      <Route path="/auth" element={<Auth />}>
        <Route
          path="sign-in"
          element={
            <PublicRoute>
              <SignIn />
            </PublicRoute>
          }
        />
        <Route
          path="sign-up"
          element={
            <PublicRoute>
              <SignUp />
            </PublicRoute>
          }
        />
      </Route>
      <Route path="*" element={<Navigate to="/dashboard/home" replace />} />
    </Routes>
  );
}

function RequireAuth({ children }) {
  const token = localStorage.getItem('authorizationToken');
  let location = useLocation();

  if (!token) {
    // 사용자가 로그인하지 않았다면 로그인 페이지로 리다이렉트
    return <Navigate to="/auth/sign-in" state={{ from: location }} replace />;
  }

  return children;
}

function PublicRoute({ children }) {
  return children;
}

export default App;
