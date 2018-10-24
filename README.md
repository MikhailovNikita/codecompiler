Code compiler
----  

### Prerequisites
- Mongo DB 
- Docker

### How to run  
`gradle bootRun`

### Configuration
- Mongo DB  
    There is no configuration for db yet. 
    Mongo service should run locally with default port `27017`
    and without authorization
- Docker  
    In order to connect to docker server you can configure 
    two env variables:   `DOCKER_HOST` and `DOCKER_CERT_PATH`      
    By default, server will try to connect to docker locally.  
    In that case **current user should have access to docker**    
    (you can check it by evaluating command `docker info` without `sudo`)  
    On windows you should expose daemon without TLS  
    Open Docker Settings -> General -> check `Expose daemon on tcp://localhost:2375 without TLS`

### API
You can try using api with Swagger UI.   
Just run server and visit `localhost:8080/swagger-ui.html`

Or you can find simple UI for that api on `localhost:8080/`

### Usage  example 

[User manual](docs/user-manual.md)

1. Create compile task
    ```
       curl -X POST "http://localhost:8080/compile-submissions/" 
        -H "accept: */*" -H "Content-Type: application/json" 
         -d "{ 
                \"language\": \"JAVA\", 
                \"sourceCode\": \"public class Main{public static void main(String args[]){System.out.println(123);}}\"
             }"
    ```
    ```
    {
      "id": "5bba8f42ea22b3211ccb0174",
      "language": "JAVA",
      "languageName": "Java 8u171",
      "status": "OK",
      "cmpErr": ""
    }
    ```
2. Run code (using id from previous step)  
    ```
    curl -X POST "http://localhost:8080/submissions/?submissionId=5bba8f42ea22b3211ccb0174" -H "accept: */*"
    ```    
    ```
    {
      "id": "5bba9011ea22b3211ccb0175",
      "language": "JAVA",
      "languageName": "Java 8u171",
      "status": "OK",
      "input": null,
      "output": "123\n",
      "stderr": "",
      "memory": null,
      "time": null
    }
    ```