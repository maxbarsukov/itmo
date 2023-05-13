import React from 'react';
import { createRoot } from 'react-dom/client';
// import { Provider } from 'react-redux';
import reportWebVitals from './reportWebVitals';

const container = document.getElementById('root')!;
const root = createRoot(container);

root.render(
  <React.StrictMode>
    {/*<Provider store={store}>*/}
      Hello
    {/*</Provider>*/}
  </React.StrictMode>
);

reportWebVitals();
