import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { alpha, Typography, useTheme } from '@material-ui/core';
import OutsidePage from 'components/blocks/OutsidePage';
import { MIN_WIDTH } from 'config/constants';
import LinkToOutsidePage from 'components/blocks/LinkToOutsidePage';
import { useTranslation } from 'react-i18next';
import isDarkTheme from 'utils/isDarkTheme';

import { Image, ChevronRight, Chat, Category } from 'react-iconly';

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    width: '100%',
    flexDirection: 'column',
    backgroundColor: theme.palette.background.paper,
    marginTop: theme.spacing(1.5),
    [theme.breakpoints.up(MIN_WIDTH)]: {
      padding: theme.spacing(1, 0),
      borderRadius: 8,
    },
  },
  link: {
    display: 'flex',
    padding: theme.spacing(1.5, 2),
    textDecoration: 'none',
    color: theme.palette.text.primary,
    alignItems: 'center',
    '-webkit-tap-highlight-color': 'transparent !important',
    '&:hover': {
      backgroundColor: alpha(theme.palette.common[isDarkTheme(theme) ? 'white' : 'black'], 0.05),
    },
    '&:active': {
      background: alpha(theme.palette.common[isDarkTheme(theme) ? 'white' : 'black'], 0.2),
    },
  },
  linkText: {
    marginLeft: theme.spacing(1.5),
    fontSize: 16,
    flexGrow: 1,
  },
}));

const Settings = () => {
  const classes = useStyles();
  const theme = useTheme();
  const { t } = useTranslation();

  const items = [
    {
      icon: Category,
      to: '/table',
      text: t`pages.Settings.subSettings.table`,
    },
    {
      icon: Image,
      to: '/appearance',
      text: t`pages.Settings.subSettings.appearance`,
    },
    {
      icon: Chat,
      to: '/language',
      text: t`pages.Settings.subSettings.language`,
    },
  ];

  return (
  <OutsidePage
    headerText={t`pages.Settings.settings`}
    disableShrinking
    backgroundColor={theme.palette.background.paper}
  >
    <div className={classes.root}>
      {items.map((e) => (
        <LinkToOutsidePage to={`/settings${e.to}`} key={e.to} className={classes.link}>
          <e.icon set="light" primaryColor={theme.palette.primary.main} size={28} />
          <Typography className={classes.linkText}>{e.text}</Typography>
          <ChevronRight set="light" primaryColor={theme.palette.text.hint} size={18} />
        </LinkToOutsidePage>
      ))}
    </div>
  </OutsidePage>
  );
};

export default React.memo(Settings);
