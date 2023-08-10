import { UserSettings } from 'interfaces';

export const APP_VERSION = 'v1';

export const API_URL = process.env.REACT_APP_API_URL;

export const USER_KEY = 'lab9_USER';
export const TOKEN_KEY = 'lab9_TOKEN';
export const LAST_UPDATED_KEY = 'lab9_LAST_UPDATED';
export const UPDATED_BY_KEY = 'lab9_UPDATED_BY';
export const REQUESTS_KEY = 'lab9_REQUESTS';
export const USER_SETTINGS_KEY = 'lab9_USER_SETTINGS';
export const NEEDS_UPDATE_KEY = 'lab9_NEEDS_UPDATE';

export type FlowAlias = 'home' | 'chart';
export interface FlowObject {
  title: string;
  alias: string;
}
export const FLOWS: FlowObject[] = [
  {
    title: 'pages.app.navigationTabs.home',
    alias: 'home',
  },
  {
    title: 'pages.app.navigationTabs.chart',
    alias: 'chart',
  },
];

export const DEFAULT_USER_SETTINGS: UserSettings = {
  themeType: 'light',
  preferredDarkTheme: 'dark',
  preferredLightTheme: 'light',
  autoChangeTheme: false,
  language: 'ru',
  table: 'material',
};

export const LANGUAGES_INTERFACE = [
  {
    type: 'en',
    name: 'English (IN)',
  },
  {
    type: 'ru',
    name: 'Русский',
  },
  {
    type: 'is',
    name: 'Íslenska',
  },
  {
    type: 'sv',
    name: 'Svenska',
  },
];

export const HOUR = 1000 * 60 * 60;
export const DEFAULT_UPDATE_INTERVAL = HOUR / 12;

export const chromeAddressBarHeight = 56;
export const MIN_WIDTH = 960;
export const MIDDLE_WIDTH = 1175;
export const MAX_WIDTH = 1280;
export const BOTTOM_BAR_HEIGHT = 44;
export const APP_BAR_HEIGHT = 48;
export const DRAWER_WIDTH = 296;
