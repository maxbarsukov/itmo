import React, { useCallback, useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';

import { useHistory } from 'react-router';
import { History } from 'history';
import { useTranslation } from 'react-i18next';

import { AppBar, darken, lighten, Fade, IconButton, Toolbar, Typography } from '@material-ui/core';
import BackRoundedIcon from '@material-ui/icons/ArrowBackRounded';
import isDarkTheme from 'utils/isDarkTheme';

import { APP_BAR_HEIGHT, DRAWER_WIDTH, MAX_WIDTH, MIDDLE_WIDTH, MIN_WIDTH } from 'config/constants';

import useScrollTrigger from 'hooks/useScrollTrigger';
import useMediaExtendedQuery from 'hooks/useMediaExtendedQuery';

import getContrastPaperColor from 'utils/getContrastPaperColor';
import getSecondaryAppBarColor from 'utils/getSecondaryAppBarColor';
import getInvertedContrastPaperColor from 'utils/getInvertedContrastPaperColor';

interface StyleProps {
  isShrinked?: boolean;
  scrollProgress?: number;
  backgroundColor?: string;
  shrinkedBackgroundColor?: string;
  disableShrinking?: boolean;
}

const useStyles = makeStyles(
  theme => ({
    root: {
      background: theme.palette.background.default,
      minHeight: '100%',
      width: '100%',
    },
    children: {
      maxWidth: MAX_WIDTH,
      display: 'flex',
      marginRight: 'auto',
      marginLeft: 'auto',
      alignItems: 'flex-start',
      [theme.breakpoints.up(MIDDLE_WIDTH)]: {
        marginTop: APP_BAR_HEIGHT,
      },
    },
  }),
  { index: 1 }
);

const useAppBarStyles = makeStyles((theme) => ({
  toolbarIcons: {
    marginRight: theme.spacing(2),
    display: 'flex',
  },
  header: {
    backgroundColor: ({
      isShrinked,
      backgroundColor,
      shrinkedBackgroundColor,
    }: StyleProps) =>
      isShrinked
        ? shrinkedBackgroundColor || getInvertedContrastPaperColor(theme)
        : backgroundColor || getContrastPaperColor(theme),
    color: theme.palette.text.primary,
    position: 'fixed',
    willChange: 'transform',
    flexGrow: 1,
    boxShadow: '0 -1px 0 0 ' + theme.palette.background.default,
    transform: ({ isShrinked }: StyleProps) =>
      `translateZ(0) translateY(${isShrinked ? -16 : 0}px)`,
    transition: ({ disableShrinking }: StyleProps) =>
      disableShrinking ? 'none' : 'all .3s cubic-bezier(0.4, 0, 0.2, 1)',
    [theme.breakpoints.up(MIDDLE_WIDTH)]: {
      paddingLeft: DRAWER_WIDTH,
      backgroundColor: getSecondaryAppBarColor(theme) + ' !important',
    },
    [theme.breakpoints.between(MIN_WIDTH, MIDDLE_WIDTH)]: {
      backgroundColor: theme.palette.background.paper + ' !important',
    },
  },
  toolbar: {
    minHeight: 'unset',
    padding: 0,
    maxWidth: '100%',
    width: '100%',
    '&::before': {
      position: 'absolute',
      width: ({ scrollProgress }: StyleProps) => scrollProgress * 100 + '%',
      background:
        theme.palette.type === 'dark'
          ? 'rgba(255, 255, 255, .1)'
          : 'rgba(0, 0, 0, .1)',
      height: '100%',
      content: '""',
      transform: 'translateZ(0)',
    },
  },
  headerTitle: {
    fontFamily: 'Google Sans',
    fontWeight: 500,
    color: theme.palette.text.primary,
    fontSize: 20,
    letterHeight: '1.6',
    whiteSpace: 'nowrap',
    overflow: 'hidden',
    zIndex: 1000,
    textOverflow: 'ellipsis',
    marginRight: theme.spacing(2),
    [theme.breakpoints.up(MIDDLE_WIDTH)]: {
      marginLeft: theme.spacing(2),
    },
  },
  headerIcon: {
    marginLeft: 4,
    color: theme.palette.text.primary,
    [theme.breakpoints.up(MIDDLE_WIDTH)]: {
      display: 'none',
    },
  },
  marginContainer: {
    display: 'flex',
    width: '100%',
    flexDirection: 'column',
  },
  content: {
    display: 'flex',
    flexDirection: 'row',
    width: '100%',
    alignItems: 'center',
    transform: 'translateZ(0)',
    height: APP_BAR_HEIGHT,
    flexGrow: 1,
  },
  shrinkedHeaderTitle: {
    fontFamily: 'Google Sans',
    fontWeight: 500,
    color: theme.palette.text.secondary,
    fontSize: 16,
    transform: 'translateX(16px) translateY(8px)',
    letterHeight: '1.6',
    whiteSpace: 'nowrap',
    overflow: 'hidden',
    zIndex: 1000,
    maxWidth: 'calc(100% - 32px)',
    position: 'absolute',
    textOverflow: 'ellipsis',
  },
  backButtonDesktop: {
    width: DRAWER_WIDTH,
    height: APP_BAR_HEIGHT,
    display: 'none',
    position: 'fixed',
    top: 0,
    left: 0,
    zIndex: theme.zIndex.drawer + 1,
    background: theme.palette.background.paper,
    cursor: 'pointer',
    '&:hover': {
      background: isDarkTheme(theme)
        ? lighten(theme.palette.background.paper, 0.05)
        : darken(theme.palette.background.paper, 0.05),
    },
    [theme.breakpoints.up(MIDDLE_WIDTH)]: {
      display: 'flex',
    },
  },
  backButtonDesktopWrapper: {
    alignItems: 'center',
    justifyContent: 'flex-start',
    display: 'flex',
    flexDirection: 'row',
    padding: theme.spacing(0, 2),
    width: '100%',
  },
  backButtonDesktopText: {
    marginLeft: theme.spacing(2),
    fontFamily: 'Google Sans',
    fontSize: 20,
    fontWeight: 500,
  },
}));

interface Props {
  children: unknown;
  headerText: unknown;
  hidePositionBar: boolean;
  shrinkedHeaderText: unknown;
  shrinkedBackgroundColor: string;
  disableShrinking: boolean;
  backgroundColor: string;
  toolbarIcons: JSX.Element;
  onBackClick: (backLinkFunction: (history: History) => void) => unknown;
  scrollElement?: HTMLElement;
}

const ShrinkedContentUnmemoized = ({ isShrinked, shrinkedHeaderText }) => {
  const classes = useAppBarStyles({});
  return (
    <Fade in={isShrinked} unmountOnExit mountOnEnter>
      <Typography className={classes.shrinkedHeaderTitle}>{shrinkedHeaderText}</Typography>
    </Fade>
  );
};
const ShrinkedContent = React.memo(ShrinkedContentUnmemoized);

const backLinkFunction = (history: History) => {
  const goHome = () => history.push('/');
  if (history.length > 1) {
    history.goBack();
  } else goHome();
};

const UnshrinkedContentUnmemoized = ({
  headerText,
  onBackClick,
  isShrinked,
  headerTextUpdated,
}) => {
  const classes = useAppBarStyles({});
  const history = useHistory();
  const text = <Typography className={classes.headerTitle}>{headerText}</Typography>;

  const onClick = () => {
    if (onBackClick) onBackClick(backLinkFunction);
    else backLinkFunction(history);
  };

  return (
    <Fade in={!isShrinked} appear={false}>
      <div className={classes.content} style={{ overflow: 'hidden' }}>
        <IconButton
          disableRipple={isShrinked}
          className={classes.headerIcon}
          onClick={() => (isShrinked ? {} : onClick())}
        >
          <BackRoundedIcon />
        </IconButton>
        {headerText && headerTextUpdated ? <Fade in>{text}</Fade> : text}
      </div>
    </Fade>
  );
};
const UnshrinkedContent = React.memo(UnshrinkedContentUnmemoized);

const ToolbarIconsWrapperUnmemoized = ({ isShrinked, children }) => {
  const classes = useAppBarStyles({});
  return (
    <Fade in={!isShrinked}>
      <div className={classes.toolbarIcons}>{children}</div>
    </Fade>
  );
};
const ToolbarIconsWrapper = React.memo(ToolbarIconsWrapperUnmemoized);

const NavBarUnmemoized = ({
  headerText,
  shrinkedHeaderText,
  hidePositionBar = false,
  shrinkedBackgroundColor,
  backgroundColor,
  onBackClick,
  disableShrinking,
  toolbarIcons,
  scrollElement,
}: Partial<Props>) => {
  const isShrinkedTrigger = useScrollTrigger({
    threshold: 48,
    triggerValue: false,
  });
  const [headerTextUpdated, setHeaderTextUpdated] = useState(false);
  const [scrollProgress, setScrollProgress] = useState(0);
  const isExtended = useMediaExtendedQuery();
  const history = useHistory();
  const { t } = useTranslation();

  const isShrinked = disableShrinking || isExtended ? false : isShrinkedTrigger;
  const classes = useAppBarStyles({
    isShrinked,
    scrollProgress,
    backgroundColor,
    shrinkedBackgroundColor,
    disableShrinking,
  });
  const scrollCallback = useCallback(
    () =>
      requestAnimationFrame(() => {
        const getElementScroll = el => {
          const docElement = document.documentElement;
          const docBody = document.body;
          const element = el || docElement || docBody;
          return (docElement.scrollTop || docBody.scrollTop) / (element.offsetHeight - 200);
        };
        const newScrollProgress = getElementScroll(scrollElement);
        setScrollProgress(newScrollProgress);
      }),
    [scrollElement]
  );

  useEffect(() => {
    if (!hidePositionBar && !disableShrinking) {
      // passive: true enhances scrolling experience
      window.addEventListener('scroll', scrollCallback, { passive: true });
      return () => window.removeEventListener('scroll', scrollCallback);
    }
    return () => null;
  }, [hidePositionBar, disableShrinking, scrollElement]);

  // Remove fade effect on a title if it was set by default
  // If the title is being fetched (ex. Post),
  // we need to change the state and render Fade component
  useEffect(() => {
    if (!headerTextUpdated) {
      setHeaderTextUpdated(!headerText);
    }
  }, [headerText]);

  const onBackButtonClick = () => {
    if (onBackClick) onBackClick(backLinkFunction);
    else backLinkFunction(history);
  };

  return (
    <>
      <div onClick={() => onBackButtonClick()} className={classes.backButtonDesktop}>
        <div className={classes.backButtonDesktopWrapper}>
          <BackRoundedIcon />
          <Typography className={classes.backButtonDesktopText}>{t`pages.app.goBack`}</Typography>
        </div>
      </div>
      <AppBar className={classes.header} elevation={0}>
        <Toolbar className={classes.toolbar}>
          <div className={classes.marginContainer}>
            <div className={classes.content}>
              <UnshrinkedContent
                isShrinked={isShrinked}
                headerText={headerText}
                onBackClick={onBackClick}
                headerTextUpdated={headerTextUpdated}
              />
              <ShrinkedContent
                isShrinked={isShrinked}
                shrinkedHeaderText={shrinkedHeaderText || headerText}
              />
              <ToolbarIconsWrapper isShrinked={isShrinked}>{toolbarIcons}</ToolbarIconsWrapper>
            </div>
          </div>
        </Toolbar>
      </AppBar>
    </>
  );
};
const NavBar = React.memo(NavBarUnmemoized);

const OutsidePage: React.FC<Partial<Props>> = ({ children, ...props }) => {
  const classes = useStyles();

  return (
    <div className={classes.root}>
      <NavBar {...props} />
      <div className={classes.children}>
        <>{children}</>
      </div>
    </div>
  );
};

export default React.memo(OutsidePage);
