import React, { useEffect, useState } from 'react';
import { Switch, Route } from 'react-router-dom';
import { getRoutes } from 'config/routes';
import { useSelector } from 'hooks';
import useRoute from 'hooks/useRoute';

const Router = () => {
  const languageSettings = useSelector(store => store.settings.language);
  const [routes, setRoutes] = useState(getRoutes());
  const route = useRoute();

  useEffect(() => {
    setRoutes(getRoutes);
    const newTitle = route.title ? `${route.title} | Lab9` : 'Lab9';
    if (document.title !== newTitle) document.title = newTitle;
  }, [languageSettings]);

  return (
    <Switch>
      {routes.map(({ path, component }, i) => (
        <Route key={i} exact path={path}>
          {component}
        </Route>
      ))}
    </Switch>
  );
};

export default React.memo(Router);
