package org.upgrad.upstac.testrequests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.lab.CreateLabResult;
import org.upgrad.upstac.testrequests.lab.LabRequestController;
import org.upgrad.upstac.testrequests.lab.TestStatus;
import org.upgrad.upstac.users.User;
import org.upgrad.upstac.users.models.Gender;

import javax.validation.constraints.NotNull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@Slf4j
class LabRequestControllerTest {


    @InjectMocks
    LabRequestController labRequestController;




    @Mock
    TestRequestQueryService testRequestQueryService;


    @Mock
    UserLoggedInService userLoggedInService;

    @Mock
    TestRequestUpdateService testRequestUpdateService;


    @Test
    @WithUserDetails(value = "tester")
    public void calling_assignForLabTest_with_valid_test_request_id_should_call_testRequestUpdateService_assignForLabTest(){


        //Implement this method

        //Create another object of the TestRequest method and explicitly assign this object for Lab Test using assignForLabTest() method
        // from labRequestController class. Pass the request id of testRequest object.

        //Use assertThat() methods to perform the following two comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'INITIATED'
        // make use of assertNotNull() method to make sure that the lab result of second object is not null
        // use getLabResult() method to get the lab result

        //Arrange
        User user = createUser();
        TestRequest  mockedTestRequest = testRequest();

        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.assignForLabTest(21L,user)).thenReturn(mockedTestRequest);

        //Act
        TestRequest testRequest = labRequestController.assignForLabTest(21L);

        // Assert
        assertNotNull(testRequest);
        assertEquals(testRequest,mockedTestRequest);

    }

    public TestRequest getTestRequestByStatus(RequestStatus status) {
        return testRequestQueryService.findBy(status).stream().findFirst().get();
    }

    @Test
    public void calling_assignForLabTest_with_Invalid_test_request_id_should_throw_exception(){

        Long InvalidRequestId= -34L;

        //Implement this method


        // Create an object of ResponseStatusException . Use assertThrows() method and pass assignForLabTest() method
        // of labRequestController with InvalidRequestId as Id


        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"

        //Arrange
        User user = createUser();

        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.assignForLabTest(InvalidRequestId,user)).thenThrow(new AppException("Invalid ID"));

        //Act
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            labRequestController.assignForLabTest(InvalidRequestId);
        });

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("Invalid ID",result.getReason());

    }

    @Test
    public void calling_updateLabTest_with_valid_test_request_id_should_update_the_request_status_and_update_test_request_details(){

        //Implement this method
        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter

        //Create another object of the TestRequest method and explicitly update the status of this object
        // to be 'LAB_TEST_IN_PROGRESS'. Make use of updateLabTest() method from labRequestController class (Pass the previously created two objects as parameters)

        //Use assertThat() methods to perform the following three comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'LAB_TEST_COMPLETED'
        // 3. the results of both the objects created should be same. Make use of getLabResult() method to get the results.

        //Arrange
        User user = createUser();
        TestRequest  mockedTestRequest = testRequest();
        CreateLabResult createLabResult =  getCreateLabResult(mockedTestRequest);

        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.updateLabTest(21L,createLabResult,user)).thenReturn(mockedTestRequest);

        //Act
        TestRequest testRequest = labRequestController.updateLabTest(21L,createLabResult);

        // Assert
        assertNotNull(testRequest);
        assertEquals(testRequest,mockedTestRequest);

    }


    @Test
    public void calling_updateLabTest_with_invalid_test_request_id_should_throw_exception(){

        //Implement this method

        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
        // of labRequestController with a negative long value as Id and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method


        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"

        Long InvalidRequestId= -34L;

        //Arrange
        User user = createUser();
        TestRequest  mockedTestRequest = testRequest();
        CreateLabResult createLabResult =  getCreateLabResult(mockedTestRequest);

        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.updateLabTest(InvalidRequestId,createLabResult,user)).thenThrow(new AppException("Invalid ID or State"));

        //Act
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            labRequestController.updateLabTest(InvalidRequestId,createLabResult);
        });

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("Invalid ID or State",result.getReason());

    }

    @Test
    public void calling_updateLabTest_with_Valid_ID_and_invalid_empty_status_should_throw_exception(){

        //Implement this method

        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
        // Set the result of the above created object to null.

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
        // of labRequestController with request Id of the testRequest object and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method


        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "ConstraintViolationException"

        //Arrange
        User user = createUser();
        TestRequest  mockedTestRequest = testRequest();
        mockedTestRequest.setStatus(RequestStatus.INITIATED);
        CreateLabResult createLabResult =  getCreateLabResult(mockedTestRequest);


        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.updateLabTest(21L,createLabResult,user)).thenThrow(new AppException("Invalid ID or State"));

        //Act
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            labRequestController.updateLabTest(21L,createLabResult);
        });

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("Invalid ID or State",result.getReason());


    }

    public CreateLabResult getCreateLabResult(TestRequest testRequest) {

        //Create an object of CreateLabResult and set all the values
        // Return the object
        CreateLabResult createLabResult = new CreateLabResult();
        createLabResult.setBloodPressure("102");
        createLabResult.setHeartBeat("97");
        createLabResult.setTemperature("95");
        createLabResult.setOxygenLevel("98");
        createLabResult.setComments("admit to hospital");
        createLabResult.setResult(TestStatus.POSITIVE);

        // return null; // Replace this line with your code
        return createLabResult;
    }
    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("someuser");
        return user;
    }
    public TestRequest testRequest() {
        TestRequest testRequest = new TestRequest();
        testRequest.setAddress("some Addres");
        testRequest.setAge(98);
        testRequest.setEmail("someone" + "123456789" + "@somedomain.com");
        testRequest.setGender(Gender.MALE);
        testRequest.setName("someuser");
        testRequest.setPhoneNumber("123456789");
        testRequest.setPinCode(716768);
        return testRequest;
    }

}