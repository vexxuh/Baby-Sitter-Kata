package com.stuel.babysitterkata.processors;

import com.stuel.babysitterkata.submissionforms.BabySitterWorkSubmissionForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class BabySitterWageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BabySitterWageProcessor.class);

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
     *      the time the child will go to bed
     * @return
     *      String value representing the total amount to be paid
     */
    public String totalPayCalculator(int startTime, int endTime, int bedTime) {

        ///////////////////////////////
        //convention defining principles
        ///////////////////////////////
        int endTimeConventionValue = getTimeConventionValue(endTime);
        int bedTimeConventionValue = getTimeConventionValue(bedTime);
        int startTimeConventionValue = getTimeConventionValue(startTime);

        /////////////////////////////////////
        // bounds check for allowed intervals
        /////////////////////////////////////

        // if the start-time is before the allowed start time then return a string to notify the user
        // that a correction needs to be made to the start-time value
        if(startTimeConventionValue < 17) {
            LOGGER.error("Invalid start time: " + startTime);
            return "The start-time that was entered is not a valid start-time, please enter a new start-time";
        }
        // if the end-time is after the allowed end time then return a string to notify the user
        // that a correction needs to be made to the end-time value and if the bedTime
        if(endTimeConventionValue > 28 || endTimeConventionValue < startTime) {
            LOGGER.error("Invalid end time: " + endTime);
            return "The end-time you have entered is not a valid end-time, please enter a new end-time";
        }
        ////////////////////
        // wage calculations
        ////////////////////

        // There are three different if cases that look at the bed time. If the bedtime is before the start-time,
        // if the bedtime is between the start-time and the end-time, or if the bedtime is after the end-time
        long totalShiftPay = 0;

        if(startTimeConventionValue < 24) {
            if (bedTimeConventionValue <= startTimeConventionValue) {
                totalShiftPay = getPayForBedTimeBeforeStartTime(startTimeConventionValue, endTimeConventionValue);
            }

            if (bedTimeConventionValue >= endTimeConventionValue) {
                totalShiftPay = getPayForBedTimeAfterEndTime(startTimeConventionValue, endTimeConventionValue);
            }

            if (startTimeConventionValue < bedTimeConventionValue && bedTimeConventionValue < endTimeConventionValue) {
                totalShiftPay = getPayForBedTimeBetweenStartAndEndTime(startTimeConventionValue, endTimeConventionValue,
                                                                        bedTimeConventionValue);
            }
        } else if(startTimeConventionValue >= 24) {
            totalShiftPay = ((endTimeConventionValue - startTimeConventionValue) * 16);
        }
        return totalShiftPay + "";
    }

    /**
     * Establishes that the convention of over a day in ints is something that is greater than 24. So 1 am on the same
     * shift would be 25 hours.
     *
     * IMPORTANT: This method is just a cleaning tool to make sure that the user data is being correctly calculated
     * meaning that the user will enter a value that is something like 4 am for shift end-time. Thus the way to look at
     * 4 am, and not worry about negatives and all that silly stuff, is to just add 24 to the total time so 24 + 4 = 28.
     * meaning that the hour that is currently is in the convention is 28 hours.
     * @param nonConventionTime
     *          The time that needs to be converted to conventional time units
     * @return
     *      an int that follows the int convention described above
     */
    public int getTimeConventionValue(int nonConventionTime) {
        int conventionTimeValue = nonConventionTime;
        if(nonConventionTime <= 4) {
            conventionTimeValue = 24 + nonConventionTime;
        }
        return conventionTimeValue;
    }

    /**
     * gets the pay for when the child goes to bed before the sitter, or just as the sitter arives
     * @param startTimeConventionValue
     *      start-time in convention format
     * @param endTimeConventionValue
     *      end-time in convention format
     * @return
     *      pay for a bedtime that is before or equal to the start time
     */
    private int getPayForBedTimeBeforeStartTime(int startTimeConventionValue, int endTimeConventionValue) {
        int totalShiftPay = 0;
        if (endTimeConventionValue <= 24) {
            totalShiftPay = (endTimeConventionValue - startTimeConventionValue) * 8;
            // otherwise, get the start-time to midnight rate then get the midnight to end-time rate
        } else {
            totalShiftPay = ((24 - startTimeConventionValue) * 8) + ((endTimeConventionValue - 24) * 16);
        }
        return totalShiftPay;
    }

    /**
     * gets the pay for when the child goes to bed after the sitter leaves
     * @param startTimeConventionValue
     *      start-time in convention format
     * @param endTimeConventionValue
     *      end-time in convention format
     * @return
     *      pay for a bedtime that is after the end-time
     */
    private int getPayForBedTimeAfterEndTime(int startTimeConventionValue, int endTimeConventionValue) {
        int totalShiftPay = 0;
        if (endTimeConventionValue <= 24) {
            totalShiftPay = (endTimeConventionValue - startTimeConventionValue) * 12;
        } else {
            totalShiftPay = ((24 - startTimeConventionValue) * 12) + ((endTimeConventionValue - 24) * 16);
        }
        return totalShiftPay;
    }

    /**
     * gets the pay for a bedtime that is between the start and end time
     * @param startTimeConventionValue
     *      start-time in convention format
     * @param endTimeConventionValue
     *      end-time in convention format
     * @param bedTimeConventionValue
     *      bedtime in convention format
     * @return
     *      pay for a bedtime that is between the start and end-times
     */
    private int getPayForBedTimeBetweenStartAndEndTime(int startTimeConventionValue, int endTimeConventionValue,
                                                       int bedTimeConventionValue) {
        int totalShiftPay = 0;
        if (endTimeConventionValue <= 24) {
            totalShiftPay = ((bedTimeConventionValue - startTimeConventionValue) * 12) +
                    ((endTimeConventionValue - bedTimeConventionValue) * 8);
        } else if (bedTimeConventionValue > 24) {
            totalShiftPay = ((24 - startTimeConventionValue) * 12) + ((endTimeConventionValue - 24) * 16);
        } else if (bedTimeConventionValue <= 24 && endTimeConventionValue > 24) {
            totalShiftPay = ((bedTimeConventionValue - startTimeConventionValue) * 12) +
                    ((24 - bedTimeConventionValue) * 8) + ((endTimeConventionValue - 24) * 16);
        }
        return totalShiftPay;
    }
}
