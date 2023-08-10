import * as React from 'react';
import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

import { makeStyles } from '@material-ui/core/styles';

import ErrorComponent from 'components/blocks/Error';
import MaterialTable from 'components/blocks/Table/MaterialTable';
import Table from 'components/blocks/Table';
import Visualizer from 'pages/Home/Visualizer';

import AppWindow from 'components/blocks/AppWindow';
import { FlowAlias } from 'config/constants';

import { useDispatch, useSelector } from 'hooks';
import { getAllProducts } from 'store/products/actions';
import {
  createProduct,
  updateProduct,
  deleteProduct,
  clearProduct,
} from 'store/products/events';
import Event, { CreateEvent, UpdateEvent, DeleteEvent } from 'interfaces/events';

import { Box, CircularProgress } from '@material-ui/core';
import { useInterval } from 'usehooks-ts';
import useWebSocket from 'react-use-websocket';
import { useSnackbar } from 'notistack';
import { useTranslation } from 'react-i18next';

const useStyles = makeStyles(theme => ({
  root: {
    background: theme.palette.background.default,
    padding: 0,
    width: '100%',
  },
  loading: {
    minWidth: '10vh',
    minHeight: '10vh',
  },
}));

const Home = ({ variant = 'home'}: { variant: FlowAlias }) => {
  const location = useLocation();
  const dispatch = useDispatch();
  const tableSettings = useSelector(store => store.settings.table);

  const classes = useStyles();

  const isFetched = useSelector(state => state.products.getAll.fetched);
  const isFetching = useSelector(state => state.products.getAll.fetching);
  const fetchError = useSelector(state => state.products.getAll.error);

  const userData = useSelector(state => state.auth.user);
  const products = useSelector(state => state.products.products);
  const [isFetchingNow, setIsFetchingNow] = useState<boolean>(false);
  const [isFirstFetch, setIsFirstFetch] = useState<boolean>(true);
  const { enqueueSnackbar } = useSnackbar();
  const { t } = useTranslation();

  const fetch = () => {
    setIsFetchingNow(true);
    dispatch(
      getAllProducts()
    ).then(() => {
      setIsFetchingNow(false);
      setIsFirstFetch(false);
    });
  };

  const handleEvent = (event: Event) => {
    const handlers = {
      'CREATE': createProduct,
      'UPDATE': updateProduct,
      'DELETE': deleteProduct,
      'ClEAR': clearProduct,
    };
    dispatch(handlers[event.messageType](event))
      .then(() => {
        const username = event.creator.name;
        const notifications = {
          'CREATE': t(
            'pages.Home.Events.CREATE',
            { username, id: (event as CreateEvent)?.createdProduct?.id || 1 }
          ),
          'UPDATE': t(
            'pages.Home.Events.UPDATE',
            { username, id: (event as UpdateEvent)?.updatedProduct?.id || 1 }
          ),
          'DELETE': t(
            'pages.Home.Events.DELETE',
            { username, id: (event as DeleteEvent)?.deletedId || 1 }
          ),
          'CLEAR': t('pages.Home.Events.CLEAR', { username }),
        };
        if (userData?.id !== event.creator.id) {
          enqueueSnackbar(notifications[event.messageType], {
            variant: 'info', autoHideDuration: 2000,
          });
        }
      });
    return event;
  };

  useEffect(() => {
    fetch();
  }, [location.pathname, location.search]);

  const supportsWebSockets = 'WebSocket' in window || 'MozWebSocket' in window;
  const WS_URL = 'ws://localhost:8081';

  if (!supportsWebSockets) {
    useInterval(
      () => {
        fetch();
      },
      !isFetchingNow ? 5000 : null
    );
  } else {
    const uuids = useSelector(state => state.requests.uuids);

    useWebSocket(WS_URL, {
      share: true,
      filter: () => false,
      shouldReconnect: () => true,
      onOpen: () => console.log('> WebSocket connection established.'),
      onClose: () => console.log('> WebSocket connection closed.'),
      onError: () => console.log('> WebSocket error.'),
      onMessage: (evt) => {
        console.log('> WebSocket: new message.');
        const event: Event = JSON.parse(evt.data);
        if (uuids.includes(event.requestUuid)) {
          console.log('> WebSocket: event by current user. Ignored.');
          return;
        }

        console.log(`> WebSocket: handling event ${event.messageType} by ${event.creator.id}.`);
        handleEvent(event);
      },
    });
  }

  return (
    <AppWindow flow={variant}>
      <div className={classes.root}>
        {isFetching && isFirstFetch && (
          <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            minHeight="50vh"
          >
            <CircularProgress className={classes.loading} color="primary" />
          </Box>
        )}
        {(isFetched || !isFirstFetch) && !fetchError && products && (
          <>
            {variant === 'home' && (tableSettings === 'material'
              ? <MaterialTable data={products} />
              : <Table data={products} />
            )}
            {variant === 'chart' && (
              <Visualizer data={products} />
            )}
          </>
        )}
        {fetchError && <ErrorComponent code={500} message={fetchError} />}
      </div>
    </AppWindow>
  );
};

export default React.memo(Home);
