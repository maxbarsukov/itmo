import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';

import translation_en from 'locales/en/translation.json';
import translation_ru from 'locales/ru/translation.json';
import translation_is from 'locales/is/translation.json';
import translation_sv from 'locales/sv/translation.json';

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    fallbackLng: 'ru',
    debug: process.env.NODE_ENV === 'development',
    interpolation: {
      escapeValue: false,
    },
    defaultNS: 'translation',
    ns: ['translation'],
    resources: {
      en: { translation: translation_en },
      ru: { translation: translation_ru },
      is: { translation: translation_is },
      sv: { translation: translation_sv },
    },
  });

export default i18n;
