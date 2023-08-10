import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Button } from '@material-ui/core';
import { useTranslation } from 'react-i18next';

import Product from 'interfaces/models/Product';
import { useSelector } from 'hooks';

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

const EditButton = (
  { productId, handleUpdate }: { productId: number; handleUpdate: (product: Product) => void}
) => {
  const classes = useStyles();
  const { t } = useTranslation();
  const products = useSelector(state => state.products.products);

  const onClick = () => {
    const product = products.filter(p => p.id === productId)[0];
    handleUpdate(product);
  };

  return (
    <div className={classes.root}>
      <Button className={classes.button} variant="outlined" onClick={onClick}>
        {t`pages.app.ProfileInfo.update`}
      </Button>
    </div>
  );
};

export default React.memo(EditButton);
