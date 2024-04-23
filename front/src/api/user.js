import { Axios } from '@/configs/axios.js';

// 유저 정보
export const getUserByUserId = async () => {
  const authorizationToken = localStorage.getItem('authorizationToken');
  if (!authorizationToken) {
    console.log('No token found');
    return;
  }

  const tokenParts = authorizationToken.split('.')[1]; // 토큰의 payload 부분만 추출
  const decodedPayload = atob(tokenParts.replace(/-/g, '+').replace(/_/g, '/')); // base64url을 base64로 변환
  const payload = JSON.parse(decodedPayload);

  return await Axios.get(`/v1/users/${payload.userId}`);
};

// 로그인
export const signin = async (email, password) => {
  return await Axios.post('/v1/users/signin', { email, password });
};

// 회원가입
export const signup = async (userData) => {
  return await Axios.post('/v1/users/signup', userData);
};

// Update a user's information
export const updateUser = async (userData) => {
  return await Axios.patch('/v1/users', userData);
};

// Delete a user
export const deleteUser = async () => {
  return await Axios.delete('/v1/users');
};

// Update a user's location
export const updateLocation = async (locationData) => {
  return await Axios.patch('/v1/users/location', locationData);
};
