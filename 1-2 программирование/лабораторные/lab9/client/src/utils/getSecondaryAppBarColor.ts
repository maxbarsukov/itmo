import { darken, Theme } from '@material-ui/core';
import isDarkTheme from './isDarkTheme';

export default (theme: Theme) => {
  return darken(theme.palette.background.paper, isDarkTheme(theme) ? 0.175 : 0.01);
};
