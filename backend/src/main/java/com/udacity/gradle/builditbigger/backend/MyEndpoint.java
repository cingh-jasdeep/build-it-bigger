package com.udacity.gradle.builditbigger.backend;

import com.example.android.jokeprovider.JokeProvider;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.builditbigger.gradle.udacity.com",
                ownerName = "backend.builditbigger.gradle.udacity.com",
                packagePath = ""
        )
)
public class MyEndpoint {

//    /** A simple endpoint method that takes a name and says Hi back */
//    @ApiMethod(name = "sayHi")
//    public MyBean sayHi(@Named("name") String name) {
//        MyBean response = new MyBean();
//        response.setData("Hi, " + name);
//
//        return response;
//    }

    /** A simple endpoint method that sends back a joke from joke provider*/
    @ApiMethod(name = "getJoke")
    public MyBean getJoke() {

        JokeProvider jokeProvider = new JokeProvider();
        MyBean response = new MyBean();
        response.setData(jokeProvider.getJoke());

        return response;
    }

}
