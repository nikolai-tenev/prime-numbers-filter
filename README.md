# How to run the services

The services are shipped as source code and dockerfiles and can be run with docker-compose.

## Build the images
First you need to go to the respective dirs and run the docker build cmd, please note that the image must be tagged as the respective name of the service (consumer/producer):

### Consumer
```
cd consumer
docker build . -t consumer
```

### Producer
```
cd producer
docker build . -t producer
```

## Run compose
To run the docker compose you need to go to the project root and execute:
```
docker-compose up 
```

## CSVs

The CSVs are located in the following directories relative to the run dir:
 - `buffer-logs` - contains all emitted random numbers. The numbers are split in multiple files one for each buffer refill.
 - `prime-numbers-logs/prime-numbers-logs.csv` - contains all the identified primes on a single row, separated by commas. 