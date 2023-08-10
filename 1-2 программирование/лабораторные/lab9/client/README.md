# Lab 9 Web Client

[![Dependabot](https://img.shields.io/badge/dependabot-enabled-success.svg)](https://dependabot.com)
[![Code style: airbnb](https://img.shields.io/badge/code%20style-airbnb-blue.svg?style=flat-square)](https://github.com/airbnb/javascript)

Web client for **Lab9** built with `React` + `Redux` + `Material UI`

### Technologies

[![Made with: TypeScript](https://img.shields.io/badge/TypeScript-007acc?style=for-the-badge&logo=TypeScript&logoColor=white)](https://www.javascript.com)
[![Made with: React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)](https://reactjs.org/)
[![Made with: React Router](https://img.shields.io/badge/React_Router-CA4245?style=for-the-badge&logo=react-router&logoColor=white)](https://reactrouter.com/)
[![Made with: Redux](https://img.shields.io/badge/Redux-593D88?style=for-the-badge&logo=redux&logoColor=white)](https://redux.js.org/)
[![Made with: MUI](https://img.shields.io/badge/Material_UI-007FFF?style=for-the-badge&logo=mui&logoColor=white)](https://mui.com/)
[![Made with: Jest](https://img.shields.io/badge/Jest-98435b?style=for-the-badge&logo=jest&logoColor=white)](https://jestjs.io/)

## Table of contents
1. [Getting Started](#getting-started)
2. [Run](#run)
3. [Available Scripts](#scripts)
4. [Testing](#testing)
5. [License](#license)

## Getting Started <a name="getting-started"></a>

Make sure you have [`git`](https://git-scm.com/), [`node`](https://nodejs.org/) (`v16.20.0`), and [`yarn`](https://classic.yarnpkg.com/en/docs/install) installed.

Clone this repository:

`git clone git@github.com:maxbarsukov/itmo.git`

Install dependencies:

`yarn install`

## Run <a name="run"></a>

Run locally:

- Development: run `yarn start`, then go to http://localhost:3000
- Production: run `yarn build` to build, then you can run `npx serve -s build -p 8080` to run your build on http://localhost:8080

## Available Scripts <a name="scripts"></a>

In the project directory, you can run:

### `./internals/scripts/check_before_ci.sh`

Please, run this script **before pushing**, there are the same commands as in **CI**.

### `./internals/scripts/check_dependencies.sh`

Checks unused dependencies.

### `yarn start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.\
You will also see any lint errors in the console.

### `yarn test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `yarn test:coverage`

Run to see the test coverage.

### `yarn codecov`

Generate coverage data. Using by CI.

### `yarn build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `yarn eject`

**Note: this is a one-way operation. Once you `eject`, you can’t go back!**

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you’re on your own.

You don’t have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn’t feel obligated to use this feature. However we understand that this tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

### `yarn prettier`

Fixes formatting errors found in the code and overwrites them.
See [Prettier](https://prettier.io/) to learn more

### `yarn prettier:eslint`

The same as the `yarn prettier`, but uses the ESLint configuration over the Prettier configuration.

### `yarn lint`

Linter will look through all the files in the `src` directory and output a detailed report on the files in which it found errors.
Using this report, you can correct these errors. \
To learn more, see [ESLint](https://eslint.org/).

### `yarn lint:fix`

ESLint will perform the same check that was performed with `yarn lint`.
The only difference is that in this mode, the system will try to correct the detected errors,
try to bring the code into as decent a form as possible.

### `yarn docs`

Generate JSDoc documentation. To view it, run `docs/index.html` in browser. \
To learn more, see [JSDoc](https://github.com/jsdoc/jsdoc) docs.

## Testing <a name="testing"></a>

Run `yarn test` to launch the test runner in the interactive watch mode.

Check the quality of code with `yarn lint`

## License <a name="license"></a>

The project is available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).
*Copyright 2023 Max Barsukov*


**Leave a star :star: if you find this project useful.**
