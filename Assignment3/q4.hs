import Data.List
import qualified Data.Set as Set
import System.IO

matrix = take 4 (repeat (take 4 (repeat 0)))

-- matrix = [ [ fromInteger (y) | y <- x ] | x<-[[1,2,4,3],[1,2,4,3],[3,4,2,1],[1,2,3,4]] ]

len = fromInteger(4)
sub = fromInteger(floor(sqrt(realToFrac(len))))
-- set :: Int -> Int -> [Int] -> [Int]
-- set 0 a (x:xs) = (a:xs)
-- set n a list = set n-1 a list


getCount :: [Integer] -> [Integer]
getCount a = [ toInteger(length ( filter (==x) a)) | x <- [0..(len)] ]

isValidList :: [Integer]-> Bool -> Bool
isValidList [] _ = True
isValidList (x:xs) True = isValidList xs False 
isValidList (x:xs) False = x<=1 && isValidList xs False


areValidLists :: [[Integer]] -> Bool
areValidLists [] = True
areValidLists (x:xs) =   isValidList  y True && areValidLists xs where y = getCount x

generateLists :: [[Integer]] -> [[Integer]]
generateLists a = a ++ [ [ a!!fromInteger(y)!!fromInteger(x) |  y <- [0..(len-1)] ] | x <- [0..(len-1)] ]  ++ [ [ (a!!(y*sub+w))!!(x*sub+z)| w<-[0..(sub-1)] , z<-[0..(sub-1)] ] | y<-[0..(sub-1)] , x<-[0..(sub-1)]] 


-- solveIndex :: Integer -> Integer -> [[Intger]] -> [[Integer]]

