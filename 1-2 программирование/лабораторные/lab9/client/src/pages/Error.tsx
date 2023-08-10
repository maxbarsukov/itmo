import React from 'react';
import { Typography, darken } from '@material-ui/core';
import makeStyles from '@material-ui/core/styles/makeStyles';

import { APP_BAR_HEIGHT, chromeAddressBarHeight, MIN_WIDTH } from 'config/constants';
import getContrastPaperColor from 'utils/getContrastPaperColor';

import { useTranslation } from 'react-i18next';
import isMobile from 'is-mobile';
import SyntaxHighlighter from 'react-syntax-highlighter';
import { atomOneLight } from 'react-syntax-highlighter/dist/esm/styles/hljs';

const useStyles = makeStyles(theme => ({
  svg: {
    display: 'flex',
    width: '80%',
    maxWidth: 500,
  },
  root: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    height: `calc(100vh - ${isMobile() ? chromeAddressBarHeight : 0}px - ${APP_BAR_HEIGHT}px)`,
    padding: theme.spacing(2),
  },
  title: {
    fontFamily: 'Google Sans',
    fontWeight: 500,
    fontSize: 18,
    marginTop: theme.spacing(3),
  },
  code: {
    width: '100%',
    maxWidth: 700,
    marginTop: theme.spacing(2),
    '& pre': {
      marginTop: '0 !important',
    },
  },
  text: {
    fontFamily: 'Google Sans',
    fontWeight: 400,
    fontSize: 16,
    marginTop: theme.spacing(1),
  },
  link: {
    color: theme.palette.primary.main,
    textDecoration: 'none',
    '&:hover': {
      textDecoration: 'underline',
      color: theme.palette.primary.light,
    },
  },
  syntaxHighlighter: {
    margin: 0,
    marginTop: theme.spacing(3),
    display: 'block',
    tabSize: 4,
    overflow: 'auto',
    border: `1px solid ${theme.palette.divider}`,
    borderRadius: 4,
    padding: `${theme.spacing(2)}px !important`,
    color: `${theme.palette.text.primary} !important`,
    boxSizing: 'border-box',
    '-moz-tab-size': 4,
    whiteSpace: 'pre',
    wordBreak: 'normal',
    wordSpacing: 'normal',
    wordWrap: 'normal',
    [theme.breakpoints.up(MIN_WIDTH)]: {
      backgroundColor: `${getContrastPaperColor(theme)} !important`,
    },
    [theme.breakpoints.down(MIN_WIDTH)]: {
      backgroundColor: `${getContrastPaperColor(theme)} !important`,
    },
    '& code': {
      whiteSpace: 'pre',
      wordBreak: 'normal',
      wordSpacing: 'normal',
      wordWrap: 'normal',
      background: 'none !important',
      padding: 0,
      '-moz-tab-size': 4,
      tabSize: 4,
      fontSize: 14,
      fontFamily: 'Menlo,Monaco,Consolas,Courier New,Courier,monospace',
    },
    '&::-webkit-scrollbar': {
      height: 12,
      background: theme.palette.background.default,
      borderRadius: 2,
    },
    '&::-webkit-scrollbar-thumb': {
      background: darken(theme.palette.background.paper, 0.08),
      borderRadius: 2,
      transition: '0.1s',
      '&:hover': {
        background: darken(theme.palette.background.paper, 0.1),
      },
      '&:active': {
        background: darken(theme.palette.background.paper, 0.2),
      },
    },
  },
}));

const Error: React.FC<{
  error: Error;
  componentStack: string;
  eventId: string;
  resetError(): void;
}> = ({ error, componentStack }) => {
  const classes = useStyles();
  const { t } = useTranslation();

  return (
    <div className={classes.root}>
      <Typography className={classes.title}>{t`pages.Error.errorOccurred`}</Typography>
      <Typography className={classes.text}>
        <a className={classes.link} href="/">{t`pages.Error.clickHere`}</a>
        {t`pages.Error.goHome`}
      </Typography>
      <div className={classes.code}>
        <SyntaxHighlighter
          style={atomOneLight}
          language={'javascript'}
          showLineNumbers={false}
          className={classes.syntaxHighlighter}
        >
          {error?.toString() || t`pages.Error.error`}
          {componentStack}
        </SyntaxHighlighter>
      </div>
    </div>
  );
};

export default React.memo(Error);
