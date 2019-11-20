import Data.List
import System.IO


-- Declaring signature of function m
m :: [[Int]] -> Int

-- Defining rules for the funtion m
m [] = 0
m x = product [sum y | y <- x]

-- Declaring signature of function greatest
greatest :: (a -> Int) -> [a] -> a
greatest func [] = error "List is Empty. Please add some elements."
greatest func list = list !! (snd (maximum [ (func x, i) | (i,x) <- zip [0..] list ]))

-- Definition for the data type List
data List a = Empty | Cons a (List a) deriving Show

-- Conversion to list
toList :: [a] -> List a
toList ([]) = Empty
toList (x:xs) = Cons x (toList(xs))

-- Conversion from list
toHaskellList :: List a -> [a]
toHaskellList(Empty) = []
toHaskellList(Cons a b) = a : toHaskellList(b)