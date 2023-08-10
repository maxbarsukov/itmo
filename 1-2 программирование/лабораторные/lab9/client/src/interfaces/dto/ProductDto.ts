import Product from 'interfaces/models/Product';
import OrganizationDto from './OrganizationDto';

type ProductDto = Omit<Product, 'id' | 'creationDate' | 'creator' | 'manufacturer'>
  & { manufacturer: OrganizationDto };

export default ProductDto;
