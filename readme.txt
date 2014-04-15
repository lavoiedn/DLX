This is a quick implementation of the DLX algorithm in java. It takes
an exact cover problem and returns the first solution it finds.

I did not include a list of sudoku with the source code, as it would have
simply made the file bigger.

The code requires the problems to be written as single lines of 81 integers
between 0 and 9. Each sudoku must separated by a new line.

If you are having trouble getting some sudokus, Gordon Royle and the University
of Western Australia offer a large collection of them on the following website:
http://school.maths.uwa.edu.au/~gordon/sudokumin.php


How to run:

Your command line should look like this:

java Sudoku "filePath" "numberOfGames"