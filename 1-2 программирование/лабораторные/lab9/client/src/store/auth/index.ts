import { createSlice } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';

import User from 'interfaces/models/User';
import Storage from 'utils/Storage';
import { USER_KEY, TOKEN_KEY } from 'config/constants';

export interface AuthState {
  user?: User;
  error?: string;
  isLoggedIn: boolean;
  isLoading: boolean;
}

const initialState: AuthState = {
  user: Storage.get(USER_KEY),
  error: null,
  isLoggedIn: !!Storage.get(TOKEN_KEY),
  isLoading: false,
};

export const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    loginLoading: state => {
      state.error = null;
      state.isLoading = true;
    },
    loginSuccess: (state, action: PayloadAction<User>) => {
      state.user = action.payload;
      state.error = null;
      state.isLoggedIn = true;
      state.isLoading = false;
    },
    loginFail: (state, action: PayloadAction<string>) => {
      state.user = null;
      state.error = action.payload;
      state.isLoggedIn = false;
      state.isLoading = false;
    },
    logout: state => {
      state.user = null;
      state.error = null;
      state.isLoggedIn = false;
      state.isLoading = false;
    },
    setCurrentUser: (state, action: PayloadAction<User>) => {
      state.user = action.payload;
      state.error = null;
      state.isLoggedIn = true;
      state.isLoading = false;
    },
  },
});

export const { loginLoading, loginSuccess, loginFail, logout, setCurrentUser } = authSlice.actions;
export default authSlice.reducer;
