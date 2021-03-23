# How to run:

Hopefully, you should be able to only run `docker-compose up` and it will trigger both services for you.
Running without docker
To run without docker, you can run:
```
cd api;
gradle build && gradle run
```
in another terminal:
```cd web
yarn && yarn run dev 
```
or if you wanna have production build
```cd web
yarn && yarn run build && yarn run start 
```
Then, access the site on `localhost:3000`

Now, I would like to talk about each part of the architecture:

# About the API

The API part is a java-written application. Since I am not too familiar with Java frameworks (not any more haha), I tried to go for frameworks that would quickly bootstrap a web server and give me a development environment without too much pain. For that, I chose Spark, and complemented it with ORMLite. At first, I was doing most things in the Main class, but when I started writing unit tests, I felt the needed to move part of it to a Service class. I did not write too many tests, but the overlapping logic is covered and it is the most logic-intense part of the application. Of course, there are many improvements to my current solution, and I would like to mention a few of them:

## improvements:

- More tests
- Creation of a proper controller;
- ErrorResponse and SuccessResponse could inherit from a base abstract Response class.
- It does not implement the concept of Blocks.

# Frontend
For the web, I used next.js since I believe it is easier to configure Typescript, and also because I was interested in setting up true server-rendered content (at least to the home page). It indeed worked, but when setting up Docker I realized I needed to have the API server live when building the site, so I gave up on that idea. Although the site works well and is fast. The homepage is using SWR strategy to load content, so it should maintain the current data live even when revalidating the cache. I usually use those cache strategies on the server too, but my lack of knowledge of Java held me back this time. The UI itself is a result of an excess of good-will, and lack of time. I got the main color from www.hostfully.com and started creating on top of it. In the end, I had to ditch my plans of making it better in mobile resolutions and finish what I needed. The main features are there, but it is clear that it lacks polishing in several places.


##  Improvements:

- Small viewports could benefit from responsive layouts 
- Better error Handling (the API errors are not being well communicated to the user)
- Desktop viewports could also benefit from a better wide layout.
- A few typescript castings were used when better typing was required.
- The homepage could have filters.
- Instead of Table, I believe a card-based layout could improve the homepage design. 

Thank you! :) 
