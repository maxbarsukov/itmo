import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { TransitionProps } from '@material-ui/core/transitions';

import {
  Backdrop,
  Button,
  CircularProgress,
  Grid,
  Dialog,
  DialogTitle,
  DialogContentText,
  DialogContent,
  DialogActions,
  Fade,
  IconButton,
  Typography,
} from '@material-ui/core';

import { Close } from '@material-ui/icons';
import { useTranslation } from 'react-i18next';
import { useDispatch, useSelector } from 'hooks';
import { deleteProduct } from 'store/products/actions';
import { useSnackbar } from 'notistack';

const useDialogStyles = makeStyles(theme => ({
  paper: {
    borderRadius: 12,
  },
  header: {
    height: 48,
  },
  headerIcon: {
    marginRight: 4,
    color: theme.palette.error.main,
  },
  headerText: {
    fontFamily: 'Google Sans',
    fontSize: 20,
    fontWeight: 500,
  },
  backdrop: {
    zIndex: 100,
  },
}));

const Transition = React.forwardRef(function Transition(
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  props: TransitionProps & { children?: React.ReactElement<any, any> },
  ref: React.Ref<unknown>
) {
  return <Fade ref={ref} {...props} />;
});

interface Props {
  productId: number;
  isOpen: boolean;
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
}

const Delete: React.FC<Props> = ({ productId, isOpen, setOpen }) => {
  const classesDialog = useDialogStyles();
  const dispatch = useDispatch();
  const { enqueueSnackbar } = useSnackbar();
  const { t } = useTranslation();

  const isFetching = useSelector(state => state.products.delete.fetching);

  const handleCancel = () => setOpen(false);
  const handleOk = () => {
    dispatch(
      deleteProduct(productId)
    ).then(() => {
      enqueueSnackbar(t`pages.Home.modals.Delete.success`, {
        variant: 'success',
        autoHideDuration: 2000,
      });
    }).catch(error => {
      enqueueSnackbar(t(`pages.Home.modals.Delete.errors.${error || 'ERROR'}`), {
        variant: 'error',
        autoHideDuration: 4000,
      });
    }).finally(() => setOpen(false));
  };

  return (
    <>
      <Dialog
        disableBackdropClick
        disableEscapeKeyDown
        maxWidth="xs"
        onClose={handleCancel}
        open={isOpen}
        aria-labelledby="delete-dialog-title"
        TransitionComponent={Transition}
        classes={{ paper: classesDialog.paper }}
      >
        <DialogTitle id="delete-dialog-title" className={classesDialog.header}>
          <Grid container direction="row" justifyContent="space-between" alignItems="center">
            <Typography className={classesDialog.headerText}>
              {t`pages.Home.modals.Delete.title`}
            </Typography>
            <IconButton className={classesDialog.headerIcon} onClick={handleCancel}>
              <Close width={24} height={24} />
            </IconButton>
          </Grid>
        </DialogTitle>

        <DialogContent dividers>
          <DialogContentText id="delete-dialog-description">
            {t`pages.Home.modals.Delete.description`}
          </DialogContentText>
        </DialogContent>

        <DialogActions>
          <Button onClick={handleCancel} color="primary" autoFocus>
            {t`pages.Home.modals.Delete.cancel`}
          </Button>
          <Button onClick={handleOk} color="primary">
            {t`pages.Home.modals.Delete.ok`}
          </Button>
        </DialogActions>
      </Dialog>
      {isFetching && <Backdrop
        className={classesDialog.backdrop}
        open={isFetching}
      >
        <CircularProgress color="primary" />
      </Backdrop>
      }
    </>
  );
};

export default React.memo(Delete);
