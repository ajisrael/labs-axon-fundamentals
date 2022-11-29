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

## Run Your Application
After that, start the application and visit http://localhost:8080. The UI should render and allow you to issue, redeem
and reimburse cards. Check out the events stored in Axon Server by visiting http://localhost:8024 and checking out the
‘Search’ page.
