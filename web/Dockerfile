FROM node:alpine
WORKDIR /app

COPY package.json .
COPY yarn.lock .
RUN yarn install --frozen-lockfile

COPY . .

EXPOSE 3000
ENTRYPOINT ["npm", "run", "dev"]