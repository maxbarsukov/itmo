import { UserSettings } from 'interfaces';

export const APP_VERSION = 'v2';

export const API_URL = 'http://habr.com/kek/';

export const USER_KEY = 'lab9_USER';
export const TOKEN_KEY = 'lab9_TOKEN';
export const USER_SETTINGS_KEY = 'lab9_USER_SETTINGS';
export const NEEDS_UPDATE_KEY = 'lab9_NEEDS_UPDATE';

export const DEFAULT_USER_SETTINGS: UserSettings = {
  language: 'ru',
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
