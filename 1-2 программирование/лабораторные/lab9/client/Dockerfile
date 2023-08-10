# pull official base image
FROM node:16.20.0-alpine

# set working directory
WORKDIR /app

# add `/app/node_modules/.bin` to $PATH
ENV PATH /app/node_modules/.bin:$PATH

# install app dependencies
RUN apk update && apk apk add g++ make python
COPY package.json ./
COPY yarn.lock ./
RUN yarn install
RUN yarn global add react-scripts@4.0.3

# add app
COPY . ./

EXPOSE 3000

# start app
CMD ["yarn", "run", "start"]
