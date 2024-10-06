FROM node:22
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
ENV PORT=4173
ENV HOST=0.0.0.0
EXPOSE 4173
RUN npm run build
ENTRYPOINT [ "npm", "run", "preview" ]
