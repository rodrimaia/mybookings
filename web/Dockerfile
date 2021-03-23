FROM node:10.18.1

COPY yarn.lock package.json /code/
WORKDIR /code

RUN yarn install --frozen-lockfile

COPY . .

RUN yarn run build

EXPOSE 3000

CMD yarn run start
