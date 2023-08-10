import { createSlice } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';

import InfoResponse from 'interfaces/dto/InfoResponse';

export interface InfoState {
  info: Partial<InfoResponse>;
  fetching: boolean;
  fetched: boolean;
  error?: string;
}

const initialState: InfoState = {
  info: {},
  fetching: false,
  fetched: false,
  error: null,
};

export const infoSlice = createSlice({
  name: 'info',
  initialState,
  reducers: {
    getInfoLoading: state => {
      state.fetching = true;
      state.fetched = false;
      state.error = null;
    },
    getInfoSuccess: (state, action: PayloadAction<InfoResponse>) => {
      Object.assign(state.info, action.payload);
      state.fetching = false;
      state.fetched = true;
      state.error = null;
    },
    getInfoFail: (state, action: PayloadAction<string>) => {
      state.error = action.payload;
      state.fetching = false;
      state.fetched = false;
    },
  },
});

export const { getInfoLoading, getInfoSuccess, getInfoFail } = infoSlice.actions;
export default infoSlice.reducer;
