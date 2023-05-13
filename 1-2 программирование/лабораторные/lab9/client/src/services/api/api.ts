import axios from 'axios';
import Storage from 'utils/Storage';
import { TOKEN_KEY } from 'config/constants';

export const API_URL = process.env.REACT_APP_API_URL;

const instance = axios.create({
  baseURL: API_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
  timeout: 10000,
});

instance.interceptors.request.use(
  config => {
    const token = Storage.get(TOKEN_KEY);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

instance.interceptors.response.use(
  response => {
    return response;
  },
  async error => {
    const originalConfig = error.config;

    // Access Token was expired
    // eslint-disable-next-line no-underscore-dangle
    if (error.response.status === 401 && !originalConfig._isRetry) {
      // eslint-disable-next-line no-underscore-dangle
      originalConfig._isRetry = true;

      try {
        return instance.request(originalConfig);
      } catch (_error) {
        return Promise.reject(_error);
      }
    }

    return Promise.reject(error);
  }
);

export default instance;
