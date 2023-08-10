import Event from './Event';

interface ClearEvent extends Event {
  messageType: 'CLEAR';
  deletedIds: number[];
}

export default ClearEvent;
