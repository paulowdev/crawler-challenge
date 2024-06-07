## Crawler Challenge - SparkJava

Develop a Java application to navigate a site for a user-supplied term and list as URLs where the term was found.

### Challenge requirements

1.User interaction with the application must happen via through an HTTP API, to be made available on port 4567. Two operations must be supported:
   
a. **POST:** Starts a new search for a term (keyword).
\
_Request:_
\
<code>
POST /crawl HTTP/1.1 \
Host: localhost:4567 \
Content-Type: application/json \
Body: {"keyword": "linux"} \
</code>
_Response:_
\
<code>
200 OK \
Content-Type: application/json \
Body: {"id": "6ovu0lyT"} \
</code>

b. **GET:** Queries search results.
\
_Request:_
\
<code>
GET /crawl/6ovu0lyT \
HTTP/1.1 \
Host: localhost:4567 \
</code>
_Response:_
\
<code>
200 OK \
Content-Type: application/json \
{ 
   "id": "6ovu0lyT", \
   "status": "active", \
   "urls": [ \
   "http://url.example.com/index2.html",
   "http://url.example.com/htmlman1/chcon.1.html" \
] \
   }
</code>

2.The search term must have a minimum of 4 and a maximum of 32 characters. The search must be case-insensitive, in any part of the HTML content (including tags and comments). \
3.The search id must be an 8-character alphanumeric code automatically generated. \
4.The base URL of the site where the analyzes are performed is determined by an environment variable. Searches should follow links (absolute and relative) in anchor elements of visited pages if and only if they have the same base URL. \
5.The application must support the execution of multiple simultaneous searches. Information about searches in progress (status active) or already completed (status done) must be maintained indefinitely while the application is running.
6.While a search is in progress, its already found partial results must be returned by the GET operation. \
7.The project must follow the basic structure provided. The Dockerfile and pom.xml files cannot be modified. Any other files provided can be modified. \
8.From the project root directory, the following two commands, executed in sequence, must compile and initialize the application:\
<code>
$ docker build . -t crawler-challenge/java \
$ docker run -e BASE_URL=http://url.example.com/ -p 4567:4567 --rm crawler-challenge/java
</code>