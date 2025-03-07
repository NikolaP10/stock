# Nikola Pasic - MDS - TEST 
#### `nikolapasic90@yahoo.com`
#### `0642922181`

### Instructions
1. Open the terminal inside the project `/stock`
2. Execute command `mvn package`
3. Execute command `docker image build -t docker-npasic-test:latest .
   `
4. Execute command \
   to monitor logs: `docker run -p 8080:8080 docker-npasic-test:latest` \
   in detached mode: `docker run -d -p 8080:8080 docker-npasic-test:latest` \
   monitor logs in detached mode: `docker logs -f <containerId>`
5. In Postman import `MDS.postman_collection.json` from `postman` directory

