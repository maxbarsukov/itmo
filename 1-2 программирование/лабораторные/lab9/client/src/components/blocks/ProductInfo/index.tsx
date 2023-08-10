import React from 'react';
import { lighten, makeStyles } from '@material-ui/core/styles';

import {
  ClickAwayListener,
  Paper,
  Portal,
  Collapse,
} from '@material-ui/core';
import isDarkTheme from 'utils/isDarkTheme';

import ProductName from './ProductName';
import EditButton from './EditButton';
import Product from 'interfaces/models/Product';

const MODAL_MAX_WIDTH = 300;

const useModalStyles = makeStyles(theme => ({
  root: ({x, y}: {x: number; y: number}) => ({
    background: isDarkTheme(theme)
      ? lighten(theme.palette.background.paper, 0.05)
      : theme.palette.background.paper,
    display: 'flex',
    width: '100%',
    maxWidth: MODAL_MAX_WIDTH,
    borderRadius: 8,
    flexDirection: 'column',
    zIndex: theme.zIndex.modal,
    position: 'fixed',
    paddingTop: theme.spacing(1),
    top: `calc(${y}px - ${theme.spacing(2)}px)`,
    left: `calc(${x}px + ${theme.spacing(2.5)}px)`,
    boxShadow:
      '0px 3px 5px -1px rgb(0 0 0 / 12%), 0px 6px 10px 0px rgb(0 0 0 / 7%), 0px 1px 18px 0px rgb(0 0 0 / 4%) !important',
  }),
}));

interface Props {
  x: number;
  y: number;
  productId: number;
  isOpen: boolean;
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
  handleDelete: (product: Product) => void;
  handleUpdate: (product: Product) => void;
}

const ProductInfo: React.FC<Props> = ({
  x,
  y,
  productId,
  isOpen,
  setOpen,
  handleDelete,
  handleUpdate,
}) => {
  const classesModal = useModalStyles({ x, y });
  const handleClose = () => setOpen(false);

  return isOpen ? (
    <Collapse in={isOpen}>
      <Portal>
        <ClickAwayListener onClickAway={handleClose}>
          <div>
            <Paper className={classesModal.root}>
              <ProductName productId={productId} handleDelete={handleDelete} />
              <EditButton productId={productId} handleUpdate={handleUpdate}/>
            </Paper>
          </div>
        </ClickAwayListener>
      </Portal>
    </Collapse>
  ) : null;
};

export default React.memo(ProductInfo);
