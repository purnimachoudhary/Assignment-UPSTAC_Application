package org.upgrad.upstac.testrequests;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.Contracts;
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
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.consultation.ConsultationController;
import org.upgrad.upstac.testrequests.consultation.CreateConsultationRequest;
import org.upgrad.upstac.testrequests.consultation.DoctorSuggestion;
import org.upgrad.upstac.testrequests.lab.CreateLabResult;
import org.upgrad.upstac.testrequests.lab.LabResult;
import org.upgrad.upstac.testrequests.lab.TestStatus;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
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
class ConsultationControllerTest {


    @InjectMocks
    ConsultationController consultationController;


    @Mock
    TestRequestQueryService testRequestQueryService;

    @Mock
    TestRequestUpdateService testRequestUpdateService;

    @Mock
    UserLoggedInService userLoggedInService;


    @Test
    public void calling_assignForConsultation_with_valid_test_request_id_should_update_the_request_status(){

        //Implement this method

        //Create another object of the TestRequest method and explicitly assign this object for Consultation using assignForConsultation() method
        // from consultationController class. Pass the request id of testRequest object.

        //Use assertThat() methods to perform the following two comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'DIAGNOSIS_IN_PROCESS'
        // make use of assertNotNull() method to make sure that the consultation value of second object is not null
        // use getConsultation() method to get the lab result

        //Arrange
        User user = createUser();
        TestRequest  mockedTestRequest = testRequest();

        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.assignForConsultation(21L,user)).thenReturn(mockedTestRequest);

        //Act
        TestRequest testRequest = consultationController.assignForConsultation(21L);

        // Assert
        Contracts.assertNotNull(testRequest);
        assertEquals(testRequest,mockedTestRequest);


    }

    public TestRequest getTestRequestByStatus(RequestStatus status) {
        return testRequestQueryService.findBy(status).stream().findFirst().get();
    }

    @Test
    public void calling_assignForConsultation_with_Invalid_test_request_id_should_throw_exception(){

        Long InvalidRequestId= -34L;

        //Implement this method


        // Create an object of ResponseStatusException . Use assertThrows() method and pass assignForConsultation() method
        // of consultationController with InvalidRequestId as Id


        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"

        //Arrange
        User user = createUser();

        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.assignForConsultation(InvalidRequestId,user)).thenThrow(new AppException("Invalid ID or State"));
        //Act
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            consultationController.assignForConsultation(InvalidRequestId);
        });

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("Invalid ID or State",result.getReason());

    }

    @Test
    public void calling_updateConsultation_with_valid_test_request_id_should_update_the_request_status_and_update_consultation_details(){

        //Implement this method
        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object. Pass the above created object as the parameter

        //Create another object of the TestRequest method and explicitly update the status of this object
        // to be 'COMPLETED'. Make use of updateConsultation() method from consultationController class (Pass the previously created two objects as parameters)

        //Use assertThat() methods to perform the following three comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'COMPLETED'
        // 3. the suggestion of both the objects created should be same.
        // Compare the getSuggestion() method for CreateConsultationRequest object
        // with the getConsultation.getSuggestion() method for TestRequest object

        //Arrange
        User user = createUser();
        TestRequest  mockedTestRequest = testRequest();
        mockedTestRequest.setStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);
        CreateConsultationRequest createConsultationRequest = getCreateConsultationRequest(mockedTestRequest);

        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.updateConsultation(21L,createConsultationRequest,user)).thenReturn(mockedTestRequest);

        //Act
        TestRequest testRequest = consultationController.updateConsultation(21L,createConsultationRequest);

        // Assert
        assertNotNull(testRequest);
        assertEquals(testRequest,mockedTestRequest);

    }


    @Test
    public void calling_updateConsultation_with_invalid_test_request_id_should_throw_exception(){

        //Implement this method

        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object. Pass the above created object as the parameter

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateConsultation() method
        // of consultationController with a negative long value as Id and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method


        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"
        Long InvalidRequestId= -34L;

        //Arrange
        User user = createUser();
        TestRequest  mockedTestRequest = testRequest();
        mockedTestRequest.setStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);
        CreateConsultationRequest createConsultationRequest = getCreateConsultationRequest(mockedTestRequest);

        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.updateConsultation(InvalidRequestId,createConsultationRequest,user)).thenThrow(new AppException("Invalid ID or State"));

        //Act
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            consultationController.updateConsultation(InvalidRequestId,createConsultationRequest);
        });

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("Invalid ID or State",result.getReason());

    }

    @Test
    public void calling_updateConsultation_with_invalid_empty_status_should_throw_exception(){

        //Implement this method

        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object. Pass the above created object as the parameter
        // Set the suggestion of the above created object to null.

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateConsultation() method
        // of consultationController with request Id of the testRequest object and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method

        //Arrange
        User user = createUser();
        TestRequest  mockedTestRequest = testRequest();
        mockedTestRequest.setStatus(RequestStatus.INITIATED);
        CreateConsultationRequest createConsultationRequest = getCreateConsultationRequest(mockedTestRequest);

        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.updateConsultation(21L,createConsultationRequest,user)).thenThrow(new AppException("Invalid ID or State"));

        //Act
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            consultationController.updateConsultation(21L,createConsultationRequest);
        });

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("Invalid ID or State",result.getReason());


    }

    public CreateConsultationRequest getCreateConsultationRequest(TestRequest testRequest) {

        //Create an object of CreateLabResult and set all the values
        // if the lab result test status is Positive, set the doctor suggestion as "HOME_QUARANTINE" and comments accordingly
        // else if the lab result status is Negative, set the doctor suggestion as "NO_ISSUES" and comments as "Ok"
        // Return the object
        CreateConsultationRequest createConsultationRequest = new CreateConsultationRequest();

        createConsultationRequest.setSuggestion(DoctorSuggestion.HOME_QUARANTINE);
        createConsultationRequest.setComments("should be left alone");

        return createConsultationRequest;

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
        testRequest.setStatus(RequestStatus.LAB_TEST_COMPLETED);
        return testRequest;
    }
}