import React, { useEffect } from 'react';

import makeStyles from '@material-ui/core/styles/makeStyles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import { Setting, User } from 'react-iconly';
import WifiOffRoundedIcon from '@material-ui/icons/WifiOffRounded';

import { useHistory, useLocation } from 'react-router-dom';
import { APP_BAR_HEIGHT, MAX_WIDTH, MIDDLE_WIDTH } from 'config/constants';

import { Offline } from 'react-detect-offline';
import useRoute from 'hooks/useRoute';
import { useSelector } from 'hooks';
import { CircularProgress, alpha, useMediaQuery, useTheme, Theme } from '@material-ui/core';
import useAppBarScrollTrigger from 'hooks/useAppBarScrollTrigger';
import getSecondaryAppBarColor from 'utils/getSecondaryAppBarColor';
import UserMenu from 'components/blocks/UserMenu';
import UserAvatar from 'components/blocks/UserAvatar';
import { useTranslation } from 'react-i18next';

interface StyleProps {
  isTransformed: boolean;
  appBarColor: (theme: Theme) => string;
  shouldChangeColors: boolean;
}
const makeAppBarBackgroundColor = ({ isTransformed, appBarColor, shouldChangeColors, theme }) => {
  if (shouldChangeColors) {
    return isTransformed ? getSecondaryAppBarColor(theme) : theme.palette.background.paper;
  }
  return appBarColor ? appBarColor(theme) : theme.palette.background.paper;
};

const useStyles = makeStyles(theme => ({
  root: {
    backgroundColor: (props: StyleProps) => makeAppBarBackgroundColor({ ...props, theme }),
    [theme.breakpoints.up(MIDDLE_WIDTH)]: {
      display: 'none',
    },
    color: theme.palette.text.primary,
    position: 'fixed',
    height: APP_BAR_HEIGHT + 1,
    flexGrow: 1,
    zIndex: theme.zIndex.appBar + 1,
    willChange: 'transform',
  },
  toolbar: {
    margin: 'auto',
    minHeight: 'unset',
    height: APP_BAR_HEIGHT,
    padding: 0,
    maxWidth: MAX_WIDTH,
    width: '100%',
    flexDirection: 'column',
  },
  headerTitle: {
    color: theme.palette.text.primary,
    fontWeight: 800,
    height: '100%',
    alignItems: 'center',
    display: 'flex',
    fontFamily: 'Google Sans',
    cursor: 'pointer',
    '-webkit-tap-highlight-color': alpha(theme.palette.background.paper, 0.3),
    userSelect: 'none',
  },
  headerTitleWrapper: {
    display: 'flex',
    alignItems: 'center',
    flexGrow: 1,
  },
  offline: {
    color: theme.palette.text.disabled,
    marginLeft: theme.spacing(1),
    display: 'flex',
    alignItems: 'center',
  },
  avatar: {
    height: theme.spacing(3),
    width: theme.spacing(3),
    borderRadius: theme.shape.borderRadius,
  },
  content: {
    display: 'flex',
    alignItems: 'center',
    width: `calc(100% - ${theme.spacing(2) * 2}px)`,
  },
}));

const AppBarComponent = () => {
  const trigger = useAppBarScrollTrigger();
  const history = useHistory();
  const location = useLocation();
  const route = useRoute();

  const shouldChangeColors = route.shouldAppBarChangeColors;
  const appBarColor = route.appBarColor;
  const isHidden = !route.shouldShowAppBar;
  const theme = useTheme();
  const { t } = useTranslation();

  const appHasDrawer = useMediaQuery(theme.breakpoints.up(MIDDLE_WIDTH));
  const [isUserMenuOpen, setUserMenuOpen] = React.useState(false);

  const classes = useStyles({
    isTransformed: trigger,
    appBarColor,
    shouldChangeColors,
  });

  const userData = useSelector(state => state.auth.user);

  const shouldShowUser = useSelector(state => state.auth.isLoggedIn);
  const shouldShowLoadingSpinner = useSelector(state => state.auth.isLoading);

  const goHome = () => {
    window.scrollTo(0, 0);
    history.push('/');
  };

  const goSettings = () =>
    history.push('/settings', {
      from: location.pathname + location.search,
      scroll: window.pageYOffset,
    });

  const goAuth = () =>
    history.push('/login', {
      from: location.pathname + location.search,
      scroll: window.pageYOffset,
    });

  const openUserMenu = () => setUserMenuOpen(true);

  useEffect(() => {
    if (appHasDrawer && isUserMenuOpen) setUserMenuOpen(false);
  }, [shouldChangeColors, isHidden, route, appHasDrawer]);

  if (isHidden) return null;

  return (
    <>
      <AppBar className={classes.root} elevation={0}>
        <Toolbar className={classes.toolbar}>
          <div className={classes.content}>
            <div className={classes.headerTitleWrapper}>
              <Typography onClick={goHome} variant="h6" className={classes.headerTitle}>
                {t`pages.app.appName`}
              </Typography>
              <Offline
                polling={{
                  url: 'https://ipv4.icanhazip.com',
                  enabled: true,
                  interval: 5000,
                  timeout: 5000,
                }}
              >
                <WifiOffRoundedIcon className={classes.offline} />
              </Offline>
            </div>
            <IconButton onClick={goSettings}>
              <Setting set="light" size={24} />
            </IconButton>
            {shouldShowLoadingSpinner && (
              <IconButton style={{ width: 48, height: 48 }}>
                <CircularProgress style={{ width: 16, height: 16 }} color="primary" />
              </IconButton>
            )}
            {!shouldShowUser && !shouldShowLoadingSpinner && (
              <IconButton onClick={goAuth}>
                <User set="light" size={24} />
              </IconButton>
            )}
            {shouldShowUser && userData && (
              <IconButton onClick={openUserMenu}>
                <UserAvatar src={null} alias={userData.name} className={classes.avatar} />
              </IconButton>
            )}
          </div>
        </Toolbar>
      </AppBar>
      <UserMenu variant="dialog" isOpen={isUserMenuOpen} setOpen={setUserMenuOpen} />
    </>
  );
};

export default React.memo(AppBarComponent);
