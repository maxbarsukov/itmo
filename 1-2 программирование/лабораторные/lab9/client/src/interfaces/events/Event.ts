import User from 'interfaces/models/User';
import MessageType from './MessageType';

interface Event {
  creator: User;
  messageType: MessageType;
  requestUuid: string;
}

export default Event;
