import React, { MemoExoticComponent } from 'react';
import { Theme } from '@material-ui/core';

export interface Route {
  path: string | string[];
  component: MemoExoticComponent<() => React.ReactElement> | React.ReactElement;
  title?: string;
  alias: string;
  shouldShowAppBar?: boolean;
  appBarColor?: (theme: Theme) => string;
  shouldAppBarChangeColors?: boolean;
}

export const routes: Route[] = [
  {
    title: 'Таблица',
    path: ['/', '/home', '/products'],
    component: <Home />,
    alias: 'home',
    shouldShowAppBar: true,
    shouldAppBarChangeColors: true,
    appBarColor: theme => theme.palette.background.paper,
  },
  {
    title: 'Карта',
    path: ['/chart'],
    component: <Chart />,
    alias: 'chart',
    shouldShowAppBar: true,
    shouldAppBarChangeColors: true,
    appBarColor: theme => theme.palette.background.paper,
  },
  {
    title: 'Авторизация',
    path: '/login',
    component: <Login />,
    alias: 'login',
    shouldShowAppBar: false,
    shouldAppBarChangeColors: false,
    appBarColor: theme => theme.palette.background.default,
  },
  {
    title: 'Регистрация',
    path: '/register',
    component: <Register />,
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
