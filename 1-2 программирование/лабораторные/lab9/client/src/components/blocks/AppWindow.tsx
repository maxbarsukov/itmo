import * as React from 'react';
import { useHistory } from 'react-router-dom';
import { makeStyles } from '@material-ui/core/styles';

import MainBlock from 'components/blocks/MainBlock';
import Sidebar from 'components/blocks/InfoSideBar';
import FlowsBar from 'components/blocks/FlowsBar';

import { APP_BAR_HEIGHT, FlowAlias } from 'config/constants';

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    flexDirection: 'column',
    maxWidth: '100%',
    width: '100%',
    marginTop: APP_BAR_HEIGHT + 3,
  },
  flexContainer: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'flex-start',
    width: '100%',
    marginTop: theme.spacing(1),
  },
}));

interface Props {
  children: unknown;
  flow: FlowAlias;
}

const AppWindow = ({ children, flow }: Props) => {
  const history = useHistory();
  const classes = useStyles();

  const onFlowsBarLinkClick = e => {
    if (e.alias !== flow) {
      history.push(`/${e.alias}`);
    }
  };

  return (
    <div className={classes.root}>
      <FlowsBar onClick={onFlowsBarLinkClick} flow={flow} />

      <div className={classes.flexContainer}>
        <MainBlock>{children}</MainBlock>
        <Sidebar />
      </div>
    </div>
  );
};

export default React.memo(AppWindow);
