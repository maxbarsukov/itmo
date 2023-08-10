import React, { useEffect, useState } from 'react';

import { makeStyles } from '@material-ui/core/styles';
import BottomNavigation from '@material-ui/core/BottomNavigation';
import BottomNavigationAction from '@material-ui/core/BottomNavigationAction';
import { Paper, useMediaQuery, useTheme } from '@material-ui/core';

import { useHistory } from 'react-router-dom';
import { useSelector } from 'hooks';

import { BOTTOM_BAR_HEIGHT, MIDDLE_WIDTH } from 'config/constants';
import { makeNavigationTabs } from 'config/themes';

import getContrastPaperColor from 'utils/getContrastPaperColor';
import useRoute from 'hooks/useRoute';

const useStyles = makeStyles(theme => ({
  root: {
    position: 'fixed',
    bottom: 0,
    left: 0,
    zIndex: theme.zIndex.appBar,
    width: '100%',
    willChange: 'transform',
    [theme.breakpoints.up(MIDDLE_WIDTH)]: {
      display: 'none',
    },
  },
  container: {
    background: getContrastPaperColor(theme),
    height: BOTTOM_BAR_HEIGHT,
    paddingBottom: 'env(safe-area-inset-bottom, 0)',
  },
  item: {
    fontFamily: 'Google Sans',
    fontSize: '12px',
    transitionDuration: '50ms',
  },
  selected: {
    fontSize: '12.5px !important',
  },
}));

const makeTabs = () => makeNavigationTabs(28, 28, true);

const findPathValue = route => {
  return makeTabs().findIndex(e => {
    if (Array.isArray(e.match)) {
      return e.match.some(k => k === route.alias);
    }
    return e.match === route.alias;
  });
};

const BottomBar = () => {
  const route = useRoute();
  const classes = useStyles();
  const theme = useTheme();
  const history = useHistory();
  const [isShown, setShown] = React.useState(false);
  const [value, setValue] = useState(findPathValue(route));

  const languageSettings = useSelector(store => store.settings.language);

  const [navigationTabs, setNavigationTabs] = useState(makeTabs());

  useEffect(() => {
    setNavigationTabs(makeTabs());
  }, [languageSettings]);

  const shouldHideBottomBar = useMediaQuery(theme.breakpoints.up(MIDDLE_WIDTH), {
    noSsr: true,
  });

  const handleChange = (_event, newValue) => {
    setValue(newValue);
  };

  const go = e => {
    if (history.location.pathname !== e.to()) {
      history.push(e.to());
    }
  };

  const hideAppBarHandler = r => {
    setShown(r.shouldShowAppBar);
  };

  useEffect(() => {
    hideAppBarHandler(route);
    setValue(findPathValue(route));
  }, [route]);

  if (shouldHideBottomBar) return null;

  return isShown ? (
    <Paper elevation={2} className={classes.root}>
      <BottomNavigation
        value={value}
        onChange={handleChange}
        showLabels
        className={classes.container}
      >
        {navigationTabs.map(e => (
          <BottomNavigationAction
            key={e.label}
            classes={{ label: classes.item, selected: classes.selected }}
            icon={e.icon}
            onClick={() => go(e)}
          />
        ))}
      </BottomNavigation>
    </Paper>
  ) : null;
};

export default React.memo(BottomBar);
