import { configureStore } from '@reduxjs/toolkit';
import { createLogger } from 'redux-logger';

import rootReducer from 'store/reducers';

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
export default store;
