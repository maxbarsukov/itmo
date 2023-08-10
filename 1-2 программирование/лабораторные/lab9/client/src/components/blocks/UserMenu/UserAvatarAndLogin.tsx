import React from 'react';
import { IconButton, Typography } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';

import { Logout } from 'react-iconly';
import { useDispatch, useSelector } from 'hooks';
import { logout } from 'store/auth/actions';
import { useSnackbar } from 'notistack';

import UserAvatar from 'components/blocks/UserAvatar';
import LinkToOutsidePage from 'components/blocks/LinkToOutsidePage';
import { useTranslation } from 'react-i18next';

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    padding: theme.spacing(0, 2),
    margin: theme.spacing(1.5, 0),
    marginTop: theme.spacing(0.5),
  },
  avatarAndLogin: {
    display: 'flex',
    flexGrow: 1,
  },
  icons: {
    display: 'flex',
    flexDirection: 'row',
    marginLeft: theme.spacing(1),
  },
  link: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    textDecoration: 'none',
  },
  avatar: {
    borderRadius: '50%',
  },
  usernameAndAliasWrapper: {
    display: 'flex',
    flexDirection: 'column',
    marginLeft: theme.spacing(1),
  },
  username: {
    fontFamily: 'Google Sans',
    fontWeight: 500,
    fontSize: 17,
    color: theme.palette.text.primary,
    lineHeight: '20px',
  },
  alias: {
    fontFamily: 'Google Sans',
    fontWeight: 500,
    fontSize: 15,
    color: theme.palette.primary.light,
    lineHeight: '18px',
    marginTop: theme.spacing(0.35),
  },
  logoutIcon: {
    color: theme.palette.text.secondary,
  },
}));

const UserAvatarAndLogin: React.FC<{ handleClose: () => void }> = ({ handleClose }) => {
  const user = useSelector(store => store.auth.user);
  const classes = useStyles();
  const dispatch = useDispatch();
  const { enqueueSnackbar } = useSnackbar();
  const { t } = useTranslation();

  const logoutUser = () => {
    enqueueSnackbar(t`pages.app.UserMenu.youLoggedOut`, {
      variant: 'info',
      autoHideDuration: 3000,
    });
    handleClose();
    dispatch(logout());
  };

  return (
    <div className={classes.root}>
      <div className={classes.avatarAndLogin}>
        <LinkToOutsidePage to={`/user/${user?.id}`} className={classes.link} onClick={handleClose}>
          <UserAvatar src={null} alias={user?.name} className={classes.avatar} />
          <div className={classes.usernameAndAliasWrapper}>
            <Typography className={classes.username}>{user?.name}</Typography>
            <Typography className={classes.alias}>@{user?.name}</Typography>
          </div>
        </LinkToOutsidePage>
      </div>
      <div className={classes.icons}>
        <IconButton className={classes.logoutIcon} onClick={logoutUser}>
          <Logout set="light" size={24} />
        </IconButton>
      </div>
    </div>
  );
};

export default React.memo(UserAvatarAndLogin);
