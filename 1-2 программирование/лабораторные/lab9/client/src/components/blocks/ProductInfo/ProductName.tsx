import React from 'react';
import { IconButton, Typography } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';

import { useSelector } from 'hooks';
import { Delete } from 'react-iconly';

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    padding: theme.spacing(0, 2),
    margin: theme.spacing(1.5, 0),
    marginTop: theme.spacing(0.5),
  },
  container: {
    display: 'flex',
    flexGrow: 1,
  },
  icons: {
    display: 'flex',
    flexDirection: 'row',
    marginLeft: theme.spacing(1),
  },
  wrapper: {
    display: 'flex',
    flexDirection: 'column',
    marginLeft: theme.spacing(1),
  },
  name: {
    fontFamily: 'Google Sans',
    fontWeight: 500,
    fontSize: 17,
    color: theme.palette.text.primary,
    lineHeight: '20px',
  },
  id: {
    fontFamily: 'Google Sans',
    fontWeight: 500,
    fontSize: 15,
    color: theme.palette.primary.light,
    lineHeight: '18px',
    marginTop: theme.spacing(0.35),
  },
  logoutIcon: {
    color: theme.palette.error.main,
  },
}));

const ProductName: React.FC<{ productId: number; handleDelete: (productProduct) => void }> = ({
  productId, handleDelete,
}) => {
  const classes = useStyles();

  const products = useSelector(state => state.products.products);
  const product = products.filter(p => p.id === productId)[0];

  const onClick = () => {
    handleDelete(product);
  };

  return (
    <div className={classes.root}>
      <div className={classes.container}>
        <div className={classes.wrapper}>
          <Typography className={classes.name}>{product.name}</Typography>
          <Typography className={classes.id}>ID {product.id}</Typography>
        </div>
      </div>
      <div className={classes.icons}>
        <IconButton className={classes.logoutIcon} onClick={onClick}>
          <Delete set="light" size={24} />
        </IconButton>
      </div>
    </div>
  );
};

export default React.memo(ProductName);
