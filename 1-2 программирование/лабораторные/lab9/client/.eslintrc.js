module.exports = {
  parser: '@typescript-eslint/parser',
  extends: [
    'eslint:recommended',
    'plugin:react/recommended',
    'plugin:@typescript-eslint/recommended',
    'prettier',
  ],
  globals: {
    Atomics: 'readonly',
    SharedArrayBuffer: 'readonly',
  },
  parserOptions: {
    ecmaVersion: 2018,
    sourceType: 'module',
    ecmaFeatures: {
      jsx: true,
    },
  },
  settings: {
    react: {
      version: 'detect',
      pragma: 'React',
    },
  },
  env: {
    browser: true,
    commonjs: true,
    es6: true,
    node: true,
  },
  plugins: [
    'react',
    'react-hooks',
  ],
  rules: {
    'semi': 'off',
    '@typescript-eslint/semi': 'error',

    'quotes': ['error', 'single'],
    'no-console': 'off',
    'jsx-no-lambda': 'off',
    'jsx-boolean-value': 'off',

    'linebreak-style': 'off',
    'arrow-parens': 'off',
    'comma-dangle': [
      'error',
      {
        arrays: 'always-multiline',
        objects: 'always-multiline',
        imports: 'always-multiline',
        exports: 'never',
        functions: 'never',
      },
    ],
    'object-curly-newline': 'off',
    'no-mixed-operators': 'off',
    'arrow-body-style': 'off',
    'function-paren-newline': 'off',
    'no-plusplus': 'off',
    'no-unused-vars': 'off',
    '@typescript-eslint/no-unused-vars': 'warn',

    'space-before-function-paren': 0,
    'no-underscore-dangle': 'warn',

    'max-len': ['warn', 100, 2, { ignoreUrls: true }],
    'no-alert': 'warn',

    'no-param-reassign': 'off',

    'react/require-default-props': 'off',
    'react/forbid-prop-types': 'off',
    'react/prop-types': 'off',
    'react/display-name': 'off',
    'react/jsx-props-no-spreading': 'off',
    'react/jsx-one-expression-per-line': 'off',
    'react/destructuring-assignment': 'warn',
    'react/jsx-filename-extension': ['warn', { extensions: ['.ts', 'tsx'] }],

    'prefer-destructuring': 'off',
    'operator-linebreak': 'off',

    '@typescript-eslint/camelcase': 'off',
    '@typescript-eslint/no-use-before-define': 'off',
    '@typescript-eslint/explicit-function-return-type': 'off',
    '@typescript-eslint/explicit-module-boundary-types': 'off',
    '@typescript-eslint/member-delimiter-style': [
      'error',
      {
        multiline: {
          delimiter: 'semi',
          requireLast: true,
        },
        singleline: {
          delimiter: 'semi',
          requireLast: false,
        },
      },
    ],
  },
};
