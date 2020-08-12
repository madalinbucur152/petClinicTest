package com.endava.petclinic;

import com.endava.petclinic.clients.OwnerClient;
import com.endava.petclinic.clients.UserClient;
import com.endava.petclinic.data.DataGenerator;
import com.endava.petclinic.models.*;
import com.endava.petclinic.util.EnvReader;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.HeaderTransformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PetClinicTest {

    private Faker faker = new Faker();
    private OwnerClient ownerClient = new OwnerClient();
    private UserClient userClient = new UserClient();
    private DataGenerator dataGenerator = new DataGenerator();


    @Test
    public void createOwnerTest() {

        // create new user
        User user = dataGenerator.getUser(RoleName.OWNER_ADMIN);
        Response createUserResponse = userClient.createUser(user);
        createUserResponse.then().statusCode(HttpStatus.SC_CREATED);

        // create new Owner
        Owner owner = dataGenerator.getOwner();
        Response response = ownerClient.createOwner(owner, user);
        response.prettyPeek().then().statusCode(HttpStatus.SC_CREATED);

        Integer id = response.jsonPath().getInt("id");

        // get owner by id
        Response getResponse = ownerClient.getOwnerById(id, user);
        getResponse.then().statusCode(HttpStatus.SC_OK);

        Owner actualOwner = getResponse.as(Owner.class);
        assertThat(actualOwner, is(owner));


    }

    @Test
    public void putOwnerTest() {
        Owner owner = new Owner();
        owner.setFirstName(faker.name().firstName());
        owner.setLastName(faker.name().lastName());
        owner.setCity(faker.address().city());
        owner.setAddress(faker.address().streetAddress());
        owner.setTelephone(faker.number().digits(10));

        ValidatableResponse ownerResponse = given()
                .baseUri(EnvReader.getBaseuri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(owner)
                .post("/api/owners")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        owner.setId(ownerResponse.extract().jsonPath().getInt("id"));

        owner.setFirstName(faker.name().firstName());
        owner.setCity(faker.address().city());

        given()
                .baseUri(EnvReader.getBaseuri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(owner)
                .pathParam("ownerId", owner.getId())
                .put("/api/owners/{ownerId}")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_NO_CONTENT);

        ValidatableResponse getResponse = given()
                .basePath(EnvReader.getBasePath())
                .baseUri(EnvReader.getBaseuri())
                .port(EnvReader.getPort())
                .pathParam("ownerId", owner.getId())
                .get("/api/owners/{ownerId}")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        Owner actualOwner = getResponse.extract().as(Owner.class);

        assertThat(actualOwner, is(owner));

    }

    // Test the get owner by id API
    @Test
    public void getOwnerByIdTest() {

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


    @Test
    public void postPetTest() {
        //add owner
        //add pet type
        //add pet


        //add owner
        Owner owner = new Owner();
        owner.setFirstName(faker.name().firstName());
        owner.setLastName(faker.name().lastName());
        owner.setCity(faker.address().city());
        owner.setAddress(faker.address().streetAddress());
        owner.setTelephone(faker.number().digits(10));

        ValidatableResponse postOwnerResponse = given()
                .baseUri(EnvReader.getBaseuri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(owner)
                .post("/api/owners")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);
        owner.setId(postOwnerResponse.extract().jsonPath().getInt("id"));


        //add pet type
        AnimalType animalType = new AnimalType(faker.animal().name());

        ValidatableResponse postPetTypeResponse = given()
                .baseUri(EnvReader.getBaseuri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(animalType)
                .post("/api/pettypes")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        animalType.setId(postPetTypeResponse.extract().jsonPath().getInt("id"));

        //add pet
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String birthDate = formatter.format(faker.date().birthday(0, 10));
        Pet pet = new Pet(faker.name().firstName(), birthDate, animalType, owner);

        ValidatableResponse postPetResponse = given()
                .baseUri(EnvReader.getBaseuri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(pet)
                .post("/api/pets")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        Pet actualPet = postPetResponse.extract().as(Pet.class);
        assertThat(actualPet, is(pet));

        //get owner

        given().baseUri(EnvReader.getBaseuri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .pathParam("ownerID", owner.getId())
                .get("/api/owners/{ownerID}")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

    }


    // Test the get pet list API
    @Test
    public void getAllPets() {
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
    public void addVisit() {

        Visit visit = new Visit();

        visit.setDescription("");
        visit.setDate(faker.date().toString());
        //visit.setPet(new Pet(faker.funnyName().name(),faker.date().birthday().toString()));

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

    @Test
    public void postVisitTest() {
        // add owner
        Owner owner = new Owner();
        owner.setFirstName(faker.name().firstName());
        owner.setLastName(faker.name().lastName());
        owner.setCity(faker.address().city());
        owner.setAddress(faker.address().streetAddress());
        owner.setTelephone(faker.number().digits(10));

        ValidatableResponse postOwnerResponse = given()
                .baseUri(EnvReader.getBaseuri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(owner)
                .post("/api/owners")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);
        owner.setId(postOwnerResponse.extract().jsonPath().getInt("id"));


        //add pet type
        AnimalType animalType = new AnimalType(faker.animal().name());

        ValidatableResponse postPetTypeResponse = given()
                .baseUri(EnvReader.getBaseuri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(animalType)
                .post("/api/pettypes")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        animalType.setId(postPetTypeResponse.extract().jsonPath().getInt("id"));

        //add pet
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String birthDate = formatter.format(faker.date().birthday(0, 10));
        Pet pet = new Pet(faker.name().firstName(), birthDate, animalType, owner);

        ValidatableResponse postPetResponse = given()
                .baseUri(EnvReader.getBaseuri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(pet)
                .post("/api/pets")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        pet.setId(postPetResponse.extract().jsonPath().getInt("id"));

        // add visit
        String date = formatter.format(faker.date().past(10, TimeUnit.DAYS));
        Visit visit = new Visit(date, faker.chuckNorris().fact(), pet);

        ValidatableResponse postVisitResponse = given()
                .baseUri(EnvReader.getBaseuri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(visit)
                .post("api/visits")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        Visit actualVisit = postVisitResponse.extract().as(Visit.class);
        assertThat(actualVisit, is(visit));

    }


}
