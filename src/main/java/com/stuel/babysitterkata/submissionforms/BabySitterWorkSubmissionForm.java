package com.stuel.babysitterkata.submissionforms;

import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * POJO(plain old java object) for input from web submission form
 * that will store the data for calculating the total amount that
 * I, or any baby-sitter should get paid.
 */
public class BabySitterWorkSubmissionForm {
    private String ShiftStartTime;
    private String ShiftEndTime;
    private String ChildBedTime;
    private String PayCheckAmount;

    //===================================================
    // Getters and Setters
    //===================================================
    public String getShiftStartTime() {
        return ShiftStartTime;
    }

    public void setShiftStartTime(String shiftStartTime) {
        ShiftStartTime = shiftStartTime;
    }

    public String getShiftEndTime() {
        return ShiftEndTime;
    }

    public void setShiftEndTime(String shiftEndTime) {
        ShiftEndTime = shiftEndTime;
    }

    public String getChildBedTime() {
        return ChildBedTime;
    }

    public void setChildBedTime(String childBedTime) {
        ChildBedTime = childBedTime;
    }

    public String getPayCheckAmount() {
        return PayCheckAmount;
    }

    public void setPayCheckAmount(String payCheckAmount) {
        PayCheckAmount = payCheckAmount;
    }

    //===================================================
    // To String Representation
    //===================================================
    @Override
    public String toString() {
        return "BabySitterWorkSubmissionForm{" +
                "ShiftStartTime='" + ShiftStartTime + '\'' +
                ", ShiftEndTime='" + ShiftEndTime + '\'' +
                ", ChildsBedTime='" + ChildBedTime + '\'' +
                '}';
    }

    //===================================================
    // Standard Equals Comparator
    //===================================================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BabySitterWorkSubmissionForm that = (BabySitterWorkSubmissionForm) o;

        if (!Objects.equals(ShiftStartTime, that.ShiftStartTime))
            return false;
        if (!Objects.equals(ShiftEndTime, that.ShiftEndTime)) return false;
        return Objects.equals(ChildBedTime, that.ChildBedTime);
    }

    //===================================================
    // Standard HashCode Implementation
    //===================================================
    @Override
    public int hashCode() {
        int result = ShiftStartTime != null ? ShiftStartTime.hashCode() : 0;
        result = 31 * result + (ShiftEndTime != null ? ShiftEndTime.hashCode() : 0);
        result = 31 * result + (ChildBedTime != null ? ChildBedTime.hashCode() : 0);
        return result;
    }
}
