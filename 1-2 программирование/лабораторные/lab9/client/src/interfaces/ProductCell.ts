import UnitOfMeasure from './models/UnitOfMeasure';
import Organization from './models/Organization';
import User from './models/User';
import OrganizationType from './models/OrganizationType';
import Product from './models/Product';

export default interface ProductCell {
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
  manufacturerId?: number;
  manufacturerName?: string;
  manufacturerEmployeesCount?: string;
  manufacturerType?: OrganizationType;
  manufacturerStreet?: string;
  manufacturerZipCode?: string;
  manufacturerCreator?: User;
  creatorId: number;
  creatorName: string;
}

export const productToCell = (product: Product): ProductCell => {
  return {
    ...product,
    manufacturerId: product?.manufacturer?.id,
    manufacturerName: product?.manufacturer?.name,
    manufacturerEmployeesCount: product?.manufacturer?.employeesCount,
    manufacturerType: product?.manufacturer?.type,
    manufacturerStreet: product?.manufacturer?.street,
    manufacturerZipCode: product?.manufacturer?.zipCode,
    manufacturerCreator: product?.manufacturer?.creator,
    creatorId: product?.creator?.id,
    creatorName: product?.creator?.name,
  };
};
