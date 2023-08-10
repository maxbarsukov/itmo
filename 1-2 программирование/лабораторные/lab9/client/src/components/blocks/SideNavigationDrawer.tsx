import React, { useState, useEffect } from 'react';

import { ButtonBase, Drawer, Typography, useTheme } from '@material-ui/core';
import { makeStyles, alpha } from '@material-ui/core/styles';

import { APP_BAR_HEIGHT, DRAWER_WIDTH, MIDDLE_WIDTH } from 'config/constants';
import { makeNavigationTabs } from 'config/themes';

import getSecondaryAppBarColor from 'utils/getSecondaryAppBarColor';

import { useSelector } from 'hooks';
import useRoute from 'hooks/useRoute';
import { Route } from 'config/routes';
import TabObject from 'interfaces/NavigationTabObject';

import { useHistory } from 'react-router';
import { Link } from 'react-router-dom';
import { Skeleton } from '@material-ui/lab';
import { Login } from 'react-iconly';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import { useTranslation } from 'react-i18next';

import UserMenu from './UserMenu';
import UserAvatar from './UserAvatar';

const avatarWidth = 32;
const avatarHeight = 32;

const useStyles = makeStyles(theme => ({
  drawer: {
    flexShrink: 0,
    display: 'flex',
    width: DRAWER_WIDTH,
  },
  drawerPaper: {
    width: DRAWER_WIDTH,
    border: 'none',
    height: 'auto',
    backgroundColor: theme.palette.background.default,
  },
  drawerContainer: {
    overflow: 'auto',
    display: 'flex',
    width: '100%',
    flexDirection: 'column',
    height: '100%',
  },
  logoHolder: {
    backgroundColor: getSecondaryAppBarColor(theme),
    color: theme.palette.text.primary,
    height: APP_BAR_HEIGHT,
    display: 'flex',
    width: '100%',
    flexDirection: 'column',
    cursor: 'pointer',
    '-webkit-tap-highlight-color': alpha(theme.palette.background.paper, 0.3),
    userSelect: 'none',
  },
  logo: {
    color: theme.palette.text.primary,
    fontWeight: 800,
    height: '100%',
    alignItems: 'center',
    display: 'flex',
    fontSize: 20,
    marginLeft: theme.spacing(2),
    padding: theme.spacing(0, 2),
    fontFamily: 'Google Sans',
  },
  tabsHolder: {
    padding: theme.spacing(0, 2),
    display: 'flex',
    flexDirection: 'column',
    marginTop: theme.spacing(2),
    flexGrow: 1,
  },
  bottomBlock: {
    padding: theme.spacing(0, 2),
    margin: theme.spacing(1, 0),
  },
}));

const useNavButtonStyles = makeStyles(theme => ({
  root: {
    width: '100%',
    padding: theme.spacing(1, 1.5, 1, 1.25),
    borderRadius: 8,
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'flex-start',
    color: theme.palette.text.secondary,
    '&:hover': {
      color: alpha(theme.palette.text.primary, 0.75),
    },
  },
  icon: {
    color: theme.palette.primary.main,
  },
  match: {
    color: `${theme.palette.text.primary} !important`,
    background: alpha(theme.palette.text.primary, 0.1),
  },
  label: {
    transitionDuration: '0.1s',
    marginLeft: theme.spacing(2),
    fontSize: 16,
    fontWeight: 500,
    fontFamily: 'Google Sans',
  },
}));

const useProfileButtonStyles = makeStyles(theme => ({
  root: {
    margin: theme.spacing(2),
    marginBottom: 0,
    display: 'flex',
    flexDirection: 'row',
    padding: theme.spacing(1, 1.2),
    textDecoration: 'none !important',
    background: alpha(theme.palette.text.primary, 0.075),
    borderRadius: 8,
    transitionDuration: '100ms',
    transitionTimingFunction: theme.transitions.easing.easeIn,
    alignItems: 'center',
    '&:hover': {
      background: alpha(theme.palette.text.primary, 0.1),
    },
  },
  avatar: {
    width: avatarWidth,
    height: avatarHeight,
    color: theme.palette.primary.main,
  },
  textHolder: {
    marginLeft: theme.spacing(1.5),
    display: 'flex',
    width: '100%',
    flexDirection: 'column',
    alignItems: 'flex-start',
    overflow: 'hidden',
  },
  username: {
    fontFamily: 'Google Sans',
    fontWeight: 'bold',
    fontSize: 16,
    color: theme.palette.text.primary,
    whiteSpace: 'nowrap',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    width: '100%',
    textAlign: 'start',
  },
}));

const isCurrent = (obj: TabObject, route: Route): boolean => {
  if (Array.isArray(obj.match)) {
    return obj.match.some(k => k === route.alias);
  }
  return obj.match === route.alias;
};

interface NavButtonProps extends TabObject {
  current: boolean;
}

const NavButton: React.FC<NavButtonProps> = ({ label, icon, to, current }) => {
  const classes = useNavButtonStyles();
  const history = useHistory();
  const handleClick = () => {
    const link = to();
    if (history.location.pathname !== link) {
      history.push(link);
    }
  };

  return (
    <ButtonBase
      className={classes.root + (current ? ` ${classes.match}` : '')}
      onClick={() => handleClick()}
    >
      <div className={classes.icon}>
        <>{icon}</>
      </div>
      <Typography className={classes.label} component="span">
        {label}
      </Typography>
    </ButtonBase>
  );
};

const ProfileButton = () => {
  const classes = useProfileButtonStyles();
  const { t } = useTranslation();

  const [isUserMenuOpen, setUserMenuOpen] = useState(false);
  const user = useSelector(state => state.auth.user);

  const shouldShowUser = useSelector(state => state.auth.isLoggedIn);
  const shouldShowLoadingSpinner = useSelector(state => state.auth.isLoading);

  const openUserMenu = () => setUserMenuOpen(true);

  if (shouldShowLoadingSpinner) {
    return (
      <div className={classes.root}>
        <div>
          <Skeleton variant="circle" className={classes.avatar} />
        </div>
        <div className={classes.textHolder}>
          <Skeleton width="75%" />
        </div>
      </div>
    );
  }

  return shouldShowUser && user ? (
    <>
      <ButtonBase className={classes.root} onClick={openUserMenu}>
        <UserAvatar alias={user.name} className={classes.avatar} src={null} />
        <div className={classes.textHolder}>
          <Typography className={classes.username}>{user.name}</Typography>
        </div>
      </ButtonBase>
      <UserMenu isOpen={isUserMenuOpen} setOpen={setUserMenuOpen} variant="modal" />
    </>
  ) : (
    <Link className={classes.root} to="/login">
      <div className={classes.avatar}>
        <Login set="light" size={Math.max(avatarWidth, avatarHeight)} />
      </div>
      <div className={classes.textHolder}>
        <Typography className={classes.username}>{t`pages.Login.login`}</Typography>
      </div>
    </Link>
  );
};

const SideNavigationDrawer = () => {
  const theme = useTheme();
  const route = useRoute();
  const classes = useStyles();

  const history = useHistory();
  const { t } = useTranslation();

  const languageSettings = useSelector(store => store.settings.language);

  const [navigationTabs, setNavigationTabs] = useState(
    makeNavigationTabs(28, 28, true)
  );

  useEffect(() => {
    setNavigationTabs(makeNavigationTabs(28, 28, true));
  }, [languageSettings]);

  const shouldShowDrawer = useMediaQuery(theme.breakpoints.up(MIDDLE_WIDTH), {
    noSsr: true,
  });

  // Do not render drawer on mobile and tablet
  if (!shouldShowDrawer) return null;

  const goHome = () => {
    window.scrollTo(0, 0);
    history.push('/');
  };

  return (
    <Drawer
      variant="permanent"
      anchor="left"
      className={classes.drawer}
      classes={{
        paper: classes.drawerPaper,
      }}
    >
      <div className={classes.drawerContainer}>
        <div className={classes.logoHolder}>
          <Typography onClick={() => goHome()} variant="h6" className={classes.logo}>
            {t`pages.app.appName`}
          </Typography>
        </div>
        <ProfileButton />
        <div className={classes.tabsHolder}>
          {navigationTabs.map((e, i) => (
            <NavButton current={isCurrent(e, route)} key={i} {...e} />
          ))}
        </div>
      </div>
    </Drawer>
  );
};

export default React.memo(SideNavigationDrawer);
