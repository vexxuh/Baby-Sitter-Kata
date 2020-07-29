package com.stuel.babysitterkata.processors;

import com.stuel.babysitterkata.submissionforms.BabySitterWorkSubmissionForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class BabySitterWageProcessor {

    private final Logger LOGGER = LoggerFactory.getLogger(BabySitterWageProcessor.class);

    /**
     * Calculates the PayCheckAmount value for the passed BabySitterWorkSubmissionForm
     * object. If a value already exists for PayCheckAmount then it will be OVERWRITTEN
     * and updated to the contain the new calculated value!
     * @param submissionForm
     *      a form object to be updated to contain pay
     */
    public BabySitterWorkSubmissionForm processTotalPay(BabySitterWorkSubmissionForm submissionForm) {
        int startTime;
        int endTime;
        int bedTime;
        try {
            // converts string values from the user input to local date time for time comparision operations
            // for calculating pay!
            startTime = Integer.parseInt(submissionForm.getShiftStartTime());
            endTime = Integer.parseInt(submissionForm.getShiftEndTime());
            bedTime = Integer.parseInt(submissionForm.getChildBedTime());

        } catch(NumberFormatException numberFormatException) {
            LOGGER.error("Could not parse number to Integer please check data entry", numberFormatException);
            submissionForm.setPayCheckAmount("Uh Oh! Could not calculate your payment. Please check your entries and make sure" +
                    "they are valid numbers");
            return submissionForm;
        }
        // gets the total amount of money due at the end of the night at a shift and saves that to the POJO
        submissionForm.setPayCheckAmount( "$"+ totalPayCalculator(startTime, endTime, bedTime));
        return submissionForm;
    }

    /**
     * Calculates the total earning of a shift!
     * Internal convention:
     *      a day cycle is measured in 24 hours so if anything is after the current day,
     *      such as the endtime of the shift, then it will add the hours to the total 24.
     *      So if someone ends their shift at the max amount of time, 4:00 am, allowed then
     *      the value be used to calculate pay for midnight to end of shift would be 28.
     * @param startTime
     *      the time the baby-sitter shift begins
     * @param endTime
     *      the time the baby-sitter shift ends
     * @param bedTime
     *      the time the child will go to bed: must be >=0:00 or midnight
     * @return
     *      String value representing the total amount to be paid
     */
    public String totalPayCalculator(int startTime, int endTime, int bedTime) {

        ///////////////////////////////
        //convention defining principles
        ///////////////////////////////

        // this creates the convention that a new days time is greater than 24 hours so a shift's
        // max length is 28 or 4:00 am the next day
        int endTimeConventionValue = endTime;
        if(endTime <= 4) {
            endTimeConventionValue = endTime + 24;
        }
        // if the user enters 0 for midnight then change that value to 24 to keep the convention
        // or if the value is set to be after midnight then make sure to add the value to keep convention
        int bedTimeConventionValue = bedTime;
        if(bedTime == 0 || bedTime <= 4){
            bedTimeConventionValue = 24 + bedTime;
        }

        /////////////////////////////////////
        // bounds check for allowed intervals
        /////////////////////////////////////

        // if the start-time is before the allowed start time then return a string to notify the user
        // that a correction needs to be made to the start-time value
        if(startTime < 17) {
            LOGGER.error("Invalid start time: " + startTime);
            return "The start-time that was entered is not a valid start-time, please enter a new start-time";
        }
        // if the end-time is after the allowed end time then return a string to notify the user
        // that a correction needs to be made to the end-time value
        if(endTimeConventionValue > 28 || endTimeConventionValue < bedTimeConventionValue) {
            LOGGER.error("Invalid end time: " + endTime);
            return "The end-time you have entered is not a valid end-time, please enter a new end-time";
        }
        // if the child's bedtime is after midnight then return a string to notify the user the input is incorrect
        if(bedTimeConventionValue < startTime || bedTimeConventionValue > 24) {
            LOGGER.error("Invalid bedtime: " + bedTime);
            return "The bedtime you have entered is not a valid bedtime, please enter the correct bedtime";
        }

        ////////////////////
        // wage calculations
        ////////////////////

        // get the difference between each time interval that is possible
        long startTimeToBedTimeDifference = bedTimeConventionValue - startTime;
        long bedTimeToMidnightDifference = 0;
        long midnightToEndTimeDifference = 0;
        // if the end time is less than midnight then..
        if(endTimeConventionValue <= 24) {
            // .. as long as its end time is not equals to the bed time the subtract the endtime and bed time to get the
            // difference
            if(endTimeConventionValue != bedTimeConventionValue) {
                bedTimeToMidnightDifference = endTimeConventionValue - bedTimeConventionValue;
            }
        // otherwise if the bedtime is after midnight then..
        } else {
            // .. subtract the difference from midnight from bed time to get the hour payed
            bedTimeToMidnightDifference = 24 - bedTimeConventionValue;
            // then subtract the difference from midnight to end of shift time to get the hours to be payed
            midnightToEndTimeDifference = endTimeConventionValue - 24;
        }
        // multiply the differences by the rate that is earned for each interval
        long startToBedAmount = startTimeToBedTimeDifference * 12L;
        long bedToMidnightAmount = bedTimeToMidnightDifference * 8L;
        long midnightToEndAmount = midnightToEndTimeDifference * 16L;

        // then add all of the wage intervals together to get the total amount paid for the shift
        long totalShiftPay = startToBedAmount + bedToMidnightAmount + midnightToEndAmount;

        return totalShiftPay + "";
    }
}
