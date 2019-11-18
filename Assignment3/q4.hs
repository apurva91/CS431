import Data.List
import System.IO
import Debug.Trace
import qualified Data.Set as Set
import Data.Char (ord)
import Data.Maybe
-- matrix = take 4 (repeat (take 4 (repeat (fromInteger(0)))))

-- matrix = [ [ fromInteger (y) | y <- x ] | x<-[[0,0,0,1],[0,2,0,0],[3,0,0,0],[4,0,0,0]]]

-- len = fromInteger(4)
-- sub = fromInteger(floor(sqrt(realToFrac(len))))

getCount :: [Integer] -> [Integer]
getCount a = [ toInteger(length ( filter (==x) a)) | x <- [0..9] ]

isValidList :: [Integer]-> Bool -> Bool
isValidList [] _ = True
isValidList (x:xs) True = isValidList xs False 
isValidList (x:xs) False = x<=1 && isValidList xs False


areValidLists :: [[Integer]] -> Bool
areValidLists [] = True
areValidLists (x:xs) =   isValidList  y True && areValidLists xs where y = getCount x

generateLists :: [[Integer]] -> [[Integer]]
generateLists a = (a ++ [ [ a!!y!!x |  y <- [0..(len-1)] ] | x <- [0..(len-1)] ]  ++ [ [ (a!!(y*sub+w))!!(x*sub+z)| w<-[0..(sub-1)] , z<-[0..(sub-1)] ] | y<-[0..(sub-1)] , x<-[0..(sub-1)]])
    where len = length(a) 
          sub = fromInteger(floor(sqrt(realToFrac(length(a)))))


replaceIJ :: [[Integer]] -> Int -> Int -> Integer -> [[Integer]]
replaceIJ m i j k = p
    where 
        z = m!!i
        p = take i m ++ [  take j z ++ [k] ++ (drop (j+1) z) ] ++ (drop (i+1) m)
    
check_valid :: [[Integer]] -> Bool
check_valid a = (areValidLists . generateLists ) a

printMatrix :: [[Integer]] -> IO (Bool)
printMatrix m = do
    print m;
    return True;

debug = flip trace

solveIndex :: Int -> Int -> [[Integer]] -> Integer -> (Bool,[[Integer]])

solveIndex i j matrix len|
        (check_valid(matrix)==False) = (False,[]) |
        ( j == fromInteger(len) ) = (True,matrix) |
        (matrix!!j!!i /= 0) = ( solveIndex ( (i+1) `mod` fromInteger(len) ) ( j + ((i+1) `div` fromInteger(len)) ) matrix len)  |
        (null q) = (False,[])|
        otherwise = q!!0 
        where 
            q = (take 1 [  z  | x <- [1..len] , z <- [(solveIndex ( (i+1) `mod` fromInteger(len) ) ( j + ((i+1) `div` fromInteger(len)) ) (replaceIJ matrix j i x) len )], fst(z)==True ])

-- solver :: (Bool, [[Integer]])
-- solver = solveIndex 0 0 matrix 

digitToInt :: Char -> Int
digitToInt x = fromEnum(x) - fromEnum(0) 

solve :: Integer -> [String] -> (Bool, [String])

solve len y = (fst d,k)
    where mapping = (' ':[ y!!fromInteger(i)!!fromInteger(j) | i <- [0..len-1] , j <- [0..len-1], y!!fromInteger(i)!!fromInteger(j) /=' ' ])
          matrix = ([ [ toInteger(fromJust (elemIndex (y!!fromInteger(i)!!fromInteger(j)) mapping )) | j<-[0..len-1] ] | i <- [0..len-1] ])
          d = (solveIndex 0 0 matrix len)
          k = ([ [ mapping!!(fromInteger( (snd d) !!fromInteger(i)!!fromInteger(j)))  | j<-[0..len-1] ] | i <- [0..len-1] , fst(d)==True ])

inputValid :: Int -> [String] -> Bool
inputValid x y = (length(y)==x) && (foldl (&&) True [length(z)==x | z <- y])

getValid :: Int -> String -> Bool
getValid y xs = y == length(xs) && length(xs) == length( Set.fromList(xs) )

solver :: Int -> [String] -> (Bool,[String])
solver x y |
        (x == 4 || x == 9 ) && (inputValid x y) == False = (False, ["Size mismatch."]) |
        (x == 4 || x == 9 ) && ((getValid x ([ y!!i!!j | i <- [0..x-1] , j <- [0..x-1], y!!i!!j /=' ' ]) ) ==False) = (False,["One occurence of each symbol only."])|
        (x == 4 || x == 9 ) = (solve (toInteger(x)) y) |
        otherwise = (False,["Valid Sizes 4 & 9 Only."])
