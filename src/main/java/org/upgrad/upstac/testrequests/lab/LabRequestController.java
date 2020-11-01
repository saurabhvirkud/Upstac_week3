package org.upgrad.upstac.testrequests.lab;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.testrequests.TestRequestUpdateService;
import org.upgrad.upstac.testrequests.flow.TestRequestFlowService;
import org.upgrad.upstac.users.User;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.upgrad.upstac.exception.UpgradResponseStatusException.asBadRequest;
import static org.upgrad.upstac.exception.UpgradResponseStatusException.asConstraintViolation;


@RestController
@RequestMapping("/api/labrequests")
public class LabRequestController {

    Logger log = LoggerFactory.getLogger(LabRequestController.class);




    @Autowired
    private TestRequestUpdateService testRequestUpdateService;

    @Autowired
    private TestRequestQueryService testRequestQueryService;
    @Autowired
    private TestRequestFlowService testRequestFlowService;



    @Autowired
    private UserLoggedInService userLoggedInService;



    @GetMapping("/to-be-tested")
    @PreAuthorize("hasAnyRole('TESTER')")
    public List<TestRequest> getForTests()  {

        //Implement this method to return the list of test requests having status as 'INITIATED'
        //Make use of the findBy() method from testRequestQueryService class to get the list
        // For reference check the method requestHistory() method from TestRequestController class
        return testRequestQueryService.findBy(RequestStatus.INITIATED); // replace this line with your code

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TESTER')")
    public List<TestRequest> getForTester()  {
        User user = userLoggedInService.getLoggedInUser();

        // Create an object of User class and store the current logged in user first
        //Implement this method to return the list of test requests assigned to current tester(make use of the above created User object)
        //Make use of the findByTester() method from testRequestQueryService class to get the list
        // For reference check the method getPendingTests() method from TestRequestController class

        return testRequestQueryService.findByTester(user); // replace this line with your code


    }


    @PreAuthorize("hasAnyRole('TESTER')")
    @PutMapping("/assign/{id}")
    public TestRequest assignForLabTest(@PathVariable Long id) {

        // Implement this method to assign a particular test request to the current tester(logged in user)
        //Create an object of User class and get the current logged in user
        //Create an object of TestRequest class and use the assignForLabTest() method of testRequestUpdateService to assign the particular id to the current user
        // return the above created object
        // Refer to the method createRequest() from the TestRequestController class

        try {
            User user = userLoggedInService.getLoggedInUser();// replace this line of code with your implementation
            TestRequest assign = testRequestUpdateService.assignForLabTest(id,user);
            return assign;
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('TESTER')")
    @PutMapping("/update/{id}")
    public TestRequest updateLabTest(@PathVariable Long id,@RequestBody CreateLabResult createLabResult) {


        // Implement this method to update the result of the current test request id with test results
        // Create an object of the User class to get the logged in user
        // Create an object of TestResult class and make use of updateLabTest() method from testRequestUpdateService class
        //to update the current test request id with the createLabResult details by the current user(object created)
        try {
            User user = userLoggedInService.getLoggedInUser();
            TestRequest labResult = testRequestUpdateService.updateLabTest(id,createLabResult,user);

            return labResult; // replace this line of code with your implementation

        } catch (ConstraintViolationException e) {
            throw asConstraintViolation(e);
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }





}
