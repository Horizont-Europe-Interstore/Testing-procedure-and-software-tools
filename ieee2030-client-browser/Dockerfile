FROM node:16-alpine

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

ENV REACT_APP_EXPRESS_URL=http://express-mid:5000

EXPOSE 3002

CMD ["npm", "start"]