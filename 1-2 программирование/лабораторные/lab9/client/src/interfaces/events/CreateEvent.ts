import Event from './Event';
import Product from '../models/Product';

interface CreateEvent extends Event {
  messageType: 'CREATE';
  createdProduct: Product;
}

export default CreateEvent;
