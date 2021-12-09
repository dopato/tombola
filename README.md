# Bingo 90 tickets generator
This is an application that will generate tickets to play bingo 90(housie bingo, tombola).

# Requirements

Generate a strip of 6 tickets
Tickets are created as strips of 6, because this allows every number from 1 to 90 to appear across all 6 tickets. If they buy a full strip of six it means that players are guaranteed to mark off a number every time a number is called.
A bingo ticket consists of 9 columns and 3 rows.
Each ticket row contains five numbers and four blank spaces
Each ticket column consists of one, two or three numbers and never three blanks.
The first column contains numbers from 1 to 9 (only nine),
The second column numbers from 10 to 19 (ten), the third, 20 to 29 and so on up until
The last column, which contains numbers from 80 to 90 (eleven).
Numbers in the ticket columns are ordered from top to bottom (ASC).
There can be no duplicate numbers between 1 and 90 in the strip (since you generate 6 tickets with 15 numbers each)
Please make sure you add unit tests to verify the above conditions and an output to view the strips generated (command line is ok).

Try to also think about the performance aspects of your solution. How long does it take to generate 10k strips? The recommended time is less than 1s (with a lightweight random implementation)

#Consideration
The execution time unfortunately is bigger than 1 sec, however less than 4 secs.

I am using a ThreadLocalRandom which should perform better while multithreading.

Preferred an ExecutorService which should be good to run independent tasks in parallel.
I have used a WorkStealingPool which seems performing slightly better than the FixedThreadPool,also tried to apply the formula
Number of threads = Number of Available Cores * (1 + Wait time / Service time) which considering 1 ms waiting and 250 ms service 
time won't go far from 1,004 so preferred to just keep the number of threads.

Only part that is using a parallel stream is while creating tickets from columns.

For Row and Column internal implementation is using an array, trying to improve performances.

Jprofiler could only highlight issue related with logback which in the end is not making execution time that bad.

#Thing to try to lower execution time
Maybe uses of stream could be replace with for loops and operation on arrays, however that will make the code very hard to maintain 
and for me this was a good exercise for practicing functional programming .
