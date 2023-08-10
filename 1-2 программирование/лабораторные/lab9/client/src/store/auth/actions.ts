import {
  loginFail,
  loginLoading,
  loginSuccess,
  logout as logoutUser,
  setCurrentUser as setUser,
} from 'store/auth';

import AuthService from 'services/api/AuthService';
import Storage from 'utils/Storage';

import { USER_KEY, TOKEN_KEY } from 'config/constants';
import Credentials from 'interfaces/dto/Credentials';

const getErrorMessage = error => error?.response?.data?.message || 'ERROR';

export const setCurrentUser = user => dispatch => {
  Storage.set(USER_KEY, user);
  dispatch(setUser(user));
};

export const loadCurrentUser = () => dispatch => {
  dispatch(loginLoading());

  return AuthService.me().then(
    response => {
      dispatch(loginSuccess(response.data.user));
      Storage.set(USER_KEY, response.data.user);
      return Promise.resolve();
    },
    error => {
      dispatch(loginFail(getErrorMessage(error)));
      Storage.remove(USER_KEY);
      return Promise.reject();
    }
  );
};

export const login = (credentials: Credentials) => dispatch => {
  dispatch(loginLoading());

  return AuthService.login(credentials).then(
    response => {
      const { data } = response;
      dispatch(loginSuccess(data.user));
      Storage.set(TOKEN_KEY, data.token);
      Storage.set(USER_KEY, data.user);
      return Promise.resolve();
    },
    error => {
      dispatch(loginFail(getErrorMessage(error)));
      return Promise.reject();
    }
  );
};

export const register = (credentials: Credentials) => dispatch => {
  dispatch(loginLoading());

  return AuthService.register(credentials).then(
    response => {
      const { data } = response;
      dispatch(loginSuccess(data.user));
      Storage.set(TOKEN_KEY, data.token);
      Storage.set(USER_KEY, data.user);
      return Promise.resolve();
    },
    error => {
      dispatch(loginFail(getErrorMessage(error)));
      return Promise.reject();
    }
  );
};

export const logout = () => dispatch => {
  Storage.remove(TOKEN_KEY);
  Storage.remove(USER_KEY);

  dispatch(logoutUser());
};
