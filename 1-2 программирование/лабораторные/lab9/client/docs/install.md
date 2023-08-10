# Installing Lab9 Client

You can install Lab9 Client directly on your machine by following the next steps.

## Quick Summary of Steps

1. Check that you have all prerequisites (Git, Node, NPM). See [below](install.md#prerequisites) for more details. Pay close attention to software versions.
2. Clone this repository locally.
3. Add `127.0.0.1 lab9.localhost` to your local `hosts` file.
4. Execute `yarn` and then `yarn start` from the root directory of the repository.
5. Open [`lab9.localhost:3000`](http://lab9.localhost:3000/) in your browser.

## Prerequisites

To be able to clone the repository and run the application you need:

- Install the [Node.js](http://nodejs.org/) and matching [NPM](https://www.npmjs.com/) [version](https://nodejs.org/en/download/releases/) matching to version in [.nvmrc](../.nvmrc) **(this bit about versions is important, that's why I'm using bold)**. On Mac OS X and Linux using [nvm](https://github.com/creationix/nvm) or [n](https://github.com/tj/n) makes it easy to manage `node` versions. On Windows you may want to try [nvm-windows](https://github.com/coreybutler/nvm-windows) or [nodist](https://github.com/marcelklehr/nodist).
- Please note that in Debian/Ubuntu versions of Linux, `node` command is renamed to `nodejs`. This will cause Lab9 Client to fail during installation. Follow the instructions [here](https://stackoverflow.com/a/18130296) to create a symlink to `node`.
- [Git](http://git-scm.com/). Try the `git` command from your terminal, if it's not found then use this [installer](http://git-scm.com/download/).

## Installing and Running

Clone this git repository to your machine via the terminal using the `git clone` command and then run `yarn start` from the root Lab9 Client directory:

```bash
$ git clone https://github.com/maxbarsukov/itmo.git
$ cd itmo/1-2\ программирование/лабораторные/lab9/client
$ yarn
$ yarn start
```

<!--eslint ignore no-emphasis-as-heading-->

_Note - if you are planning on pushing changes back to Lab9, this workflow will ask you for a username and password every time you push a change, which will not work if you have GitHub 2-factor auth enabled. In this case you should use `git clone git@github.com:maxbarsukov/itmo.git` instead, and follow the instructions [here](https://help.github.com/articles/about-ssh/) to set up authentication._

The `yarn start` command will install any `npm` dependencies and start the development server. When changes are made to either the JavaScript files or the Sass stylesheets, the build process will run automatically. The build process compiles both the JavaScript and CSS to make sure that you have the latest versions of both.

To run Lab9 Client locally, you'll need to add `127.0.0.1 lab9.localhost` to [your hosts file](http://www.howtogeek.com/howto/27350/beginner-geek-how-to-edit-your-hosts-file/), and load the app at [http://lab9.localhost:3000](http://lab9.localhost:3000) instead of just `localhost`.

### Starting the node debugger

You can use the `NODE_OPTIONS` environment variable to pass options to the Node command. This means that if you want to start up the debugger on a specific port you can run `NODE_OPTIONS="--inspect=5858" yarn start`. If you would like to debug the build process as well, it might be convenient to have the inspector break on the first line and wait for you. In that case, you should also pass in the `--inspect-brk` option like so `NODE_OPTIONS="--inspect-brk" yarn start`.
