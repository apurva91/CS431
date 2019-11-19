import Data.List
import System.IO
import Data.Maybe
import Data.Char (ord)
import System.Exit (die)
import System.Directory (doesFileExist)
import qualified Data.Set as Set
-- Importing all the required packages

-- For debugging purposes
import Debug.Trace (trace)
debug = flip trace

-- Constants
blank = '-'
filename = "input.txt"

-- Function to get the frequency of each number from 0 to 9
getCount :: [Integer] -> [Integer]
getCount a = [ toInteger(length ( filter (==x) a)) | x <- [0..9] ]

-- Function to see if a given list has count of all numbers except 0 less than or equal to 1
isValidList :: [Integer]-> Bool -> Bool
isValidList [] _ = True
isValidList (x:xs) True = isValidList xs False 
isValidList (x:xs) False = x<=1 && isValidList xs False

-- Helper Function thats calls to see if all the lists present are valid. If not breaks.
areValidLists :: [[Integer]] -> Bool
areValidLists [] = True
areValidLists (x:xs) =   isValidList  y True && areValidLists xs where y = getCount x

-- Generate all possible type of lists to check validity Horizontal, Vertical, Box wise
generateLists :: [[Integer]] -> [[Integer]]
generateLists a = (a ++ [ [ a!!y!!x |  y <- [0..(len-1)] ] | x <- [0..(len-1)] ]  ++ [ [ (a!!(y*sub+w))!!(x*sub+z)| w<-[0..(sub-1)] , z<-[0..(sub-1)] ] | y<-[0..(sub-1)] , x<-[0..(sub-1)]])
    where len = length(a) 
          sub = fromInteger(floor(sqrt(realToFrac(length(a)))))

-- Given a list of lists replaces element at (i,j) using take and drop functions
replaceIJ :: [[Integer]] -> Int -> Int -> Integer -> [[Integer]]
replaceIJ m i j k = p
    where 
        z = m!!i
        p = take i m ++ [  take j z ++ [k] ++ (drop (j+1) z) ] ++ (drop (i+1) m)

-- Helper function to generate all lists and check whether they are valid
check_valid :: [[Integer]] -> Bool
check_valid a = (areValidLists . generateLists ) a

{- Solves for a index i,j in the matrix first checks if matrix till now is valid if not return 
    then if we have reached the end after checking all entires are valid this is our answer so return the matrix and return True
    If the i,j entry is already filled move to the next entry and return its output
    If i,j is not filled then try filling it with the numbers once. We use the fact that haskell is lazy over here and once we get one anwer we dont look for anymore
-}

solveIndex :: Int -> Int -> [[Integer]] -> Integer -> (Bool,[[Integer]])
solveIndex i j matrix len|
        (check_valid(matrix)==False) = (False,[]) |
        ( j == fromInteger(len) ) = (True,matrix)  | --`debug` show(matrix)
        (matrix!!j!!i /= 0) = ( solveIndex ( (i+1) `mod` fromInteger(len) ) ( j + ((i+1) `div` fromInteger(len)) ) matrix len)  |
        (null q) = (False,[])|
        otherwise = q!!0 
        where 
            q = (take 1 [  z  | x <- [1..len] , z <- [(solveIndex ( (i+1) `mod` fromInteger(len) ) ( j + ((i+1) `div` fromInteger(len)) ) (replaceIJ matrix j i x) len )], fst(z)==True ])

-- Covnerts Digit Char to Int
digitToInt :: Char -> Int
digitToInt x = fromEnum(x) - fromEnum('0') 

-- Solve function it takes input replaces all creates a mapping that converts symbols to numbers from (0..4) (0..9) depending on the input solves it and replaces the numbers back with the mapping
solve :: Integer -> [String] -> (Bool, [String])
solve len y = (fst d,k)
    where mapping = (blank: Set.toList(Set.fromList([ y!!fromInteger(i)!!fromInteger(j) | i <- [0..len-1] , j <- [0..len-1], y!!fromInteger(i)!!fromInteger(j) /=blank ])))
          matrix = ([ [ toInteger(fromJust (elemIndex (y!!fromInteger(i)!!fromInteger(j)) mapping )) | j<-[0..len-1] ] | i <- [0..len-1] ])
          d = (solveIndex 0 0 matrix len)
          k = ([ [ mapping!!(fromInteger( (snd d) !!fromInteger(i)!!fromInteger(j)))  | j<-[0..len-1] ] | i <- [0..len-1] , fst(d)==True ])

-- Checks whether input string lengths are valid
inputValid :: Int -> [String] -> Bool
inputValid x y = (length(y)==x) && (foldl (&&) True [length(z)==x | z <- y])

-- Checks whether each symbol occurs only once and all the required symbols are present
getValid :: Int -> String -> Bool
getValid y xs = y == length( Set.fromList(xs) ) && y == length(xs) 

-- solver First see if the matrix is of size 4 & 9 if no return else check for valid input matrix and solve it
solver :: Int -> [String] -> (Bool,[String])
solver x y |
        (x == 4 || x == 9 ) && (inputValid x y) == False = (False, ["Size mismatch."]) |
        (x == 4 || x == 9 ) && ((getValid x ([ y!!i!!j | i <- [0..x-1] , j <- [0..x-1], y!!i!!j /=blank ]) ) ==False) = (False,["One occurence of each symbol only."])|
        (x == 4 || x == 9 ) =  (solve (toInteger(x)) y) |
        otherwise = (False,["Valid Sizes 4 & 9 Only."])

-- Problem can be solved using just the solver but main is for reading from external file

main = do 
    -- check if file exists if not exit 
    x <- doesFileExist(filename)
    
    if x then
        putStr("")
    else 
        die("Input file " ++ filename ++ " not found.")
    
    -- Read the input if empty exit
    content <- readFile "input.txt"

    if length(content) == 0 then
        die("Empty File.")
    else
        putStr("")

    -- Check if valid length given in the first line where input size is expected
    let len = digitToInt((lines content)!!0!!0)

    if length((lines content)!!0) /= 1 then
        die("Invalid Input Size, First Line One Character 4 or 9.")
    else
        putStrLn("Input Size: " ++ [(lines content)!!0!!0]++"\nInput Matrix:"++(drop 1 content))

    -- Creating the matrix 
    let matrix = (drop 1 (lines content))
    -- Finding the answer
    let ans = (solver len matrix)
    -- Print the solution accordingly, If there was some error exit else return answer (Not possible/solution)
    let z = (foldl (++) "" [ "\n"++y | y<- (snd ans)])
    
    if length(z) == 0 then
        putStrLn("No Solution Possible.")
    else
        if (fst ans) then
            putStrLn("Solution:" ++ z)
        else
            die(z)
