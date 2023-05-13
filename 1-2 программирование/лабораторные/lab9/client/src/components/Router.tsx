import * as React from 'react';
import { Switch, Route } from 'react-router-dom';
import { routes } from 'config/routes';

const Router = () => {
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
