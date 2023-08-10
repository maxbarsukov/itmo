import React from 'react';
import { Toolbar, AppBar } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import getSecondaryAppBarColor from 'utils/getSecondaryAppBarColor';
import {
  MAX_WIDTH,
  APP_BAR_HEIGHT,
  DRAWER_WIDTH,
  FLOWS,
  MIDDLE_WIDTH,
  FlowObject,
  FlowAlias,
} from 'config/constants';
import { useTranslation } from 'react-i18next';

const useStyles = makeStyles(theme => ({
  root: {
    backgroundColor: getSecondaryAppBarColor(theme),
    color: theme.palette.text.primary,
    position: 'absolute',
    height: APP_BAR_HEIGHT,
    top: APP_BAR_HEIGHT,
    flexGrow: 1,
    zIndex: theme.zIndex.appBar,
    willChange: 'transform',
    [theme.breakpoints.up(MIDDLE_WIDTH)]: {
      paddingLeft: DRAWER_WIDTH,
      position: 'fixed',
      top: 0,
    },
  },
  toolbar: {
    minHeight: 'unset',
    height: APP_BAR_HEIGHT,
    padding: 0,
    maxWidth: MAX_WIDTH,
    width: '100%',
    flexDirection: 'column',
  },
  content: {
    height: '100%',
    display: 'flex',
    alignItems: 'center',
    width: '100%',
    overflow: 'auto',
  },
  link: {
    '&:first-child': {
      marginLeft: theme.spacing(1),
    },
    '&:last-child': {
      paddingRight: theme.spacing(2),
    },
    flexShrink: 0,
    textDecoration: 'none',
    fontWeight: 700,
    fontFamily: 'Google Sans',
    fontSize: 15,
    color: theme.palette.text.secondary,
    transitionDuration: `${theme.transitions.duration.shortest}ms`,
    transitionTimingFunction: theme.transitions.easing.easeInOut,
    paddingRight: theme.spacing(1),
    paddingLeft: theme.spacing(1),
    height: '100%',
    display: 'flex',
    alignItems: 'center',
    cursor: 'pointer',
    userSelect: 'none',
    '&:hover': {
      color: theme.palette.primary.main,
    },
  },
  selected: {
    color: theme.palette.text.primary,
    '&:hover': {
      color: theme.palette.text.primary,
    },
  },
}));

const FlowsBar: React.FC<{
  flow: FlowAlias;
  onClick: (e: FlowObject) => void;
}> = ({ flow, onClick }) => {
  const classes = useStyles();
  const { t } = useTranslation();

  const FlowItem: React.FC<{ data: FlowObject }> = ({ data }) => (
    <a
      className={[classes.link]
        .concat(flow == data.alias ? [classes.selected] : [])
        .join(' ')}
      onClick={() => onClick(data)}
    >
      {t(data.title)}
    </a>
  );

  return (
    <AppBar className={classes.root} elevation={0}>
      <Toolbar className={classes.toolbar}>
        <div className={classes.content}>
          {FLOWS.map(e => (
            <FlowItem key={e.alias} data={e} />
          ))}
        </div>
      </Toolbar>
    </AppBar>
  );
};

export default FlowsBar;
