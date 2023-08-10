import React from 'react';
import { lighten, makeStyles } from '@material-ui/core/styles';
import { TransitionProps } from '@material-ui/core/transitions';

import {
  ClickAwayListener,
  Dialog,
  Fade,
  IconButton,
  Paper,
  Portal,
  Typography,
} from '@material-ui/core';
import isDarkTheme from 'utils/isDarkTheme';

import { Close } from '@material-ui/icons';
import { useTranslation } from 'react-i18next';

import { APP_BAR_HEIGHT, DRAWER_WIDTH } from 'config/constants';

import UserAvatarAndLogin from './UserAvatarAndLogin';
import ManageAccountButton from './ManageAccountButton';

const DIALOG_MAX_WIDTH = 440;
const MODAL_MAX_WIDTH = 300;

const useDialogStyles = makeStyles(_theme => ({
  paper: {
    display: 'flex',
    width: '100%',
    maxWidth: DIALOG_MAX_WIDTH,
    borderRadius: 12,
  },
  header: {
    display: 'flex',
    alignItems: 'center',
    height: 48,
  },
  headerIcon: {
    marginRight: 4,
    zIndex: 2,
  },
  headerText: {
    fontFamily: 'Google Sans',
    fontSize: 20,
    fontWeight: 500,
    width: '100%',
    textAlign: 'center',
    position: 'absolute',
    zIndex: 1,
  },
}));

const useModalStyles = makeStyles(theme => ({
  root: {
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
    top: `calc(${APP_BAR_HEIGHT}px + ${theme.spacing(1.5)}px)`,
    left: `calc(${DRAWER_WIDTH}px + ${theme.spacing(2)}px)`,
    boxShadow:
      '0px 3px 5px -1px rgb(0 0 0 / 12%), 0px 6px 10px 0px rgb(0 0 0 / 7%), 0px 1px 18px 0px rgb(0 0 0 / 4%) !important',
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
  variant: 'dialog' | 'modal';
}

const UserMenu: React.FC<Props> = ({ isOpen, setOpen, variant }) => {
  const classesDialog = useDialogStyles();
  const classesModal = useModalStyles();
  const handleClose = () => setOpen(false);
  const { t } = useTranslation();

  if (variant === 'dialog') {
    return (
      <Dialog
        onClose={handleClose}
        open={isOpen}
        TransitionComponent={Transition}
        classes={{ paper: classesDialog.paper }}
      >
        <div className={classesDialog.header}>
          <IconButton className={classesDialog.headerIcon} onClick={handleClose}>
            <Close width={24} height={24} />
          </IconButton>
          <Typography className={classesDialog.headerText}>
            {t`pages.app.UserMenu.account`}
          </Typography>
        </div>
        <UserAvatarAndLogin handleClose={handleClose} />
        <ManageAccountButton />
      </Dialog>
    );
  }
  if (variant === 'modal') {
    return isOpen ? (
      <Portal>
        <ClickAwayListener onClickAway={handleClose}>
          <div>
            <Paper className={classesModal.root}>
              <UserAvatarAndLogin handleClose={handleClose} />
              <ManageAccountButton />
            </Paper>
          </div>
        </ClickAwayListener>
      </Portal>
    ) : null;
  }
  throw new Error('Unknown variant for UserMenu ("dialog" | "modal")');
};

export default React.memo(UserMenu);
