import { Axios } from '@/configs/axios.js';

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
