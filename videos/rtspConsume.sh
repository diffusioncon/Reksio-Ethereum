#!/bin/sh

openRTSP -D 1 -c -B 10000000 -b 10000000 -4 -Q -F CAM -P $VIDEO_DURATION -t rtsp://$RTSP_IP:$RTSP_PORT/$RTSP_RESOURCE_NAME &

while true; do
    for file in ./*; do
        filename=${file##*/}
        if [ $filename != "rtspConsume.sh" ]; then
            dateTime=`date '+%Y_%m_%d__%H_%M_%S'`;
            filenameWithDateTime="${dateTime}.mp4"
            path=$(pwd)/$filenameWithDateTime
            mv $(pwd)/$filename $path
            curl -F "key=$filenameWithDateTime" -F "file=@$path" -F "Content-Type=video/mp4" $SECRETARY_URL
            rm $path
        fi
    done
    sleep 10
done
