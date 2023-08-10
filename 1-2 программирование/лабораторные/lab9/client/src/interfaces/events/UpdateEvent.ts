import Event from './Event';
import Product from '../models/Product';

interface UpdateEvent extends Event {
  messageType: 'UPDATE';
  updatedProduct: Product;
}

export default UpdateEvent;
