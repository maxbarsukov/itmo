import { createSlice } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';

import Storage from 'utils/Storage';
import { REQUESTS_KEY } from 'config/constants';

export interface RequestsState {
  uuids: string[];
}

const initialState: RequestsState = {
  uuids: Storage.get(REQUESTS_KEY) || [],
};

export const requestsSlice = createSlice({
  name: 'requests',
  initialState,
  reducers: {
    addRequest: (state, action: PayloadAction<string>) => {
      if (action.payload.startsWith('[')) {
        state.uuids.push(action.payload.slice(1,-1));
      } else {
        state.uuids.push(action.payload);
      }
      Storage.set(REQUESTS_KEY, state.uuids);
    },
  },
});

export const { addRequest } = requestsSlice.actions;
export default requestsSlice.reducer;
