const config = {
  verbose: true,
  testEnvironment: 'node',
  setupFilesAfterEnv: ['jest-extended'],
  coverageDirectory: './coverage',
  collectCoverage: true,
  collectCoverageFrom: [
    'src/**/*.{js,jsx,ts,tsx}',
    '!src/**/*/*.d.ts',
    '!src/reportWebVitals.{js,ts}',
    '!src/**/*/types.{ts,js}',
    '!src/index.{js,jsx,ts,tsx}',
  ],
};

module.exports = config;
