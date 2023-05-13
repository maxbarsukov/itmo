import React from 'react';
import { createRoot } from 'react-dom/client';

import { Provider } from 'react-redux';
import { BrowserRouter as Router } from 'react-router-dom';

import dayjs from 'dayjs';
import relativeTimePlugin from 'dayjs/plugin/relativeTime';
import calendarPlugin from 'dayjs/plugin/calendar';
import updateLocalePlugin from 'dayjs/plugin/updateLocale';
import 'dayjs/locale/ru';

import reportWebVitals from 'reportWebVitals';
import swConfig from 'serviceWorkerConfig';
import * as serviceWorker from 'serviceWorker';

import store from 'store';
import App from 'components/App';

import * as userSettingsUtils from 'utils/userSettings';

const container = document.getElementById('root')!;
const root = createRoot(container);

const userSettings = userSettingsUtils.get();
dayjs.locale(userSettings.language || 'ru');
dayjs.extend(relativeTimePlugin);
dayjs.extend(calendarPlugin);
dayjs.extend(updateLocalePlugin);

dayjs.updateLocale('ru', {
  calendar: {
    lastWeek: 'D MMMM, в hh:mm',
    sameDay: 'Сегодня, в hh:mm',
    lastDay: 'Вчера, в hh:mm',
    sameElse: 'DD.MM.YYYY',
  },
});
dayjs.updateLocale('en', {
  calendar: {
    lastWeek: 'D MMMM, at hh:mm',
    sameDay: 'Today, at hh:mm',
    lastDay: 'Yesterday, at hh:mm',
    sameElse: 'DD.MM.YYYY',
  },
});
dayjs.updateLocale('sv', {
  calendar: {
    lastWeek: 'D MMMM, klockan hh:mm',
    sameDay: 'Idag klockan hh:mm',
    lastDay: 'Igår klockan hh:mm',
    sameElse: 'DD.MM.YYYY',
  },
});
dayjs.updateLocale('is', {
  calendar: {
    lastWeek: 'D. MMMM, klukkan hh:mm',
    sameDay: 'Í dag, klukkan hh:mm',
    lastDay: 'Í gær, klukkan hh:mm',
    sameElse: 'DD.MM.YYYY',
  },
});

root.render(
  <React.StrictMode>
    <Provider store={store}>
      <Router>
        <App />
      </Router>
    </Provider>
  </React.StrictMode>
);

reportWebVitals();
serviceWorker.register(swConfig);
