import User from 'interfaces/models/User';
import OrganizationType from 'interfaces/models/OrganizationType';

type Organization = {
  id: number;
  name: string;
  employeesCount: string;
  type: OrganizationType;
  street: string;
  zipCode?: string;
  creator?: User;
};

export default Organization;
