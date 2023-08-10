import React from 'react';
import OutsidePage from 'components/blocks/OutsidePage';
import { makeStyles } from '@material-ui/core/styles';
import { MIN_WIDTH } from 'config/constants';
import { FormControlLabel, Radio, RadioGroup, Typography, useTheme } from '@material-ui/core';
import { useDispatch, useSelector } from 'hooks';
import { useTranslation } from 'react-i18next';
import { setSettings } from 'store/settings';

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

const Table = () => {
  const theme = useTheme();
  const classes = useStyles();
  const tableSettings = useSelector(store => store.settings.table);
  const dispatch = useDispatch();

  const { t } = useTranslation();

  const setTableSettings = (value) => {
    dispatch(
      setSettings({
        table: value,
      })
    );
  };

  const handleTableChange = (_event, value) => {
    setTableSettings(value);
  };

  return (
    <OutsidePage
      headerText={t`pages.app.routes.settingsTable`}
      disableShrinking
      backgroundColor={theme.palette.background.paper}
    >
      <div className={classes.root}>
        <div className={classes.section}>
          <Typography className={classes.sectionHeader}>
            {t`pages.Settings.Table.table`}
          </Typography>
          <RadioGroup
            aria-label="rable"
            name="table"
            value={tableSettings}
            onChange={handleTableChange}
          >
            <FormControlLabel
              value={'material'}
              key={'material'}
              control={<Radio color="primary" />}
              label={t`pages.Settings.Table.material`}
            />
            <FormControlLabel
              value={'custom'}
              key={'custom'}
              control={<Radio color="primary" />}
              label={t`pages.Settings.Table.custom`}
            />
          </RadioGroup>
        </div>
      </div>
    </OutsidePage>
  );
};

export default React.memo(Table);
