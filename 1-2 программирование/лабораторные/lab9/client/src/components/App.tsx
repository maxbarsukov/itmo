import * as React from 'react';

import {
  Theme,
  createTheme,
  ThemeProvider,
  alpha,
  makeStyles,
  darken,
} from '@material-ui/core/styles';

import isMobile from 'is-mobile';
import { SnackbarProvider } from 'notistack';
import { useSelector } from 'hooks';

import useTitleChange from 'hooks/useTitleChange';
import useRoute from 'hooks/useRoute';
import useUserDataFetch from 'hooks/useUserDataFetch';
import generateTheme from 'utils/theme';

import UpdateNotification from 'components/blocks/UpdateNotification';
import BottomBar from 'components/blocks/BottomBar';
import AppBar from 'components/blocks/AppBar';
import SideNavigationDrawer from 'components/blocks/SideNavigationDrawer';

import Router from 'components/Router';

import {
  APP_BAR_HEIGHT,
  BOTTOM_BAR_HEIGHT,
  chromeAddressBarHeight,
  DRAWER_WIDTH,
  MAX_WIDTH as maxWidth,
  MIDDLE_WIDTH,
  MIN_WIDTH,
} from 'config/constants';

interface StyleProps {
  theme: Theme;
  shouldShowAppBar: boolean;
}

const useStyles = makeStyles({
  '@global': {
    // Needed for IconButton touch ripple tweaks
    '@keyframes enter': {
      '0%': {
        opacity: 0.1,
      },
      '100%': {
        opacity: 0.15,
      },
    },
    '@keyframes exit': {
      '0%': {
        opacity: 1,
      },
      '100%': {
        opacity: 0,
      },
    },
    '.IconButton_TouchRipple-rippleVisible': {
      animation: 'enter 0ms ease',
      opacity: 0.15,
    },
    '.IconButton_TouchRipple-childLeaving': {
      animation: 'exit 255ms ease',
    },
  },
  app: ({ shouldShowAppBar, theme }: StyleProps) => ({
    display: 'flex',
    minHeight: `calc(100vh - ${APP_BAR_HEIGHT}px - ${isMobile() ? chromeAddressBarHeight : 0}px - ${
      shouldShowAppBar ? BOTTOM_BAR_HEIGHT : 0
    }px + env(safe-area-inset-bottom, 0px))`,
    borderRadius: 0,
    alignItems: 'flex-start',
    flexDirection: 'row',
    width: '100%',
    maxWidth: maxWidth,
    margin: `${APP_BAR_HEIGHT}px auto calc(${
      shouldShowAppBar ? BOTTOM_BAR_HEIGHT : 0
    }px + env(safe-area-inset-bottom, 0px)) auto`,
    boxSizing: 'border-box',
    [theme.breakpoints.up(MIDDLE_WIDTH)]: {
      marginTop: 0,
      marginBottom: 0,
    },
    [theme.breakpoints.up(MIN_WIDTH)]: {
      padding: theme.spacing(0, 2),
    },
  }),
  /** Body class */
  body: ({ theme }: StyleProps) => ({
    /** Disable blue highlight for links. Can be bad for accessibility. */
    '& a': {
      '-webkit-tap-highlight-color': alpha(theme.palette.background.paper, 0.3),
    },
    backgroundColor: theme.palette.background.default,
    color: theme.palette.text.primary,
    margin: 0,
    fontFamily: '-apple-system, BlinkMacSystemFont, Roboto, Arial, sans-serif',
    lineHeight: 1.5,
    '&::-webkit-scrollbar': {
      width: 15,
      height: 10,
      background: theme.palette.background.paper,
      border: '1px solid ' + darken(theme.palette.background.paper, 0.05),
    },
    '&::-webkit-scrollbar-thumb': {
      minHeight: 28,
      background: darken(theme.palette.background.paper, 0.08),
      transition: '0.1s',
      '&:hover': {
        background: darken(theme.palette.background.paper, 0.1),
      },
      '&:active': {
        background: darken(theme.palette.background.paper, 0.2),
      },
    },
    '& *::selection': {
      background: darken(theme.palette.primary.main, 0.5),
    },
  }),
  appWrapper: ({ theme }: StyleProps) => ({
    display: 'flex',
    paddingLeft: 0,
    width: '100%',
    [theme.breakpoints.up(MIDDLE_WIDTH)]: {
      paddingLeft: DRAWER_WIDTH,
      width: `calc(100% - ${DRAWER_WIDTH}px)`,
    },
  }),
});

const App = () => {
  const storeTheme = generateTheme();
  const theme = React.useMemo(() => createTheme(storeTheme), [storeTheme]);
  const route = useRoute();
  const classes = useStyles({
    theme,
    shouldShowAppBar: route?.shouldShowAppBar,
  });

  useTitleChange();
  useUserDataFetch();

  return (
    <ThemeProvider theme={theme}>
      <SnackbarProvider>
        <SideNavigationDrawer />
        <div className={classes.appWrapper}>
          <div className={classes.app}>
            <UpdateNotification />
            <AppBar />
            <BottomBar />
            <Router />
          </div>
        </div>
      </SnackbarProvider>
    </ThemeProvider>
  );
};

export default React.memo(App);
