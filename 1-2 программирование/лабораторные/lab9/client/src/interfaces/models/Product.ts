import UnitOfMeasure from 'interfaces/models/UnitOfMeasure';
import User from 'interfaces/models/User';
import Organization from 'interfaces/models/Organization';

type Product = {
  id: number;
  name: string;
  x: number;
  y: number;
  creationDate: string;
  price: number;
  partNumber?: string;
  unitOfMeasure?: UnitOfMeasure;
  manufacturer?: Organization;
  creator?: User;
};

export default Product;
