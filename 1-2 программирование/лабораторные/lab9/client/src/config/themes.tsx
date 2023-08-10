import React from 'react';
import TabObject from 'interfaces/NavigationTabObject';

import { blue } from '@material-ui/core/colors';
import {
  darken,
  alpha,
  lighten,
  PaletteType as MUIPaletteType,
} from '@material-ui/core';

import i18n from 'config/i18n';

import { Home, Location, Setting } from 'react-iconly';

export type PaletteType = 'light' | 'dark' | 'lab8';

export const THEMES: PaletteType[] = ['light', 'dark', 'lab8'];

export const THEME_TYPES: Record<PaletteType, MUIPaletteType> = {
  light: 'light',
  dark: 'dark',
  lab8: 'light',
};

const t = i18n.t.bind(i18n);
export const getThemeNames = (): Record<PaletteType, string> => {
  return {
    light: t`pages.app.themes.light`,
    dark: t`pages.app.themes.dark`,
    lab8: t`pages.app.themes.lab8`,
  };
};

/** Colors for app background */
export const BACKGROUND_COLORS_DEFAULT = {
  light: '#f5f5f5',
  dark: '#0e0e0e',
  lab8: '#e4f5ff',
};

/** Colors for app foreground elements, such as Paper */
export const BACKGROUND_COLORS_PAPER = {
  light: '#ffffff',
  dark: '#181818',
  lab8: '#d2efff',
};

export const THEME_PRIMARY_COLORS = {
  light: {
    main: blue.A400,
    light: blue.A200,
    dark: blue.A700,
  },
  dark: {
    main: blue.A100,
    light: lighten(blue.A100, 0.05),
    dark: darken(blue.A100, 0.1),
  },
  lab8: {
    main: '#93a7b2',
    light: '#a2b3bd',
    dark: '#758f9d',
  },
};

export const THEME_TEXT_COLORS = {
  light: {
    primary: 'rgb(0, 0, 0, 0.87)',
    secondary: 'rgb(0, 0, 0, 0.54)',
    disabled: 'rgba(0, 0, 0, 0.38)',
    hint: 'rgba(0, 0, 0, 0.38)',
  },
  dark: {
    primary: '#e9e9e9',
    secondary: alpha('#e9e9e9', 0.54),
    disabled: alpha('#e9e9e9', 0.38),
    hint: alpha('#e9e9e9', 0.38),
  },
  lab8: {
    primary: 'rgb(0, 0, 0, 0.54)',
    secondary: 'rgb(0, 0, 0, 0.38)',
    disabled: 'rgba(0, 0, 0, 0.24)',
    hint: 'rgba(0, 0, 0, 0.20)',
  },
};

export const makeNavigationTabs = (
  w = 24,
  h = 24,
  replaceProfile = false
): TabObject[] => {
  const size = Math.max(w, h);
  const t = i18n.t.bind(i18n);

  const tabs: TabObject[] = [
    {
      label: t`pages.app.navigationTabs.home`,
      icon: <Home set="light" size={size} />,
      to: () => '/',
      tab: 'home',
      match: 'home',
    },
    {
      label: t`pages.app.navigationTabs.chart`,
      icon: <Location set="light" size={size} />,
      to: () => '/chart',
      match: 'chart',
      tab: 'chart',
    },
  ];
  if (replaceProfile) {
    tabs.push({
      label: t`pages.app.navigationTabs.settings`,
      icon: <Setting set="light" size={size} />,
      to: () => '/settings',
      match: [
        'settings',
        'settingsAppearance',
        'settingsLanguage',
      ],
      tab: 'settings',
    });
  }
  return tabs;
};
