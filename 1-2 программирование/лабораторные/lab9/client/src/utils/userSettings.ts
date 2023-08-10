import { UserSettings } from 'interfaces';
import { DEFAULT_USER_SETTINGS, USER_SETTINGS_KEY } from 'config/constants';
import { THEMES } from 'config/themes';

import Storage from 'utils/Storage';

export const get = (): UserSettings => {
  try {
    const data: UserSettings = Storage.get(USER_SETTINGS_KEY);
    if (!data) return DEFAULT_USER_SETTINGS;

    for (const key in DEFAULT_USER_SETTINGS) {
      if (!data[key]) {
        data[key] = DEFAULT_USER_SETTINGS[key];
      } else if (key === 'themeType') {
        const { themeType } = data;
        const isThemeInDefaultThemes = THEMES.find((e) => e === themeType);

        if (!isThemeInDefaultThemes) {
          data.themeType = DEFAULT_USER_SETTINGS.themeType;
        }
      }
    }

    return data;
  } catch (e) {
    console.error(
      'Cannot parse user settings:',
      e,
      '\nGot:',
      localStorage.getItem(USER_SETTINGS_KEY)
    );
    return DEFAULT_USER_SETTINGS;
  }
};

export const set = (payload: Partial<UserSettings>): UserSettings => {
  const data = get() || DEFAULT_USER_SETTINGS;

  for (const key in payload) {
    data[key] = payload[key];
  }

  Storage.set(USER_SETTINGS_KEY, data);
  return data;
};
