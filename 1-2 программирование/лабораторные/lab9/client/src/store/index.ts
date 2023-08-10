import { AnyAction, configureStore } from '@reduxjs/toolkit';
import { ThunkAction, ThunkDispatch } from 'redux-thunk';
import { createLogger } from 'redux-logger';

import rootReducer from 'store/reducers';
import { useDispatch } from 'react-redux';

const store = configureStore({
  reducer: rootReducer,
  middleware: getDefaultMiddleware => {
    if (process.env.NODE_ENV === 'production') {
      return getDefaultMiddleware();
    }
    return getDefaultMiddleware().concat(createLogger());
  },
  devTools: process.env.NODE_ENV !== 'production',
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export type TypedDispatch = ThunkDispatch<AppDispatch, any, AnyAction>;
export type TypedThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  AppDispatch,
  unknown,
  AnyAction
>;

export const useTypedDispatch = () => useDispatch<TypedDispatch>();

export default store;
