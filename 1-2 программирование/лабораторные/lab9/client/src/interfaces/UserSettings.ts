import { Language } from 'interfaces';
import { PaletteType } from 'config/themes';

export interface CustomTheme {
  name: string;
  type: string;
  palette: {
    type: 'dark' | 'light';
    primary: {
      main: string;
      light: string;
      dark: string;
    };
    background: {
      paper: string;
      default: string;
    };
    text: {
      primary: string;
      secondary: string;
      disabled: string;
      hint: string;
    };
  };
}

export type TableType = 'custom' | 'material';

export default interface UserSettings {
  autoChangeTheme: boolean;
  preferredLightTheme: PaletteType | string;
  preferredDarkTheme: PaletteType | string;
  themeType: PaletteType | string;
  language: Language;
  table: TableType;
}
