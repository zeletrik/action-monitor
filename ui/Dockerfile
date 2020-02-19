FROM node:13.8-alpine AS build
COPY . ./action-monitor-ui
WORKDIR /action-monitor-ui
RUN npm i
RUN $(npm bin)/ng build --prod

FROM nginx:1.17.8-alpine
COPY --from=build /action-monitor-ui/dist/action-monitor-ui /usr/share/nginx/html
EXPOSE 80