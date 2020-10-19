package com.stuel.babysitterkata.processors;

import com.stuel.babysitterkata.submissionforms.BabySitterWorkSubmissionForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BabySitterWageProcessorTest {
    private BabySitterWageProcessor wageProcessor;

    @BeforeEach
    void setup() {
         wageProcessor = new BabySitterWageProcessor();
    }
    @Test
    void userErrorOutOfRangeDataForTotalPayCalculatorOfStartTimeBefore5() {
        int startTime = 15;// before the allowed start time of 5 p.m
        int endTime = 3;
        int bedTime = 22;
        String errorMessageForBeforeAllowedStartTime =
                wageProcessor.totalPayCalculator(startTime, endTime, bedTime);
        assertEquals(errorMessageForBeforeAllowedStartTime,
                "The start-time that was entered is not a valid start-time, please enter a new start-time");
    }

    @Test
    void userErrorOutOfRangeDataForTotalPayCalculatorOfEndTimeAfterAllowedTime() {
        int startTime = 17;
        int endTime = 5; // invalid end time
        int bedTime = 22;
        String errorMessageForAfterAllowedWorkTime =
                wageProcessor.totalPayCalculator(startTime, endTime, bedTime);
        assertEquals(errorMessageForAfterAllowedWorkTime,
                "The end-time you have entered is not a valid end-time, please enter a new end-time");
    }

     @Test
    void testRandomValues1() {
        int startTime = 19;
        int endTime = 1;
        int bedTime = 24;
        String errorMessageForBeforeAllowedStartTime =
                wageProcessor.totalPayCalculator(startTime, endTime, bedTime);
        assertEquals(errorMessageForBeforeAllowedStartTime,
                "76");
    }

    @Test
    void testRandomValues2() {
        int startTime = 20;
        int endTime = 2;
        int bedTime = 21;
        String errorMessageForBeforeAllowedStartTime =
                wageProcessor.totalPayCalculator(startTime, endTime, bedTime);
        assertEquals(errorMessageForBeforeAllowedStartTime,
                "68");
    }

    @Test
    void testRandomValues3() {
        int startTime = 17;
        int endTime = 24;
        int bedTime = 17;
        String errorMessageForBeforeAllowedStartTime =
                wageProcessor.totalPayCalculator(startTime, endTime, bedTime);
        assertEquals(errorMessageForBeforeAllowedStartTime,
                "56");
    }

    @Test
    void testRandomValues4() {
        int startTime = 17;
        int endTime = 21;
        int bedTime = 19;
        String errorMessageForBeforeAllowedStartTime =
                wageProcessor.totalPayCalculator(startTime, endTime, bedTime);
        assertEquals(errorMessageForBeforeAllowedStartTime,
                "40");
    }

    @Test
    void testRandomValues5() {
        int startTime = 19;
        int endTime = 4;
        int bedTime = 23;
        String errorMessageForBeforeAllowedStartTime =
                wageProcessor.totalPayCalculator(startTime, endTime, bedTime);
        assertEquals(errorMessageForBeforeAllowedStartTime,
                "120");
    }

    @Test
    void testRandomValues6() {
        int startTime = 19;
        int endTime = 3;
        int bedTime = 23;
        String errorMessageForBeforeAllowedStartTime =
                wageProcessor.totalPayCalculator(startTime, endTime, bedTime);
        assertEquals(errorMessageForBeforeAllowedStartTime,
                "104");
    }

    @Test
    void testRandomValues7() {
        int startTime = 19;
        int endTime = 3;
        int bedTime = 5;
        String value =
                wageProcessor.totalPayCalculator(startTime, endTime, bedTime);
        assertEquals(value,
                "88");
    }

    @Test
    void userErrorProcessPayTestInvalidInput(){
        BabySitterWorkSubmissionForm expectForm =
                new BabySitterWorkSubmissionForm();
        expectForm.setShiftStartTime("17");
        expectForm.setShiftEndTime("Invalid Input");
        expectForm.setChildBedTime("23");
        expectForm = wageProcessor.processTotalPay(expectForm);
        BabySitterWorkSubmissionForm actualForm =
                new BabySitterWorkSubmissionForm();
        actualForm.setShiftStartTime("17");
        actualForm.setShiftEndTime("Invalid Input");
        actualForm.setChildBedTime("23");
        actualForm.setPayCheckAmount("Uh Oh! Could not calculate your payment. Please check your entries and make " +
                "sure they are valid numbers");
        assertEquals(expectForm, actualForm);
    }

    @Test
    void processWageRandomTest(){
        BabySitterWorkSubmissionForm expectForm =
                new BabySitterWorkSubmissionForm();
        expectForm.setShiftStartTime("17");
        expectForm.setShiftEndTime("21");
        expectForm.setChildBedTime("18");
        expectForm = wageProcessor.processTotalPay(expectForm);
        BabySitterWorkSubmissionForm actualForm =
                new BabySitterWorkSubmissionForm();
        actualForm.setShiftStartTime("17");
        actualForm.setShiftEndTime("21");
        actualForm.setChildBedTime("18");
        actualForm.setPayCheckAmount("$36");
        assertEquals(expectForm, actualForm);
    }
}