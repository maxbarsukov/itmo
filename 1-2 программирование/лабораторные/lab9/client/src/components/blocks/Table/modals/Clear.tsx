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
import { clearProducts } from 'store/products/actions';
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
  isOpen: boolean;
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
}

const Clear: React.FC<Props> = ({ isOpen, setOpen }) => {
  const classesDialog = useDialogStyles();
  const dispatch = useDispatch();
  const { enqueueSnackbar } = useSnackbar();
  const { t } = useTranslation();

  const userData = useSelector(state => state.auth.user);
  const isFetching = useSelector(state => state.products.clear.fetching);

  const handleCancel = () => setOpen(false);
  const handleOk = () => {
    dispatch(
      clearProducts(userData.id)
    ).then(() => {
      enqueueSnackbar(t`pages.Home.modals.Clear.success`, {
        variant: 'success',
        autoHideDuration: 2000,
      });
    }).catch(error => {
      enqueueSnackbar(t(`pages.Home.modals.Clear.errors.${error || 'ERROR'}`), {
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
        aria-labelledby="clear-dialog-title"
        TransitionComponent={Transition}
        classes={{ paper: classesDialog.paper }}
      >
        <DialogTitle id="clear-dialog-title" className={classesDialog.header}>
          <Grid container direction="row" justifyContent="space-between" alignItems="center">
            <Typography className={classesDialog.headerText}>
              {t`pages.Home.modals.Clear.title`}
            </Typography>
            <IconButton className={classesDialog.headerIcon} onClick={handleCancel}>
              <Close width={24} height={24} />
            </IconButton>
          </Grid>
        </DialogTitle>

        <DialogContent dividers>
          <DialogContentText id="clear-dialog-description">
            {t`pages.Home.modals.Clear.description`}
          </DialogContentText>
        </DialogContent>

        <DialogActions>
          <Button onClick={handleCancel} color="primary" autoFocus>
            {t`pages.Home.modals.Clear.cancel`}
          </Button>
          <Button onClick={handleOk} color="primary">
            {t`pages.Home.modals.Clear.ok`}
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

export default React.memo(Clear);
