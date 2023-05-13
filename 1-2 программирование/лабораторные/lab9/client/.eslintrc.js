module.exports = {
  parser:  '@typescript-eslint/parser',
  extends:  [
    'plugin:react/recommended',
    'plugin:@typescript-eslint/recommended',
    'plugin:prettier/recommended',
    'plugin:jest/recommended',
    'jest-enzyme'
  ],
  parserOptions: {
    ecmaVersion: 2018,
    sourceType: 'module',
    ecmaFeatures: {
      jsx: true,
    },
  },
  settings: {
    'import/resolver': {
      node: {
        paths: ['src'],
      },
    },
    react:  {
      version:  'detect',
    },
  },
  env: {
    es6: true,
    browser: true,
    node: true,
  },
  plugins: [
    'babel',
    'import',
    'no-use-extend-native',
    'optimize-regex',
    'prettier',
    'promise',
    'react',
  ],
  rules: {
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
    'no-unused-vars': 'warn',
    'space-before-function-paren': 0,
    'no-underscore-dangle': 'warn',
    'import/no-extraneous-dependencies': ['error', { devDependencies: true }],

    'max-len': ['warn', 100, 2, { ignoreUrls: true }],
    'no-alert': 'warn',

    'no-param-reassign': 'off',

    'react/require-default-props': 'off',
    'react/forbid-prop-types': 'off',
    'react/jsx-props-no-spreading': 'off',
    'react/jsx-one-expression-per-line': 'off',
    'react/destructuring-assignment': 'warn',
    'react/jsx-filename-extension': ['warn', { extensions: ['.ts', 'tsx'] }],

    'prefer-destructuring': 'off',
    'operator-linebreak': 'off',

    'prettier/prettier': ['warn'],

    "@typescript-eslint/camelcase": "off",
    "@typescript-eslint/no-use-before-define": "off",
    "@typescript-eslint/explicit-function-return-type": "off",
    "@typescript-eslint/no-unused-vars": "off",
    "@typescript-eslint/explicit-module-boundary-types": "off",
    "@typescript-eslint/member-delimiter-style": [
      "error",
      {
        multiline: {
          delimiter: 'semi',
          requireLast: true,
        },
        singleline: {
          delimiter: 'semi',
          requireLast: false,
        },
      }
    ]
  }
};
