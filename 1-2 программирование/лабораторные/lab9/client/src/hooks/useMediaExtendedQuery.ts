import { useMediaQuery, useTheme } from '@material-ui/core';
import { MIN_WIDTH } from 'config/constants';

const useMediaExtendedQuery = () => {
  const theme = useTheme();
  return useMediaQuery(theme.breakpoints.up(MIN_WIDTH), {
    noSsr: true,
  });
};

export default useMediaExtendedQuery;
