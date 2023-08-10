import Organization from 'interfaces/models/Organization';

type OrganizationDto = Omit<Organization, 'id' | 'creator'>;

export default OrganizationDto;
