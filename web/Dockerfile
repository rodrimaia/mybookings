FROM node:alpine
WORKDIR /app

COPY package.json .
COPY yarn.lock .
RUN yarn install --frozen-lockfile

COPY pages pages
COPY types.ts types.ts
COPY public public
COPY components components
COPY styles styles
RUN yarn build

EXPOSE 3000
ENTRYPOINT ["npm", "run", "dev"]