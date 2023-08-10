import React from 'react';
import ReactDOM from 'react-dom';

import { Provider } from 'react-redux';
import store from 'store';

import { BrowserRouter as Router } from 'react-router-dom';

import dayjs from 'dayjs';
import relativeTimePlugin from 'dayjs/plugin/relativeTime';
import calendarPlugin from 'dayjs/plugin/calendar';
import updateLocalePlugin from 'dayjs/plugin/updateLocale';

import 'dayjs/locale/ru';
import 'dayjs/locale/en';
import 'dayjs/locale/sv';
import 'dayjs/locale/is';

import 'config/i18n';

import reportWebVitals from 'reportWebVitals';
import swConfig from 'serviceWorkerConfig';
import * as serviceWorker from 'serviceWorker';

import App from 'components/App';

import * as userSettingsUtils from 'utils/userSettings';

const userSettings = userSettingsUtils.get();
dayjs.locale(userSettings.language || 'ru');
dayjs.extend(relativeTimePlugin);
dayjs.extend(calendarPlugin);
dayjs.extend(updateLocalePlugin);

dayjs.updateLocale('ru', {
  calendar: {
    lastWeek: 'D MMMM[, в ]HH:mm',
    sameDay: '[Сегодня, в ]HH:mm',
    lastDay: '[Вчера, в ]HH:mm',
    sameElse: 'DD.MM.YYYY',
  },
});
dayjs.updateLocale('en', {
  calendar: {
    lastWeek: 'D MMMM[, at ]hh:mm A',
    sameDay: '[Today, at ]hh:mm',
    lastDay: '[Yesterday, at ]hh:mm',
    sameElse: 'DD.MM.YYYY',
  },
});
dayjs.updateLocale('sv', {
  calendar: {
    lastWeek: 'D MMMM[, klockan ]HH:mm',
    sameDay: '[Idag klockan ]HH:mm',
    lastDay: '[Igår klockan ]HH:mm',
    sameElse: 'DD.MM.YYYY',
  },
});
dayjs.updateLocale('is', {
  calendar: {
    lastWeek: 'D[. ]MMMM[, klukkan ]HH:mm',
    sameDay: '[Í dag, klukkan ]HH:mm',
    lastDay: '[Í gær, klukkan ]HH:mm',
    sameElse: 'DD.MM.YYYY',
  },
});

ReactDOM.render(
  <React.StrictMode>
    <Provider store={store}>
      <Router>
        <App />
      </Router>
    </Provider>
  </React.StrictMode>,
  document.getElementById('root')
);

reportWebVitals();
serviceWorker.register(swConfig);
