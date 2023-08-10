import { createSlice } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';

import { ThemeOptions } from '@material-ui/core';
import generateTheme from 'utils/theme';

import { UserSettings } from 'interfaces';
import * as userSettings from 'utils/userSettings';

const userLocalSettings = userSettings.get();
const theme = generateTheme(userLocalSettings.themeType);

export interface SettingsState extends UserSettings {
  theme: ThemeOptions;
}

const initialState: SettingsState = {
  theme,
  ...userLocalSettings,
};

export const settingsSlice = createSlice({
  name: 'settings',
  initialState,
  reducers: {
    setSettings: (state, action: PayloadAction<Partial<UserSettings>>) => {
      const newSettings = userSettings.set(action.payload);
      const shouldUpdateTheme = !!action.payload.themeType;

      Object.assign(state, newSettings);
      if (shouldUpdateTheme) {
        Object.assign(state.theme, generateTheme(newSettings.themeType));
      }
    },
    getSettings: state => {
      Object.assign(state, userSettings.get());
    },
  },
});

export const { setSettings, getSettings } = settingsSlice.actions;
export default settingsSlice.reducer;
