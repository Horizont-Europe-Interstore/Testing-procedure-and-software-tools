FROM node:22-alpine3.18
RUN apk add git
RUN mkdir electron
WORKDIR /electron
COPY . .
RUN npm install