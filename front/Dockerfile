FROM nginx:stable-alpine
COPY build /usr/share/nginx/html
COPY nginx /etc/nginx/conf.d/default.conf
COPY nginx /etc/nginx/api_proxy.conf
COPY nginx /
CMD ["./run.sh"]
