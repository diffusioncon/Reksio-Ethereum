FROM ubuntu
RUN apt-get update && apt-get install -y wget livemedia-utils build-essential curl
RUN wget http://www.live555.com/liveMedia/public/live555-latest.tar.gz && \
    tar -xzf live555-latest.tar.gz && \
    cd live && ./genMakefiles linux && make && make install
COPY /videos/rtspConsume.sh /videos/rtspConsume.sh
WORKDIR /videos
CMD ["./rtspConsume.sh"]
