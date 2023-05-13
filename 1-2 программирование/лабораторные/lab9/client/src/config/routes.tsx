import React, { MemoExoticComponent } from 'react';

export interface Route {
  path: string | string[];
  component: MemoExoticComponent<() => React.ReactElement> | React.ReactElement;
  title?: string;
  alias: string;
}

export const routes: Route[] = [
  {
    title: 'Home',
    path: ['/', '/home', '/products'],
    component: <Home />,
    alias: 'home',
  },
  {
    title: '404',
    path: '/:404*',
    component: <NotFound />,
    alias: '404',
  },
  {
    title: 'Авторизация',
    path: '/login',
    component: <Login />,
    alias: 'login',
  },
  {
    title: 'Регистрация',
    path: '/register',
    component: <Register />,
    alias: 'register',
  },
];
