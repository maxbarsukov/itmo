import * as React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Typography, useTheme } from '@material-ui/core';
import OutsidePage from 'components/blocks/OutsidePage';

import { Link } from 'react-router-dom';
import isMobile from 'is-mobile';
import { useTranslation } from 'react-i18next';

const chromeAddressBarHeight = 56;
const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    minHeight: 600,
    width: '100%',
    flexDirection: 'column',
    height: `calc(100vh - 96px - ${isMobile() ? chromeAddressBarHeight : '0'}px)`,
  },
  title: {
    fontFamily: 'Google Sans',
    fontWeight: 800,
    fontSize: 28,
  },
  text: {
    fontFamily: 'Google Sans',
    fontSize: 16,
  },
  link: {
    textDecoration: 'none',
    color: theme.palette.primary.main,
    '&:hover': {
      textDecoration: 'underline',
      color: theme.palette.primary.light,
    },
  },
  svg: {
    marginTop: theme.spacing(4),
    width: '100%',
    display: 'flex',
    justifyContent: 'center',
    '& svg': { maxWidth: 256, width: '100%', height: '100%' },
  },
}));

const NotFound = () => {
  const classes = useStyles();
  const theme = useTheme();
  const { t } = useTranslation();

  return (
    <OutsidePage
      backgroundColor={theme.palette.background.paper}
      headerText={t`pages.NotFound.notFound`}
      disableShrinking
    >
      <div className={classes.root}>
        <Typography className={classes.title}>404</Typography>
        <Typography className={classes.text}>{t`pages.NotFound.notFound`}</Typography>
        <Typography className={classes.text}>
          <Link to="/" className={classes.link}>
            {t`pages.NotFound.goHome`}
          </Link>
        </Typography>
      </div>
    </OutsidePage>
  );
};

export default React.memo(NotFound);
