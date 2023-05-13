import React from 'react';

import { blue } from '@material-ui/core/colors';

import {
  Home,
  Location,
  Setting,
} from 'react-iconly';

export const THEMES = [
  'light',
];

export const THEME_TYPES = {
  light: 'light',
};

export const THEME_NAMES = {
  light: 'Светлая',
};

/** Colors for app background */
export const BACKGROUND_COLORS_DEFAULT = {
  light: '#f5f5f5',
};

/** Colors for app foreground elements, such as Paper */
export const BACKGROUND_COLORS_PAPER = {
  light: '#ffffff',
};

export const THEME_PRIMARY_COLORS = {
  light: {
    main: blue.A400,
    light: blue.A200,
    dark: blue.A700,
  },
};

export const THEME_TEXT_COLORS = {
  light: {
    primary: 'rgb(0, 0, 0, 0.87)',
    secondary: 'rgb(0, 0, 0, 0.54)',
    disabled: 'rgba(0, 0, 0, 0.38)',
    hint: 'rgba(0, 0, 0, 0.38)',
  },
};

export const makeNavigationTabs = (
  w = 24,
  h = 24,
  replaceProfile = false
) => {
  const size = Math.max(w, h);
  const tabs = [
    {
      label: 'Home',
      icon: <Home set="light" size={size} />,
      to: () => '/',
      tab: 'home',
      match: 'home',
    },
    {
      label: 'Chart',
      icon: <Location set="light" size={size} />,
      to: () => '/chart',
      match: 'chart',
      tab: 'chart',
    },
  ];
  if (replaceProfile) {
    tabs.push({
      label: 'Настройки',
      icon: <Setting set="light" size={size} />,
      to: () => '/settings',
      match: 'settings',
      tab: 'settings',
    });
  }
  return tabs;
};
