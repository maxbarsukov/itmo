import i18n from 'config/i18n';

type OrganizationType = 'COMMERCIAL' | 'GOVERNMENT' | 'TRUST' | 'PRIVATE_LIMITED_COMPANY';

const t = i18n.t.bind(i18n);
export const getOrganizationTypes = () => {
  const organizationTypes: { [key in OrganizationType]: string } = {
    COMMERCIAL: t('pages.app.other.organizationTypes.COMMERCIAL'),
    GOVERNMENT: t('pages.app.other.organizationTypes.GOVERNMENT'),
    TRUST: t('pages.app.other.organizationTypes.TRUST'),
    PRIVATE_LIMITED_COMPANY: t('pages.app.other.organizationTypes.PRIVATE_LIMITED_COMPANY'),
  };
  return organizationTypes;
};

export default OrganizationType;
