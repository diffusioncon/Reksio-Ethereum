FROM nginx:stable-alpine
COPY build /usr/share/nginx/html
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf
COPY nginx/api_proxy.conf /etc/nginx/api_proxy.conf
COPY nginx/run.sh /
CMD ["./run.sh"]
