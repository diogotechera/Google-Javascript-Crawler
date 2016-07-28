# Google-Javascript-Crawler

 Simple Java crawler that counts the most popular Javascripts libs based on a Google search result.
 
  You can run it with Gradle, just build it with:
    
 gradle build
   
   Then execute the generated jar file in /build/libs:
   
    java -jar google-javascript-crawler.jar <GOOGLE SEARCH QUERY>
    
    Points of improvement:
    
    - Tests. the services were projected on the single responsibility principle, for it can be most easily tested
    - It must found a better pattern for the same libraries with different names could be counted as the same lib (i.e.: jquery, jquery.min, jquery-1.0)
    
    
 
 
