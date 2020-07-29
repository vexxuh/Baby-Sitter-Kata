# Baby-sitter Kata - Ohio Health
**[Description of how to download/clone and run the code will go here.]**

## Description of kata
Taken from https://gist.github.com/jameskbride/5482722. (Preservation fork here: https://gist.github.com/jduffey/dd68b22689713ab575d57b2119949f8c)

This kata simulates a babysitter(me) working and getting paid for one night.  The rules are as follows:

Baby-sitter shift and p1ay rules:
- starts no earlier than 5:00PM
- leaves no later than 4:00AM
- gets paid $12/hour from start-time to bedtime
- gets paid $8/hour from bedtime to midnight
- gets paid $16/hour from midnight to end of job
- gets paid for full hours (no fractional hours)

Feature:
As a baby-sitter, I want to calculate my
nightly earnings.  

## Pre-coding notes
- The goal of this exercise is to find the **total amount** of money I will receive after one night 
- I can simply have a method that will take three different parameters: 
	- The **starting-time** of my shift
	- The **ending-time** of my shift
	- The **bedtime** of the child I am baby-sitting
- I am also thinking of a very basic UI that I can use as well. The UI will allow me to enter input(Starting-time, ending-time, and bed-time)
 and get the output(total amount paid). This would be better than running a java main method after I come home from my late shifts.

- I can break down what I know about the problem like so (thinking about the problem):
    - I know that none of my shifts will start before 5:00pm and will go later than 4:00am. So I am going to create a
    check that ensures my times are not inputted incorrectly and the contract between me and the client is not broken.
    - I know that regardless of the hours given to work the rates are solid, for example start-time: 6:00pm, end-time: 12:00am, and 
    bed-time: 10:00. 
        - The child is awake from 6 to 10pm so I know I wil get paid 12 dollars/per hour for 4 hours. I also know I will get, for those 
        last 2 hours 8 dollars/hour. Since I don't work past midnight I won't see the 16 dollars an hour rate.
    There is a very interesting problem here under the covers. Since the hours given for my or another 
    baby-sitters start time is given the key words "no earlier" and "no later." This implies that these values are changing
    and can be anything in the interval [5:00pm - 4:00am]. What if the child's bed time was after midnight? Then would I get paid
    the start-time to bed-time rate or the midnight to end of job rate? Since this requires me make some assumptions that may be
    incorrect or not clear I am going to state the problem's assumptions below.   
        
 
## Problem Assumptions
- Since this problem is a more of a "simulation" of sorts I am going to assume the following:
    - My clients, or the child's parents, are going to have the child go to bed before or at 12:00am    
- Please Note: that making this assumption changes the problem such that I only have to deal with three base
time intervals [start-time(>=5:00pm) - bedtime(<=12:00am)], [bedtime(<=12:00am) - midnight(=12:00)], 
and [midnight(=12:00am) - end-time(<=4:00am)].

- Also Note: that if this was an application I was building for the public or even internal use, practically. I would
get clarification from the clients themselves which rate I would get or even suggest changing rates to 
meet this kind of condition. However, for the scope of the problem I am going to assume the above assumptions. 

## Pseudocode(idea of how I am going to solve the problem)
- A UI(very basic html page) will take three inputs:
	1. A start-time >= 5:00 pm
	2. An end-time <= 4:00 am
	3. A bedtime >=12:00am
- Once the input is entered, calculate the total amount due by the client by:
    1. First looking at the start-time to bedtime interval and determining how many intervals of time to be paid
        (i.e. [start-time(>=5:00pm) - bedtime(<12:00am)] and [bedtime(<=12:00am) - midnight(=12:00)]) Then calculate the
        total amount due for this lot of time. If there is one time interval [start-time(>=5:00pm) - bedtime(=12:00am)] then 
        take that difference in hours and multiply that by the rate of 12 dollars/hour. If the bedtime is less than midnight then calculate
        the time between the bedtime and midnight and apply the rate of 8 dollars/hour.
         
    2.  Finally, I will check the end-time and compare that with midnight value. If the value is before midnight then subtract the difference
        between the end-time and the bedtime and then subtract the rate from the total. Otherwise, I will find the difference between
        midnight and the end-time and add the 16 dollars/hour rate to the total. 
- Now that I have the total amount that is due for the client I will finally display that value to the UI in a
  total amount due field below the input fields.  