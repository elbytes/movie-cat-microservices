package com.elbytes.moviecatalogservice.resources;

import com.elbytes.moviecatalogservice.models.CatalogItem;
import com.elbytes.moviecatalogservice.models.Movie;
import com.elbytes.moviecatalogservice.models.Rating;
import com.elbytes.moviecatalogservice.models.UserRating;
import com.netflix.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/catalog")
@RestController
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;


//    @Autowired
//    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        UserRating ratings = restTemplate.getForObject("http://rating-data-service/ratingsdata/users/" + userId, UserRating.class);


        return ratings.getUserRating().stream().map(rating ->{
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getId() , Movie.class);
//            Movie movie = webClientBuilder.build().get().uri("http://localhost:8081/movies/" + rating.getId()).retrieve().bodyToMono(Movie.class).block();

            return new CatalogItem(movie.getName(), "some desc", rating.getRating());
        }).collect(Collectors.toList());
    }
}
