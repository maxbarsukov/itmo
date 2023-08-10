FROM node:16.20-alpine

RUN mkdir -p /broadcaster
WORKDIR /broadcaster

COPY package.json .
COPY yarn.lock .
RUN yarn add global typescript
RUN yarn install

COPY . .
RUN yarn build

ENV PORT=8081
ENV NODE_ENV=production
EXPOSE 8081
CMD [ "yarn", "run" ]
