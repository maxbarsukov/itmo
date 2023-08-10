import React, { useEffect, useRef, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import OutsidePage from 'components/blocks/OutsidePage';
import { useDispatch, useSelector } from 'hooks';
import { setSettings } from 'store/settings';
import isMobile from 'is-mobile';
import { CustomTheme } from 'interfaces/UserSettings';
import { useTranslation } from 'react-i18next';

import {
  BACKGROUND_COLORS_DEFAULT,
  BACKGROUND_COLORS_PAPER,
  THEMES,
  getThemeNames,
  THEME_PRIMARY_COLORS,
  THEME_TEXT_COLORS,
  THEME_TYPES,
} from 'config/themes';
import { MIN_WIDTH } from 'config/constants';

import {
  Radio,
  Typography,
  Grid,
  useTheme,
  ButtonBase,
  FormControl,
  darken,
  lighten,
  ListItem,
  ListItemText,
  Switch,
  alpha,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControlLabel,
  RadioGroup, GridSize,
} from '@material-ui/core';
import isDarkTheme from 'utils/isDarkTheme';

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    flexDirection: 'column',
    width: '100%',
    marginBottom: theme.spacing(2),
  },
  section: {
    backgroundColor: theme.palette.background.paper,
    marginTop: 12,
    paddingBottom: theme.spacing(1.5),
    [theme.breakpoints.up(MIN_WIDTH)]: {
      borderRadius: 8,
    },
  },
  sectionHeader: {
    fontSize: 13,
    color: theme.palette.text.hint,
    textTransform: 'uppercase',
    fontWeight: 500,
    lineHeight: 'normal',
    fontFamily: 'Google Sans',
    marginBottom: theme.spacing(1.5),
    paddingBottom: 0,
    padding: theme.spacing(1.5, 2),
  },
  singleRowGrid: {
    display: 'flex',
    width: '100%',
    [theme.breakpoints.down('xs')]: {
      display: 'none',
    },
  },
  oneByTwoGrid: {
    display: 'none',
    width: '100%',
    [theme.breakpoints.down('xs')]: {
      display: 'flex',
    },
  },
  previewContainer: {
    position: 'relative',
    background: lighten(theme.palette.background.default, isDarkTheme(theme) ? 0.01 : 0.3),
    [theme.breakpoints.up(MIN_WIDTH)]: {
      border: `1px solid ${alpha(theme.palette.divider, 0.03)}`,
      borderRadius: 8,
      marginTop: theme.spacing(1.5),
    },
  },
  leftItem: {
    [theme.breakpoints.up(MIN_WIDTH)]: {
      borderBottomLeftRadius: 8,
    },
  },
  rightItem: {
    [theme.breakpoints.up(MIN_WIDTH)]: {
      borderBottomRightRadius: 8,
    },
  },
  themeCardsContainer: {
    whiteSpace: 'nowrap',
    flexDirection: 'row',
    alignItems: 'baseline',
    display: 'flex',
    flexWrap: 'nowrap',
    // Promote the list into his own layer on Chrome. This cost memory but helps keeping high FPS.
    transform: 'translateZ(0)',
    overflowX: 'auto',
    overflowY: 'hidden',
    paddingTop: 1,
    // Do not change the scrollbar on mobile due to design flaws
    // If the -webkit-scrollbar is empty, any other scrollbar style will not be applied.
    '&::-webkit-scrollbar': !isMobile() && {
      background: isDarkTheme(theme)
        ? lighten(theme.palette.background.default, 0.03)
        : theme.palette.background.paper,
      border: `1px solid ${darken(theme.palette.background.paper, 0.05)}`,
      borderRadius: 4,
      height: 12,
    },
    '&::-webkit-scrollbar-thumb': {
      minHeight: 12,
      borderRadius: 4,
      background: isDarkTheme(theme)
        ? lighten(theme.palette.background.paper, 0.08)
        : darken(theme.palette.background.paper, 0.08),
      transition: '0.1s',
      '&:hover': {
        background: isDarkTheme(theme)
          ? lighten(theme.palette.background.paper, 0.1)
          : darken(theme.palette.background.paper, 0.1),
      },
      '&:active': {
        background: isDarkTheme(theme)
          ? lighten(theme.palette.background.paper, 0.2)
          : darken(theme.palette.background.paper, 0.2),
      },
    },
  },
}));

const usePaletteGridItemStyles = makeStyles(theme => ({
  gridItem: {
    position: 'relative',
    alignItems: 'baseline',
    justifyContent: 'start',
    width: '100%',
    '&::before': {
      content: '""',
      paddingBottom: (p: number) => `${p * 100}%`,
    },
  },
  floatText: {
    position: 'absolute',
    marginTop: 4,
    marginLeft: 8,
    fontSize: 14,
    fontWeight: 700,
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
    overflow: 'hidden',
    maxWidth: `calc(100% - ${theme.spacing(2)}px)`,
  },
  floatSubtext: {
    position: 'absolute',
    marginTop: 22,
    marginLeft: 8,
    fontSize: 14,
    fontWeight: 500,
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
    overflow: 'hidden',
    opacity: 0.8,
    maxWidth: `calc(100% - ${theme.spacing(2)}px)`,
  },
  paletteSymbolContainer: {
    height: 30,
    left: '50%',
    position: 'absolute',
    top: '50%',
    width: 30,
    transform: 'translate(-50%, -50%)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: 16,
    fontWeight: 700,
    background: theme.palette.getContrastText(theme.palette.primary.main),
    color: theme.palette.primary.main,
    borderRadius: '50%',
  },
}));

const useThemeCardStyles = makeStyles(theme => ({
  root: {
    display: 'inline-flex',
    flexDirection: 'column',
    marginLeft: theme.spacing(2),
    alignItems: 'center',
    justifyContent: 'center',
    position: 'relative',
    '&:last-child': {
      paddingRight: theme.spacing(2),
    },
  },
  border: {
    boxShadow: `0 0 0 1px ${theme.palette.divider}`,
  },
  box: {
    height: 128,
    width: 96,
    borderRadius: 12,
    alignItems: 'baseline',
  },
  item: {
    height: '50%',
  },
  type: {
    fontSize: 14,
    marginTop: theme.spacing(0.5),
    maxWidth: 100,
    whiteSpace: 'nowrap',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
  },
  radioHolder: {
    height: '50%',
    width: '100%',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  radio: {
    color: ({ color }: { color: string }) => color,
  },
  radioChecked: {
    color: theme.palette.primary.main,
  },
  themeLimitedLabel: {
    position: 'absolute',
    top: theme.spacing(1),
    background: ({ color }: { color: string }) => color,
    padding: '3px 6px',
    color: ({ color }: { color: string }) => theme.palette.getContrastText(color),
    textTransform: 'uppercase',
    borderRadius: 8,
    lineHeight: 'normal',
  },
}));

const makeCustomThemeFromThemeType = type => ({
  name: getThemeNames()[type],
  type,
  palette: {
    type: THEME_TYPES[type],
    primary: THEME_PRIMARY_COLORS[type],
    background: {
      paper: BACKGROUND_COLORS_PAPER[type],
      default: BACKGROUND_COLORS_DEFAULT[type],
    },
    text: THEME_TEXT_COLORS[type],
  },
});

const ThemeCard = ({ theme }: { theme: CustomTheme }) => {
  const paper = theme.palette.background.paper;
  const defaultColor = theme.palette.background.default;
  const primaryColor = theme.palette.primary.main;
  const textColor = theme.palette.text.primary;
  const dispatch = useDispatch();
  const classes = useThemeCardStyles({ color: textColor });
  const themeType = useSelector(state => state.settings.themeType);
  const isCurrent = themeType === theme.type;
  const ref = useRef<HTMLDivElement>();
  const changeTheme: React.MouseEventHandler<HTMLButtonElement> = _event => {
    if (!isCurrent) dispatch(setSettings({ themeType: theme.type, autoChangeTheme: false }));
  };

  // Scroll to the element on page first load
  useEffect(() => {
    if (ref.current && isCurrent) {
      ref.current.scrollIntoView({
        behavior: 'auto',
        block: 'end',
        inline: 'center',
      });
    }
  }, []);

  return (
    <div className={classes.root} ref={ref}>
      <Grid
        component={ButtonBase}
        onClick={changeTheme}
        container
        justify="center"
        className={`${classes.box} ${isCurrent ? classes.border : ''}`}
        style={{ background: paper }}
      >
        <Grid
          item
          xs={6}
          style={{ background: primaryColor, borderTopLeftRadius: 12 }}
          className={classes.item}
        />
        <Grid
          item
          xs={6}
          style={{ background: defaultColor, borderTopRightRadius: 12 }}
          className={classes.item}
        />
        <div className={classes.radioHolder}>
          <Radio
            disableRipple
            value={theme.type}
            color="primary"
            checked={isCurrent}
            classes={{
              colorPrimary: classes.radio,
            }}
          />
        </div>
      </Grid>
      <Typography className={classes.type}>{theme.name}</Typography>
    </div>
  );
};

const PaletteGridItem = ({
  width,
  color,
  text,
  withSymbol,
  height = 1,
  className = '',
}: {
  width: number;
  color: string;
  text: string;
  withSymbol?: boolean;
  height?: number;
  className?: string;
}) => {
  const theme = useTheme();
  const classes = usePaletteGridItemStyles(height);

  return (
    <Grid
      item
      component={ButtonBase}
      xs={width as boolean | GridSize}
      className={`${classes.gridItem} ${className}`}
      style={{
        background: color,
        color: theme.palette.getContrastText(color),
      }}
    >
      <Typography className={classes.floatText}>{text}</Typography>
      <Typography className={classes.floatSubtext}>{color}</Typography>
      {withSymbol && <span className={classes.paletteSymbolContainer}>P</span>}
    </Grid>
  );
};

const Switcher = ({
  onClick = null,
  primary,
  secondary,
  checked = false,
}) => {
  return (
    <ListItem button onClick={onClick}>
      <ListItemText primary={primary} secondary={secondary} />
      <Switch disableRipple checked={checked} color="primary" />
    </ListItem>
  );
};

export const OneByTwoGrid = ({ component: Item = PaletteGridItem }) => {
  const classes = useStyles();
  const theme = useTheme();

  return (
    <div className={classes.oneByTwoGrid}>
      <Grid item xs={6}>
        <Grid container direction="column">
          <Item
            width={12}
            color={theme.palette.primary.main}
            text="main"
            height={0.5}
            withSymbol
          />
          <Grid container direction="row">
            <Item
              width={6}
              height={1}
              color={theme.palette.primary.light}
              text="light"
            />
            <Item
              width={6}
              height={1}
              color={theme.palette.primary.dark}
              text="dark"
            />
          </Grid>
        </Grid>
      </Grid>
      <Grid item xs={6}>
        <Grid container direction="column">
          <Item
            width={12}
            height={0.5}
            color={theme.palette.background.paper}
            text="paper"
          />
          <Grid container direction="row">
            <Item
              width={6}
              height={1}
              color={theme.palette.background.default}
              text="default"
            />
            <Item
              width={6}
              height={1}
              color={theme.palette.text.primary}
              text="text"
            />
          </Grid>
        </Grid>
      </Grid>
    </div>
  );
};

export const SingleRowGrid = ({ component: Item = PaletteGridItem }) => {
  const theme = useTheme();
  const classes = useStyles();

  return (
    <Grid
      item
      xs={12}
      container
      direction="row"
      className={classes.singleRowGrid}
    >
      <Item
        width={2}
        height={1}
        color={theme.palette.primary.light}
        text="light"
        className={classes.leftItem}
      />
      <Item
        width={2}
        height={1}
        color={theme.palette.primary.main}
        text="main"
        withSymbol
      />
      <Item
        width={2}
        height={1}
        color={theme.palette.primary.dark}
        text="dark"
      />
      <Item
        width={2}
        height={1}
        color={theme.palette.background.default}
        text="default"
      />
      <Item
        width={2}
        height={1}
        color={theme.palette.background.paper}
        text="paper"
      />
      <Item
        width={2}
        height={1}
        color={theme.palette.text.primary}
        text="text"
        className={classes.rightItem}
      />
    </Grid>
  );
};

export interface ThemeSelectDialogProps {
  classes?: Record<'paper', string>;
  id: string;
  keepMounted?: boolean;
  value: string;
  open: boolean;
  onClose: (value?: string) => void;
}

const ThemeSelectDialog = (props: ThemeSelectDialogProps) => {
  const { onClose, value: valueProp, open, ...other } = props;
  const [value, setValue] = React.useState(valueProp);
  const radioGroupRef = React.useRef<HTMLElement>(null);

  const options = THEMES.map((e) => makeCustomThemeFromThemeType(e));
  const { t } = useTranslation();

  useEffect(() => {
    if (!open) {
      setValue(valueProp);
    }
  }, [valueProp, open]);

  const handleEntering = () => {
    if (radioGroupRef.current != null) {
      radioGroupRef.current.focus();
    }
  };

  const handleCancel = () => onClose();
  const handleOk = () => onClose(value);
  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setValue((event.target as HTMLInputElement).value);
  };

  return (
    <Dialog
      disableBackdropClick
      disableEscapeKeyDown
      maxWidth="xs"
      onEntering={handleEntering}
      aria-labelledby="theme-select-dialog-title"
      open={open}
      {...other}
    >
      <DialogTitle id="theme-select-dialog-title">{t`pages.Settings.Appearance.themeSelect`}</DialogTitle>
      <DialogContent dividers>
        <RadioGroup
          ref={radioGroupRef}
          aria-label="theme-select"
          name="theme-select"
          value={value}
          onChange={handleChange}
        >
          {options.map((option) => (
            <FormControlLabel
              value={option.type}
              key={option.type}
              control={<Radio color="primary" />}
              label={option.name}
            />
          ))}
        </RadioGroup>
      </DialogContent>
      <DialogActions>
        <Button autoFocus onClick={handleCancel} color="primary">
          {t`pages.Settings.Appearance.cancel`}
        </Button>
        <Button onClick={handleOk} color="primary">
          {t`pages.Settings.Appearance.ok`}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

const Appearance = () => {
  const classes = useStyles();
  const theme = useTheme();
  const dispatch = useDispatch();
  const { t } = useTranslation();

  const autoChangeTheme = useSelector((store) => store.settings.autoChangeTheme);
  const preferredLightTheme = useSelector(
    (store) => store.settings.preferredLightTheme
  );
  const preferredDarkTheme = useSelector(
    (store) => store.settings.preferredDarkTheme
  );
  const preferredLightThemeName = getThemeNames()[preferredLightTheme];
  const preferredDarkThemeName = getThemeNames()[preferredDarkTheme];
  const [
    isPreferredLightThemeDialogOpen,
    setPreferredLightThemeDialogOpen,
  ] = useState(false);
  const [
    isPreferredDarkThemeDialogOpen,
    setPreferredDarkThemeDialogOpen,
  ] = useState(false);

  const handlePreferredLightThemeDialogClose = (value?: string) => {
    setPreferredLightThemeDialogOpen(false);
    value &&
    dispatch(
      setSettings({
        preferredLightTheme: value,
      })
    );
  };
  const handlePreferredDarkThemeDialogClose = (value?: string) => {
    setPreferredDarkThemeDialogOpen(false);
    value &&
    dispatch(
      setSettings({
        preferredDarkTheme: value,
      })
    );
  };
  return (
    <OutsidePage
      headerText={t`pages.Settings.Appearance.appearance`}
      disableShrinking
      backgroundColor={theme.palette.background.paper}
    >
      <div className={classes.root}>
        <Grid container className={classes.previewContainer} direction="row">
          <Typography className={classes.sectionHeader}>
            {t`pages.Settings.Appearance.palette`}
          </Typography>
          <SingleRowGrid />
          <OneByTwoGrid />
        </Grid>
        <div
          className={classes.section}
          style={{ paddingBottom: null }}
        >
          <Typography className={classes.sectionHeader}>
            {t`pages.Settings.Appearance.themes`}
          </Typography>
          <FormControl
            component="fieldset"
            aria-label="theme-type"
            name="theme-type"
            className={classes.themeCardsContainer}
          >
            {THEMES.map((e, i) => (
              <ThemeCard theme={makeCustomThemeFromThemeType(e)} key={i} />
            ))}
          </FormControl>
        </div>
        <div className={classes.section}>
          <Typography
            className={classes.sectionHeader}
            style={{ marginBottom: 0 }}
          >
            {t`pages.Settings.settings`}
          </Typography>
          <Switcher
            primary={t`pages.Settings.Appearance.useSystemTheme`}
            secondary={t`pages.Settings.Appearance.useSystemThemeDescription`}
            checked={autoChangeTheme}
            onClick={() => {
              dispatch(
                setSettings({
                  autoChangeTheme: !autoChangeTheme,
                })
              );
            }}
          />
          <ListItem
            button
            disabled={!autoChangeTheme}
            onClick={() => setPreferredLightThemeDialogOpen(true)}
          >
            <ListItemText
              primary={t`pages.Settings.Appearance.preferredLightTheme`}
              secondary={preferredLightThemeName}
            />
          </ListItem>
          <ListItem
            button
            disabled={!autoChangeTheme}
            onClick={() => setPreferredDarkThemeDialogOpen(true)}
          >
            <ListItemText
              primary={t`pages.Settings.Appearance.preferredDarkTheme`}
              secondary={preferredDarkThemeName}
            />
          </ListItem>
          <ThemeSelectDialog
            keepMounted={false}
            id="preferred-light-theme-select-dialog"
            open={isPreferredLightThemeDialogOpen}
            onClose={handlePreferredLightThemeDialogClose}
            value={preferredLightTheme}
          />
          <ThemeSelectDialog
            keepMounted={false}
            id="preferred-dark-theme-select-dialog"
            open={isPreferredDarkThemeDialogOpen}
            onClose={handlePreferredDarkThemeDialogClose}
            value={preferredDarkTheme}
          />
        </div>
      </div>
    </OutsidePage>
  );
};

export default React.memo(Appearance);
