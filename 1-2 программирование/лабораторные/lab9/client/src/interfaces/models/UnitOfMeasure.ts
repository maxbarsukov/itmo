import i18n from 'config/i18n';

type UnitOfMeasure = 'KILOGRAMS' | 'SQUARE_METERS' | 'LITERS' | 'MILLILITERS';

export const getUnitsOfMeasure = () => {
  const t = i18n.t.bind(i18n);
  const unitsOfMeasure: { [key in UnitOfMeasure]: string } = {
    KILOGRAMS: t('pages.app.other.unitsOfMeasure.KILOGRAMS'),
    SQUARE_METERS: t('pages.app.other.unitsOfMeasure.SQUARE_METERS'),
    LITERS: t('pages.app.other.unitsOfMeasure.LITERS'),
    MILLILITERS: t('pages.app.other.unitsOfMeasure.MILLILITERS'),
  };
  return unitsOfMeasure;
};

export default UnitOfMeasure;
