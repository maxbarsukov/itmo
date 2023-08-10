import {
  createEvent,
  updateEvent,
  deleteEvent,
  clearEvent,
} from 'store/products';
import { addRequest } from 'store/requests';

import Event, { CreateEvent, UpdateEvent, DeleteEvent, ClearEvent } from 'interfaces/events';

export const createProduct = (event: Event) => dispatch => {
  dispatch(addRequest(event.requestUuid));
  dispatch(createEvent(event as CreateEvent));
  return Promise.resolve();
};

export const updateProduct = (event: Event) => dispatch => {
  dispatch(addRequest(event.requestUuid));
  dispatch(updateEvent(event as UpdateEvent));
  return Promise.resolve();
};

export const deleteProduct = (event: Event) => dispatch => {
  dispatch(addRequest(event.requestUuid));
  dispatch(deleteEvent(event as DeleteEvent));
  return Promise.resolve();
};

export const clearProduct = (event: Event) => dispatch => {
  dispatch(addRequest(event.requestUuid));
  dispatch(clearEvent(event as ClearEvent));
  return Promise.resolve();
};
