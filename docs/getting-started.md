# Getting Started

## Setting up Axon Server Locally
There are two ways of setting up the Axon Server locally. 
    
Download it [here](https://download.axoniq.io/training/AxonServer.zip) and run the following command:
```
java -jar axonserver.jar
```

Or Run it in Docker: (Personal recommendation)
```
docker run -d --name axonserver -p 8024:8024 -p 8124:8124 -e AXONIQ_AXONSERVER_DEVMODE_ENABLED=true axoniq/axonserver
```

