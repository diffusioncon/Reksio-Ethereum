#!/bin/sh

sed -i 's|{{SECRETARY_HOST}}|'"${SECRETARY_HOST}"'|g' /etc/nginx/conf.d/default.conf
nginx -g 'daemon off;'
