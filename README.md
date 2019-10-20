![Reksio Config Page](https://i.imgur.com/2DSUyjE.png)

![Reksio File List](https://i.imgur.com/wIdXzmd.png)

## Inspiration
Information is power and _data is the new oil_. Whoever controls data or has the power and resources to manipulate it can influence entire countries, _e.g._, through [search engine manipulation effect](https://en.wikipedia.org/wiki/Search_engine_manipulation_effect).  Similarly, content can easily be maliciously doctored or manufactured all together, _e.g._, using [deepfake](https://en.wikipedia.org/wiki/Deepfake) technologies. Knowing the origin of a piece of information is becoming vitally important to our societies. We want to bring back trust in the data and give users the power to validate the source of the content they consume by bridging the gap between a fully decentralized web and the current centralized infrastructure of Web 2.0.

## What it does
![Reksio mock](https://i.imgur.com/uuoyC7R.png)

Once on the distributed ledger, information is considered secure, immutable, tamper-resistant and permanent. Our ambition is to seamlessly _extend this trust_ to include _sensors_, _gateways_ and other _devices provisioning data_, and do that with little to none changes to existing data pipelines and infrastructure. At the same time, we'd like to abstract the DLT layer from an end-user, since the majority of the population needs a transition period to get acquainted with the decentralized web.

_Reksio_ creates a _trust layer_ on top of your existing _data layer_. It's a _data notarization service_ that is both DLT- and storage-agnostic, specifically designed for streaming data: video and IoT sensor measurement stream. 

The trust layer is used to verify that the data we own has not been modified, serving as proof to ourselves and third party consumers. An example scenario is a video surveillance maintainer providing an extra means of security, a court looking to authenticate evidence or a media agency looking to tamper-proof video assets.


## How we built it

Our goal was to create an intuitive, low-cost, non-intrusive MVP capable of efficiently tackling high velocity, high volume video and data stream and storage inefficiency of the Ethereum Network.

### Status quo
For Diffusion 2019 purposes our team decided to set up a version handling the **notarization of a live video feed** only. We started with a very simple landscape simulating a _status quo_ situation using the following components:
* _RTSP server_ (based on [GStreamer](https://gstreamer.freedesktop.org/)) producing video stream
* _RTSP client_ (based on [openRTSP](http://www.live555.com/openRTSP/)) receiving the data, generating 30-second long `mp4` files
* _S3-compatible storage_ ([Minio](https://min.io/)) to host the client output.

![Without Reksio](https://i.imgur.com/5qdH2IU.jpg)

### Pluggable data notary
Reksio is an agent that can be seamlessly plugged into an existing architecture like the one above. It is positioned between the RTSP client and storage, calculates hashes of the data and commits the hashes to the blockchain to effectively create a permanent data seal of authenticity.

![With Reksio](https://i.imgur.com/53XN2At.jpg)

The following core services are part of the implementation:
* _Secretary_ is a proxy agent that (a) intercepts the RTSP client output files, (b) calculates hashes for each file, (c) sends the files to the S3 storage and (d) sends the calculated hashes, including file name, to the Notary service.
* _Notary_ is an agent communicating with the DLT for (a) storing the filenames and their corresponding hashes and (b) retrieving hashes from the DLT.
* _Frontend_ to facilitate configuration and provide dashboards.

### Hardware-based security layer
Our solution has a very strong focus on security. We're using hardware wallets in the form of [Infineon Blockchain Security 2 Go](https://www.infineon.com/cms/en/product/security-smart-card-solutions/blockchain-security-2go/) a smart card to sign transactions. In the PoC it is connected to the single-board computer (SBC) running the Notary service through an off-the-shelf NFC/smartcard card reader.

## Challenges we ran into
_S3 interface._ We underestimated the complexity of the AWS S3 interface. Initially, we wanted to use _Secretary_ to intercept AWS S3 request to (a) send it 1:1 to AWS S3 for storage and (b) process the file being sent for the notarization purposes. This turned out impossible due to the authorization mechanism in AWS S3. Due to time limitations, this remains still partially unresolved. As of now, we read a client request, proxy the request to S3 but we do not proxy the S3 response back to the caller.

_Additional network traffic._ The incoming data stream is now directed to the Secretary, which in turn sends it to the storage, effectively increasing the number of transmitted packages. However, depending on backend technologies, this can be optimized in future, _e.g._, by using storage hooks and hashes provided by the data persistence solution.

## Accomplishments that we're proud of

Thanks to a well-established team workflow we were able to streamline the development process without frictions and deliver the MVP exactly as initially envisioned.

## What we learned

A lot!

We’ve learned that DLTs and traditional data technologies can complement each other to create new business value.

We’ve also learned some of the technologies in this project better, including the internals of the S3 API, RTSP protocol, signing Solidity transaction using hardware NFC wallets.

## What's next for Reksio

_Optimize the notarization architecture._ Reduction of bandwidth, ledger storage cost, and computational complexity can be reduced by improving system architecture and component integration.

_Full chain of trust and hardware security._ Digitally signing data at the source using secure elements to provide an end-to-end, unbroken chain of trust between the data source and its persistence. This will allow not only means to prove that the data in the storage has not been altered, but also no modification had not been possible during transmission.

_Decreased hashing latency._ By increasing buffering time, we limit the time when an attacker could modify the data before the hash is calculated.

_Lower notarization cost._ Lower latency results in a higher notarization hashrate, which can be reduced by using hash trees. The notarization cost for our MVP is calculated to be 0.0103 ETH/h (1.58 €/h) per video stream. This can be reduced by trading off temporal granularity.

_Cross-stream hashing._ In order to scale to the exploding IoT data sources, lower volume and velocity data streams will be aggregated and jointly signed.

_Data consumption component._ Our MVP does not facilitate an interface to consume the notarized data. Based on future customer experience we would gradually extend our product to cover the entire life cycle of the collected data, extending the chain of trust to the consumption by the end-user. The authentication and authorization mechanism could be based on DID like [Sovrin](https://sovrin.org/).