import React from 'react';
import OutsidePage from 'components/blocks/OutsidePage';
import { makeStyles } from '@material-ui/core/styles';
import { LANGUAGES_INTERFACE, MIN_WIDTH } from 'config/constants';
import { FormControlLabel, Radio, RadioGroup, Typography, useTheme } from '@material-ui/core';
import { useDispatch, useSelector } from 'hooks';
import { useTranslation } from 'react-i18next';
import { setSettings } from 'store/settings';
import { useSnackbar } from 'notistack';
import dayjs from 'dayjs';

const useStyles = makeStyles(theme => ({
  root: {
    width: '100%',
    display: 'flex',
    flexDirection: 'column',
  },
  section: {
    backgroundColor: theme.palette.background.paper,
    marginTop: theme.spacing(1.5),
    [theme.breakpoints.up(MIN_WIDTH)]: {
      borderRadius: 8,
    },
    position: 'relative',
    overflow: 'hidden',
    padding: theme.spacing(1.8, 2),
  },
  sectionHeader: {
    fontSize: 13,
    color: theme.palette.text.hint,
    textTransform: 'uppercase',
    fontWeight: 500,
    lineHeight: 'normal',
    fontFamily: 'Google Sans',
    paddingBottom: theme.spacing(1),
  },
}));

const Language = () => {
  const theme = useTheme();
  const classes = useStyles();
  const languageSettings = useSelector(store => store.settings.language);
  const dispatch = useDispatch();

  const { enqueueSnackbar } = useSnackbar();
  const { t, i18n } = useTranslation();

  const setLanguageSettings = (value) => {
    dispatch(
      setSettings({
        language: value,
      })
    );
  };

  const handleInterfaceLanguageChange = (_event, value) => {
    setLanguageSettings(value);
    i18n.changeLanguage(value)
      .then(() => dayjs.locale(value))
      .catch(error => {
        console.error(error);
        enqueueSnackbar(t`pages.Settings.Language.cannotChange`, {
          variant: 'error',
          autoHideDuration: 4000,
        });
      });
  };

  return (
    <OutsidePage
      headerText={t`pages.app.routes.settingsLanguage`}
      disableShrinking
      backgroundColor={theme.palette.background.paper}
    >
      <div className={classes.root}>
        <div className={classes.section}>
          <Typography className={classes.sectionHeader}>
            {t`pages.Settings.Language.interface`}
          </Typography>
          <RadioGroup
            aria-label="language"
            name="language"
            value={languageSettings}
            onChange={handleInterfaceLanguageChange}
          >
            {LANGUAGES_INTERFACE.map(({ name, type }) => (
              <FormControlLabel
                value={type}
                key={type}
                control={<Radio color="primary" />}
                label={name}
              />
            ))}
          </RadioGroup>
        </div>
      </div>
    </OutsidePage>
  );
};

export default React.memo(Language);
