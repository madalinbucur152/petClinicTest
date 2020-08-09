package com.endava.petclinic;

import com.endava.petclinic.models.AnimalType;
import com.endava.petclinic.models.Owner;
import com.endava.petclinic.models.Pet;
import com.endava.petclinic.models.Visit;
import com.endava.petclinic.util.EnvReader;
import com.github.javafaker.Animal;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PetClinicTest {

    private Faker faker = new Faker();


    @Test
    public void addOwnerTest(){

        Owner owner = new Owner();
        owner.setAddress(faker.address().streetAddress());
        owner.setCity(faker.address().city());
        owner.setFirstName(faker.name().firstName());
        owner.setLastName(faker.name().lastName());
        owner.setTelephone(faker.number().digits(10));

        ValidatableResponse response = given()
                .baseUri(EnvReader.getBaseuri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePath())
                .contentType(ContentType.JSON)
                .body(owner)
                .log().all()
                .post("/api/owners/")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        Integer id = response.extract().jsonPath().getInt("id");


        ValidatableResponse getResponse = given()
                //request
                .baseUri(EnvReader.getBaseuri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePath())
                .pathParam("ownerId", id)
                .log().all()
                .get("/api/owners/{ownerId}")
                //response
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
//                .body("id", is(id))
//                .body("firstName", is(owner.getFirstName()))
//                .body("lastName", is(owner.getLastName()))
//                .body("address", is(owner.getAddress()))
//                .body("city", is(owner.getCity()))
//                .body("telephone", is(owner.getTelephone()))

        Owner actualOwner = getResponse.extract().as(Owner.class);

        assertThat(actualOwner, is(owner));


    }

    // Test the get owner by id API
    @Test
    public void getOwnerByIdTest(){

        ValidatableResponse getResponse = given()
                //request
                .baseUri(EnvReader.getBaseuri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePath())
                .pathParam("ownerId", 22)
                .log().all()
                .get("/api/owners/{ownerId}")
                //response
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
    }


    // Test the add pet API --FAILED--
    @Test
    public void addPetTest() throws ParseException {

        Pet pet = new Pet();

        AnimalType animalType = new AnimalType();
        animalType.setName(faker.animal().name());

        Owner owner = new Owner();
        owner.setAddress(faker.address().streetAddress());
        owner.setCity(faker.address().city());
        owner.setFirstName(faker.name().firstName());
        owner.setLastName(faker.name().lastName());
        owner.setTelephone(faker.number().digits(10));

        pet.setName(faker.funnyName().name());
        pet.setBirthDate(faker.date().birthday().toString());
        pet.setType(animalType);
        pet.setOwner(owner);


        ValidatableResponse response = given()
                .baseUri(EnvReader.getBaseuri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePath())
                .contentType(ContentType.JSON)
                .body(pet)
                .log().all()
                .post("/api/pets/")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        Integer id = response.extract().jsonPath().getInt("id");

        ValidatableResponse getResponse = given()
                //request
                .baseUri(EnvReader.getBaseuri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePath())
                .pathParam("petId", id)
                .log().all()
                .get("/api/owners/{petId}")
                //response
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        Pet actualPet = getResponse.extract().as(Pet.class);

        assertThat(actualPet, is(pet));


    }

    // Test the get pet list API
    @Test
    public void getAllPets(){
        given()
                //request
                .baseUri(EnvReader.getBaseuri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePath())
                .log().all()
                .get("/api/pets")
                //response
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
    }

    // Test the create visit API --FAILED--
    @Test
    public void addVisit(){

        Visit visit = new Visit();

        visit.setDescription("");
        visit.setDate(faker.date().toString());
        visit.setPet(new Pet(faker.funnyName().name(),faker.date().birthday().toString()));

        given()
                .baseUri(EnvReader.getBaseuri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePath())
                .contentType(ContentType.JSON)
                .body(visit)
                .log().all()
                .post("/api/visits/")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

    }


}
