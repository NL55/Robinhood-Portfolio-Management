import client from './client';

export const getHoldingsByUserId = (userId) => {
  return client.get(`/api/holdings/user/${userId}`);
};