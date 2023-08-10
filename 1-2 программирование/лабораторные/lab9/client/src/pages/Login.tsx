import React, { useEffect, useState } from 'react';
import {
  Button,
  Typography,
  Grid,
  TextField,
  IconButton,
  InputAdornment,
  OutlinedInput,
  FormHelperText,
  useTheme,
} from '@material-ui/core';
import makeStyles from '@material-ui/core/styles/makeStyles';
import VisibilityIcon from '@material-ui/icons/Visibility';
import VisibilityOffIcon from '@material-ui/icons/VisibilityOff';

import { useDispatch, useSelector } from 'hooks';
import { login as loginUser, register as registerUser } from 'store/auth/actions';

import { useHistory } from 'react-router-dom';
import { useSnackbar } from 'notistack';
import OutsidePage from 'components/blocks/OutsidePage';
import LinkToOutsidePage from 'components/blocks/LinkToOutsidePage';
import Credentials from 'interfaces/dto/Credentials';
import { MIN_WIDTH } from 'config/constants';
import { useTranslation } from 'react-i18next';

const useStyles = makeStyles(theme => ({
  root: {
    width: '100%',
    display: 'flex',
    flexDirection: 'column',
  },
  paper: {
    marginTop: theme.spacing(2),
    marginBottom: theme.spacing(2),
    margin: '0 auto',
    maxWidth: 460,
    width: '100%',
    background: theme.palette.background.paper,
    [theme.breakpoints.up(MIN_WIDTH)]: {
      borderRadius: 8,
    },
  },
  grid: {
    width: '100%',
    height: '100%',
    padding: theme.spacing(2.5),
  },
  headerTitle: {
    fontFamily: 'Google Sans',
    fontWeight: 500,
    fontSize: 28,
  },
  input: {
    paddingTop: theme.spacing(2),
    paddingBottom: theme.spacing(1),
  },
  inputPassword: {
    paddingBottom: theme.spacing(1),
  },
  inputLabel: {
    color: theme.palette.text.secondary,
    marginBottom: theme.spacing(1),
    fontSize: 16,
  },
  signUp: {
    fontFamily: 'Google Sans',
    fontSize: 15,
    textDecoration: 'none',
    color: theme.palette.primary.light,
    '&:hover': {
      color: theme.palette.primary.dark,
    },
    lineHeight: '18px',
    marginTop: theme.spacing(0.35),
  },
  signUpText: {
    marginTop: theme.spacing(0.35),
    paddingTop: theme.spacing(1),
    paddingBottom: theme.spacing(0.5),
  },
  loginButton: {
    marginTop: theme.spacing(2),
    padding: theme.spacing(1.25, 2),
  },
}));

const isBlank = (str: string) => {
  return (!str || /^\s*$/.test(str));
};

const Login = ({ register = false }: { register?: boolean }) => {
  const auth = register ? 'Register' : 'Login';

  const classes = useStyles();
  const { enqueueSnackbar } = useSnackbar();
  const theme = useTheme();
  const [showPassword, setShowPassword] = useState(false);
  const [wasInput, setWasInput] = useState(false);

  const [inputs, setInputs] = useState({
    username: '',
    password: '',
  });
  const { username, password } = inputs;

  const isLoggedIn = useSelector(store => store.auth.isLoggedIn);
  const authDataFetchError = useSelector(store => store.auth.error);

  const isLoading = useSelector(store => store.auth.isLoading);

  const dispatch = useDispatch();
  const history = useHistory();
  const { t } = useTranslation();

  const handleInputsChange = e => {
    const { name, value } = e.target;
    setWasInput(true);
    setInputs(inputsBefore => ({ ...inputsBefore, [name]: value }));
  };

  const isUsernameError = () => {
    return wasInput && (isBlank(username) || username.length < 4 || username.length > 15);
  };

  const usernameErrorMessage = () => {
    if (username.length < 4 || username.length > 15) return t('pages.Auth.errors.username.from4to15');
    return t('pages.Auth.errors.username.blank');
  };

  const isPasswordError = () => {
    return wasInput && (isBlank(password) || password.length < 4);
  };

  const passwordErrorMessage = () => {
    if (password.length < 4) return t('pages.Auth.errors.password.from4');
    return t('pages.Auth.errors.password.blank');
  };

  const shouldDisableButton = isLoading || !wasInput || isUsernameError() || isPasswordError();

  const handleLoginSubmit = async e => {
    e.preventDefault();
    const credentials: Credentials = { name: username, password };
    dispatch((register ? registerUser : loginUser)(credentials));
  };

  useEffect(() => {
    if (isLoggedIn) {
      enqueueSnackbar(t(`pages.${auth}.success`), {
        variant: 'success',
        autoHideDuration: 3000,
      });
      history.push('/');
    } else if (authDataFetchError) {
      enqueueSnackbar(t(`pages.Auth.requestErrors.${authDataFetchError || 'ERROR'}`), {
        variant: 'error',
        autoHideDuration: 4000,
      });
    }
  }, [authDataFetchError, history, isLoggedIn]);

  return (
    <OutsidePage
      backgroundColor={theme.palette.background.paper}
      headerText={t(`pages.${auth}.auth`)}
      disableShrinking
    >
      <form className={classes.root} onSubmit={handleLoginSubmit}>
        <div className={classes.paper}>
          <Grid container direction="column" className={classes.grid}>
            <Grid item>
              <Typography className={classes.headerTitle}>{t(`pages.${auth}.label`)}</Typography>
            </Grid>
            <Grid item className={classes.input}>
              <Typography className={classes.inputLabel}>{t(`pages.${auth}.username`)}</Typography>
              <TextField
                onChange={handleInputsChange}
                autoFocus
                name="username"
                size="small"
                fullWidth
                variant="outlined"
                error={isUsernameError()}
                helperText={isUsernameError() ? usernameErrorMessage() : ' '}
              />
            </Grid>
            <Grid item className={classes.inputPassword}>
              <Typography className={classes.inputLabel}>{t(`pages.${auth}.password`)}</Typography>
              <OutlinedInput
                onChange={handleInputsChange}
                autoComplete="current-password"
                name="password"
                type={showPassword ? 'text' : 'password'}
                fullWidth
                margin="dense"
                error={isPasswordError()}
                endAdornment={
                  <InputAdornment position="end" style={{ marginRight: -8 }}>
                    <IconButton onClick={() => setShowPassword(prev => !prev)}>
                      {showPassword ? (
                        <VisibilityIcon color="disabled" />
                      ) : (
                        <VisibilityOffIcon color="disabled" />
                      )}
                    </IconButton>
                  </InputAdornment>
                }
              />
              {isPasswordError() && (
                <FormHelperText variant="outlined" error margin="dense">
                  {passwordErrorMessage()}
                </FormHelperText>
              )}
              {!isPasswordError() && (
                <FormHelperText variant="outlined" margin="dense">{' '}</FormHelperText>
              )}
            </Grid>
            <Grid item>
              <LinkToOutsidePage to={register ? '/login' : '/register'} className={classes.signUp}>
                <Typography className={classes.signUpText}>
                  {t(`pages.${auth}.${register ? 'login' : 'register'}`)}
                </Typography>
              </LinkToOutsidePage>
            </Grid>
            <Grid item>
              <Button
                disableElevation
                className={classes.loginButton}
                fullWidth
                variant="contained"
                color="primary"
                type="submit"
                disabled={shouldDisableButton}
              >
                {t(`pages.${auth}.submit`)}
              </Button>
            </Grid>
          </Grid>
        </div>
      </form>
    </OutsidePage>
  );
};

export default React.memo(Login);
