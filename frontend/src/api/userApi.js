import { api } from './client';

export const userApi = {
  getUserById: (id) => api.get(`/api/users/${id}`),
  getUserByUsername: (username) => api.get(`/api/users/username/${username}`),
};
