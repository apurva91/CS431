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