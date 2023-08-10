import Event from './Event';

interface DeleteEvent extends Event {
  messageType: 'DELETE';
  deletedId: number;
}

export default DeleteEvent;
