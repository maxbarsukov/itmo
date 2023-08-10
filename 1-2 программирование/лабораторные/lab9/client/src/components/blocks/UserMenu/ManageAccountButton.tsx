import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Button } from '@material-ui/core';
import { useTranslation } from 'react-i18next';

import LinkToOutsidePage from 'components/blocks/LinkToOutsidePage';

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    justifyContent: 'center',
    marginBottom: theme.spacing(2),
  },
  button: {
    borderRadius: 50,
    textTransform: 'none',
    fontFamily: 'Google Sans',
    padding: '2px 15px',
    letterSpacing: '.04rem',
  },
}));

const ManageAccountButton = () => {
  const classes = useStyles();
  const { t } = useTranslation();

  return (
    <div className={classes.root}>
      <LinkToOutsidePage to={'/settings'}>
        <Button className={classes.button} variant="outlined">
          {t`pages.app.UserMenu.profileManagement`}
        </Button>
      </LinkToOutsidePage>
    </div>
  );
};

export default React.memo(ManageAccountButton);
