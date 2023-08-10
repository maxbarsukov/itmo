import React, { MemoExoticComponent } from 'react';
import { Theme } from '@material-ui/core';

import Home from 'pages/Home';
import NotFound from 'pages/NotFound';
import Login from 'pages/Login';
import Settings from 'pages/Settings';
import SettingsAppearance from 'pages/Settings/Appearance';
import SettingsLanguage from 'pages/Settings/Language';
import SettingsTable from 'pages/Settings/Table';

import i18n from 'config/i18n';
import getContrastPaperColor from 'utils/getContrastPaperColor';

export interface Route {
  path: string | string[];
  component: MemoExoticComponent<() => React.ReactElement> | React.ReactElement;
  title?: string;
  alias: string;
  shouldShowAppBar?: boolean;
  appBarColor?: (theme: Theme) => string;
  shouldAppBarChangeColors?: boolean;
}

export const getRoutes = () => {
  const t = i18n.t.bind(i18n);
  const routes: Route[] = [
    {
      title: t`pages.app.routes.home`,
      path: ['/', '/home', '/products'],
      component: <Home variant="home" />,
      alias: 'home',
      shouldShowAppBar: true,
      shouldAppBarChangeColors: true,
      appBarColor: theme => theme.palette.background.paper,
    },
    {
      title: t`pages.app.routes.chart`,
      path: '/chart',
      component: <Home variant="chart" />,
      alias: 'chart',
      shouldShowAppBar: true,
      shouldAppBarChangeColors: true,
      appBarColor: theme => theme.palette.background.paper,
    },
    {
      title: t`pages.app.routes.settings`,
      path: '/settings',
      component: <Settings />,
      alias: 'settings',
      shouldShowAppBar: false,
      shouldAppBarChangeColors: false,
      appBarColor: theme => getContrastPaperColor(theme),
    },
    {
      title: t`pages.app.routes.settingsLanguage`,
      path: '/settings/language',
      component: <SettingsLanguage />,
      alias: 'settingsLanguage',
      shouldShowAppBar: false,
      shouldAppBarChangeColors: false,
      appBarColor: theme => getContrastPaperColor(theme),
    },
    {
      title: t`pages.app.routes.settingsAppearance`,
      path: '/settings/appearance',
      component: <SettingsAppearance />,
      alias: 'settingsAppearance',
      shouldShowAppBar: false,
      shouldAppBarChangeColors: false,
      appBarColor: theme => getContrastPaperColor(theme),
    },
    {
      title: t`pages.app.routes.settingsTable`,
      path: '/settings/table',
      component: <SettingsTable />,
      alias: 'settingsTable',
      shouldShowAppBar: false,
      shouldAppBarChangeColors: false,
      appBarColor: theme => getContrastPaperColor(theme),
    },
    {
      title: t`pages.app.routes.login`,
      path: '/login',
      component: <Login />,
      alias: 'login',
      shouldShowAppBar: false,
      shouldAppBarChangeColors: false,
      appBarColor: theme => theme.palette.background.default,
    },
    {
      title: t`pages.app.routes.register`,
      path: '/register',
      component: <Login register />,
      alias: 'register',
      shouldShowAppBar: false,
      shouldAppBarChangeColors: false,
      appBarColor: theme => theme.palette.background.default,
    },
    {
      title: '404',
      path: '/:404*',
      component: <NotFound />,
      alias: '404',
      shouldShowAppBar: false,
      shouldAppBarChangeColors: false,
      appBarColor: theme => theme.palette.background.default,
    },
  ];
  return routes;
};
